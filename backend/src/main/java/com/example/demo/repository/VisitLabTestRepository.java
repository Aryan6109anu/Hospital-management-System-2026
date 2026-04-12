package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.VisitLabTest;

public interface VisitLabTestRepository extends JpaRepository<VisitLabTest, Long> {
	
      List<VisitLabTest> findByVisitId(Long visitId);
      
      void deleteByVisitId(Long visitId);
      
}