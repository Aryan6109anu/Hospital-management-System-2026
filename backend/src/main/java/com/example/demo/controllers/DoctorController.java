package com.example.demo.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.DoctorRegisterDto;
import com.example.demo.dto.DoctorResponseDto;
import com.example.demo.dto.DoctorUpdateDto;
import com.example.demo.services.DoctorService;

@RestController 
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // =========================
    // ✅ ADMIN → Register Doctor 
    // =========================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDto> registerDoctor(
            @RequestBody DoctorRegisterDto dto) {

        return ResponseEntity.ok(
                doctorService.registerDoctor(dto)
        );
    }

    // =========================
    // ✅ ADMIN → BULK Register Doctors
    // =========================
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DoctorResponseDto>> registerDoctorsBulk(
            @RequestBody List<DoctorRegisterDto> dtos) {

        return ResponseEntity.ok(
                doctorService.registerDoctorsBulk(dtos)
        );
    }

    // =========================
    // ✅ ADMIN / PATIENT → Get All Doctors
    // =========================
    @GetMapping
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors() {

        return ResponseEntity.ok(
                doctorService.getAllDoctors()
        );
    }

    // =========================
    // ✅ ADMIN / PATIENT → Get Doctor By ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                doctorService.getDoctorById(id)
        );
    }

    // =========================
    // ✅ ADMIN → Update Doctor
    // =========================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDto> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorUpdateDto dto) {

        return ResponseEntity.ok(
                doctorService.updateDoctor(id, dto)
        );
    }

    // =========================
    // ✅ ADMIN → Delete Doctor
    // =========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteDoctor(
            @PathVariable Long id) {

        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor deleted successfully");
    }

    // =========================
    // ✅ ADMIN → Toggle Availability
    // =========================
    @PutMapping("/{id}/toggle-availability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDto> toggleAvailability(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                doctorService.toggleAvailability(id)
        );
    }

    // =========================
    // ✅ DOCTOR → My Profile
    // =========================
    @GetMapping("/me")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorResponseDto> myProfile(
            Principal principal) {

        return ResponseEntity.ok(
                doctorService.getProfileByUsername(
                        principal.getName()
                )
        );
    }

    // =========================  
    // ✅ PUBLIC / AUTH → Available Doctors
    // 🔥 MUST RETURN DTO (not entity)
    // =========================
    @GetMapping("/available")
    public ResponseEntity<List<DoctorResponseDto>> getAvailableDoctors() {
        return ResponseEntity.ok(
                doctorService.getAvailableDoctors()   // ⬅ DTO based
        );
    }
}
