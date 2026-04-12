package com.example.demo.dto;

public class PatientResponseDto {

    private Long id;
    private String name;
    private Integer age;
    private String gender;
    private String mobile;
    private String address;
    private String username;
    private boolean active; // soft-delete flag

    public PatientResponseDto() {}

    public PatientResponseDto(Long id, String name, Integer age, String gender, String mobile, String address,
                              String username, boolean active) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.mobile = mobile;
        this.address = address;
       
        this.username = username;
        this.active = active;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
