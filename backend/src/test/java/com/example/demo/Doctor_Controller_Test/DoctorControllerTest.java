package com.example.demo.Doctor_Controller_Test;

import com.example.demo.controllers.DoctorController;
import com.example.demo.dto.DoctorRegisterDto;
import com.example.demo.dto.DoctorResponseDto;
import com.example.demo.dto.DoctorScheduleDto;
import com.example.demo.model.WorkingDay;
import com.example.demo.security.JwtAuthFilter;
import com.example.demo.security.JwtUtil;
import com.example.demo.services.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= REGISTER DOCTOR =================
    @Test
    void registerDoctor_success() throws Exception {

        DoctorRegisterDto dto = new DoctorRegisterDto();
        dto.setName("Dr Amit");
        dto.setUsername("dramit");
        dto.setPassword("pass123");
        dto.setSpecialization("Cardiology");
        dto.setExperience(10);
        dto.setAvailable(true);
        dto.setDepartment("CARDIOLOGY");

        DoctorResponseDto response =
                new DoctorResponseDto(
                        1L, "Dr Amit", "Cardiology", 10, true,
                        "dramit", "amit@gmail.com", "9999999999", "CARDIOLOGY", null
                );

        when(doctorService.registerDoctor(any())).thenReturn(response);

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dr Amit"))
                .andExpect(jsonPath("$.department").value("CARDIOLOGY"));
    }

    // ================= GET ALL DOCTORS =================
    @Test
    void getAllDoctors_success() throws Exception {

        List<DoctorResponseDto> doctors = List.of(
                new DoctorResponseDto(1L, "Dr A", "Cardio", 10, true, "drA", null, null, "CARDIOLOGY", null),
                new DoctorResponseDto(2L, "Dr B", "Neuro", 15, true, "drB", null, null, "NEUROLOGY", null)
        );

        when(doctorService.getAllDoctors()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].department").value("CARDIOLOGY"))
                .andExpect(jsonPath("$[1].department").value("NEUROLOGY"));
    }

    // ================= GET DOCTOR BY ID =================
    @Test
    void getDoctorById_success() throws Exception {

        DoctorResponseDto dto =
                new DoctorResponseDto(1L, "Dr A", "Cardiology", 10, true, "drA", null, null, "CARDIOLOGY", null);

        when(doctorService.getDoctorById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dr A"))
                .andExpect(jsonPath("$.department").value("CARDIOLOGY"));
    }

    // ================= UPDATE DOCTOR =================
    @Test
    void updateDoctor_success() throws Exception {

        DoctorRegisterDto dto = new DoctorRegisterDto();
        dto.setName("Dr Updated");
        dto.setSpecialization("Ortho");
        dto.setExperience(12);
        dto.setAvailable(true);
        dto.setDepartment("ORTHO");

        DoctorResponseDto response =
                new DoctorResponseDto(
                        1L, "Dr Updated", "Ortho", 12, true,
                        "drA", null, null, "ORTHO", null
                );

        when(doctorService.updateDoctor(anyLong(), any())).thenReturn(response);

        mockMvc.perform(put("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dr Updated"))
                .andExpect(jsonPath("$.department").value("ORTHO"));
    }

    // ================= DELETE DOCTOR =================
    @Test
    void deleteDoctor_success() throws Exception {

        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Doctor deleted successfully"));
    }

    // ================= TOGGLE AVAILABILITY =================
    @Test
    void toggleAvailability_success() throws Exception {

        DoctorResponseDto dto =
                new DoctorResponseDto(1L, "Dr A", "Cardiology", 10, false, "drA", null, null, "CARDIOLOGY", null);

        when(doctorService.toggleAvailability(1L)).thenReturn(dto);

        mockMvc.perform(put("/api/doctors/1/toggle-availability"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    // ================= DOCTOR PROFILE =================
    @Test
    void getDoctorProfile_success() throws Exception {

        DoctorResponseDto dto =
                new DoctorResponseDto(
                        1L, "Dr Profile", "ENT", 8, true,
                        "doctor1", "doc@mail.com", "8888888888", "ENT", null
                );

        when(doctorService.getProfileByUsername("doctor1")).thenReturn(dto);

        mockMvc.perform(get("/api/doctors/me")
                        .principal(new TestingAuthenticationToken("doctor1", null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("doctor1"))
                .andExpect(jsonPath("$.department").value("ENT"));
    }

    // ================= AVAILABLE DOCTORS =================
 // ================= AVAILABLE DOCTORS =================
    @Test
    void getAvailableDoctors_success() throws Exception {

        // ✅ Schedule DTO banao
        DoctorScheduleDto scheduleDto = new DoctorScheduleDto(
                LocalTime.parse("08:00"),
                LocalTime.parse("11:00"),
                List.of(WorkingDay.MONDAY, WorkingDay.WEDNESDAY)
        );

        // ✅ DoctorResponseDto me schedule DTO pass karo
        List<DoctorResponseDto> doctors = List.of(
                new DoctorResponseDto(
                        1L,
                        "Dr A",
                        "Cardio",
                        10,
                        true,
                        "drA",
                        null,
                        null,
                        "CARDIOLOGY",
                        scheduleDto
                )
        );

        // ✅ Mock service
        when(doctorService.getAvailableDoctors()).thenReturn(doctors);

        // ✅ Perform GET and verify JSON response
        mockMvc.perform(get("/api/doctors/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Dr A"))
                .andExpect(jsonPath("$[0].department").value("CARDIOLOGY"))
                .andExpect(jsonPath("$[0].schedule.startTime").value("08:00:00"))
                .andExpect(jsonPath("$[0].schedule.endTime").value("11:00:00"))
                .andExpect(jsonPath("$[0].schedule.days[0]").value("MONDAY"))
                .andExpect(jsonPath("$[0].schedule.days[1]").value("WEDNESDAY"));
    }

    
}