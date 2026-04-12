package com.example.demo.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.entities.LabDepartment;
import com.example.demo.repository.LabDepartmentRepository;

@Service
public class LabDepartmentServiceImpl implements LabDepartmentService {

    private final LabDepartmentRepository repository;

    public LabDepartmentServiceImpl(LabDepartmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public LabDepartment createDepartment(LabDepartment department) {
        return repository.save(department);
    }

    @Override
    public List<LabDepartment> getAllDepartments() {
        return repository.findAll();
    }

    @Override
    public LabDepartment getDepartmentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    @Override
    public LabDepartment updateDepartment(Long id, LabDepartment department) {

        LabDepartment existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        existing.setName(department.getName());

        return repository.save(existing);
    }

    @Override
    public void deleteDepartment(Long id) {

        LabDepartment existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        repository.delete(existing);
    }
}