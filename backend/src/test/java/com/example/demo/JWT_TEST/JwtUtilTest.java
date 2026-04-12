package com.example.demo.JWT_TEST;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.Role;
import com.example.demo.security.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() throws Exception {
        jwtUtil = new JwtUtil();

        // 🔐 Inject secret key
        Field secretField = JwtUtil.class.getDeclaredField("secretKey");
        secretField.setAccessible(true);
        secretField.set(jwtUtil, "mysecretkeymysecretkeymysecretkey123");

        // ⏰ Token expiry = 1 minute
        Field expiryField = JwtUtil.class.getDeclaredField("expirationMs");
        expiryField.setAccessible(true);
        expiryField.set(jwtUtil, 60_000L);
    }

    // ===============================
    // TOKEN GENERATION
    // ===============================
    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(
                101L,
                "amit",
                Role.ROLE_PATIENT.name()
        );

        assertNotNull(token);
        assertTrue(token.length() > 20);
    }

    // ===============================
    // EXTRACT USERNAME
    // ===============================
    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken(
                101L,
                "amit",
                Role.ROLE_PATIENT.name()
        );

        String username = jwtUtil.extractUsername(token);

        assertEquals("amit", username);
    }

    // ===============================
    // EXTRACT ROLE
    // ===============================
    @Test
    void testExtractRole() {
        String token = jwtUtil.generateToken(
                202L,
                "amit",
                Role.ROLE_DOCTOR.name()
        );

        String role = jwtUtil.extractRole(token);

        assertEquals(Role.ROLE_DOCTOR.name(), role);
    }

    // ===============================
    // EXTRACT USER ID
    // ===============================
    @Test
    void testExtractUserId() {
        String token = jwtUtil.generateToken(
                101L,
                "amit",
                Role.ROLE_PATIENT.name()
        );

        Long userId = jwtUtil.extractUserId(token);

        assertEquals(101L, userId);
    }

    // ===============================
    // VALID TOKEN
    // ===============================
    @Test
    void testValidateToken_success() {
        String token = jwtUtil.generateToken(
                101L,
                "amit",
                Role.ROLE_PATIENT.name()
        );

        UserDetails user = User
                .withUsername("amit")
                .password("dummy")
                .roles("PATIENT")
                .build();

        boolean isValid = jwtUtil.validateToken(token, user);

        assertTrue(isValid);
    }

    // ===============================
    // EXPIRED TOKEN
    // ===============================
    @Test
    void testExpiredToken() throws Exception {

        // ⛔ expire token immediately
        Field expiryField = JwtUtil.class.getDeclaredField("expirationMs");
        expiryField.setAccessible(true);
        expiryField.set(jwtUtil, -1000L);

        String token = jwtUtil.generateToken(
                101L,
                "amit",
                Role.ROLE_PATIENT.name()
        );

        UserDetails user = User
                .withUsername("amit")
                .password("dummy")
                .roles("PATIENT")
                .build();

        assertThrows(ExpiredJwtException.class, () -> {
            jwtUtil.validateToken(token, user);
        });
    }
}
