package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DoctorRegisterDto;
import com.example.demo.dto.DoctorResponseDto;
import com.example.demo.dto.DoctorScheduleDto;
import com.example.demo.dto.DoctorUpdateDto;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.User;
import com.example.demo.model.Department;
import com.example.demo.model.Role;
import com.example.demo.model.WorkingDay;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DoctorServiceImpl(DoctorRepository doctorRepo,
                             UserRepository userRepo,
                             PasswordEncoder passwordEncoder) {
        this.doctorRepo = doctorRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= Admin registers doctor =================
    @Override
    public DoctorResponseDto registerDoctor(DoctorRegisterDto dto) {

        if (userRepo.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        // ---- Save User ----
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ROLE_DOCTOR);
        user.setEmail(dto.getEmail());
        User savedUser = userRepo.save(user);

        // ---- Save Doctor ----
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setExperience(dto.getExperience());
        doctor.setAvailable(dto.getAvailable());
        doctor.setEmail(dto.getEmail());
        doctor.setMobile(dto.getMobile());
        doctor.setUser(savedUser);

        // ✅ SAFE Department parsing
        if (dto.getDepartment() != null && !dto.getDepartment().isBlank()) {
            doctor.setDepartment(parseDepartment(dto.getDepartment()));
        }

        Doctor savedDoctor = doctorRepo.save(doctor);
        return mapToDto(savedDoctor);
    }

    // ================= Get all doctors =================
    @Override
    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponseDto getDoctorById(Long id) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return mapToDto(doctor);
    }

    // ================= Update doctor =================
    @Transactional
    @Override
    public DoctorResponseDto updateDoctor(Long id, DoctorUpdateDto dto) {

        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setName(dto.getName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setExperience(dto.getExperience());
        doctor.setAvailable(dto.getAvailable());
        doctor.setEmail(dto.getEmail());
        doctor.setMobile(dto.getMobile());

        // ✅ SAFE Department parsing
        if (dto.getDepartment() != null && !dto.getDepartment().isBlank()) {
            doctor.setDepartment(parseDepartment(dto.getDepartment()));
        }

        User user = doctor.getUser();

        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            if (!user.getUsername().equals(dto.getUsername())
                    && userRepo.existsByUsername(dto.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(dto.getUsername());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepo.save(user);
        doctorRepo.save(doctor);

        return mapToDto(doctor);
    }

    // ================= Delete doctor =================
    @Override
    public void deleteDoctor(Long id) {
        doctorRepo.deleteById(id);
    }

    // ================= Toggle availability =================
    @Override
    public DoctorResponseDto toggleAvailability(Long id) {

        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setAvailable(!doctor.getAvailable());
        return mapToDto(doctorRepo.save(doctor));
    }

    // ================= Doctor profile (after login) =================
    @Override
    public DoctorResponseDto getProfileByUsername(String username) {

        Doctor doctor = doctorRepo.findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Doctor profile not found for username: " + username));

        return mapToDto(doctor);
    }

    // ================= Available doctors =================
    @Override
    public List<Doctor> findByAvailableTrue() {
        return doctorRepo.findByAvailableTrue();
    }

    // ================= Find doctor by User ID =================
    @Override
    public Optional<Doctor> findByUserId(Long userId) {
        return doctorRepo.findByUserId(userId);
    }

    // ================= SAFE Department parser =================
    private Department parseDepartment(String value) {
        try {
            return Department.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid department: " + value);
        }
    }
    // ================= BULK Registration BY ADMIN ONLY =================
    public List<DoctorResponseDto> registerDoctorsBulk(List<DoctorRegisterDto> dtos) {
        List<DoctorResponseDto> registeredDoctors = new ArrayList<>();

        for (DoctorRegisterDto dto : dtos) {
            // Optional: duplicate username/email check
            DoctorResponseDto doctor = registerDoctor(dto);  // reuse single doctor registration logic
            registeredDoctors.add(doctor);
        }

        return registeredDoctors;
    }


 /// ================= Mapper =================
    private DoctorResponseDto mapToDto(Doctor doctor) {

        DoctorScheduleDto scheduleDto = null;

        if (doctor.getSchedule() != null && doctor.getSchedule().getDays() != null) {

            // ✅ Direct enum list from entity
            List<WorkingDay> days = doctor.getSchedule().getDays();

            scheduleDto = new DoctorScheduleDto(
                    doctor.getSchedule().getStartTime(),
                    doctor.getSchedule().getEndTime(),
                    days
            );
        }

        return new DoctorResponseDto(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getExperience(),
                doctor.getAvailable(),
                doctor.getUser() != null ? doctor.getUser().getUsername() : null,
                doctor.getEmail(),
                doctor.getMobile(),
                doctor.getDepartment() != null ? doctor.getDepartment().name() : null,
                scheduleDto
        );
    }


    @Override
    public List<DoctorResponseDto> getAvailableDoctors() {
        return doctorRepo.findByAvailableTrue()
                .stream()
                .map(this::mapToDto)
                .toList();
    }
}