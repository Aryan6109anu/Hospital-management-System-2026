package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.AppointmentStaff;

@Repository
public interface AppointmentStaffRepository extends JpaRepository<AppointmentStaff, Long> {

    Optional<AppointmentStaff> findByMobile(String mobile);

    Optional<AppointmentStaff> findByEmail(String email);

    boolean existsByMobile(String mobile);

    boolean existsByEmail(String email);
    
    Optional<AppointmentStaff> findByUserUsername(String username);
}