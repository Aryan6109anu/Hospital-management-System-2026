package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.HospitalLocation;

@Repository
public interface HospitalLocationRepository extends JpaRepository<HospitalLocation, Long> {
}
