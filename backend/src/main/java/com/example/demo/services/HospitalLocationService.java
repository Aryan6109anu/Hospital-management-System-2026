package com.example.demo.services;

import com.example.demo.dto.HospitalLocationDTO;

public interface HospitalLocationService {

    HospitalLocationDTO getLocation();

    HospitalLocationDTO saveOrUpdateLocation(HospitalLocationDTO locationDTO);
}
