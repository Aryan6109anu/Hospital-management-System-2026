package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.dto.PatientDTO;
import com.example.demo.dto.PatientRegisterDto;
import com.example.demo.dto.PatientResponseDto;
import com.example.demo.entities.Patient;
import com.example.demo.entities.User;
import com.example.demo.model.Role;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;


@Service
@Transactional // ensure both user and patient are saved in one transaction
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientServiceImpl(PatientRepository patientRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ===== Helper: Convert Patient to PatientResponseDto =====
    private PatientResponseDto mapToDto(Patient patient) {

        if (patient == null) {
            throw new ResourceNotFoundException("Patient entity is null");
        }

        String username = null;

        // ✅ IMPORTANT FIX — walk-in patient support
        if (patient.getUser() != null) {
            username = patient.getUser().getUsername();
        } else {
            username = "WALK-IN"; // or null (frontend handle kare)
        }

        return new PatientResponseDto(
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getMobile(),
                patient.getAddress(),
                username,
                patient.isActive()
        );
    }



    // ==============================
    // ✅ Patient self-registration
    // ==============================
    @Override
    public PatientResponseDto registerPatient(PatientRegisterDto dto) {

        if(userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        // 🔐 Save User first
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ROLE_PATIENT);

        User savedUser = userRepository.saveAndFlush(user);

        // 🔹 Create Patient linked to User
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setAge(dto.getAge());
        patient.setGender(dto.getGender());
        patient.setMobile(dto.getMobile());
        patient.setAddress(dto.getAddress());
        patient.setUser(savedUser);
        patient.setActive(true);

        Patient savedPatient = patientRepository.save(patient);
        return mapToDto(savedPatient);
    }
   
    // ==============================
    // ✅ Admin: read all active patients
    // ==============================
    @Override
    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .filter(Patient::isActive)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ==============================
    // ✅ Admin: read patient by ID
    // ==============================
    @Override
    public PatientResponseDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
        if(!patient.isActive()) throw new ResourceNotFoundException("Patient is deactivated");
        return mapToDto(patient);
    }

   
    // ==============================
    // ✅ Admin: soft delete patient
    // ==============================
    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
        patient.setActive(false);
        patientRepository.saveAndFlush(patient);
    }

    // ==============================
    // ✅ Patient: get own profile by username
    // ==============================
    @Override
    public PatientResponseDto getLoggedInPatient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        String username;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new RuntimeException("Cannot extract username from authentication principal");
        }

        Patient patient = patientRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.isActive()) {
            throw new RuntimeException("Patient is deactivated");
        }
  
        return mapToDto(patient);
    }

    @Override
    public PatientResponseDto updateMyProfile(String username, PatientDTO dto) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with username: " + username));

        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found for user: " + username));

        if (!patient.isActive()) {
            throw new ResourceNotFoundException("Patient profile is deactivated");
        }

        patient.setAge(dto.getAge());
        patient.setMobile(dto.getMobile());
        patient.setAddress(dto.getAddress());

        Patient updatedPatient = patientRepository.save(patient);

        // ✅ FIXED: correct mapper method
        return mapToDto(updatedPatient);
    }



}
