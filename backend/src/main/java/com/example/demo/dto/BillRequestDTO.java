package com.example.demo.dto;

import java.util.List;

//BillRequestDTO.java
public class BillRequestDTO {
 private List<MedicineDto> medicines;
 private List<LabTestDto> labTests;
 // getters and setters
public List<MedicineDto> getMedicines() {
	return medicines;
}
public void setMedicines(List<MedicineDto> medicines) {
	this.medicines = medicines;
}
public List<LabTestDto> getLabTests() {
	return labTests;
}
public void setLabTests(List<LabTestDto> labTests) {
	this.labTests = labTests;
}
 
 
}