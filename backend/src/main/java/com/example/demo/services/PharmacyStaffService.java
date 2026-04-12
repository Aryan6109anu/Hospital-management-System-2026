package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.PharmacyStaffDto;

public interface PharmacyStaffService {

    PharmacyStaffDto createStaff(PharmacyStaffDto dto);

    PharmacyStaffDto updateStaff(Long id, PharmacyStaffDto dto);

    PharmacyStaffDto getById(Long id);

    List<PharmacyStaffDto> getAll();

    void deleteById(Long id);
    
 // 🔥 Logged-in pharmacy staff
    PharmacyStaffDto getByUsername(String username);
    
    
}