package com.example.demo.services;

import com.example.demo.dto.DoctorUnavailableRequest;

public interface DoctorUnavailableService {

    void markUnavailable(DoctorUnavailableRequest request);
}
