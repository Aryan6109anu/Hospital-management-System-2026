package com.example.demo.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserProfileDto;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // ✅ REGISTER (NO TOKEN)
    // =========================
    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(
                userService.register(request)
        );
    }

    // =========================
    // ✅ LOGIN (RETURNS TOKEN)
    // =========================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request) {

        return ResponseEntity.ok(
                userService.login(request)
        );
    }

    // =========================
    // ✅ GET LOGGED-IN USER
    // =========================
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    public ResponseEntity<UserProfileDto> getMyProfile(
            Principal principal) {

        return ResponseEntity.ok(
                userService.getLoggedInUser(
                        principal.getName()
                )
        );
    }
    
    @GetMapping("/test-role")
    public String testRole() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("User: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());

        return auth.getAuthorities().toString();
    }
}
