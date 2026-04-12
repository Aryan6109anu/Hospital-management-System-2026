package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DoctorUnavailableRequest;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.DoctorUnavailableDate;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.DoctorUnavailableDateRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DoctorUnavailableServiceImpl
        implements DoctorUnavailableService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private DoctorUnavailableDateRepository unavailableRepo;

    @Override
    public void markUnavailable(DoctorUnavailableRequest req) {

        Doctor doctor = doctorRepo.findById(req.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        DoctorUnavailableDate off = new DoctorUnavailableDate();
        off.setDoctor(doctor);
        off.setUnavailableDate(req.getDate());
        off.setReason(req.getReason());

        unavailableRepo.save(off);
    }
}
