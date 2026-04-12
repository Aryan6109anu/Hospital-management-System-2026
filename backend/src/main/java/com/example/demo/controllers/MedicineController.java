package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.MedicineDto;
import com.example.demo.services.MedicineService;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService service;

    public MedicineController(MedicineService service) {
        this.service = service;
    }

    @PostMapping
    public MedicineDto create(@RequestBody MedicineDto dto) {
        return service.create(dto);
    }
    
    @PostMapping("/bulk")
    public List<MedicineDto> createBulk(@RequestBody List<MedicineDto> medicines) {
        return service.createBulk(medicines);
    }
    
    @GetMapping("/{id}")
    public MedicineDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<MedicineDto> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public MedicineDto update(@PathVariable Long id, @RequestBody MedicineDto dto) {
        return service.update(id, dto);
    }

    @PatchMapping("/{id}/sell")
    public MedicineDto sell(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Integer quantity = payload.get("quantity"); 
        
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Bhai, quantity toh sahi dalo (1 ya usse zyada)!");
        }
        
        return service.sellMedicine(id, quantity);
    }

    // 1. Kisi ek dawai ka sirf stock dekhne ke liye
    // GET /api/medicines/610/stock 
       @GetMapping("/{id}/stock")
       public Integer getStock(@PathVariable Long id) {
       return service.getStockCount(id);
    }
    // 2. Low stock wali medicines dekhne ke liye (Threshold maan lo 10 hai)
    // GET /api/medicines/low-stock?limit=10
       @GetMapping("/low-stock")
       public List<MedicineDto> getLowStock(@RequestParam(defaultValue = "10") Integer limit) {
       return service.getLowStockMedicines(limit);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}