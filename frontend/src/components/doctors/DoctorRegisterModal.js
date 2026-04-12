// src/components/modals/DoctorRegisterModal.js
import React, { useState, useEffect } from "react";
import "../styles/doctorregister.css";
import api from "../../services/api";
import { showAlert } from "../common/Alert";

export default function DoctorRegisterModal({ onClose, onRegistered }) {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    username: "",
    password: "",
    specialization: "",
    experience: "",
    available: true,
    mobile: "",
    department: "",
  });

  const [loading, setLoading] = useState(false);
  const [doctors, setDoctors] = useState([]);

useEffect(() => {
  api.get("/doctors").then(res => setDoctors(res.data));
}, []);

const departments = [...new Set(doctors.map(d => d.department))];

  const handleChange = (e) => {
    const { name, value, type } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]:
        type === "number"
          ? Number(value)
          : name === "available"
          ? value === "true"
          : value,
    }));
  };

  const handleSubmit = async () => {
    if (loading) return;

    try {
      setLoading(true);

      let emailToSend = formData.email.trim();
      if (emailToSend && !emailToSend.includes("@wellspring.com")) {
        emailToSend = `${emailToSend}@wellspring.com`;
      }

      await api.post("/doctors", { ...formData, email: emailToSend });
      showAlert("success", "Doctor registered successfully ✅");

      if (onRegistered) await onRegistered();
      onClose();
    } catch (err) {
      console.error("Doctor register error:", err);
      showAlert("error", err?.response?.data?.message || "Failed to register doctor ❌");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <h3>Register New Doctor (Admin)</h3>

        {/* ===== FORM GRID ===== */}
        <div className="form-grid">
          <div className="form-group">
            <label>Full Name</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="Full Name"
            />
          </div>

          <div className="form-group">
            <label>Email</label>
            <div className="email-wrapper">
              <input
                type="text"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="Enter username"
              />
              <span>@wellspring.com</span>
            </div>
          </div>

          <div className="form-group">
            <label>Username</label>
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Username"
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Password"
            />
          </div>

          <div className="form-group">
  <label>Department</label>
  <select
    name="department"
    value={formData.department}
    onChange={handleChange}
  >
    <option value="">Select Department</option>
    {departments.map(dep => (
      <option key={dep} value={dep}>
        {dep.replaceAll("_", " ")}
      </option>
    ))}
  </select>
</div>
          <div className="form-group">
            <label>Specialization</label>
            <input
              type="text"
              name="specialization"
              value={formData.specialization}
              onChange={handleChange}
              placeholder="Specialization"
            />
          </div>

          <div className="form-group">
            <label>Experience (Years)</label>
            <input
              type="number"
              name="experience"
              value={formData.experience}
              onChange={handleChange}
              placeholder="Experience"
            />
          </div>

          <div className="form-group">
            <label>Mobile</label>
            <input
              type="text"
              name="mobile"
              value={formData.mobile}
              onChange={handleChange}
              placeholder="Mobile Number"
            />
          </div>

          <div className="form-group">
            <label>Availability</label>
            <select
              name="available"
              value={formData.available}
              onChange={handleChange}
            >
              <option value={true}>Available</option>
              <option value={false}>Not Available</option>
            </select>
          </div>
        </div>

        <div className="modal-actions">
          <button
            className="btn-primary"
            onClick={handleSubmit}
            disabled={loading}
          >
            {loading ? "Registering..." : "Register"}
          </button>
          <button
            className="btn-secondary"
            onClick={onClose}
            disabled={loading}
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}
