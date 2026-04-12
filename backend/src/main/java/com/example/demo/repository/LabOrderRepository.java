package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.LabOrder;

public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {

    List<LabOrder> findByPatientId(Long patientId);

    List<LabOrder> findByAppointmentId(Long appointmentId);

}