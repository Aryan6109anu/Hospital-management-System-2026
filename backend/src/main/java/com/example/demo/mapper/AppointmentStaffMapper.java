package com.example.demo.mapper;

import com.example.demo.dto.AppointmentStaffDto;
import com.example.demo.entities.AppointmentStaff;
import com.example.demo.entities.User;

public class AppointmentStaffMapper {

    public static AppointmentStaffDto toDto(AppointmentStaff staff) {

        if (staff == null) return null;

        AppointmentStaffDto dto = new AppointmentStaffDto();

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

    public static AppointmentStaff toEntity(AppointmentStaffDto dto, User user) {

        if (dto == null) return null;

        AppointmentStaff staff = new AppointmentStaff();

        staff.setFullName(dto.getFullName());
        staff.setAge(dto.getAge());
        staff.setGender(dto.getGender());
        staff.setMobile(dto.getMobile());
        staff.setAadhaarNumber(dto.getAadhaarNumber());
        staff.setAddress(dto.getAddress());
        staff.setEmail(dto.getEmail());

        staff.setUser(user); // IMPORTANT

        return staff;
    }
}