package com.example.demo.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.entities.LabTest;
import com.example.demo.repository.LabTestRepository;

@Service
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository labTestRepository;

    public LabTestServiceImpl(LabTestRepository labTestRepository) {
        this.labTestRepository = labTestRepository;
    }

    @Override
    public List<LabTest> getAllTests() {
        return labTestRepository.findAll();
    }

    @Override
    public LabTest getTestById(Long id) {
        return labTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found"));
    }

    @Override
    public LabTest createTest(LabTest labTest) {
        return labTestRepository.save(labTest);
    }

    @Override
    public LabTest updateTest(Long id, LabTest updatedTest) {

        LabTest test = labTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found"));

        test.setName(updatedTest.getName());
        test.setPrice(updatedTest.getPrice());

        if(updatedTest.getSection() != null){
            test.setSection(updatedTest.getSection());
        }

        return labTestRepository.save(test);
    }

    @Override
    public void deleteTest(Long id) {

        LabTest test = labTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found"));

        labTestRepository.delete(test);
    }
    
    @Override
    public List<LabTest> getTestsBySection(Long sectionId) {
        return labTestRepository.findBySectionId(sectionId);
    }
    
    @Override
    public List<LabTest> createTestsBulk(List<LabTest> tests) {
        return labTestRepository.saveAll(tests);
    }
    
}