package com.example.demo.dto;

import java.util.List;

public class LabOrderResponse {

    private Long labOrderId;

    private Long patientId;

    private String patientName;

    private Long appointmentId;

    private String status;

    private List<String> tests;

	public Long getLabOrderId() {
		return labOrderId;
	}

	public void setLabOrderId(Long labOrderId) {
		this.labOrderId = labOrderId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getTests() {
		return tests;
	}

	public void setTests(List<String> tests) {
		this.tests = tests;
	}
    
    
    

}