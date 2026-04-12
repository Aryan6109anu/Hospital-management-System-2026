package com.example.demo.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.dto.AppointmentRequest;
import com.example.demo.dto.AppointmentResponse;
import com.example.demo.dto.DoctorSlotResponse;
import com.example.demo.dto.WalkInAppointmentRequestDto;
import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.User;
import com.example.demo.entities.Visit;
import com.example.demo.model.AppointmentStatus;
import com.example.demo.model.Role;
import com.example.demo.model.WorkingDay;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VisitRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DoctorScheduleService doctorScheduleService;
    private final VisitRepository visitRepository;

   

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
			DoctorRepository doctorRepository, UserRepository userRepository,
			DoctorScheduleService doctorScheduleService, VisitRepository visitRepository) {
		super();
		this.appointmentRepository = appointmentRepository;
		this.patientRepository = patientRepository;
		this.doctorRepository = doctorRepository;
		this.userRepository = userRepository;
		this.doctorScheduleService = doctorScheduleService;
		this.visitRepository = visitRepository;
	}

	private LocalTime normalize(LocalTime time) {
        if (time == null) return null;
        return LocalTime.of(time.getHour(), time.getMinute());
    }

    @Override
    public List<LocalDate> getAvailableDates(Long doctorId, LocalDate from, LocalDate to) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorId));

        if (doctor.getSchedule() == null || doctor.getSchedule().getDays() == null)
            return List.of();

        List<WorkingDay> workingDays = doctor.getSchedule().getDays();
        List<LocalDate> availableDates = new ArrayList<>();

        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            WorkingDay day = WorkingDay.valueOf(d.getDayOfWeek().name());
            if (workingDays.contains(day)) availableDates.add(d);
        }
        return availableDates;
    }

    @Override
    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Patient patient;
        if (user.getRole() == Role.ROLE_PATIENT) {
            patient = patientRepository.findByUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found for this user"));
        } else {
            if (request.getPatientId() == null)
                throw new RuntimeException("Patient ID is required for admin booking");
            patient = patientRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + request.getPatientId()));
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + request.getDoctorId()));

        LocalTime requestedStart = normalize(request.getSlotStartTime());
        LocalDate requestedDate = request.getSlotDate();

        // 🔥 SLOT END TIME CALCULATION (FIXED)
        LocalTime requestedEnd = normalize(request.getSlotEndTime());
        if (requestedEnd == null) {
            int duration = 15; // Default 15 mins
            if (doctor.getSchedule() != null && doctor.getSchedule().getSlotDuration() > 0) {
                duration = doctor.getSchedule().getSlotDuration();
            }
            requestedEnd = requestedStart.plusMinutes(duration);
        }

        // ✅ FIXED: Using repository method with Start and End time
        boolean alreadyBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateAndSlotStartTimeAndSlotEndTime(
                doctor.getId(), requestedDate, requestedStart, requestedEnd
        );
         
        if (alreadyBooked)
            throw new RuntimeException("Slot already booked for doctor " + doctor.getName() + " on " + requestedDate);

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDisease(request.getDisease());
        appointment.setSlotStartTime(requestedStart);
        appointment.setSlotEndTime(requestedEnd); // Setting calculated EndTime
        appointment.setAppointmentDate(requestedDate);
        appointment.setStatus(AppointmentStatus.BOOKED);

        appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    @Override
    @Transactional
    public AppointmentResponse bookWalkInAppointment(WalkInAppointmentRequestDto dto) {
        Patient patient = patientRepository.findByMobile(dto.getMobile())
                .orElseGet(() -> {
                    Patient p = new Patient();
                    p.setName(dto.getPatientName());
                    p.setAge(dto.getAge());
                    p.setGender(dto.getGender());
                    p.setMobile(dto.getMobile());
                    p.setAddress(dto.getAddress());
                    return patientRepository.save(p);
                });

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        List<DoctorSlotResponse> availableSlots = doctorScheduleService.getAvailableSlots(doctor.getId(), dto.getAppointmentDate());
        
        LocalTime requestedStart = normalize(dto.getSlotStartTime());
        LocalTime requestedEnd = normalize(dto.getSlotEndTime());

        boolean slotAvailable = availableSlots.stream()
                .anyMatch(s -> normalize(s.getStartTime()).equals(requestedStart) && 
                               normalize(s.getEndTime()).equals(requestedEnd));

        if (!slotAvailable) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested slot not available.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDisease(dto.getDisease());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setSlotStartTime(requestedStart);
        appointment.setSlotEndTime(requestedEnd);
        appointment.setStatus(AppointmentStatus.BOOKED);

        appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    @Override
    @Transactional
    public AppointmentResponse updateWalkInAppointment(Long appointmentId, WalkInAppointmentRequestDto dto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        List<DoctorSlotResponse> availableSlots = doctorScheduleService.getAvailableSlots(doctor.getId(), dto.getAppointmentDate());

        LocalTime cleanStart = normalize(dto.getSlotStartTime());
        LocalTime cleanEnd = normalize(dto.getSlotEndTime());

        boolean slotAvailable = availableSlots.stream()
                .anyMatch(s -> normalize(s.getStartTime()).equals(cleanStart) && 
                               normalize(s.getEndTime()).equals(cleanEnd));

        if (!slotAvailable) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Slot not available");

        appointment.setDoctor(doctor);
        appointment.setDisease(dto.getDisease());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setSlotStartTime(cleanStart);
        appointment.setSlotEndTime(cleanEnd);
        
        return mapToResponse(appointmentRepository.save(appointment));
    }

    // ✅ FIXED MAPPER LOGIC
    private AppointmentResponse mapToResponse(Appointment appointment) {
        if (appointment == null) return new AppointmentResponse();
        
        AppointmentResponse resp = new AppointmentResponse();
        resp.setId(appointment.getId());
        resp.setDisease(appointment.getDisease());
        resp.setStatus(appointment.getStatus());
        resp.setSlotStartTime(appointment.getSlotStartTime());
        resp.setSlotEndTime(appointment.getSlotEndTime());
        resp.setAppointmentDate(appointment.getAppointmentDate()); 
        resp.setSlotId(appointment.getId()); 

        if (appointment.getPatient() != null) {
            Patient p = appointment.getPatient();
            resp.setPatientId(p.getId());
            resp.setPatientName(p.getName());
            resp.setPatientMobile(p.getMobile());
            resp.setPatientGender(p.getGender());
            resp.setPatientAge(p.getAge());
            resp.setPatientAddress(p.getAddress());
        }

        if (appointment.getDoctor() != null) {
            Doctor d = appointment.getDoctor();
            resp.setDoctorId(d.getId());
            resp.setDoctorName(d.getName());
            resp.setDoctorDepartment(d.getDepartment());
            resp.setSpecialization(d.getSpecialization());
        }
        
        Visit visit = visitRepository
                .findByAppointment_Id(appointment.getId())
                .orElse(null);  

        if (visit != null) {
            resp.setVisitId(visit.getId());
        }

        return resp;
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getMyAppointmentsForDoctor(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Doctor doctor = doctorRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return appointmentRepository.findByDoctorId(doctor.getId()).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        return appointmentRepository.findById(id).map(this::mapToResponse).orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }

    @Override
    public List<AppointmentResponse> getMyAppointmentsForPatient(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Patient patient = patientRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return appointmentRepository.findByPatientId(patient.getId()).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String cancelAppointmentByPatient(Long appointmentId, String username) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        if (appointment.getPatient().getUser() == null || !appointment.getPatient().getUser().getUsername().equals(username))
            throw new RuntimeException("Unauthorized cancellation");
        
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        return "Appointment cancelled";
    }

    @Override
    @Transactional
    public String cancelAppointmentByAdmin(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        return "Appointment cancelled by admin";
    }
}