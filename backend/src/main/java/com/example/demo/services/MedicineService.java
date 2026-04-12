package com.example.demo.services;

import java.util.List;
import com.example.demo.dto.MedicineDto;

public interface MedicineService {

    MedicineDto create(MedicineDto dto);
    
    List<MedicineDto> createBulk(List<MedicineDto> medicines);
    
    MedicineDto getById(Long id);

    List<MedicineDto> getAll();
    
    MedicineDto update(Long id, MedicineDto dto);

    // Naya Method: Patient ko dawai dene par stock auto-update karne ke liye
    MedicineDto sellMedicine(Long id, Integer quantityToSell);
    
    // Naya: Ek dawai ka stock check karne ke liye
    Integer getStockCount(Long id);

    // Naya: Kam stock wali dawaiyon ki list nikaalne ke liye
    List<MedicineDto> getLowStockMedicines(Integer threshold);

    void delete(Long id);
}