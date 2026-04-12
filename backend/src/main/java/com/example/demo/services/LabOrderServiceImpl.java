package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.LabOrderRequest;
import com.example.demo.dto.LabOrderResponse;
import com.example.demo.entities.Appointment;
import com.example.demo.entities.LabOrder;
import com.example.demo.entities.LabOrderItem;
import com.example.demo.entities.LabTest;
import com.example.demo.entities.Patient;
import com.example.demo.mapper.LabMapper;
import com.example.demo.model.LabOrderStatus;
import com.example.demo.model.LabTestStatus;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.LabOrderRepository;
import com.example.demo.repository.LabTestRepository;
import com.example.demo.repository.PatientRepository;

@Service
public class LabOrderServiceImpl implements LabOrderService {

    private final LabOrderRepository orderRepo;
    private final LabTestRepository testRepo;
    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;

    public LabOrderServiceImpl(
            LabOrderRepository orderRepo,
            LabTestRepository testRepo,
            PatientRepository patientRepo,
            AppointmentRepository appointmentRepo
    ) {
        this.orderRepo = orderRepo;
        this.testRepo = testRepo;
        this.patientRepo = patientRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @Override
    public LabOrderResponse createOrder(LabOrderRequest request) {

        Patient patient = patientRepo.findById(request.getPatientId()).orElseThrow();

        Appointment appointment = appointmentRepo
                .findById(request.getAppointmentId())
                .orElseThrow();

        LabOrder order = new LabOrder();

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(LabOrderStatus.PENDING);
        order.setPatient(patient);
        order.setAppointment(appointment);

        List<LabOrderItem> items = new ArrayList<>();

        for(Long testId : request.getLabTestIds()){

            LabTest test = testRepo.findById(testId).orElseThrow();

            LabOrderItem item = new LabOrderItem();
            item.setLabOrder(order);
            item.setLabTest(test);
            item.setStatus(LabTestStatus.PENDING);

            items.add(item);
        }

        order.setItems(items);

        orderRepo.save(order);

        return LabMapper.toResponse(order);
    }

    @Override
    public List<LabOrderResponse> getByPatient(Long patientId) {

        return orderRepo.findByPatientId(patientId)
                .stream()
                .map(LabMapper::toResponse)
                .toList();
    }

    @Override
    public List<LabOrderResponse> getByAppointment(Long appointmentId) {

        return orderRepo.findByAppointmentId(appointmentId)
                .stream()
                .map(LabMapper::toResponse)
                .toList();
    }

    @Override
    public List<LabOrderResponse> getAllOrders() {

        return orderRepo.findAll()
                .stream()
                .map(LabMapper::toResponse)
                .toList();

    }
}