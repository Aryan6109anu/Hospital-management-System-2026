import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../styles/patientRegister.css";
import { showAlert } from "../../components/common/Alert";

export default function PatientRegister() {

  const nav = useNavigate();

  const [form, setForm] = useState({
    name: "",
    age: "",
    gender: "",
    mobile: "",
    address: "",
    username: "",
    password: "",
    email: "",
    role: "ROLE_PATIENT"
  });

  const handleChange = (e) => {
    const { name, value } = e.target;

    // Only allow spaces for name & address
    if (name === "name" || name === "address") {
      setForm({
        ...form,
        [name]: value
      });
    } else {
      // Remove spaces from other fields
      setForm({
        ...form,
        [name]: value.replace(/\s/g, "")
      });
    }
  };

  const validateForm = (data) => {
    if (!data.name.trim()) return "Please enter full name";
    if (!data.age) return "Please enter age";
    if (!data.gender) return "Please select gender";
    
    // Updated: Mobile number validation (checks if it's exactly 10 digits)
    const mobileRegex = /^[0-9]{10}$/;
    if (!data.mobile) return "Please enter mobile number";
    if (!mobileRegex.test(data.mobile)) return "Mobile number must be exactly 10 digits";

    if (!data.username) return "Please enter username";
    if (!data.password) return "Please enter password";
    return null;
  };

  const register = async () => {
    const cleanedForm = {
      ...form,
      name: form.name.trim(),
      address: form.address.trim(),
      username: form.username.trim(),
      password: form.password.trim(),
      email: form.email.trim()
    };

    const error = validateForm(cleanedForm);
    if (error) {
      showAlert("warning", "⚠️ " + error);
      return;
    }

    try {
      await axios.post(
        "http://localhost:8080/api/patients/register",
        cleanedForm
      );
      showAlert("success", "✅ Registration successful! Please login.");
      nav("/login/patient");
    } catch (e) {
      // Improved error message display from backend
      const serverMsg = e.response?.data?.message || e.response?.data || e.message;
      showAlert(
        "error",
        "❌ Registration Failed: " + serverMsg
      );
    }
  };

  return (
    <div className="login-container register">

      <h2>Patient Registration</h2>

      <input
        name="name"
        placeholder="Full Name *"
        value={form.name}
        onChange={handleChange}
      />

      <input
        name="age"
        type="number"
        placeholder="Age *"
        value={form.age}
        onChange={handleChange}
      />

      <select
        name="gender"
        value={form.gender}
        onChange={handleChange}
      >
        <option value="">Select Gender *</option>
        <option value="Male">Male</option>
        <option value="Female">Female</option>
      </select>

      <input
        name="mobile"
        placeholder="Mobile Number *"
        value={form.mobile}
        onChange={handleChange}
      />

      <input
        name="address"
        placeholder="Address"
        value={form.address}
        onChange={handleChange}
      />

      <input
        name="email"
        type="email"
        placeholder="Email (no spaces)"
        value={form.email}
        onChange={handleChange}
      />

      <input
        name="username"
        placeholder="Create Username (no spaces) *"
        value={form.username}
        onChange={handleChange}
      />

      <input
        type="password"
        name="password"
        placeholder="Create Password (no spaces) *"
        value={form.password}
        onChange={handleChange}
      />

      <button onClick={register}>Register</button>

      <p className="register-link">
        Already have an account?
        <span onClick={() => nav("/login/patient")}> Sign in</span>
      </p>

    </div>
  );
}