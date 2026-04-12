package com.example.demo.services;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.dto.DoctorScheduleRequest;
import com.example.demo.dto.DoctorSlotResponse;
import com.example.demo.entities.DoctorSchedule;

public interface DoctorScheduleService {

    // ===== CRUD =====
    DoctorSchedule create(DoctorScheduleRequest request);
    
    List<DoctorSchedule> createBulk(List<DoctorScheduleRequest> requests);

    DoctorSchedule update(Long doctorId, DoctorScheduleRequest request);

    DoctorSchedule getByDoctorId(Long doctorId);

    List<DoctorSchedule> getAll();

    void deleteByDoctorId(Long doctorId);

    // ===== EXTRA =====
    boolean isDoctorAvailable(Long doctorId, LocalDate date);

    List<DoctorSlotResponse> getAvailableSlots(Long doctorId, LocalDate date);
   
}
