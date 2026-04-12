package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.LabDepartment;

public interface LabDepartmentRepository extends JpaRepository<LabDepartment, Long> {
}