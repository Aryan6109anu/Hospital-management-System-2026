// src/components/common/Navbar.js
import { useNavigate, Link } from "react-router-dom";
import { FaEnvelope } from "react-icons/fa";
import "../../styles/navbar.css";
import { showAlert } from "../common/Alert.js";

export default function Navbar({ openModal, MODAL }) {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    navigate("/login");
  };

  // Normalize role
  const normalizedRole = role
    ? role.startsWith("ROLE_")
      ? role
      : "ROLE_" + role
    : null;

  // Role checks
  const isAdmin = normalizedRole === "ROLE_ADMIN";
  const isDoctor = normalizedRole === "ROLE_DOCTOR";
  const isPatient = normalizedRole === "ROLE_PATIENT";
  const isAppointment = normalizedRole === "ROLE_APPOINTMENT";
  const isPharmacy = normalizedRole === "ROLE_PHARMACY";
  const isBilling = normalizedRole === "ROLE_BILLING";
  const isLab = normalizedRole === "ROLE_LAB";

  // 🔥 Book Appointment Logic
  const handleBook = () => {
    if (!token) {
      showAlert("info", "🔐 Please login to book an appointment");
      navigate("/login");
      return;
    }

    // ❌ Doctor, Billing, Pharmacy cannot book
    if (isDoctor || isBilling || isPharmacy || isLab) {
      showAlert("warning", "You are not allowed to book appointments");
      return;
    }

    // ✅ Admin → open modal
    // ✅ Admin + Appointment Staff → modal OR dashboard
         if (isAdmin || isAppointment) {
      if (openModal && MODAL) {
            openModal(MODAL.APPOINTMENT_BOOK);
      } else {
    navigate("/appointmentStaff/account");
  }
  return;
}

    // ✅ Patient + Appointment Staff → booking page
    if (isPatient) {
      navigate("/online_book_appointment");
    }
  };

  // Dashboard routing
  const openDashboard = () => {
    if (!normalizedRole) return navigate("/login");

    if (isAdmin) navigate("/admin/account");
    else if (isDoctor) navigate("/doctor/account");
    else if (isPatient) navigate("/patient/account");
    else if (isAppointment) navigate("/appointmentStaff/account");
    else if (isPharmacy) navigate("/pharmacyStaff/account");
    else if (isBilling) navigate("/billingStaff/account");
    else if (isLab) navigate("/labStaff/account");
    else navigate("/login");
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">

        {/* Logo */}
        <div className="navbar-logo" onClick={() => navigate("/")}>
          WS
        </div>

        {/* Links */}
        <ul className="navbar-links">

          <li onClick={() => navigate("/")}>Home</li>

          {token && (
            <li onClick={openDashboard}>
              {isAdmin
                ? "Admin Account"
                : isDoctor
                ? "Doctor Account"
                : isAppointment
                ? "Appointment Account"
                : isPharmacy
                ? "Pharmacy Account"
                : isBilling
                ? "Billing Account"
                : isLab
                ? "Lab Account"
                : "My Account"}
            </li>
          )}

          {(isAdmin || isPatient) && (
            <li onClick={() => navigate("/doctors")}>Doctors</li>
          )}

          {(isAdmin || isPatient || isAppointment) && (
            <li onClick={() => navigate("/appointments")}>
              Appointments
            </li>
          )}

          <li onClick={() => navigate("/contact")}>Contact</li>

          {/* Admin Inbox */}
          {isAdmin && (
            <li>
              <Link to="/admin/inbox" className="nav-icon">
                <div className="icon-wrapper">
                  <FaEnvelope size={24} />
                  <span className="icon-text">Inbox</span>
                </div>
              </Link>
            </li>
          )}

        </ul>

        {/* Actions */}
        <div className="navbar-actions">

          {/* Book Appointment */}
          {(isAdmin || isPatient || isAppointment) && (
            <button
              className="btn-cta book-appointment"
              onClick={handleBook}
            >
              📅 Book Appointment
            </button>
          )}

          {/* Login / Logout */}
          {token ? (
            <button className="btn-logout" onClick={logout}>
              Logout
            </button>
          ) : (
            <button
              className="btn-cta"
              onClick={() => navigate("/login")}
            >
              Login
            </button>
          )}

        </div>
      </div>
    </nav>
  );
}