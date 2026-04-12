package com.example.demo.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.entities.LabSection;
import com.example.demo.repository.LabSectionRepository;

@Service
public class LabSectionServiceImpl implements LabSectionService {

    private final LabSectionRepository repository;

    public LabSectionServiceImpl(LabSectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public LabSection createSection(LabSection section) {
        return repository.save(section);
    }

    @Override
    public List<LabSection> getAllSections() {
        return repository.findAll();
    }

    @Override
    public LabSection getSectionById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));
    }

    @Override
    public LabSection updateSection(Long id, LabSection section) {

        LabSection existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        existing.setName(section.getName());

        if(section.getDepartment() != null){
            existing.setDepartment(section.getDepartment());
        }

        return repository.save(existing);
    }

    @Override
    public void deleteSection(Long id) {

        LabSection existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        repository.delete(existing);
    }
    
    @Override
    public List<LabSection> getSectionsByDepartment(Long departmentId) {
        return repository.findByDepartmentId(departmentId);
    }
    
    @Override
    public List<LabSection> createSectionsBulk(List<LabSection> sections) {
        return repository.saveAll(sections);
    }
}