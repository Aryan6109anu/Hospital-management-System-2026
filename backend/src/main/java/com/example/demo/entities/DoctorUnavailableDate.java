package com.example.demo.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DoctorUnavailableDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "unavailable_date", nullable = false)
    private LocalDate unavailableDate;

    private String reason;

	public DoctorUnavailableDate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DoctorUnavailableDate(Long id, Doctor doctor, LocalDate unavailableDate, String reason) {
		super();
		this.id = id;
		this.doctor = doctor;
		this.unavailableDate = unavailableDate;
		this.reason = reason;
	}
    // getters setters

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

	public LocalDate getUnavailableDate() {
		return unavailableDate;
	}

	public void setUnavailableDate(LocalDate unavailableDate) {
		this.unavailableDate = unavailableDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
}
