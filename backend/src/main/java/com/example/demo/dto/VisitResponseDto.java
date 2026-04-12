package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class VisitResponseDto {

    private Long id;
    
    private Long appointmentId;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private String doctorDepartment;  // ✅ Department info

    private String chiefComplaint;
    private String pastHistory;
    private String currentMedication;
    private String diagnosis;
    private String notes;
    
    private List<String> labTests;
    private List<String> medicines;
    
    private LocalDateTime visitDate;

    // ===== Constructors =====
    public VisitResponseDto() {
        super();
    }

	public VisitResponseDto(Long id, Long appointmentId, Long patientId, String patientName, Long doctorId,
			String doctorName, String doctorDepartment, String chiefComplaint, String pastHistory,
			String currentMedication, String diagnosis, String notes, List<String> labTests, List<String> medicines,
			LocalDateTime visitDate) {
		super();
		this.id = id;
		this.appointmentId = appointmentId;
		this.patientId = patientId;
		this.patientName = patientName;
		this.doctorId = doctorId;
		this.doctorName = doctorName;
		this.doctorDepartment = doctorDepartment;
		this.chiefComplaint = chiefComplaint;
		this.pastHistory = pastHistory;
		this.currentMedication = currentMedication;
		this.diagnosis = diagnosis;
		this.notes = notes;
		this.labTests = labTests;
		this.medicines = medicines;
		this.visitDate = visitDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
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

	public String getDoctorDepartment() {
		return doctorDepartment;
	}

	public void setDoctorDepartment(String doctorDepartment) {
		this.doctorDepartment = doctorDepartment;
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

	public List<String> getLabTests() {
		return labTests;
	}

	public void setLabTests(List<String> labTests) {
		this.labTests = labTests;
	}

	public List<String> getMedicines() {
		return medicines;
	}

	public void setMedicines(List<String> medicines) {
		this.medicines = medicines;
	}

	public LocalDateTime getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(LocalDateTime visitDate) {
		this.visitDate = visitDate;
	}

	
}
