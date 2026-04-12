package com.example.demo.entities;
import com.example.demo.model.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 3000)
    private String message;

    @Column(length = 3000)
    private String aiReply;

    @Column(length = 3000)
    private String adminReply;

    private boolean replied;

    private LocalDateTime createdAt;

	public ContactMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ContactMessage(Long id, String name, String email, Role role, String message, String aiReply,
			String adminReply, boolean replied, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.message = message;
		this.aiReply = aiReply;
		this.adminReply = adminReply;
		this.replied = replied;
		this.createdAt = createdAt;
	}
	
    // getters & setters

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAiReply() {
		return aiReply;
	}

	public void setAiReply(String aiReply) {
		this.aiReply = aiReply;
	}

	public String getAdminReply() {
		return adminReply;
	}

	public void setAdminReply(String adminReply) {
		this.adminReply = adminReply;
	}

	public boolean isReplied() {
		return replied;
	}

	public void setReplied(boolean replied) {
		this.replied = replied;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
