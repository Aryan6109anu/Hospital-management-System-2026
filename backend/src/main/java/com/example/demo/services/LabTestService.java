package com.example.demo.services;

import java.util.List;
import com.example.demo.entities.LabTest;

public interface LabTestService {

    List<LabTest> getAllTests();

    LabTest getTestById(Long id);

    LabTest createTest(LabTest labTest);
    
    List<LabTest> createTestsBulk(List<LabTest> tests);

    LabTest updateTest(Long id, LabTest labTest);

    void deleteTest(Long id);
    
    List<LabTest> getTestsBySection(Long sectionId);

}