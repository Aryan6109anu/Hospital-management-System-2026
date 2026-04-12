package com.example.demo.services;

import com.example.demo.dto.*;
import java.util.List;

public interface LabOrderService {

    LabOrderResponse createOrder(LabOrderRequest request);

    List<LabOrderResponse> getByPatient(Long patientId);

    List<LabOrderResponse> getByAppointment(Long appointmentId);
    
    List<LabOrderResponse> getAllOrders();

}