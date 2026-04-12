package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.demo.dto.MedicineDto;
import com.example.demo.entities.Medicine;
import com.example.demo.mapper.MedicineMapper;
import com.example.demo.repository.MedicineRepository;

@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository repository;

    public MedicineServiceImpl(MedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public MedicineDto create(MedicineDto dto) {
        Medicine medicine = MedicineMapper.toEntity(dto);
        return MedicineMapper.toDto(repository.save(medicine));
    }
    
    @Override
    public List<MedicineDto> createBulk(List<MedicineDto> medicines) {
        List<Medicine> entities = medicines
                .stream()
                .map(MedicineMapper::toEntity)
                .toList();

        List<Medicine> saved = repository.saveAll(entities);

        return saved.stream()
                .map(MedicineMapper::toDto)
                .toList();
    }
    
    @Override
    public MedicineDto getById(Long id) {
        Medicine medicine = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
        return MedicineMapper.toDto(medicine);
    }

    @Override
    public List<MedicineDto> getAll() {
        return repository.findAll()
                .stream()
                .map(MedicineMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public MedicineDto update(Long id, MedicineDto dto) {
        Medicine medicine = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        medicine.setMedicineName(dto.getMedicineName());
        medicine.setCategory(dto.getCategory());
        medicine.setPrice(dto.getPrice());
        medicine.setUnit(dto.getUnit());
        medicine.setDescription(dto.getDescription());
        
        // Zaroori: Stock ko update karne ke liye ye line add ki hai
        medicine.setQuantity(dto.getQuantity());

        return MedicineMapper.toDto(repository.save(medicine));
    }

    @Override
    public MedicineDto sellMedicine(Long id, Integer quantityToSell) {
        Medicine medicine = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        // Logic: Agar stock maangi gayi quantity se kam hai toh error do
        if (medicine.getQuantity() < quantityToSell) {
            throw new RuntimeException("Insufficient stock! Available: " + medicine.getQuantity());
        }

        // Auto-update stock logic
        int updatedStock = medicine.getQuantity() - quantityToSell;
        medicine.setQuantity(updatedStock);

        return MedicineMapper.toDto(repository.save(medicine));
    }
    
    @Override
    public Integer getStockCount(Long id) {
        Medicine medicine = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
        return medicine.getQuantity();
    }

    @Override
    public List<MedicineDto> getLowStockMedicines(Integer threshold) {
        // Saari medicines check karo jinka stock 'threshold' se kam hai
        return repository.findAll()
                .stream()
                .filter(m -> m.getQuantity() < threshold)
                .map(MedicineMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}