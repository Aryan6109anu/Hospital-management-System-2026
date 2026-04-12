package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.Department;

public class BillResponseDTO {
	
	private Long billId;
    private Long visitId;
    private Long patientId;    
    private Department department; 
    private String patientName;
    private String doctorName;
    private List<MedicineDto> medicines;
    private List<LabTestDto> labTests;
    private Double grandTotal;
    private boolean isPaid;
	public BillResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BillResponseDTO(Long billId, Long visitId, Long patientId, Department department, String patientName,
			String doctorName, List<MedicineDto> medicines, List<LabTestDto> labTests, Double grandTotal,
			boolean isPaid) {
		super();
		this.billId = billId;
		this.visitId = visitId;
		this.patientId = patientId;
		this.department = department;
		this.patientName = patientName;
		this.doctorName = doctorName;
		this.medicines = medicines;
		this.labTests = labTests;
		this.grandTotal = grandTotal;
		this.isPaid = isPaid;
	}
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
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
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
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
	public Double getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}
	public boolean isPaid() {
		return isPaid;
	}
	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
	
	    
}
