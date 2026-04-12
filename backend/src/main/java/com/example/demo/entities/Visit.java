package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.Department;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@Entity
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String chiefComplaint;

    @Column(length = 500)
    private String pastHistory;

    @Column(length = 500)
    private String currentMedication;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String diagnosis;

    @Column(length = 1000)
    private String notes;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime visitDate = LocalDateTime.now();
    
    
 // Visit.java
    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitLabTest> labTests = new ArrayList<>();

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitMedicine> medicines = new ArrayList<>();

    // =========================
    // ✅ NEW: Department field
    // =========================
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    // ===== Constructors =====
    public Visit() {}

	public Visit(Long id, Patient patient, Doctor doctor, Appointment appointment, @NotBlank String chiefComplaint,
			String pastHistory, String currentMedication, @NotBlank String diagnosis, String notes,
			@NotNull LocalDateTime visitDate, List<VisitLabTest> labTests, List<VisitMedicine> medicines,
			Department department) {
		super();
		this.id = id;
		this.patient = patient;
		this.doctor = doctor;
		this.appointment = appointment;
		this.chiefComplaint = chiefComplaint;
		this.pastHistory = pastHistory;
		this.currentMedication = currentMedication;
		this.diagnosis = diagnosis;
		this.notes = notes;
		this.visitDate = visitDate;
		this.labTests = labTests;
		this.medicines = medicines;
		this.department = department;
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

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public String getChiefComplaint() {
		return chiefComplaint;
	}

	public void setChiefComplaint(String chiefComplaint) {
		this.chiefComplaint = chiefComplaint;
	}

	public String getPastHistory() {
		return pastHistory;
	}

	public void setPastHistory(String pastHistory) {
		this.pastHistory = pastHistory;
	}

	public String getCurrentMedication() {
		return currentMedication;
	}

	public void setCurrentMedication(String currentMedication) {
		this.currentMedication = currentMedication;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(LocalDateTime visitDate) {
		this.visitDate = visitDate;
	}

	public List<VisitLabTest> getLabTests() {
		return labTests;
	}

	public void setLabTests(List<VisitLabTest> labTests) {
		this.labTests = labTests;
	}

	public List<VisitMedicine> getMedicines() {
		return medicines;
	}

	public void setMedicines(List<VisitMedicine> medicines) {
		this.medicines = medicines;
	}

	public Department getDepartment() {
		return department; 
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

  
}
