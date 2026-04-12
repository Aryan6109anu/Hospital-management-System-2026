package com.example.demo.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class VisitUpdateDto {

    @NotBlank
    private String chiefComplaint;

    private String pastHistory;
    private String currentMedication;

    @NotBlank
    private String diagnosis;

    private String notes;
    
    private List<Long> labTestIds;
    private List<VisitMedicineDto> medicines;

    // ===== Constructors =====
    public VisitUpdateDto() {
        super();
    }

	public VisitUpdateDto(@NotBlank String chiefComplaint, String pastHistory, String currentMedication,
			@NotBlank String diagnosis, String notes, List<Long> labTestIds, List<VisitMedicineDto> medicines) {
		super();
		this.chiefComplaint = chiefComplaint;
		this.pastHistory = pastHistory;
		this.currentMedication = currentMedication;
		this.diagnosis = diagnosis;
		this.notes = notes;
		this.labTestIds = labTestIds;
		this.medicines = medicines;
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
