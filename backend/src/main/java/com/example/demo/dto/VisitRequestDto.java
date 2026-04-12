package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.Department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VisitRequestDto {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull(message = "Department is required")
    private Department department;   // ✅ NEW

    @NotBlank
    private String chiefComplaint;

    private String pastHistory;
    private String currentMedication;

    @NotBlank
    private String diagnosis;

    private String notes;
    
    private List<Long> labTestIds;

    private List<VisitMedicineDto> medicines;

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

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
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

	public List<Long> getLabTestIds() {
		return labTestIds;
	}

	public void setLabTestIds(List<Long> labTestIds) {
		this.labTestIds = labTestIds;
	}

	public List<VisitMedicineDto> getMedicines() {
		return medicines;
	}

	public void setMedicines(List<VisitMedicineDto> medicines) {
		this.medicines = medicines;
	}
     
  
   
}
