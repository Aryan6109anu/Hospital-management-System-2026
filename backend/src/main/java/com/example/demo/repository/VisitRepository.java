package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Visit;
import com.example.demo.model.Department;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    // ✅ All visits of a patient
    List<Visit> findByPatientId(Long patientId);

    // ✅ All visits of a doctor
    List<Visit> findByDoctorId(Long doctorId);

    // ✅ Check if patient has any visit (general)
    boolean existsByPatient_Id(Long patientId);

    // ✅ Check if patient already has visit in specific department
    // 🔥 MATCHES ServiceImpl EXACTLY
    boolean existsByPatient_IdAndDepartment(Long patientId, Department department);

    // ✅ Get all visits of a patient in a specific department
    List<Visit> findByPatient_IdAndDepartment(Long patientId, Department department);
    
    boolean existsByAppointment_Id(Long appointmentId);
      
    Optional<Visit> findByAppointment_Id(Long appointmentId);
   
    
}