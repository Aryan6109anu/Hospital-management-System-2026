package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PatientRegisterDto {

    @NotBlank(message = "Name is required")
    private String name;

    private Integer age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String mobile;

    private String address;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    public PatientRegisterDto() {}

    public PatientRegisterDto(String name, Integer age, String gender, String mobile, String address,
                              String username, String password) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.mobile = mobile;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    // ===== Getters & Setters =====
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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
