package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.DoctorUnavailableRequest;
import com.example.demo.services.DoctorUnavailableService;

@RestController
@RequestMapping("/api/doctor-unavailable")
@CrossOrigin("*")
public class DoctorUnavailableController {

    private final DoctorUnavailableService doctorUnavailableService;

    public DoctorUnavailableController(
            DoctorUnavailableService doctorUnavailableService) {
        this.doctorUnavailableService = doctorUnavailableService;
    }

    // ==============================
    // ✅ ADMIN / DOCTOR: mark doctor unavailable
    // ==============================
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<String> markUnavailable(
            @RequestBody DoctorUnavailableRequest request) {

        doctorUnavailableService.markUnavailable(request);

        return ResponseEntity.ok("Doctor marked unavailable successfully");
    }
}
