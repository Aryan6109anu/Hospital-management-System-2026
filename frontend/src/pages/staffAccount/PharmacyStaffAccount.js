
import React, { useEffect, useState, useCallback, useMemo } from "react";
import api from "../../services/api";
import "../../styles/global.css";
import "../../styles/table.css";
import "../../styles/pharmacyStaffAccount.css";
import Navbar from "../../components/common/Navbar";
import { showAlert } from "../../components/common/Alert";

export default function PharmacyStaffAccount() {

  const token = localStorage.getItem("token");
  const authHeader = useMemo(() => ({
    headers: { Authorization: `Bearer ${token}` }
  }), [token]);

  const [staff, setStaff] = useState(null);
  const [medicines, setMedicines] = useState([]);
  const [patients, setPatients] = useState([]);
  const [activePage, setActivePage] = useState("DASHBOARD");

  /* ================= FETCH MEDICINES ================= */
  const fetchMedicines = useCallback(async () => {
    try {
      const res = await api.get("/medicines", authHeader);
      setMedicines(res.data);
    } catch {
      showAlert("error", "Failed to load medicines ❌");
    }
  }, [authHeader]);

  /* ================= INITIAL LOAD ================= */
  useEffect(() => {
    const loadData = async () => {
      try {
        const [staffRes, patientRes] = await Promise.all([
          api.get("/pharmacy-staff/me", authHeader),
          api.get("/patients", authHeader)
        ]);

        setStaff(staffRes.data);
        setPatients(patientRes.data);

        fetchMedicines();

      } catch {
        showAlert("error", "Failed to load dashboard ❌");
      }
    };
    loadData();
  }, [authHeader, fetchMedicines]);

  return (
    <div className="doctor-profile-container admin-dashboard">
      <Navbar />

      {/* ================= PROFILE ================= */}
      <div className="profile-card">
        <div className="profile-header">
          <h2>💊 Pharmacy Staff Profile</h2>
        </div>
        {staff && (
          <div className="profile-body">
            <ProfileField label="Name" value={staff.fullName} />
            <ProfileField label="Username" value={staff.username} />
            <ProfileField label="Email" value={staff.email} />
            <ProfileField label="Mobile" value={staff.mobile} />
          </div>
        )}
      </div>

      {/* ================= ACTION BUTTONS ================= */}
      <div className="appointments-card">
        <h3>Actions</h3>
        <div className="action-row">
          <button
            className="action-btn pharmacy-btn"
            onClick={() => setActivePage("MEDICINES")}
          >
            💊 Medicine List
          </button>
          <button
            className="action-btn patient-btn"
            onClick={() => setActivePage("PATIENTS")}
          >
            🧑 Patient List
          </button>
        </div>
      </div>

      {/* ================= MEDICINE TABLE ================= */}
      {activePage === "MEDICINES" && (
        <DataTable
          title="Medicine List"
          headers={["ID","Name","Category","Price","Stock","Expiry","Action"]}
          data={medicines}
          renderRow={(m) => (
            <>
              <td>{m.id}</td>
              <td>{m.name}</td>
              <td>{m.category}</td>
              <td>₹ {m.price}</td>
              <td>{m.stock}</td>
              <td>{m.expiryDate}</td>
              <td>
                <button className="btn-primary" onClick={() => {
                  showAlert("info", `Edit medicine ${m.name} functionality coming soon!`);
                }}>
                  ✏ Edit
                </button>
              </td>
            </>
          )}
        />
      )}

      {/* ================= PATIENT TABLE ================= */}
      {activePage === "PATIENTS" && (
        <DataTable
          title="Patients List"
          headers={["ID", "Name", "Gender", "Mobile"]}
          data={patients}
          renderRow={(p) => (
            <>
              <td>{p.id}</td>
              <td>{p.name}</td>
              <td>{p.gender}</td>
              <td>{p.mobile}</td>
            </>
          )}
        />
      )}
    </div>
  );
}

/* ================= PROFILE FIELD ================= */
const ProfileField = ({ label, value }) => (
  <div className="profile-field">
    <label>{label}</label>
    <span>{value || "-"}</span>
  </div>
);

/* ================= DATA TABLE ================= */
const DataTable = ({ title, headers, data, renderRow }) => {

  const [search, setSearch] = useState("");

  const filteredData = useMemo(() => {
    if (!search) return data;
    return data.filter(item =>
      Object.values(item)
        .join(" ")
        .toLowerCase()
        .includes(search.toLowerCase())
    );
  }, [data, search]);

  return (
    <div className="appointments-card">
      <div className="table-header">
        <h3>{title}</h3>
        <input
          className="table-search"
          placeholder="Search by ID / Name"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>
      {filteredData.length === 0 ? (
        <p>No data found</p>
      ) : (
        <table className="appointments-table">
          <thead>
            <tr>{headers.map(h => <th key={h}>{h}</th>)}</tr>
          </thead>
          <tbody>
            {filteredData.map(item => (
              <tr key={item.id}>{renderRow(item)}</tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};  