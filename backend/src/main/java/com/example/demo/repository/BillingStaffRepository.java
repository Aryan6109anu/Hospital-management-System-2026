package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.BillingStaff;

@Repository
public interface BillingStaffRepository extends JpaRepository<BillingStaff, Long> {

    Optional<BillingStaff> findByMobile(String mobile);

    Optional<BillingStaff> findByEmail(String email);

    boolean existsByMobile(String mobile);

    boolean existsByEmail(String email);
    
    Optional<BillingStaff> findByUserUsername(String username);
}