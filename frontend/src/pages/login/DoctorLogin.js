import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../styles/login.css";
import { showAlert } from "../../components/common/Alert";

export default function DoctorLogin() {
  const [form, setForm] = useState({
    username: "",
    password: "",
    role: "ROLE_DOCTOR"
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const nav = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const login = async () => {
    // Basic validation
    if (!form.username || !form.password) {
      showAlert("warning", "Please enter username and password");
      return;
    }

    setLoading(true);
    setError("");

    try {
      const res = await axios.post(
        "http://localhost:8080/api/auth/login",
        form
      );

      console.log("✅ DOCTOR LOGIN RESPONSE:", res.data);

      localStorage.setItem("user", JSON.stringify(res.data));
      localStorage.setItem("token", res.data.token);
      localStorage.setItem("role", res.data.role);

      // ✅ SAVE DOCTOR ID
      if (res.data.role === "ROLE_DOCTOR") {
        localStorage.setItem("doctorId", res.data.doctorId);
      }

      // Success SweetAlert
      showAlert("success", "Doctor login successful!");

      nav("/"); // doctor dashboard

    } catch (e) {
      console.error("❌ DOCTOR LOGIN ERROR:", e);

      let msg = "Login failed";

      if (e.response) {
        msg =
          e.response.data?.message ||
          e.response.data ||
          "Invalid username or password";
      } else {
        msg = "Server not responding. Please try again.";
      }

      setError(msg);
      showAlert("error", msg);   // Error SweetAlert

    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container doctor">
      <h2>Doctor Login</h2>

      {error && <p className="error-text">❌ {error}</p>}

      <input
        name="username"
        placeholder="Doctor Username"
        value={form.username}
        onChange={handleChange}
      />

      <input
        name="password"
        type="password"
        placeholder="Password"
        value={form.password}
        onChange={handleChange}
      />

      <button onClick={login} disabled={loading}>
        {loading ? "Logging in..." : "Login as Doctor"}
      </button>
    </div>
  );
}
