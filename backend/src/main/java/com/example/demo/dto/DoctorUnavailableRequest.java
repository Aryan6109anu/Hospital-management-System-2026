package com.example.demo.dto;

import java.time.LocalDate;

public class DoctorUnavailableRequest {

    private Long doctorId;
    private LocalDate date; // calendar se
    private String reason;
   
    // getters setters
	public Long getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

}
