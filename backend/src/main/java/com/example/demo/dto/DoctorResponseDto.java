package com.example.demo.dto;

public class DoctorResponseDto {

    private Long id;
    private String name;
    private String specialization;
    private Integer experience;
    private Boolean available;
    private String username;

    private String email;
    private String mobile;
    private String department; // String for response
    private DoctorScheduleDto schedule; // <-- Change here from DoctorScheduleRequest to DoctorScheduleDto

    // ✅ Add constructor matching mapper
    public DoctorResponseDto(Long id, String name, String specialization, Integer experience, Boolean available,
                             String username, String email, String mobile, String department, DoctorScheduleDto schedule) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.experience = experience;
        this.available = available;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.department = department;
        this.schedule = schedule;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public DoctorScheduleDto getSchedule() {
		return schedule;
	}

	public void setSchedule(DoctorScheduleDto schedule) {
		this.schedule = schedule;
	}

    // getters and setters...
    
    
}
