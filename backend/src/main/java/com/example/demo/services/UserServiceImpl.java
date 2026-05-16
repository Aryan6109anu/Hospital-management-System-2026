package com.example.demo.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.EmailConfig;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserProfileDto;
import com.example.demo.entities.User;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailConfig emailConfig;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil,
                           EmailConfig emailConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailConfig = emailConfig;
    }

    // ==============================
    // ✅ REGISTER (NO TOKEN HERE)
    // ==============================
    @Override
    public Object register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(
                request.getRole() == null
                        ? Role.ROLE_PATIENT
                        : request.getRole()
        );

        userRepository.save(user);

        System.out.println(
                "✅ USER REGISTERED | USERNAME = "
                        + user.getUsername()
                        + " | ROLE = "
                        + user.getRole()
        );

        return "User registered successfully";
    }

    // ==============================
    // ✅ LOGIN (TOKEN GENERATED HERE)
    // ==============================
    @Override
    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Username or password is incorrect");
        }

        if (!user.getRole().equals(request.getRole())) {
            throw new RuntimeException(
                    "Only " + request.getRole().name().replace("ROLE_", "")
                            + " is allowed to login here"
            );
        }

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );

        return new AuthResponse(
                token,
                user.getRole().name(),
                user.getId()
        );
    }

    // ==============================
    // ✅ GET LOGGED-IN USER
    // ==============================
    @Override
    public UserProfileDto getLoggedInUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileDto dto = new UserProfileDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());

        return dto;
    }
}
