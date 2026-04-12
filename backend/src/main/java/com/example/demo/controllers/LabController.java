package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.LabDepartment;
import com.example.demo.entities.LabSection;
import com.example.demo.entities.LabTest;
import com.example.demo.services.LabDepartmentService;
import com.example.demo.services.LabSectionService;
import com.example.demo.services.LabTestService;

@RestController
@RequestMapping("/api/lab")
@CrossOrigin("*")
public class LabController {

    private final LabDepartmentService departmentService;
    private final LabSectionService sectionService;
    private final LabTestService testService;

    public LabController(
            LabDepartmentService departmentService,
            LabSectionService sectionService,
            LabTestService testService) {

        this.departmentService = departmentService;
        this.sectionService = sectionService;
        this.testService = testService;
    }

    /* =====================================
       LAB DEPARTMENT APIs
    ===================================== */

    @PostMapping("/departments")
    public LabDepartment createDepartment(@RequestBody LabDepartment department) {
        return departmentService.createDepartment(department);
    }

    @GetMapping("/departments")
    public List<LabDepartment> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/departments/{id}")
    public LabDepartment getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PutMapping("/departments/{id}")
    public LabDepartment updateDepartment(
            @PathVariable Long id,
            @RequestBody LabDepartment department) {

        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/departments/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }


    /* =====================================
       LAB SECTION APIs
    ===================================== */

    @PostMapping("/sections")
    public LabSection createSection(@RequestBody LabSection section) {
        return sectionService.createSection(section);
    }
    
    @PostMapping("/sections/bulk")
    public ResponseEntity<List<LabSection>> createSectionsBulk(@RequestBody List<LabSection> sections) {
        List<LabSection> savedSections = sectionService.createSectionsBulk(sections);
        return ResponseEntity.ok(savedSections);
    }

    @GetMapping("/sections")
    public List<LabSection> getAllSections() {
        return sectionService.getAllSections();
    }

    @GetMapping("/sections/{id}")
    public LabSection getSectionById(@PathVariable Long id) {
        return sectionService.getSectionById(id);
    }

    @PutMapping("/sections/{id}")
    public LabSection updateSection(
            @PathVariable Long id,
            @RequestBody LabSection section) {

        return sectionService.updateSection(id, section);
    }

    @DeleteMapping("/sections/{id}")
    public void deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
    }


    /* =====================================
       LAB TEST APIs
    ===================================== */

    @PostMapping("/tests")
    public LabTest createTest(@RequestBody LabTest test) {
        return testService.createTest(test);
    }
    
 // Naya BULK method
    @PostMapping("tests/bulk")
    public ResponseEntity<List<LabTest>> createTestsBulk(@RequestBody List<LabTest> tests) {
        List<LabTest> savedTests = testService.createTestsBulk(tests);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTests);
    }

    @GetMapping("/tests")
    public List<LabTest> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/tests/{id}")
    public LabTest getTestById(@PathVariable Long id) {
        return testService.getTestById(id);
    }

    @PutMapping("/tests/{id}")
    public LabTest updateTest(
            @PathVariable Long id,
            @RequestBody LabTest test) {

        return testService.updateTest(id, test);
    }

    @DeleteMapping("/tests/{id}")
    public void deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
    }
    
    @GetMapping("/departments/{departmentId}/sections")
    public List<LabSection> getSectionsByDepartment(@PathVariable Long departmentId) {
        return sectionService.getSectionsByDepartment(departmentId);
    }

    @GetMapping("/sections/{sectionId}/tests")
    public List<LabTest> getTestsBySection(@PathVariable Long sectionId) {
        return testService.getTestsBySection(sectionId);
    }

}