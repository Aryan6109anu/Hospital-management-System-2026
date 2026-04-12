package com.example.demo.dto;

public class PatientDTO {

    private String name;
    private Integer age;
    private String gender;
    private String mobile;
    private String address;
    private String disease;

    public PatientDTO() {}

    // getters & setters
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

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }
}
