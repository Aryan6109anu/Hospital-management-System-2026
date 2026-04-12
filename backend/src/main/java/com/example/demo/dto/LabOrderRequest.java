package com.example.demo.dto;

import java.util.List;

public class LabOrderRequest {

    private Long patientId;

    private Long appointmentId;

    private List<Long> labTestIds;

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public List<Long> getLabTestIds() {
		return labTestIds;
	}

	public void setLabTestIds(List<Long> labTestIds) {
		this.labTestIds = labTestIds;
	}

    
}