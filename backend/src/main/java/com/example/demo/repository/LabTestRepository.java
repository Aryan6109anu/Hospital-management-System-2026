package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.LabTest;

public interface LabTestRepository extends JpaRepository<LabTest, Long> {

    List<LabTest> findBySectionId(Long sectionId);

}