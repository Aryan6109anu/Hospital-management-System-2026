package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AppointmentResponse;
import com.example.demo.dto.AppointmentStaffDto;
import com.example.demo.entities.AppointmentStaff;
import com.example.demo.entities.User;
import com.example.demo.mapper.AppointmentStaffMapper;
import com.example.demo.model.Role;
import com.example.demo.repository.AppointmentStaffRepository;
import com.example.demo.repository.UserRepository;

@Service

public class AppointmentStaffServiceImpl implements AppointmentStaffService {

    private final AppointmentStaffRepository staffRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    
    

    public AppointmentStaffServiceImpl(AppointmentStaffRepository staffRepo, UserRepository userRepo,
			PasswordEncoder passwordEncoder) {
		super();
		this.staffRepo = staffRepo;
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	// ---------------- CREATE ----------------
    @Override
    public AppointmentStaffDto createStaff(AppointmentStaffDto dto) {

        if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole(Role.ROLE_APPOINTMENT);
        userRepo.save(user);

        AppointmentStaff staff = new AppointmentStaff();
        staff.setFullName(dto.getFullName());
        staff.setAge(dto.getAge());
        staff.setGender(dto.getGender());
        staff.setMobile(dto.getMobile());
        staff.setAadhaarNumber(dto.getAadhaarNumber());
        staff.setAddress(dto.getAddress());
        staff.setEmail(dto.getEmail());
        staff.setUser(user);

        return AppointmentStaffMapper.toDto(staffRepo.save(staff));
    }

    // ---------------- UPDATE ----------------
    @Override
    public AppointmentStaffDto updateStaff(Long id, AppointmentStaffDto dto) {

        AppointmentStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment Staff not found"));

        staff.setFullName(dto.getFullName());
        staff.setAge(dto.getAge());
        staff.setGender(dto.getGender());
        staff.setMobile(dto.getMobile());
        staff.setAadhaarNumber(dto.getAadhaarNumber());
        staff.setAddress(dto.getAddress());
        staff.setEmail(dto.getEmail());

        if (dto.getUsername() != null &&
            !dto.getUsername().equals(staff.getUser().getUsername())) {

            if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }

            staff.getUser().setUsername(dto.getUsername());
        }

        return AppointmentStaffMapper.toDto(staffRepo.save(staff));
    }

    // ---------------- GET BY ID ----------------
    @Override
    public AppointmentStaffDto getById(Long id) {
        AppointmentStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment Staff not found"));
        return AppointmentStaffMapper.toDto(staff);
    }

    // ---------------- GET ALL ----------------
    @Override
    public List<AppointmentStaffDto> getAll() {
        return staffRepo.findAll()
                .stream()
                .map(AppointmentStaffMapper::toDto)
                .collect(Collectors.toList());
    }

    // ---------------- DELETE ----------------
    @Override
    public void deleteById(Long id) {
        AppointmentStaff staff = staffRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment Staff not found"));

        userRepo.delete(staff.getUser());
        staffRepo.delete(staff);
    }

    // ---------------- GET BY USERNAME ----------------
    @Override
    public AppointmentStaffDto getByUsername(String username) {
        AppointmentStaff staff = staffRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Appointment Staff not found"));
        return AppointmentStaffMapper.toDto(staff);
    }

    // ---------------- GET APPOINTMENTS ----------------
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByStaffUsername(String username) {

        AppointmentStaff staff = staffRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Appointment Staff not found"));

        return staff.getAppointments()
                .stream()
                .map(appointment -> {
                    AppointmentResponse response = new AppointmentResponse();

                    // Appointment info
                    response.setId(appointment.getId());
                    response.setDisease(appointment.getDisease());
                    response.setSlotId(appointment.getId()); // use appointment id for slot if no slot entity
                    response.setSlotStartTime(appointment.getSlotStartTime());
                    response.setStatus(appointment.getStatus());

                    // Patient info
                    if (appointment.getPatient() != null) {
                        response.setPatientId(appointment.getPatient().getId());
                        response.setPatientName(appointment.getPatient().getName());
                        response.setPatientMobile(appointment.getPatient().getMobile());
                        response.setPatientGender(appointment.getPatient().getGender());
                        response.setPatientAge(appointment.getPatient().getAge());
                        response.setPatientAddress(appointment.getPatient().getAddress());
                    }

                    // Doctor info
                    if (appointment.getDoctor() != null) {
                        response.setDoctorId(appointment.getDoctor().getId());
                        response.setPatientName(appointment.getPatient().getName());
                        response.setDoctorDepartment(appointment.getDoctor().getDepartment());
                        response.setSpecialization(appointment.getDoctor().getSpecialization());
                    }

                    return response;
                })
                .collect(Collectors.toList());
    }
}