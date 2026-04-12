package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.LabStaff;

@Repository
public interface LabStaffRepository extends JpaRepository<LabStaff, Long> {

    Optional<LabStaff> findByMobile(String mobile);

    Optional<LabStaff> findByEmail(String email);

    boolean existsByMobile(String mobile);

    boolean existsByEmail(String email);

    Optional<LabStaff> findByUserUsername(String username);
}