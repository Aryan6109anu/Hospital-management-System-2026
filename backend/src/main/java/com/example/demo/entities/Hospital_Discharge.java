package com.example.demo.entities;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "hospital_discharges")
public class Hospital_Discharge {
	



	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    // Mapping with Patient (Foreign Key)
	    @OneToOne
	    @JoinColumn(name = "patient_id", nullable = false)
	    private Patient patient;

	 

	    @Column(nullable = false)
	    private LocalDateTime dischargeDateTime;

	    @Column(columnDefinition = "TEXT")
	    private String dischargeSummary; // Doctor's final remarks

	    private String dischargeType; // Example: Normal, AMA (Against Medical Advice), Expired, Referred

	    @Column(columnDefinition = "TEXT")
	    private String treatmentGiven; // Summary of medicines/surgery

	    private LocalDate followUpDate; // Agli baar kab aana hai

	    @Column(nullable = false)
	    private String billingStatus; // Example: Cleared, Pending

	    private String dischargedBy; // Doctor or Staff Name

		public Hospital_Discharge() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Hospital_Discharge(Long id, Patient patient, LocalDateTime dischargeDateTime, String dischargeSummary,
				String dischargeType, String treatmentGiven, LocalDate followUpDate, String billingStatus,
				String dischargedBy) {
			super();
			this.id = id;
			this.patient = patient;
			this.dischargeDateTime = dischargeDateTime;
			this.dischargeSummary = dischargeSummary;
			this.dischargeType = dischargeType;
			this.treatmentGiven = treatmentGiven;
			this.followUpDate = followUpDate;
			this.billingStatus = billingStatus;
			this.dischargedBy = dischargedBy;
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

		public LocalDateTime getDischargeDateTime() {
			return dischargeDateTime;
		}

		public void setDischargeDateTime(LocalDateTime dischargeDateTime) {
			this.dischargeDateTime = dischargeDateTime;
		}

		public String getDischargeSummary() {
			return dischargeSummary;
		}

		public void setDischargeSummary(String dischargeSummary) {
			this.dischargeSummary = dischargeSummary;
		}

		public String getDischargeType() {
			return dischargeType;
		}

		public void setDischargeType(String dischargeType) {
			this.dischargeType = dischargeType;
		}

		public String getTreatmentGiven() {
			return treatmentGiven;
		}

		public void setTreatmentGiven(String treatmentGiven) {
			this.treatmentGiven = treatmentGiven;
		}

		public LocalDate getFollowUpDate() {
			return followUpDate;
		}

		public void setFollowUpDate(LocalDate followUpDate) {
			this.followUpDate = followUpDate;
		}

		public String getBillingStatus() {
			return billingStatus;
		}

		public void setBillingStatus(String billingStatus) {
			this.billingStatus = billingStatus;
		}

		public String getDischargedBy() {
			return dischargedBy;
		}

		public void setDischargedBy(String dischargedBy) {
			this.dischargedBy = dischargedBy;
		}


	    // Getters and Setters (Add them here)
		
		
		
	}

