package com.example.demo.dto;

import java.time.LocalTime;
import java.util.List;

import com.example.demo.model.WorkingDay;
import com.fasterxml.jackson.annotation.JsonFormat;

public class DoctorScheduleRequest {

    private Long doctorId;

    private List<WorkingDay> days;
    
    @JsonFormat(pattern = "hh:mm a")
    private LocalTime startTime;
    @JsonFormat(pattern = "hh:mm a")
    private LocalTime endTime;
    
    private int slotDuration;

    private Integer maxPatientsPerDay;

	public DoctorScheduleRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DoctorScheduleRequest(Long doctorId, List<WorkingDay> days, LocalTime startTime, LocalTime endTime,
			int slotDuration, Integer maxPatientsPerDay) {
		super();
		this.doctorId = doctorId;
		this.days = days;
		this.startTime = startTime;
		this.endTime = endTime;
		this.slotDuration = slotDuration;
		this.maxPatientsPerDay = maxPatientsPerDay;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public List<WorkingDay> getDays() {
		return days;
	}

	public void setDays(List<WorkingDay> days) {
		this.days = days;
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

	public int getSlotDuration() {
		return slotDuration;
	}

	public void setSlotDuration(int slotDuration) {
		this.slotDuration = slotDuration;
	}

	public Integer getMaxPatientsPerDay() {
		return maxPatientsPerDay;
	}

	public void setMaxPatientsPerDay(Integer maxPatientsPerDay) {
		this.maxPatientsPerDay = maxPatientsPerDay;
	}

	
}
