package com.example.demo.entities;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "medicines")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicineName;

    private String category;

    private BigDecimal price;
 
    // ✅ Default value (null nahi aayega)
    private Integer quantity = 0;

    private String unit;

    private String description;

    // 🔹 Default constructor
    public Medicine() {
    }

    // 🔹 Parameterized constructor (null safe)
    public Medicine(Long id, String medicineName, String category, BigDecimal price,
                    Integer quantity, String unit, String description) {
        this.id = id;
        this.medicineName = medicineName;
        this.category = category;
        this.price = price;
        this.quantity = (quantity != null) ? quantity : 0;
        this.unit = unit;
        this.description = description;
    }

    // 🔹 Getters & Setters

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

    // 🔥 MAIN FIX (null safe getter)
    public Integer getQuantity() {
        return quantity != null ? quantity : 0;
    }

    // 🔥 null safe setter
    public void setQuantity(Integer quantity) {
        this.quantity = (quantity != null) ? quantity : 0;
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