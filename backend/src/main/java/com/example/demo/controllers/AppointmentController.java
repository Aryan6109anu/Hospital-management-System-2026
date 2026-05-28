package com.example.demo.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AppointmentRequest;
import com.example.demo.dto.AppointmentResponse;
import com.example.demo.dto.WalkInAppointmentRequestDto;
import com.example.demo.services.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // ✅ Book Appointment (Patient/Admin/Staff)
    @PostMapping("/book")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','APPOINTMENT')")
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @RequestBody AppointmentRequest request,
            Principal principal) {
        AppointmentResponse response = appointmentService.bookAppointment(request, principal.getName());
        return ResponseEntity.ok(response);
    }

    // ✅ Walk-in Appointment (Admin/Staff)
    @PostMapping("/walk-in")
    @PreAuthorize("hasAnyRole('ADMIN','APPOINTMENT')")
    public ResponseEntity<AppointmentResponse> bookWalkInAppointment(
            @RequestBody WalkInAppointmentRequestDto dto) {
        AppointmentResponse response = appointmentService.bookWalkInAppointment(dto);
        return ResponseEntity.ok(response);
    }

    // ✅ Get All Appointments (Admin/Staff)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','APPOINTMENT')")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    // ✅ Get Appointment by ID (All Roles)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN','PATIENT','APPOINTMENT')")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    // ✅ Cancel Appointment by Patient (Own only)
    @PatchMapping("/cancel/patient/{appointmentId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> cancelByPatient(
            @PathVariable Long appointmentId,
            Principal principal) {
        String msg = appointmentService.cancelAppointmentByPatient(appointmentId, principal.getName());
        return ResponseEntity.ok(msg);
    }

    // ✅ Cancel Appointment by Admin/Staff (Any)
    @PatchMapping("/cancel/admin/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','APPOINTMENT')")
    public ResponseEntity<String> cancelByAdmin(@PathVariable Long appointmentId) {
        String msg = appointmentService.cancelAppointmentByAdmin(appointmentId);
        return ResponseEntity.ok(msg);
    }

    // ✅ Doctor -> View My Appointments
    @GetMapping("/doctor/me")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentResponse>> myAppointmentsForDoctor(Principal principal) {
        return ResponseEntity.ok(appointmentService.getMyAppointmentsForDoctor(principal.getName()));
    }

    // ✅ Appointments by Doctor ID (Admin/Staff)
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN','APPOINTMENT')")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(doctorId));
    }

    // ✅ Patient -> View My Appointments
    @GetMapping("/patient/me")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<AppointmentResponse>> myAppointmentsForPatient(Principal principal) {
        return ResponseEntity.ok(appointmentService.getMyAppointmentsForPatient(principal.getName()));
    }

    // ✅ Update Walk-in Appointment (Admin/Staff)
    @PutMapping("/update/walk-in/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','APPOINTMENT')")
    public ResponseEntity<AppointmentResponse> updateWalkInAppointment(
            @PathVariable Long appointmentId,
            @RequestBody WalkInAppointmentRequestDto dto) {
        AppointmentResponse response = appointmentService.updateWalkInAppointment(appointmentId, dto);
        return ResponseEntity.ok(response);
    }
}