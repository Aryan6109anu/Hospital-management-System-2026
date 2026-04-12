package com.example.demo.mapper;

import com.example.demo.dto.AppointmentResponse;
import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;

public class AppointmentMapper {

    public static AppointmentResponse toResponse(Appointment appointment) {

        if (appointment == null) {
            return new AppointmentResponse();
        }

        AppointmentResponse response = new AppointmentResponse();

        // Basic Info
        response.setId(appointment.getId());
        response.setDisease(appointment.getDisease());
        response.setStatus(appointment.getStatus());

        // ===== PATIENT INFO =====
        Patient patient = appointment.getPatient();
        if (patient != null) {
            response.setPatientId(patient.getId());
            response.setPatientName(patient.getName());
            response.setPatientMobile(patient.getMobile());
            response.setPatientGender(patient.getGender());
            response.setPatientAge(patient.getAge());
            response.setPatientAddress(patient.getAddress());
        }

        // ===== DOCTOR INFO =====
        Doctor doctor = appointment.getDoctor();
        if (doctor != null) {
            response.setDoctorId(doctor.getId());
            response.setDoctorName(doctor.getName());
            response.setDoctorDepartment(doctor.getDepartment()); // Department added
            response.setSpecialization(doctor.getSpecialization());
        }

        // ===== APPOINTMENT / SLOT INFO (FIXED) =====
        
        // Entity me 'appointmentDate' hai aur DTO me bhi humne yahi rakha hai
        response.setAppointmentDate(appointment.getAppointmentDate()); 
        
        response.setSlotStartTime(appointment.getSlotStartTime());
        response.setSlotEndTime(appointment.getSlotEndTime());
        
        // DTO me slotId @NotNull hai, isliye Appointment ID pass kar rahe hain
        response.setSlotId(appointment.getId());

        return response;
    }
}