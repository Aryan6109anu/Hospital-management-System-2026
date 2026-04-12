package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.AppointmentResponse;
import com.example.demo.dto.AppointmentStaffDto;

public interface AppointmentStaffService {

    AppointmentStaffDto createStaff(AppointmentStaffDto dto);

    AppointmentStaffDto updateStaff(Long id, AppointmentStaffDto dto);

    AppointmentStaffDto getById(Long id);

    List<AppointmentStaffDto> getAll();

    void deleteById(Long id);
    // 🔥 Logged-in staff
    AppointmentStaffDto getByUsername(String username);
    
    List<AppointmentResponse> getAppointmentsByStaffUsername(String username);
}