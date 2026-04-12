import React, { useState } from "react";
import "../../styles/contact.css";
import api from "../../services/api";
import { showAlert } from "../../components/common/Alert";

export default function Contact() {

  // 🔥 Logged user from session
  const loggedUser = JSON.parse(localStorage.getItem("user"));

  const [form, setForm] = useState({
    name: "",
    email: "",
    message: ""
  });

  const [aiReply, setAiReply] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // ENUM SAFE ROLE (only valid backend enums)
  const getUserRole = () => {
    if (!loggedUser || !loggedUser.role) return null;
    return loggedUser.role; 
    // ROLE_DOCTOR / ROLE_PATIENT / ROLE_ADMIN
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const payload = {
      ...form,
      role: getUserRole()
    };

    const res = await api.post("/contact", payload);

    setAiReply(res.data.aiReply);
    showAlert("success", "Message sent successfully");
    setForm({ name: "", email: "", message: "" });
  };

  return (
    <div className="contact-container">
      <h1 className="contact-title">Contact Us</h1>
      <p className="contact-subtitle">
        Need help? Feel free to contact our hospital team.
      </p>

      <div className="contact-box">

        {/* LEFT INFO */}
        <div className="contact-info">
          <h3>Hospital Office</h3>
          <p><b>Address:</b> Ranchi, Jharkhand, India</p>
          <p><b>Email:</b> support@hospital.com</p>
          <p><b>Phone:</b> +91 98765 43210</p>

          {/* Optional debug (remove later) */}
          {loggedUser && (
            <p style={{ marginTop: "10px", color: "#555" }}>
              Logged as: <b>{loggedUser.name}</b> ({loggedUser.role})
            </p>
          )}
        </div>

        {/* RIGHT FORM */}
        <form className="contact-form" onSubmit={handleSubmit}>
          <input
            type="text"
            name="name"
            placeholder="Enter your name"
            value={form.name}
            onChange={handleChange}
            required
          />

          <input
            type="email"
            name="email"
            placeholder="Enter your email"
            value={form.email}
            onChange={handleChange}
            required
          />

          <textarea
            name="message"
            placeholder="Write message here..."
            value={form.message}
            onChange={handleChange}
            required
          />

          <button type="submit">Send Message</button>
        </form>
      </div>

      {/* AI AUTO REPLY */}
      {aiReply && (
        <div className="ai-reply-box">
          <h3>🤖 AI Auto Reply</h3>
          <p>{aiReply}</p>
        </div>
      )}
    </div>
  );
}
