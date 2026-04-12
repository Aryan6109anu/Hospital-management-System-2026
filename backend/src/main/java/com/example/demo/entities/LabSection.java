package com.example.demo.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.*;

@Entity
public class LabSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "department_id")
    private LabDepartment department;

    @JsonManagedReference
    @OneToMany(mappedBy = "section")
    private List<LabTest> tests = new ArrayList<>();

    public LabSection() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LabDepartment getDepartment() {
        return department;
    }

    public List<LabTest> getTests() {
        return tests;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(LabDepartment department) {
        this.department = department;
    }

    public void setTests(List<LabTest> tests) {
        this.tests = tests;
    }
}