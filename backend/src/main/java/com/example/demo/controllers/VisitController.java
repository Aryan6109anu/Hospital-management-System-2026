package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.VisitRequestDto;
import com.example.demo.dto.VisitResponseDto;
import com.example.demo.dto.VisitUpdateDto;
import com.example.demo.model.Department;
import com.example.demo.services.VisitService;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) { 
        this.visitService = visitService;
    }

    // ================= START VISIT =================
    @PostMapping("/start")
    public ResponseEntity<VisitResponseDto> startVisit(
            @RequestBody VisitRequestDto dto) {

        return ResponseEntity.ok(visitService.startVisit(dto));
    }

    // ================= PATIENT HISTORY =================
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VisitResponseDto>> getHistory(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                visitService.getPatientHistory(patientId));
    }

    // ================= GET BY ID =================
    @GetMapping("/{visitId}")
    public ResponseEntity<VisitResponseDto> getById(
            @PathVariable Long visitId) {

        return ResponseEntity.ok(
                visitService.getVisitById(visitId));
    }

    // ================= UPDATE =================
    @PutMapping("/{visitId}")
    public ResponseEntity<VisitResponseDto> updateVisit(
            @PathVariable Long visitId,
            @RequestBody VisitUpdateDto dto) {

        return ResponseEntity.ok(
                visitService.updateVisit(visitId, dto));
    }

    // ================= CHECK DUPLICATE =================
    @GetMapping("/check/{patientId}")
    public ResponseEntity<Boolean> checkVisit(
            @PathVariable Long patientId,
            @RequestParam String department) {

        return ResponseEntity.ok(
                visitService.isPatientAlreadyVisitedInDepartment(
                        patientId,
                        Department.valueOf(department)
                ));
    }
}