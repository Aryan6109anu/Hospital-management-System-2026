package com.example.demo.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.*;

@Entity
public class LabDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "department")
    private List<LabSection> sections = new ArrayList<>();

    public LabDepartment() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<LabSection> getSections() {
        return sections;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSections(List<LabSection> sections) {
        this.sections = sections;
    }
    
}