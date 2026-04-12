package com.example.demo.services;

import java.util.List;
import com.example.demo.entities.LabDepartment;

public interface LabDepartmentService {

    LabDepartment createDepartment(LabDepartment department);

    List<LabDepartment> getAllDepartments();

    LabDepartment getDepartmentById(Long id);

    LabDepartment updateDepartment(Long id, LabDepartment department);

    void deleteDepartment(Long id);

}