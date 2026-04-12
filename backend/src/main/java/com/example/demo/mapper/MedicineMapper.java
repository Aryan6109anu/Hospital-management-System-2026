package com.example.demo.mapper;

import com.example.demo.dto.MedicineDto;
import com.example.demo.entities.Medicine;

public class MedicineMapper {

    public static MedicineDto toDto(Medicine medicine) {

        MedicineDto dto = new MedicineDto();

        dto.setId(medicine.getId());
        dto.setMedicineName(medicine.getMedicineName());
        dto.setCategory(medicine.getCategory());
        dto.setPrice(medicine.getPrice());
        dto.setQuantity(medicine.getQuantity());
        dto.setUnit(medicine.getUnit());
        dto.setDescription(medicine.getDescription());

        return dto;
    }

    public static Medicine toEntity(MedicineDto dto) {

        Medicine medicine = new Medicine();

        medicine.setId(dto.getId());
        medicine.setMedicineName(dto.getMedicineName());
        medicine.setCategory(dto.getCategory());
        medicine.setPrice(dto.getPrice());
        medicine.setQuantity(dto.getQuantity());
        medicine.setUnit(dto.getUnit());
        medicine.setDescription(dto.getDescription());

        return medicine;
    }
}