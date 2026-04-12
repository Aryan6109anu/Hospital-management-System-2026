package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.BillingStaffDto;

public interface BillingStaffService {

    BillingStaffDto createStaff(BillingStaffDto dto);

    BillingStaffDto updateStaff(Long id, BillingStaffDto dto);

    BillingStaffDto getById(Long id);

    List<BillingStaffDto> getAll();
    
    BillingStaffDto getByUsername(String username);

    void deleteById(Long id);
}