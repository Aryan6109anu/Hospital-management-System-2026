package com.example.demo.entities;

import com.example.demo.model.LabTestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class LabOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private LabOrder labOrder;

    @ManyToOne
    private LabTest labTest;

    @Enumerated(EnumType.STRING)
    private LabTestStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LabOrder getLabOrder() {
		return labOrder;
	}

	public void setLabOrder(LabOrder labOrder) {
		this.labOrder = labOrder;
	}

	public LabTest getLabTest() {
		return labTest;
	}

	public void setLabTest(LabTest labTest) {
		this.labTest = labTest;
	}

	public LabTestStatus getStatus() {
		return status;
	}

	public void setStatus(LabTestStatus status) {
		this.status = status;
	}
    
    
    
    

}