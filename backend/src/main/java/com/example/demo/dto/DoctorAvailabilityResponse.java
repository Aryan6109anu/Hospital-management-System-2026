package com.example.demo.dto;

import java.time.LocalDate;

public class DoctorAvailabilityResponse {
 
    private Long doctorId;
    private LocalDate date;
    private boolean available;
    private String message;
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
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

   
}
