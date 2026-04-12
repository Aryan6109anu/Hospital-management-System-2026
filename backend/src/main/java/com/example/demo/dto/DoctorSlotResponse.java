package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorSlotResponse {

    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean booked; 

    public DoctorSlotResponse() {}

	public DoctorSlotResponse(LocalDate slotDate, LocalTime startTime, LocalTime endTime, boolean booked) {
		super();
		this.slotDate = slotDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.booked = booked;
	}

	public LocalDate getSlotDate() {
		return slotDate;
	}

	public void setSlotDate(LocalDate slotDate) {
		this.slotDate = slotDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public boolean isBooked() {
		return booked;
	}

	public void setBooked(boolean booked) {
		this.booked = booked;
	}

   
}
