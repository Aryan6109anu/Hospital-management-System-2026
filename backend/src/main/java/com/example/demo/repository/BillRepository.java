package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByVisitId(Long visitId);
}