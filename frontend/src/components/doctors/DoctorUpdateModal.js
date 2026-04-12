// src/components/modals/DoctorUpdateModal.js
import React, { useState, useEffect } from "react";
import "../styles/doctorupdate.css";
import api from "../../services/api";
import { showAlert } from "../common/Alert";

/* ================= DOCTOR UPDATE MODAL (ADMIN) ================= */
export default function DoctorUpdateModal({ doctor, onClose, onUpdated }) {

  const [formData, setFormData] = useState({ ...doctor });
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

  const handleSave = async () => {
    try {
      await api.put(`/doctors/${doctor.id}`, formData);

      showAlert("success", "Doctor updated successfully ✅");
      onUpdated(); // refresh list
      onClose();
    } catch (err) {
      console.error(err);
      showAlert("error", "Failed to update doctor ❌");
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">

        <h3>Edit Doctor Details</h3>

        <div className="modal-form-grid">

          {/* Full Name */}
          <div className="form-field">
            <label>Full Name</label>
            <input
              type="text"
              name="name"
              value={formData.name || ""}
              onChange={handleChange}
            />
          </div>

          {/* Email */}
          <div className="form-field">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email || ""}
              onChange={handleChange}
            />
          </div>

          {/* Username */}
          <div className="form-field">
            <label>Username</label>
            <input
              type="text"
              name="username"
              value={formData.username || ""}
              onChange={handleChange}
            />
          </div>

          {/* Password */}
          <div className="form-field">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={formData.password || ""}
              onChange={handleChange}
              placeholder="Leave blank to keep same"
            />
          </div>

          {/* Department */}
          <div className="form-field">
            <label>Department</label>
            <select
              name="department"
              value={formData.department || ""}
              onChange={handleChange}
            >
              <option value="">-- Select Department --</option>
              {departments.map((dep, index) => (
                <option key={index} value={dep}>
                  {dep}
                </option>
              ))}
            </select>
          </div>

          {/* Specialization */}
          <div className="form-field">
            <label>Specialization</label>
            <input
              type="text"
              name="specialization"
              value={formData.specialization || ""}
              onChange={handleChange}
            />
          </div>

          {/* Experience */}
          <div className="form-field">
            <label>Experience (Years)</label>
            <input
              type="number"
              name="experience"
              value={formData.experience || ""}
              onChange={handleChange}
            />
          </div>

          {/* Mobile */}
          <div className="form-field">
            <label>Mobile</label>
            <input
              type="text"
              name="mobile"
              value={formData.mobile || ""}
              onChange={handleChange}
              placeholder="Mobile Number"
            />
          </div>

          {/* Availability */}
          <div className="form-field full-width">
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

        {/* Buttons */}
        <div className="modal-actions">
          <button className="btn-primary" onClick={handleSave}>
            Save
          </button>
          <button className="btn-secondary" onClick={onClose}>
            Cancel
          </button>
        </div>

      </div>
    </div>
  );
}
