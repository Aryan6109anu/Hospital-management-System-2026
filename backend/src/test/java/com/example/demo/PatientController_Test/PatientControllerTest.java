package com.example.demo.PatientController_Test;

import com.example.demo.controllers.PatientController;
import com.example.demo.dto.PatientRegisterDto;
import com.example.demo.dto.PatientResponseDto;
import com.example.demo.security.JwtAuthFilter;
import com.example.demo.security.JwtUtil;
import com.example.demo.services.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = PatientController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtUtil jwtUtil;

    // =========================
    // REGISTER PATIENT
    // =========================
    @Test
    void registerPatient_success() throws Exception {

        PatientRegisterDto dto = new PatientRegisterDto(
                "Amit", 30, "Male",
                "9876543210", "Delhi",
                 "amit123", "1234"
        );

        PatientResponseDto response = new PatientResponseDto(
                1L, "Amit", 30, "Male",
                "9876543210", "Delhi",
            "amit123", true
        );

        when(patientService.registerPatient(any())).thenReturn(response);

        mockMvc.perform(post("/api/patients/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Amit"))
                .andExpect(jsonPath("$.username").value("amit123"))
                .andExpect(jsonPath("$.active").value(true));
    }

    // =========================
    // GET ALL PATIENTS (ADMIN)
    // =========================
    @Test
    void getAllPatients_success() throws Exception {

        when(patientService.getAllPatients()).thenReturn(
                Arrays.asList(
                        new PatientResponseDto(1L, "Amit", 30, "Male", "9", "Delhi",  "amit", true),
                        new PatientResponseDto(2L, "Sonu", 25, "Female", "8", "BLR",  "sonu", true)
                )
        );

        mockMvc.perform(get("/api/patients")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // =========================
    // GET PATIENT BY ID
    // =========================
    @Test
    void getPatientById_success() throws Exception {

        when(patientService.getPatientById(1L))
                .thenReturn(new PatientResponseDto(
                        1L, "Amit", 30, "Male",
                        "9", "Delhi", "amit", true
                ));

        mockMvc.perform(get("/api/patients/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Amit"));
    }

    // =========================
    // DELETE PATIENT
    // =========================
    @Test
    void deletePatient_success() throws Exception {

        doNothing().when(patientService).deletePatient(1L);

        mockMvc.perform(delete("/api/patients/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient deactivated successfully"));
    }

    // =========================
    // GET MY PROFILE (PATIENT)
    // =========================
    @Test
    void getMyProfile_success() throws Exception {

        when(patientService.getLoggedInPatient())
                .thenReturn(new PatientResponseDto(
                        1L, "Amit", 30, "Male",
                        "9", "Delhi", "amit123", true
                ));

        mockMvc.perform(get("/api/patients/me")
                        .with(user("amit123").roles("PATIENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("amit123"))
                .andExpect(jsonPath("$.name").value("Amit"));
    }
}
