package com.example.demo.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.example.demo.dto.LabOrderRequest;
import com.example.demo.dto.LabOrderResponse;
import com.example.demo.services.LabOrderService;

@RestController
@RequestMapping("/api/lab-orders")
public class LabOrderController {

    private final LabOrderService service;

    public LabOrderController(LabOrderService service) {
        this.service = service;
    }

    /* ================= CREATE LAB ORDER ================= */

    @PostMapping
    public LabOrderResponse create(@RequestBody LabOrderRequest request){
        return service.createOrder(request);
    }

    /* ================= GET ALL LAB ORDERS ================= */

    @GetMapping
    public List<LabOrderResponse> getAllOrders(){
        return service.getAllOrders();
    }

    /* ================= SEARCH BY PATIENT ================= */

    @GetMapping("/patient/{patientId}")
    public List<LabOrderResponse> getByPatient(@PathVariable Long patientId){
        return service.getByPatient(patientId);
    }

    /* ================= SEARCH BY APPOINTMENT ================= */

    @GetMapping("/appointment/{appointmentId}")
    public List<LabOrderResponse> getByAppointment(@PathVariable Long appointmentId){
        return service.getByAppointment(appointmentId);
    }

}