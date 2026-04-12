import React, { useEffect, useState } from "react";
import api from "../../services/api"; // ✅ axios wrapper
import "../../styles/patientProfile.css";
import { showAlert } from "../../components/common/Alert";

export default function PatientAccount() {
  const [patient, setPatient] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [formData, setFormData] = useState({});

  /* ================= LOAD PROFILE ================= */
  useEffect(() => {
    api.get("/patients/me")
      .then((res) => {
        setPatient(res.data);
        setFormData(res.data);
      })
      .catch(() => {
        showAlert("error", "Failed to load patient profile ❌");
      });
  }, []);

  /* ================= INPUT CHANGE ================= */
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  /* ================= UPDATE PROFILE ================= */
  const handleUpdate = async () => {
    try {
      await api.put("/patients/me", formData);
      setPatient(formData);
      setEditMode(false);
      showAlert("success", "Profile updated successfully ✅");
    } catch (err) {
      showAlert("error", err.response?.data || "Update failed ❌");
    }
  };

  if (!patient) {
    return <h3 className="loading">Loading profile...</h3>;
  }

  return (
    <div className="patient-profile-container">
      <div className="profile-card">

        <div className="profile-header">
          <h2>Patient Profile</h2>
          <button
            className="edit-btn"
            onClick={() => setEditMode(!editMode)}
          >
            {editMode ? "Cancel" : "Edit Profile"}
          </button>
        </div>

        <div className="profile-body">
          <ProfileField label="Name" value={formData.name} disabled />
          <ProfileField label="Username" value={formData.username} disabled />

          <ProfileField
            label="Age"
            name="age"
            value={formData.age}
            editMode={editMode}
            onChange={handleChange}
          />

          <ProfileField label="Gender" value={formData.gender} disabled />

          <ProfileField
            label="Mobile"
            name="mobile"
            value={formData.mobile}
            editMode={editMode}
            onChange={handleChange}
          />

          <ProfileField
            label="Address"
            name="address"
            value={formData.address}
            editMode={editMode}
            onChange={handleChange}
          />
        </div>

        {editMode && (
          <div className="profile-footer">
            <button className="save-btn" onClick={handleUpdate}>
              Save Changes
            </button>
          </div>
        )}

      </div>
    </div>
  );
}

/* ================= REUSABLE FIELD ================= */
const ProfileField = ({
  label,
  value,
  name,
  editMode,
  onChange,
  disabled,
}) => (
  <div className="profile-field">
    <label>{label}</label>
    {editMode && !disabled ? (
      <input
        type="text"
        name={name}
        value={value || ""}
        onChange={onChange}
      />
    ) : (
      <span>{value || "-"}</span>
    )}
  </div>
);
