package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.VisitMedicine;

public interface VisitMedicineRepository extends JpaRepository<VisitMedicine, Long> {

   List<VisitMedicine> findByVisitId(Long visitId);
   
   void deleteByVisitId(Long visitId);
}