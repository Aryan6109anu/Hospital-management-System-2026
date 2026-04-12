package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AppointmentResponse;
import com.example.demo.dto.AppointmentStaffDto;
import com.example.demo.services.AppointmentStaffService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/appointment-staff")
public class AppointmentStaffController { 

    private final AppointmentStaffService service;

    public AppointmentStaffController(AppointmentStaffService service) {
        this.service = service;
    }

    // ================== CRUD ==================)
    
    @PostMapping
    public ResponseEntity<AppointmentStaffDto> create(
            @Valid @RequestBody AppointmentStaffDto dto) {
        return ResponseEntity.ok(service.createStaff(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentStaffDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentStaffDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentStaffDto> update(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentStaffDto dto) {
        return ResponseEntity.ok(service.updateStaff(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // ================== LOGGED-IN STAFF ==================

    @GetMapping("/me")
    public ResponseEntity<AppointmentStaffDto> getLoggedInStaff(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(service.getByUsername(username));
    }

    // ================== LOGGED-IN STAFF APPOINTMENTS ==================
   
    @GetMapping("/me/appointments")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(Authentication authentication) {
        String username = authentication.getName();
        List<AppointmentResponse> appointments = service.getAppointmentsByStaffUsername(username);
        return ResponseEntity.ok(appointments);
    }
}