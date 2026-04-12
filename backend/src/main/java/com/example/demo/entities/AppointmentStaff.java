package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class AppointmentStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false, unique = true, length = 10)
    private String mobile;

    @Column(nullable = false, unique = true, length = 12)
    private String aadhaarNumber;

    @Column(nullable = false, length = 300)
    private String address;

    @Column(nullable = false, unique = true)
    private String email;
    
    @OneToMany(mappedBy = "appointmentStaff", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public AppointmentStaff() {}

    // Getters
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public Integer getAge() { return age; }
    public String getGender() { return gender; }
    public String getMobile() { return mobile; }
    public String getAadhaarNumber() { return aadhaarNumber; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public List<Appointment> getAppointments() { return appointments;}
    public User getUser() { return user; }
    

    // Setters (Controlled)
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setAge(Integer age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setAadhaarNumber(String aadhaarNumber) { this.aadhaarNumber = aadhaarNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setEmail(String email) { this.email = email; }
    public void setAppointments(List<Appointment> appointments) {this.appointments = appointments;}
    public void setUser(User user) { this.user = user; }
}