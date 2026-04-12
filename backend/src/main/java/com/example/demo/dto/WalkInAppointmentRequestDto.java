package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

public class WalkInAppointmentRequestDto {

    // ======================
    // Patient basic info
    // ======================
    private String patientName;
    private Integer age;
    private String gender;
    private String mobile;
    private String address;
    private String disease;

    // ======================
    // Appointment info
    // ======================
    @NotNull
    private Long doctorId;

    // 👉 Option 1: Slot by ID (preferred)
    private Long slotId;

    // 👉 Option 2: Slot by date + time (walk-in counter use)
    private LocalDate appointmentDate;     // ✅ FIX: earlier missing
    @JsonFormat(pattern = "hh:mm a")
    private LocalTime slotStartTime;            // ✅ FIX: earlier missing
    @JsonFormat(pattern = "hh:mm a")
    private LocalTime slotEndTime;

    // ======================
    // Constructors
    // ======================
    public WalkInAppointmentRequestDto() {
        super();
    }

	public WalkInAppointmentRequestDto(String patientName, Integer age, String gender, String mobile, String address,
			String disease, @NotNull Long doctorId, Long slotId, LocalDate appointmentDate, LocalTime slotStartTime,
			LocalTime slotEndTime) {
		super();
		this.patientName = patientName;
		this.age = age;
		this.gender = gender;
		this.mobile = mobile;
		this.address = address;
		this.disease = disease;
		this.doctorId = doctorId;
		this.slotId = slotId;
		this.appointmentDate = appointmentDate;
		this.slotStartTime = slotStartTime;
		this.slotEndTime = slotEndTime;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	
}
