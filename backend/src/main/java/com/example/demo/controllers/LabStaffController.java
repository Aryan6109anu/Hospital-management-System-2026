package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.LabStaffDto;
import com.example.demo.services.LabStaffService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lab-staff")
public class LabStaffController {

    private final LabStaffService service;

    public LabStaffController(LabStaffService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<LabStaffDto> create(@Valid @RequestBody LabStaffDto dto) {
        return ResponseEntity.ok(service.createStaff(dto));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<LabStaffDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<LabStaffDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<LabStaffDto> update(
            @PathVariable Long id,
            @Valid @RequestBody LabStaffDto dto) {

        return ResponseEntity.ok(service.updateStaff(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        service.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // Logged In Staff
    @GetMapping("/me")
    public ResponseEntity<LabStaffDto> getLoggedInStaff(Authentication authentication) {

        String username = authentication.getName();
        return ResponseEntity.ok(service.getByUsername(username));
    }
}