package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.VisitMedicineDto;
import com.example.demo.dto.VisitRequestDto;
import com.example.demo.dto.VisitResponseDto;
import com.example.demo.dto.VisitUpdateDto;
import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.LabTest;
import com.example.demo.entities.Medicine;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Visit;
import com.example.demo.entities.VisitLabTest;
import com.example.demo.entities.VisitMedicine;
import com.example.demo.model.Department;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.LabTestRepository;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.VisitLabTestRepository;
import com.example.demo.repository.VisitMedicineRepository;
import com.example.demo.repository.VisitRepository;

import jakarta.transaction.Transactional;

@Service
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final VisitLabTestRepository visitLabTestRepository;
    private final VisitMedicineRepository visitMedicineRepository;
    private final LabTestRepository labTestRepository;
    private final MedicineRepository medicineRepository;

    public VisitServiceImpl(
            VisitRepository visitRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            VisitLabTestRepository visitLabTestRepository,
            VisitMedicineRepository visitMedicineRepository,
            LabTestRepository labTestRepository,
            MedicineRepository medicineRepository
    ) {
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.visitLabTestRepository = visitLabTestRepository;
        this.visitMedicineRepository = visitMedicineRepository;
        this.labTestRepository = labTestRepository;
        this.medicineRepository = medicineRepository;
    }

    // ================= START VISIT =================
    @Override
    @Transactional // ✅ Added Transactional for safe DB operations
    public VisitResponseDto startVisit(VisitRequestDto dto) {

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Department department = doctor.getDepartment();

        if (department == null) {
            throw new RuntimeException("Doctor department not configured");
        }

        Visit visit = visitRepository
                .findByAppointment_Id(dto.getAppointmentId())
                .orElse(null);

        if (visit == null) {
            visit = new Visit();
            visit.setPatient(patient);
            visit.setDoctor(doctor);
            visit.setAppointment(appointment);
            visit.setDepartment(department);
        } else {
            visitLabTestRepository.deleteByVisitId(visit.getId());
            visitMedicineRepository.deleteByVisitId(visit.getId());
        }

        visit.setChiefComplaint(dto.getChiefComplaint());
        visit.setPastHistory(dto.getPastHistory());
        visit.setCurrentMedication(dto.getCurrentMedication());
        visit.setDiagnosis(dto.getDiagnosis());
        visit.setNotes(dto.getNotes());
        visit.setVisitDate(LocalDateTime.now());

        Visit saved = visitRepository.save(visit);

        if (dto.getLabTestIds() != null) {
            for (Long testId : dto.getLabTestIds()) {
                LabTest test = labTestRepository.findById(testId)
                        .orElseThrow(() -> new RuntimeException("Lab test not found"));

                VisitLabTest vlt = new VisitLabTest();
                vlt.setVisit(saved);
                vlt.setLabTest(test);
                visitLabTestRepository.save(vlt);
            }
        }

        if (dto.getMedicines() != null) {
            for (VisitMedicineDto medDto : dto.getMedicines()) {
                Medicine medicine = medicineRepository.findById(medDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found"));

                VisitMedicine vm = new VisitMedicine();
                vm.setVisit(saved);
                vm.setMedicine(medicine);
                vm.setDose(medDto.getDose());
                vm.setDays(medDto.getDays());
                visitMedicineRepository.save(vm);
            }
        }

        return mapToDto(saved);
    }

    @Override
    public List<VisitResponseDto> getPatientHistory(Long patientId) {
        return visitRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public VisitResponseDto getVisitById(Long id) {
        // Pehle check karo ki kya ye Visit ID hai?
        // Agar nahi milti, toh check karo ki kya ye Appointment ID hai?
        Visit visit = visitRepository.findById(id)
                .orElseGet(() -> visitRepository.findByAppointment_Id(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found for ID: " + id)));
        
        return mapToDto(visit);
    }

    @Override
    @Transactional
    public VisitResponseDto updateVisit(Long visitId, VisitUpdateDto dto) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        visit.setChiefComplaint(dto.getChiefComplaint());
        visit.setPastHistory(dto.getPastHistory());
        visit.setCurrentMedication(dto.getCurrentMedication());
        visit.setDiagnosis(dto.getDiagnosis());
        visit.setNotes(dto.getNotes());
        visit.setVisitDate(LocalDateTime.now());
        visitRepository.save(visit);

        visitLabTestRepository.deleteByVisitId(visitId);
        visitMedicineRepository.deleteByVisitId(visitId);

        if (dto.getLabTestIds() != null && !dto.getLabTestIds().isEmpty()) {
            for (Long testId : dto.getLabTestIds()) {
                LabTest test = labTestRepository.findById(testId)
                        .orElseThrow(() -> new RuntimeException("Lab test not found with id: " + testId));
                VisitLabTest visitLabTest = new VisitLabTest();
                visitLabTest.setVisit(visit);
                visitLabTest.setLabTest(test);
                visitLabTestRepository.save(visitLabTest);
            }
        }

        if (dto.getMedicines() != null && !dto.getMedicines().isEmpty()) {
            for (VisitMedicineDto medDto : dto.getMedicines()) {
                Medicine medicine = medicineRepository.findById(medDto.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + medDto.getMedicineId()));
                VisitMedicine visitMedicine = new VisitMedicine();
                visitMedicine.setVisit(visit);
                visitMedicine.setMedicine(medicine);
                visitMedicine.setDose(medDto.getDose());
                visitMedicine.setDays(medDto.getDays());
                visitMedicineRepository.save(visitMedicine);
            }
        }

        return mapToDto(visit);
    }

    @Override
    public boolean isPatientAlreadyVisitedInDepartment(Long patientId, Department department) {
        return visitRepository.existsByPatient_IdAndDepartment(patientId, department);
    }

    @Override
    public boolean isPatientAlreadyVisited(Long patientId) {
        return visitRepository.existsByPatient_Id(patientId);
    }

    // ================= DTO MAPPER (FIXED) =================
    private VisitResponseDto mapToDto(Visit v) {
        VisitResponseDto res = new VisitResponseDto();
        
        res.setId(v.getId());
        
        // ✅ Safe check for Appointment (Fixed 500 error)
        if (v.getAppointment() != null) {
            res.setAppointmentId(v.getAppointment().getId());
        } else {
            res.setAppointmentId(null);
        }

        // ✅ Safe check for Patient
        if (v.getPatient() != null) {
            res.setPatientId(v.getPatient().getId());
            res.setPatientName(v.getPatient().getName());
        }

        // ✅ Safe check for Doctor (Removed duplicate line)
        if (v.getDoctor() != null) {
            res.setDoctorId(v.getDoctor().getId());
            res.setDoctorName(v.getDoctor().getName());
        }

        res.setDoctorDepartment(
                v.getDepartment() != null ? v.getDepartment().getDisplayName() : null
        );

        res.setChiefComplaint(v.getChiefComplaint());
        res.setPastHistory(v.getPastHistory());
        res.setCurrentMedication(v.getCurrentMedication());
        res.setDiagnosis(v.getDiagnosis());
        res.setNotes(v.getNotes());
        res.setVisitDate(v.getVisitDate());

        List<String> tests = visitLabTestRepository.findByVisitId(v.getId())
                .stream()
                .map(t -> t.getLabTest().getName())
                .toList();
        res.setLabTests(tests);

        List<String> meds = visitMedicineRepository.findByVisitId(v.getId())
                .stream()
                .map(m -> m.getMedicine().getMedicineName()
                        + " " + m.getDose()
                        + " (" + m.getDays() + " days)")
                .toList();
        res.setMedicines(meds);

        return res;
    }

    @Override
    public VisitResponseDto getVisitByAppointmentId(Long appointmentId) {
        Visit visit = visitRepository.findByAppointment_Id(appointmentId)
                .orElseThrow(() -> new RuntimeException("Visit not found for Appointment ID: " + appointmentId));
        return mapToDto(visit);
    }
}