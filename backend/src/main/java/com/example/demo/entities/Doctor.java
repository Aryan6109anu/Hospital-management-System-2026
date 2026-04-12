package com.example.demo.entities;

import java.util.List;

import com.example.demo.model.Department;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Doctor name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Specialization is required")
    @Column(nullable = false)
    private String specialization;

    @Min(value = 1, message = "Experience must be at least 1 year")
    @Column(nullable = false)
    private Integer experience;

    @NotNull(message = "Availability is required")
    @Column(nullable = false)
    private Boolean available;

    private String email;
    private String mobile;

    @Enumerated(EnumType.STRING)
    private Department department;   // ✅ Enum
    
    // Doctor Schedule
    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "doctor"})
    private DoctorSchedule schedule;

    // Appointments
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"patient", "doctor"})
    private List<Appointment> appointments;

    // User mapping
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    // Constructors
    public Doctor() {}

	public Doctor(Long id, @NotBlank(message = "Doctor name is required") String name,
			@NotBlank(message = "Specialization is required") String specialization,
			@Min(value = 1, message = "Experience must be at least 1 year") Integer experience,
			@NotNull(message = "Availability is required") Boolean available, String email, String mobile,
			Department department, DoctorSchedule schedule, List<Appointment> appointments, User user) {
		super();
		this.id = id;
		this.name = name;
		this.specialization = specialization;
		this.experience = experience;
		this.available = available;
		this.email = email;
		this.mobile = mobile;
		this.department = department;
		this.schedule = schedule;
		this.appointments = appointments;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public DoctorSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(DoctorSchedule schedule) {
		this.schedule = schedule;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
    
    

}
