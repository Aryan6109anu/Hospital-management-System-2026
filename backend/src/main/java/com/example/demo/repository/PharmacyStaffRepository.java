package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.PharmacyStaff;

@Repository
public interface PharmacyStaffRepository extends JpaRepository<PharmacyStaff, Long> {

    Optional<PharmacyStaff> findByMobile(String mobile);

    Optional<PharmacyStaff> findByEmail(String email);

    boolean existsByMobile(String mobile);

    boolean existsByEmail(String email);
    
    Optional<PharmacyStaff> findByUserUsername(String username);
}