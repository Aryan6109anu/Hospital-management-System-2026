package com.example.demo.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PatientDTO;
import com.example.demo.dto.PatientRegisterDto;
import com.example.demo.dto.PatientResponseDto;
import com.example.demo.services.PatientService;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin("*")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // =========================
    // ✅ Patient Self Registration
    // =========================
    @PostMapping("/register")
    public ResponseEntity<PatientResponseDto> registerPatient(
            @RequestBody PatientRegisterDto dto) {

        return ResponseEntity.ok(
                patientService.registerPatient(dto)
        );
    }

    // =========================
    // ✅ ADMIN → Get All Active Patients
    // =========================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','APPOINTMENT','PHARMACY','BILLING','LAB')")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {

        return ResponseEntity.ok(
                patientService.getAllPatients()
        );
    }

    // =========================
    // ✅ ADMIN → Get Patient By ID
    // =========================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','APPOINTMENT','PHARMACY','BILLING','LAB')")
    public ResponseEntity<PatientResponseDto> getPatientById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                patientService.getPatientById(id)
        );
    }

    // =========================
    // ✅ ADMIN → Soft Delete Patient
    // =========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePatient(
            @PathVariable Long id) {

        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient deactivated successfully");
    }

    // =========================
    // ✅ PATIENT → Get My Profile
    // =========================
    @GetMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponseDto> getMyProfile() {

        return ResponseEntity.ok(
                patientService.getLoggedInPatient()
        );
    }

    // =========================
    // ✅ PATIENT → Update My Profile
    // =========================
    @PutMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientResponseDto> updateMyProfile(
            @RequestBody PatientDTO dto,
            Principal principal) {

        return ResponseEntity.ok(
                patientService.updateMyProfile(
                        principal.getName(),
                        dto
                )
        );
    }
}
