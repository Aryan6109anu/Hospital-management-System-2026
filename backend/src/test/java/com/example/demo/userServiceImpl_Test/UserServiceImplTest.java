package com.example.demo.userServiceImpl_Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entities.User;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.services.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterRequest registerRequest;
    private AuthRequest authRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("amit");
        registerRequest.setPassword("pass123");
        registerRequest.setEmail("amit@example.com");
        registerRequest.setRole(Role.ROLE_PATIENT);

        authRequest = new AuthRequest();
        authRequest.setUsername("amit");
        authRequest.setPassword("pass123");
        authRequest.setRole(Role.ROLE_PATIENT);

        user = new User();
        user.setId(101L); // ✅ VERY IMPORTANT
        user.setUsername("amit");
        user.setPassword("encodedPass");
        user.setRole(Role.ROLE_PATIENT);
        user.setEmail("amit@example.com");
    }

    // =====================
    // REGISTER TESTS
    // =====================
    @Test
    void register_success() {
        when(userRepository.existsByUsername("amit")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = (User) userService.register(registerRequest);

        assertNotNull(savedUser);
        assertEquals("amit", savedUser.getUsername());
        assertEquals(Role.ROLE_PATIENT, savedUser.getRole());

        verify(userRepository).existsByUsername("amit");
        verify(passwordEncoder).encode("pass123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_usernameAlreadyExists() {
        when(userRepository.existsByUsername("amit")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.register(registerRequest));

        assertEquals("Username already exists", ex.getMessage());

        verify(userRepository).existsByUsername("amit");
        verify(userRepository, never()).save(any());
    }

    // =====================
    // LOGIN TESTS
    // =====================
    @Test
    void login_success() {
        when(userRepository.findByUsername("amit")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);
        when(jwtUtil.generateToken(101L, "amit", "ROLE_PATIENT"))
                .thenReturn("jwt-token");

        AuthResponse response = userService.login(authRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("ROLE_PATIENT", response.getRole());

        verify(jwtUtil).generateToken(101L, "amit", "ROLE_PATIENT");
    }

    @Test
    void login_userNotFound() {
        when(userRepository.findByUsername("amit"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.login(authRequest));

        assertEquals("User not found", ex.getMessage());

        verify(jwtUtil, never()).generateToken(anyLong(), anyString(), anyString());
    }

    @Test
    void login_invalidPassword() {
        when(userRepository.findByUsername("amit"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "encodedPass"))
                .thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.login(authRequest));

        assertEquals("Username or password is incorrect", ex.getMessage());

        verify(jwtUtil, never()).generateToken(anyLong(), anyString(), anyString());
    }
}
