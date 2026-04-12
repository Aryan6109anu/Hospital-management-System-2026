package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.LabSection;

public interface LabSectionRepository extends JpaRepository<LabSection, Long> {

    List<LabSection> findByDepartmentId(Long departmentId);

}