package com.example.demo.dto;

import java.time.LocalTime;
import java.util.List;

import com.example.demo.model.WorkingDay;

public class DoctorScheduleDto {

    private LocalTime startTime;
    private LocalTime endTime;
    private List<WorkingDay> days;

    public DoctorScheduleDto(LocalTime startTime,
                             LocalTime endTime,
                             List<WorkingDay> days) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
    }
    // getters/setters

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

	public List<WorkingDay> getDays() {
		return days;
	}

	public void setDays(List<WorkingDay> days) {
		this.days = days;
	}
    
    
}
