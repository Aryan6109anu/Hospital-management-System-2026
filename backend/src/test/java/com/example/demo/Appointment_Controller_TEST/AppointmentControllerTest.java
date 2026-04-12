package com.example.demo.Appointment_Controller_TEST;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.example.demo.controllers.AppointmentController;
import com.example.demo.dto.AppointmentRequest;
import com.example.demo.dto.AppointmentResponse;
import com.example.demo.security.JwtAuthFilter;
import com.example.demo.security.JwtUtil;
import com.example.demo.services.AppointmentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * ✅ Appointment Controller Test
 * ❌ No DB
 * ❌ No JWT logic
 */
@WebMvcTest(controllers = AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentServiceImpl appointmentService;

    // 🔥 Break JWT chain
    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // BOOK APPOINTMENT (PATIENT)
    // =========================
    @Test
    @WithMockUser(username = "amit", roles = "PATIENT")
    void bookAppointment_success() throws Exception {

        AppointmentRequest request = new AppointmentRequest();
        AppointmentResponse response = new AppointmentResponse();

        when(appointmentService.bookAppointment(any(), eq("amit")))
                .thenReturn(response);

        mockMvc.perform(post("/api/appointments/book")
                .principal(() -> "amit")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // =========================
    // CANCEL APPOINTMENT (PATIENT)
    // =========================
    @Test
    @WithMockUser(username = "amit", roles = "PATIENT")
    void cancelAppointment_patient_success() throws Exception {

        when(appointmentService.cancelAppointmentByPatient(1L, "amit"))
                .thenReturn("Cancelled");

        mockMvc.perform(patch("/api/appointments/cancel/1")
                .principal(() -> "amit"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cancelled"));
    }

    // =========================
    // CANCEL APPOINTMENT (ADMIN)
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCancelAppointment_success() throws Exception {

        when(appointmentService.cancelAppointmentByAdmin(1L))
                .thenReturn("Admin Cancelled");

        mockMvc.perform(patch("/api/appointments/admin/cancel/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin Cancelled"));
    }

    // =========================
    // GET APPOINTMENTS BY DOCTOR ID
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAppointmentsByDoctor_success() throws Exception {

        when(appointmentService.getAppointmentsByDoctorId(1L))
                .thenReturn(List.of(new AppointmentResponse()));

        mockMvc.perform(get("/api/appointments/doctor/1"))
                .andExpect(status().isOk());
    }

    // =========================
    // GET MY APPOINTMENTS (PATIENT)
    // =========================
    @Test
    @WithMockUser(username = "amit", roles = "PATIENT")
    void getAppointmentsByPatient_success() throws Exception {

        when(appointmentService.getMyAppointmentsForPatient("amit"))
                .thenReturn(List.of(new AppointmentResponse()));

        mockMvc.perform(get("/api/appointments/patient")
                .principal(() -> "amit"))
                .andExpect(status().isOk());

        verify(appointmentService, times(1))
                .getMyAppointmentsForPatient("amit");
    }

    // =========================
    // GET MY APPOINTMENTS (DOCTOR)
    // =========================
    @Test
    @WithMockUser(username = "doctor1", roles = "DOCTOR")
    void getAppointmentsByDoctorUsername_success() throws Exception {

        when(appointmentService.getMyAppointmentsForDoctor("doctor1"))
                .thenReturn(List.of(new AppointmentResponse()));

        mockMvc.perform(get("/api/appointments/doctor/me")
                .principal(() -> "doctor1"))   // ⭐ MOST IMPORTANT
                .andExpect(status().isOk());

        verify(appointmentService, times(1))
                .getMyAppointmentsForDoctor("doctor1");
    }

}
