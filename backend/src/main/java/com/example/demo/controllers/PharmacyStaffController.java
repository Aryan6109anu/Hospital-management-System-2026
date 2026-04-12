package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PharmacyStaffDto;
import com.example.demo.services.PharmacyStaffService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pharmacy-staff")
public class PharmacyStaffController {

    private final PharmacyStaffService service;

    public PharmacyStaffController(PharmacyStaffService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<PharmacyStaffDto> create(
            @Valid @RequestBody PharmacyStaffDto dto) {

        return ResponseEntity.ok(service.createStaff(dto));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<PharmacyStaffDto> getById(@PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<PharmacyStaffDto>> getAll() {

        return ResponseEntity.ok(service.getAll());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PharmacyStaffDto> update(
            @PathVariable Long id,
            @Valid @RequestBody PharmacyStaffDto dto) {

        return ResponseEntity.ok(service.updateStaff(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        service.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }
    @GetMapping("/me")
    public ResponseEntity<PharmacyStaffDto> getLoggedInStaff(Authentication authentication) {

        String username = authentication.getName();
        return ResponseEntity.ok(service.getByUsername(username));
    }
}