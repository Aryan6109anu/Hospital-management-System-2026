package com.example.demo.mapper;

import com.example.demo.dto.BillingStaffDto;
import com.example.demo.entities.BillingStaff;
import com.example.demo.entities.User;

public class BillingStaffMapper {

    // 🔹 Entity → DTO
    public static BillingStaffDto toDto(BillingStaff staff) {

        if (staff == null) return null;

        BillingStaffDto dto = new BillingStaffDto();

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
    // User ko service layer se pass karenge
    public static BillingStaff toEntity(BillingStaffDto dto, User user) {

        if (dto == null) return null;

        BillingStaff staff = new BillingStaff();

        staff.setFullName(dto.getFullName());
        staff.setAge(dto.getAge());
        staff.setGender(dto.getGender());
        staff.setMobile(dto.getMobile());
        staff.setAadhaarNumber(dto.getAadhaarNumber());
        staff.setAddress(dto.getAddress());
        staff.setEmail(dto.getEmail());

        staff.setUser(user);  // IMPORTANT

        return staff;
    }
}