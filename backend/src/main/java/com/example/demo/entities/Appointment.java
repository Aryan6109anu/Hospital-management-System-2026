package com.example.demo.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.model.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import jakarta.persistence.*;


@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Patient
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "appointments"})
    private Patient patient;

    // Doctor
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "appointments", "schedule"})
    private Doctor doctor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_staff_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AppointmentStaff appointmentStaff;

    @Column(nullable = false)
    private String disease;
    
    @JsonFormat(pattern = "hh:mm a")
    private LocalTime slotStartTime;
    
    @JsonFormat(pattern = "hh:mm a")
    private LocalTime slotEndTime;

    // Remove DoctorSlot relation
    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

	public Appointment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Appointment(Long id, Patient patient, Doctor doctor, AppointmentStaff appointmentStaff, String disease,
			LocalTime slotStartTime, LocalTime slotEndTime, LocalDate appointmentDate, AppointmentStatus status) {
		super();
		this.id = id;
		this.patient = patient;
		this.doctor = doctor;
		this.appointmentStaff = appointmentStaff;
		this.disease = disease;
		this.slotStartTime = slotStartTime;
		this.slotEndTime = slotEndTime;
		this.appointmentDate = appointmentDate;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public AppointmentStaff getAppointmentStaff() {
		return appointmentStaff;
	}

	public void setAppointmentStaff(AppointmentStaff appointmentStaff) {
		this.appointmentStaff = appointmentStaff;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public LocalTime getSlotStartTime() {
		return slotStartTime;
	}

	public void setSlotStartTime(LocalTime slotStartTime) {
		this.slotStartTime = slotStartTime;
	}

	public LocalTime getSlotEndTime() {
		return slotEndTime;
	}

	public void setSlotEndTime(LocalTime slotEndTime) {
		this.slotEndTime = slotEndTime;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	
	
}
