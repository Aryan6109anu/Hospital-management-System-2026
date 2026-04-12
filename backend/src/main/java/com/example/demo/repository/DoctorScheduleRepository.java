package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.DoctorSchedule;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    // ✅ Find schedule by doctor
    Optional<DoctorSchedule> findByDoctorId(Long doctorId);

    // ✅ Delete schedule by doctor (for CRUD)
    void deleteByDoctorId(Long doctorId);

    
    
}
