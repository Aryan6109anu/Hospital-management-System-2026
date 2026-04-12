package com.example.demo.mapper;

import com.example.demo.dto.PharmacyStaffDto;
import com.example.demo.entities.PharmacyStaff;
import com.example.demo.entities.User;

public class PharmacyStaffMapper {

    // 🔹 Entity → DTO
    public static PharmacyStaffDto toDto(PharmacyStaff staff) {

        if (staff == null) return null;

        PharmacyStaffDto dto = new PharmacyStaffDto();

        dto.setId(staff.getId());
        dto.setFullName(staff.getFullName());
        dto.setAge(staff.getAge());
        dto.setGender(staff.getGender());
        dto.setMobile(staff.getMobile());
        dto.setAadhaarNumber(staff.getAadhaarNumber());
        dto.setAddress(staff.getAddress());
        dto.setEmail(staff.getEmail());

        if (staff.getUser() != null) {
            dto.setUsername(staff.getUser().getUsername());
        }

        return dto;
    }

    // 🔹 DTO → Entity
    // User service layer se pass hoga
    public static PharmacyStaff toEntity(PharmacyStaffDto dto, User user) {

        if (dto == null) return null;

        PharmacyStaff staff = new PharmacyStaff();

        staff.setFullName(dto.getFullName());
        staff.setAge(dto.getAge());
        staff.setGender(dto.getGender());
        staff.setMobile(dto.getMobile());
        staff.setAadhaarNumber(dto.getAadhaarNumber());
        staff.setAddress(dto.getAddress());
        staff.setEmail(dto.getEmail());

        staff.setUser(user);

        return staff;
    }
}