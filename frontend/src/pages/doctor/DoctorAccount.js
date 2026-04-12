import React, { useEffect, useState, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../services/api";
import "../../styles/accountProfile.css";
import { showAlert } from "../../components/common/Alert.js";
import { formatDisplayDate, formatTimeRange } from "../../utils/dateTimeUtils";

export default function DoctorAccount() {
  const nav = useNavigate();

  const [doctor, setDoctor] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [filteredAppointments, setFilteredAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [searchTerm, setSearchTerm] = useState("");

  const token = localStorage.getItem("token");

  const authHeader = useMemo(
    () => ({ headers: { Authorization: `Bearer ${token}` } }),
    [token]
  );

  // ================= Load profile & appointments =================
  useEffect(() => {
    if (!token) return;
    setLoading(true);

    api.get("/doctors/me", authHeader)
      .then(res => setDoctor(res.data))
      .catch(err => {
        console.error(err);
        const msg = "Failed to load profile ❌";
        setError(msg);
        showAlert("error", msg);
      });

    api.get("/appointments/doctor/me", authHeader)
      .then(res => {
        setAppointments(res.data);
        setFilteredAppointments(res.data);
      })
      .catch(err => showAlert("error", "Failed to load appointments"))
      .finally(() => setLoading(false));

  }, [authHeader, token]);

  const handleSearch = (e) => {
    const value = e.target.value.toLowerCase();
    setSearchTerm(value);
    setFilteredAppointments(
      appointments.filter(a => a.patientName.toLowerCase().includes(value))
    );
  };

  if (loading) return <h3 className="loading">Loading profile...</h3>;
  if (error) return <h3 className="error">{error}</h3>;

  return (
    <div className="doctor-profile-container">
      <div className="profile-card">
        <div className="profile-header">
          <h2>Doctor Profile</h2>
        </div>
        <div className="profile-body">
          <ProfileField label="Name" value={doctor?.name} />
          <ProfileField label="Username" value={doctor?.username} />
          <ProfileField label="Email" value={doctor?.email || "-"} />
          <ProfileField label="Mobile" value={doctor?.mobile || "-"} />
          <ProfileField label="Department" value={doctor?.department || "-"} />
          <ProfileField label="Specialization" value={doctor?.specialization || "-"} />
          <ProfileField label="Experience" value={`${doctor?.experience || 0} years`} />
          <ProfileField label="Availability" value={doctor?.available ? "Available" : "Not Available"} />
        </div>
      </div>

      <div className="appointments-card">
        <h3>Today's Appointments</h3>

        {appointments.length > 0 && (
          <input
            type="text"
            placeholder="Search by patient name..."
            value={searchTerm}
            onChange={handleSearch}
            className="search-input"
          />
        )}

        {filteredAppointments.length === 0 ? (
          <p className="no-appointments">No appointments found</p>
        ) : (
          <table className="appointments-table">
            <thead>
              <tr>
                <th>Appointment Id</th>
                <th>Patient Id</th>
                <th>Patient Name</th>
                <th>Appointment Date</th>
                <th>Time</th>
                <th>Status</th>
                <th>Start Consultation</th>
                <th>View Consultation</th>
              </tr>
            </thead>
            <tbody>
              {filteredAppointments.map(a => (
                <tr key={a.id}>
                  <td>{a.id}</td>
                  <td>{a.patientId}</td>
                  <td>{a.patientName}</td>
                  <td>{formatDisplayDate(a.appointmentDate)}</td>
                  <td>{formatTimeRange(a.slotStartTime, a.slotEndTime)}</td>
                  <td>{a.status}</td>
                  
                  {/* --- START CONSULTATION COLUMN --- */}
                  <td>
                    {a.visitId ? (
                      <button className="btn-consultation btn-completed" disabled>
                        ✅ Completed
                      </button>
                    ) : (
                      <button 
                        className="btn-consultation btn-start-consult" 
                        onClick={() => nav(`/doctor/consultation/${a.id}`)}
                      >
                        🚀 Start Consultation
                      </button>
                    )}
                  </td>

                  {/* --- VIEW CONSULTATION COLUMN --- */}
                  <td>
                    <button 
                      className="btn-consultation btn-view-consult" 
                      onClick={() => nav(`/visits/${a.visitId}`)}
                      disabled={!a.visitId}
                      style={{ opacity: a.visitId ? 1 : 0.5 }}
                    >
                      👁️ View Consultation
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

const ProfileField = ({ label, value }) => (
  <div className="profile-field">
    <label>{label}</label>
    <span>{value || "-"}</span>
  </div>
);