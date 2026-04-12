package com.example.demo.dto;

import java.math.BigDecimal;

public class MedicineDto {

    private Long id;
    private String medicineName;
    private String category;
    private BigDecimal price;
    private Integer quantity;
    private String unit;
    private String description;
    
    public MedicineDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MedicineDto(Long id, String medicineName, String category, BigDecimal price, Integer quantity, String unit,
			String description) {
		super();
		this.id = id;
		this.medicineName = medicineName;
		this.category = category;
		this.price = price;
		this.quantity = quantity;
		this.unit = unit;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}