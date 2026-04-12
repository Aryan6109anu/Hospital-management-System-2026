package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.LabStaffDto;

public interface LabStaffService {

    LabStaffDto createStaff(LabStaffDto dto);

    LabStaffDto updateStaff(Long id, LabStaffDto dto);

    LabStaffDto getById(Long id);

    List<LabStaffDto> getAll();

    LabStaffDto getByUsername(String username);

    void deleteById(Long id);
}