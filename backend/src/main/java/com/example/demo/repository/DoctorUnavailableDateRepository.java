package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.DoctorUnavailableDate;

public interface DoctorUnavailableDateRepository extends JpaRepository<DoctorUnavailableDate, Long> {

boolean existsByDoctorIdAndUnavailableDate(
    Long doctorId, LocalDate unavailableDate);

List<DoctorUnavailableDate> findByDoctorId(Long doctorId);
}
