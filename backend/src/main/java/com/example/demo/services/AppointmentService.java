package com.example.demo.services;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.dto.AppointmentRequest;
import com.example.demo.dto.AppointmentResponse;
import com.example.demo.dto.WalkInAppointmentRequestDto;

public interface AppointmentService {

    // ✅ Book appointment (Logged-in PATIENT / ADMIN)
    // NOTE: Implementation me ab StartTime aur EndTime dono set honge
    AppointmentResponse bookAppointment(AppointmentRequest request, String username);

    // ✅ Get available dates for a doctor
    List<LocalDate> getAvailableDates(Long doctorId, LocalDate from, LocalDate to);

    // ✅ Get all appointments (Admin/Staff only)
    List<AppointmentResponse> getAllAppointments();
    
    // ✅ Get Appointment by ID
    AppointmentResponse getAppointmentById(Long id);

    // ✅ Get all appointments for a specific doctor by ID
    List<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId);

    // ✅ Get appointments for the currently logged-in doctor
    List<AppointmentResponse> getMyAppointmentsForDoctor(String username);

    // ✅ Get appointments for the currently logged-in patient
    List<AppointmentResponse> getMyAppointmentsForPatient(String username);

    // ✅ Walk-in patient booking (Admin / Staff)
    // Isme dto se StartTime aur EndTime dono mapping me jayenge
    AppointmentResponse bookWalkInAppointment(WalkInAppointmentRequestDto dto);

    // ✅ Update existing Walk-in appointment (Staff only)
    AppointmentResponse updateWalkInAppointment(Long appointmentId, WalkInAppointmentRequestDto dto);

    // ✅ Cancel appointment (Patient can only cancel their own)
    String cancelAppointmentByPatient(Long appointmentId, String username);

    // ✅ Cancel appointment (Admin/Staff can cancel any)
    String cancelAppointmentByAdmin(Long appointmentId);
}