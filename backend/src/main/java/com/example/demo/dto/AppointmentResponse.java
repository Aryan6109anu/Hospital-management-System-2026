package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import com.example.demo.model.AppointmentStatus;
import com.example.demo.model.Department;
import com.fasterxml.jackson.annotation.JsonFormat;

public class AppointmentResponse {
    private Long id;
    
    private Long visitId;

    // PATIENT INFO
    private Long patientId;
    private String patientName;
    private String patientMobile;
    private String patientGender;
    private Integer patientAge;
    private String patientAddress;
    
    private String disease;

    // DOCTOR INFO
    private Long doctorId;
    private String doctorName;
    private Department doctorDepartment;
    private String specialization;

    // APPOINTMENT INFO
    private Long slotId; // Filhal isme Appointment ID hi bhejenge
    private LocalDate appointmentDate; 

    @JsonFormat(pattern = "hh:mm a")
    private LocalTime slotStartTime;
    
    @JsonFormat(pattern = "hh:mm a")
    private LocalTime slotEndTime;
    
    private AppointmentStatus status;
    
    // Constructors, Getters aur Setters (Zaroor generate karein)

	public AppointmentResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppointmentResponse(Long id, Long visitId, Long patientId, String patientName, String patientMobile,
			String patientGender, Integer patientAge, String patientAddress, String disease, Long doctorId,
			String doctorName, Department doctorDepartment, String specialization, Long slotId,
			LocalDate appointmentDate, LocalTime slotStartTime, LocalTime slotEndTime, AppointmentStatus status) {
		super();
		this.id = id;
		this.visitId = visitId;
		this.patientId = patientId;
		this.patientName = patientName;
		this.patientMobile = patientMobile;
		this.patientGender = patientGender;
		this.patientAge = patientAge;
		this.patientAddress = patientAddress;
		this.disease = disease;
		this.doctorId = doctorId;
		this.doctorName = doctorName;
		this.doctorDepartment = doctorDepartment;
		this.specialization = specialization;
		this.slotId = slotId;
		this.appointmentDate = appointmentDate;
		this.slotStartTime = slotStartTime;
		this.slotEndTime = slotEndTime;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
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

	public String getPatientMobile() {
		return patientMobile;
	}

	public void setPatientMobile(String patientMobile) {
		this.patientMobile = patientMobile;
	}

	public String getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(String patientGender) {
		this.patientGender = patientGender;
	}

	public Integer getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(Integer patientAge) {
		this.patientAge = patientAge;
	}

	public String getPatientAddress() {
		return patientAddress;
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Department getDoctorDepartment() {
		return doctorDepartment;
	}

	public void setDoctorDepartment(Department doctorDepartment) {
		this.doctorDepartment = doctorDepartment;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public Long getSlotId() {
		return slotId;
	}

	public void setSlotId(Long slotId) {
		this.slotId = slotId;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
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

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	
 
    
    
   
}