package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.DoctorRegisterDto;
import com.example.demo.dto.DoctorResponseDto;
import com.example.demo.dto.DoctorUpdateDto;
import com.example.demo.entities.Doctor;


public interface DoctorService {

    // Admin se doctor register
    DoctorResponseDto registerDoctor(DoctorRegisterDto dto);
    
    // Bulk doctor registration
    List<DoctorResponseDto> registerDoctorsBulk(List<DoctorRegisterDto> dtos);

    // All doctors list
    List<DoctorResponseDto> getAllDoctors();

    // Doctor by id
    DoctorResponseDto getDoctorById(Long id);

    // Update doctor info by Admin
    DoctorResponseDto updateDoctor(Long id, DoctorUpdateDto dto);

    // Delete doctor by Admin
    void deleteDoctor(Long id);

    // Toggle availability
    DoctorResponseDto toggleAvailability(Long id);

    // Doctor login ke baad apna profile fetch
    DoctorResponseDto getProfileByUsername(String username);
    

    // Doctor ko User id se fetch karne ke liye
    Optional<Doctor> findByUserId(Long userId);
    
    // Agar available doctors chahiye
    List<Doctor> findByAvailableTrue();
    
    List<DoctorResponseDto> getAvailableDoctors();
}
