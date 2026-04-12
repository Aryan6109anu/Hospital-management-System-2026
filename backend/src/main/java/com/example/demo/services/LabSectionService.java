package com.example.demo.services;

import java.util.List;
import com.example.demo.entities.LabSection;

public interface LabSectionService {

    LabSection createSection(LabSection section);
    
    List<LabSection> createSectionsBulk(List<LabSection> sections);

    List<LabSection> getAllSections();

    LabSection getSectionById(Long id);

    LabSection updateSection(Long id, LabSection section);

    void deleteSection(Long id);
    
    List<LabSection> getSectionsByDepartment(Long departmentId);
    

}