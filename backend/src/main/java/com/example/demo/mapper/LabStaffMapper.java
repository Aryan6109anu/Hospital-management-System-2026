package com.example.demo.mapper;

import com.example.demo.dto.LabStaffDto;
import com.example.demo.entities.LabStaff;

public class LabStaffMapper {

    public static LabStaffDto toDto(LabStaff staff) {
  
        LabStaffDto dto = new LabStaffDto();

        dto.setId(staff.getId());
        dto.setFullName(staff.getFullName());
        dto.setAge(staff.getAge());
        dto.setGender(staff.getGender());
        dto.setMobile(staff.getMobile());
        dto.setAadhaarNumber(staff.getAadhaarNumber());
        dto.setAddress(staff.getAddress());
        dto.setEmail(staff.getEmail());
        dto.setUsername(staff.getUser().getUsername());

        return dto;
    }
}