package com.example.demo.entities;

import java.time.LocalTime;
import java.util.List;

import com.example.demo.model.WorkingDay;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctor_schedule")
public class DoctorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "doctor_id", nullable = false, unique = true)
    private Doctor doctor;

    @Column(nullable = false)
    @JsonFormat(pattern = "hh:mm a")
    private LocalTime startTime;

    @Column(nullable = false)
    @JsonFormat(pattern = "hh:mm a")
    private LocalTime endTime;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable( 
        name = "doctor_schedule_days",
        joinColumns = @JoinColumn(name = "schedule_id")
    )
    @Column(name = "working_day")  // <- change here
    private List<WorkingDay> days;


    @Column
    private int slotDuration = 30; // ✅ default 30 mins
    
    @Column(nullable = false)
    private Integer maxPatientsPerDay;

	public DoctorSchedule() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DoctorSchedule(Long id, Doctor doctor, LocalTime startTime, LocalTime endTime, List<WorkingDay> days,
			int slotDuration, Integer maxPatientsPerDay) {
		super();
		this.id = id;
		this.doctor = doctor;
		this.startTime = startTime;
		this.endTime = endTime;
		this.days = days;
		this.slotDuration = slotDuration;
		this.maxPatientsPerDay = maxPatientsPerDay;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
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

	public List<WorkingDay> getDays() {
		return days;
	}

	public void setDays(List<WorkingDay> days) {
		this.days = days;
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
