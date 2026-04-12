package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // ✅ Find all appointments for a doctor
    List<Appointment> findByDoctorId(Long doctorId);

    // ✅ Find all appointments for a patient
    List<Appointment> findByPatientId(Long patientId);
      
        // ✅ EXACT MATCH FOR THE SERVICE CALL:
    // Is method ka naam bilkul waisa hi hona chahiye jaisa Service me call ho raha hai
    boolean existsByDoctorIdAndAppointmentDateAndSlotStartTimeAndSlotEndTime(
            Long doctorId, 
            LocalDate appointmentDate, 
            LocalTime slotStartTime, 
            LocalTime slotEndTime
    );
}