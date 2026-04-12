package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.PatientDTO;
import com.example.demo.dto.PatientRegisterDto;
import com.example.demo.dto.PatientResponseDto;


import jakarta.validation.Valid;

public interface PatientService {

    // ✅ Patient self-registration (public)
    PatientResponseDto registerPatient(PatientRegisterDto dto);

    // ✅ Admin: read all active patients
    List<PatientResponseDto> getAllPatients();

    // ✅ Admin: read patient by ID
    PatientResponseDto getPatientById(Long id);

    // ✅ Admin: soft delete patient
    void deletePatient(Long id);

    // ✅ Patient: get current logged-in patient (via JWT)
    PatientResponseDto getLoggedInPatient();
     // Patient Update Own Profile 
	PatientResponseDto updateMyProfile(String name, @Valid PatientDTO dto);
}
