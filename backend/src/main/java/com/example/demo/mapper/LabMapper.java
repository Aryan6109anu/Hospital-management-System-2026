package com.example.demo.mapper;

import java.util.stream.Collectors;

import com.example.demo.dto.LabOrderResponse;
import com.example.demo.entities.LabOrder;

public class LabMapper {

    public static LabOrderResponse toResponse(LabOrder order){

        LabOrderResponse res = new LabOrderResponse();

        res.setLabOrderId(order.getId());
        res.setPatientId(order.getPatient().getId());
        res.setPatientName(order.getPatient().getName());
        res.setAppointmentId(order.getAppointment().getId());
        res.setStatus(order.getStatus().name());

        res.setTests(
                order.getItems()
                        .stream()
                        .map(i -> i.getLabTest().getName())
                        .collect(Collectors.toList())
        );

        return res;
    }

}