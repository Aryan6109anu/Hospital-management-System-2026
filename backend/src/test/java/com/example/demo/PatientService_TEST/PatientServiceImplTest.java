package com.example.demo.PatientService_TEST;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.dto.PatientRegisterDto;
import com.example.demo.dto.PatientResponseDto;
import com.example.demo.entities.Patient;
import com.example.demo.entities.User;
import com.example.demo.model.Role;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.PatientServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PatientServiceImpl patientService;

    private PatientRegisterDto dto;
    private User user;
    private Patient patient;

    @BeforeEach
    void setUp() {
        dto = new PatientRegisterDto();
        dto.setUsername("amit123");
        dto.setPassword("pass123");
        dto.setName("Amit");
        dto.setAge(30);
        dto.setGender("Male");
        dto.setMobile("1234567890");
        dto.setAddress("City XYZ");

        user = new User();
        user.setId(10L);
        user.setUsername("amit123");
        user.setPassword("encodedPass");
        user.setRole(Role.ROLE_PATIENT);

        patient = new Patient();
        patient.setId(1L);
        patient.setName("Amit");
        patient.setAge(30);
        patient.setGender("Male");
        patient.setMobile("1234567890");
        patient.setAddress("City XYZ");
        patient.setUser(user);
        patient.setActive(true);
    }

    // =========================
    // registerPatient
    // =========================
    @Test
    void registerPatient_success() {

        when(userRepository.existsByUsername("amit123")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientResponseDto response = patientService.registerPatient(dto);

        assertNotNull(response);
        assertEquals("Amit", response.getName());
        assertEquals("amit123", response.getUsername());
        assertTrue(response.isActive());

        verify(userRepository).saveAndFlush(any(User.class));
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void registerPatient_usernameAlreadyTaken() {

        when(userRepository.existsByUsername("amit123")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> patientService.registerPatient(dto));

        assertEquals("Username already taken", ex.getMessage());
    }

    // =========================
    // getAllPatients
    // =========================
    @Test
    void getAllPatients_onlyActiveReturned() {

        Patient inactive = new Patient();
        inactive.setActive(false);

        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient, inactive));

        List<PatientResponseDto> list = patientService.getAllPatients();

        assertEquals(1, list.size());
        assertEquals("Amit", list.get(0).getName());
    }

    // =========================
    // getPatientById
    // =========================
    @Test
    void getPatientById_success() {

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        PatientResponseDto response = patientService.getPatientById(1L);

        assertEquals("Amit", response.getName());
    }

    @Test
    void getPatientById_notFound() {

        when(patientRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.getPatientById(2L));
    }

    @Test
    void getPatientById_inactive() {

        patient.setActive(false);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.getPatientById(1L));
    }

    // =========================
    // deletePatient
    // =========================
    @Test
    void deletePatient_success() {

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.saveAndFlush(any(Patient.class)))
                .thenAnswer(i -> i.getArgument(0));

        patientService.deletePatient(1L);

        assertFalse(patient.isActive());
        verify(patientRepository).saveAndFlush(patient);
    }

    // =========================
    // getLoggedInPatient
    // =========================
    @Test
    void getLoggedInPatient_success() {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "amit123",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_PATIENT"))
                )
        );
        SecurityContextHolder.setContext(context);

        when(patientRepository.findByUserUsername("amit123"))
                .thenReturn(Optional.of(patient));

        PatientResponseDto response = patientService.getLoggedInPatient();

        assertEquals("Amit", response.getName());
        assertTrue(response.isActive());
    }

    @Test
    void getLoggedInPatient_notFound() {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "unknown",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_PATIENT"))
                )
        );
        SecurityContextHolder.setContext(context);

        when(patientRepository.findByUserUsername("unknown"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> patientService.getLoggedInPatient());

        assertEquals("Patient not found", ex.getMessage());
    }

    @Test
    void getLoggedInPatient_inactive() {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "amit123",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_PATIENT"))
                )
        );
        SecurityContextHolder.setContext(context);

        patient.setActive(false);

        when(patientRepository.findByUserUsername("amit123"))
                .thenReturn(Optional.of(patient));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> patientService.getLoggedInPatient());

        assertEquals("Patient is deactivated", ex.getMessage());
    }
}
