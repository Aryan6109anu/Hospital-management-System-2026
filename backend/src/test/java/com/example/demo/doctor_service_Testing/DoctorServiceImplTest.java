package com.example.demo.doctor_service_Testing;

import com.example.demo.dto.DoctorRegisterDto;
import com.example.demo.dto.DoctorResponseDto;
import com.example.demo.dto.DoctorUpdateDto;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.User;
import com.example.demo.model.Department;
import com.example.demo.model.Role;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.DoctorServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private User user;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setUsername("dramit");
        user.setPassword("encoded");
        user.setRole(Role.ROLE_DOCTOR);

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr Amit");
        doctor.setSpecialization("Cardiology");
        doctor.setExperience(10);
        doctor.setAvailable(true);
        doctor.setUser(user);
        doctor.setDepartment(Department.CARDIOLOGY);  // ✅ Enum example
    }

    // ================= REGISTER DOCTOR =================
    @Test
    void registerDoctor_success() {

        DoctorRegisterDto dto = new DoctorRegisterDto();
        dto.setName("Dr Amit");
        dto.setUsername("dramit");
        dto.setPassword("pass123");
        dto.setSpecialization("Cardiology");
        dto.setExperience(10);
        dto.setAvailable(true);
        dto.setDepartment("CARDIOLOGY");

        when(userRepo.existsByUsername("dramit")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encoded");
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(doctorRepo.save(any(Doctor.class))).thenReturn(doctor);

        DoctorResponseDto response = doctorService.registerDoctor(dto);

        assertNotNull(response);
        assertEquals("Dr Amit", response.getName());
        assertEquals("dramit", response.getUsername());
        assertEquals("CARDIOLOGY", response.getDepartment());  // ✅ check enum mapping

        verify(userRepo).save(any(User.class));
        verify(doctorRepo).save(any(Doctor.class));
    }

    // ================= GET ALL DOCTORS =================
    @Test
    void getAllDoctors_success() {

        when(doctorRepo.findAll()).thenReturn(List.of(doctor));

        List<DoctorResponseDto> result = doctorService.getAllDoctors();

        assertEquals(1, result.size());
        assertEquals("CARDIOLOGY", result.get(0).getDepartment());
        verify(doctorRepo).findAll();
    }

    // ================= GET BY ID =================
    @Test
    void getDoctorById_success() {

        when(doctorRepo.findById(1L)).thenReturn(Optional.of(doctor));

        DoctorResponseDto dto = doctorService.getDoctorById(1L);

        assertEquals("Dr Amit", dto.getName());
        assertEquals("CARDIOLOGY", dto.getDepartment());
        verify(doctorRepo).findById(1L);
    }

    @Test
    void getDoctorById_notFound() {

        when(doctorRepo.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> doctorService.getDoctorById(2L));

        assertEquals("Doctor not found", ex.getMessage());
    }

    // ================= UPDATE DOCTOR =================
    @Test
    void updateDoctor_success() {

        DoctorUpdateDto dto = new DoctorUpdateDto();
        dto.setName("Dr Updated");
        dto.setSpecialization("Neuro");
        dto.setExperience(12);
        dto.setAvailable(false);
        dto.setDepartment("NEUROLOGY");

        when(doctorRepo.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepo.save(any())).thenReturn(doctor);

        DoctorResponseDto updated = doctorService.updateDoctor(1L, dto);

        assertEquals("Dr Updated", updated.getName());
        assertFalse(updated.getAvailable());
        assertEquals("NEUROLOGY", updated.getDepartment());
        verify(doctorRepo).save(doctor);
    }

    // ================= DELETE =================
    @Test
    void deleteDoctor_success() {

        doNothing().when(doctorRepo).deleteById(1L);

        doctorService.deleteDoctor(1L);

        verify(doctorRepo).deleteById(1L);
    }

    // ================= TOGGLE AVAILABILITY =================
    @Test
    void toggleAvailability_success() {

        when(doctorRepo.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepo.save(any())).thenReturn(doctor);

        DoctorResponseDto result = doctorService.toggleAvailability(1L);

        assertFalse(result.getAvailable());
        verify(doctorRepo).save(doctor);
    }

    // ================= PROFILE BY USERNAME =================
    @Test
    void getProfileByUsername_success() {

        when(doctorRepo.findByUserUsername("dramit")).thenReturn(Optional.of(doctor));

        DoctorResponseDto profile = doctorService.getProfileByUsername("dramit");

        assertEquals("Dr Amit", profile.getName());
        assertEquals("CARDIOLOGY", profile.getDepartment());
        verify(doctorRepo).findByUserUsername("dramit");
    }

    // ================= AVAILABLE DOCTORS =================
    @Test
    void findByAvailableTrue_success() {

        when(doctorRepo.findByAvailableTrue()).thenReturn(List.of(doctor));

        List<Doctor> list = doctorService.findByAvailableTrue();

        assertEquals(1, list.size());
        assertEquals(Department.CARDIOLOGY, list.get(0).getDepartment());
        verify(doctorRepo).findByAvailableTrue();
    }
}
