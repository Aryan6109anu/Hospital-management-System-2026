import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../styles/login.css";
import { showAlert } from "../../components/common/Alert";

export default function PatientLogin() {
  const [form, setForm] = useState({
    username: "",
    password: "",
    role: "ROLE_PATIENT"
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const nav = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError(""); // clear error on typing
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
        "/api/auth/login",
        form
      );

      console.log("✅ LOGIN RESPONSE:", res.data);

      // save token & role
     localStorage.setItem("user", JSON.stringify(res.data));
     localStorage.setItem("token", res.data.token);
     localStorage.setItem("role", res.data.role);

      // Success SweetAlert
      showAlert("success", "Login successful!");

      // redirect
      nav("/");

    } catch (e) {
      console.error("❌ LOGIN ERROR:", e);

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
    <div className="login-container patient">
      <h2>Patient / Public Login</h2>

      {error && <p className="error-text">❌ {error}</p>}

      <input
        name="username"
        placeholder="Username / Mobile"
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
        {loading ? "Logging in..." : "Login"}
      </button>

      <p className="register-link">
        Don't have an account?
        <span onClick={() => nav("/patient-register")}>
          {" "}Register here
        </span>
      </p>
    </div>
  );
}
