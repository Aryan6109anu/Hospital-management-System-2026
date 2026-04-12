import { useNavigate } from "react-router-dom";
import "../../styles/login.css";

export default function LoginSelect() {
  const navigate = useNavigate();


  return (
    <div className="login-select">

      {/* ================= ADMIN ================= */}
      <div className="role-card role-admin" onClick={()=>navigate("/login/admin")}>
        <span>🛡️</span>
        <h3>Admin</h3>
        <p>Full system control</p>
      </div>

      {/* ================= DOCTOR ================= */}
      <div className="role-card role-doctor" onClick={()=>navigate("/login/doctor")}>
        <span>🩺</span>
        <h3>Doctor</h3>
        <p>Patient appointments</p>
      </div>

      {/* ================= PATIENT ================= */}
      <div className="role-card role-patient" onClick={()=>navigate("/login/patient")}>
        <span>👤</span>
        <h3>Patient</h3>
        <p>Book visits</p>
      </div>

      {/* ================= APPOINTMENT DESK ================= */}
      <div className="role-card role-appointment" onClick={()=>navigate("/login/appointment")}>
        <span>📅</span>
        <h3>Appointment Desk</h3>
        <p>Manage patient bookings</p>
      </div>

      {/* ================= LAB SERVICES ================= */}
      <div className="role-card role-lab" onClick={()=>navigate("/login/lab")}>
        <span>🧪</span>
        <h3>Lab Services</h3>
        <p>Lab tests & reports</p>
      </div>

      {/* ================= PHARMACY SERVICES ================= */}
      <div className="role-card role-pharmacy" onClick={()=>navigate("/login/pharmacy")}>
        <span>💊</span>
        <h3>Pharmacy Services</h3>
        <p>Medicine & prescriptions</p>
      </div>

      {/* ================= BILLING DEPARTMENT ================= */}
      <div className="role-card role-billing" onClick={()=>navigate("/login/billing")}>
        <span>💳</span>
        <h3>Billing Department</h3>
        <p>Payments & invoices</p>
      </div>

    </div>
  );
}