package com.example.demo.dto;

import java.math.BigDecimal;

public class LabTestDto {

    private Long id;

    private String testName;

    private String category;

    private BigDecimal price;

    private String description;
    
    public LabTestDto() {
		super();
		// TODO Auto-generated constructor stub
	}
    public LabTestDto(Long id, String testName, String category, BigDecimal price, String description) {
		super();
		this.id = id;
		this.testName = testName;
		this.category = category;
		this.price = price;
		this.description = description;
	}



	public Long getId() {
        return id;
    }

    public String getTestName() {
        return testName;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}