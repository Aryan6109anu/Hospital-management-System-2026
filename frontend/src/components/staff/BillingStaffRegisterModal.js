import React, { useState } from "react";
import "../styles/doctorregister.css";
import api from "../../services/api";
import { showAlert } from "../common/Alert";

export default function BillingStaffRegisterModal({ onClose, onRegistered }) {
  const [formData, setFormData] = useState({
    fullName: "",
    username: "",
    password: "",
    age: "",
    gender: "",
    aadhaarNumber: "",
    mobile: "",
    address: "",
    email: "",
  });

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === "age" ? Number(value) : value,
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

      await api.post("/billing-staff", {
      fullName: formData.fullName,
      age: formData.age,
      gender: formData.gender,
      aadhaarNumber: formData.aadhaarNumber,
      mobile: formData.mobile,
      address: formData.address,
      email: emailToSend,
      username: formData.username,   // <- root level
      password: formData.password,   // <- root level
});

      showAlert("success", "Billing Staff registered successfully ✅");

      if (onRegistered) await onRegistered();
      onClose();
    } catch (err) {
      showAlert("error", err?.response?.data || "Registration failed ❌");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <h3>Register Billing Staff</h3>

        <div className="form-grid">
          <div className="form-group">
            <label>Full Name</label>
            <input name="fullName" value={formData.fullName} onChange={handleChange} />
          </div>

          <div className="form-group">
            <label>Email</label>
            <div className="email-wrapper">
              <input name="email" value={formData.email} onChange={handleChange} />
              <span>@wellspring.com</span>
            </div>
          </div>

          <div className="form-group">
            <label>Username</label>
            <input name="username" value={formData.username} onChange={handleChange} />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input type="password" name="password" value={formData.password} onChange={handleChange} />
          </div>

          <div className="form-group">
            <label>Age</label>
            <input type="number" name="age" value={formData.age} onChange={handleChange} />
          </div>

          <div className="form-group">
            <label>Gender</label>
            <select name="gender" value={formData.gender} onChange={handleChange}>
              <option value="">Select</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
            </select>
          </div>

          <div className="form-group">
            <label>AadhaarNumber</label>
            <input name="aadhaarNumber" value={formData.aadhaarNumber} onChange={handleChange} />
          </div>

          <div className="form-group">
            <label>Mobile</label>
            <input name="mobile" value={formData.mobile} onChange={handleChange} />
          </div>

          <div className="form-group">
            <label>Address</label>
            <input name="address" value={formData.address} onChange={handleChange} />
          </div>
        </div>

        <div className="modal-actions">
          <button className="btn-primary" onClick={handleSubmit} disabled={loading}>
            {loading ? "Registering..." : "Register"}
          </button>
          <button className="btn-secondary" onClick={onClose} disabled={loading}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}