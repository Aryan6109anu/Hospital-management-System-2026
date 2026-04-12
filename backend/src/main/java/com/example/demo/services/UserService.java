package com.example.demo.services;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserProfileDto;


public interface UserService {

    Object register(RegisterRequest request);

    AuthResponse login(AuthRequest request);
    
    // ✅ ADD THIS
    UserProfileDto getLoggedInUser(String username);
}
