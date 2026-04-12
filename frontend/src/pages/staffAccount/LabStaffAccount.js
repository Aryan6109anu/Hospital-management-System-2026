import React, { useEffect, useState, useCallback, useMemo } from "react";
import api from "../../services/api";
import "../../styles/global.css";
import "../../styles/table.css";
import "../../styles/labStaffAccount.css";
import Navbar from "../../components/common/Navbar";
import { showAlert } from "../../components/common/Alert";

export default function LabStaffAccount() {

  const token = localStorage.getItem("token");

  const authHeader = useMemo(() => ({
    headers: { Authorization: `Bearer ${token}` }
  }), [token]);

  const [staff, setStaff] = useState(null);
  const [tests, setTests] = useState([]);
  const [patients, setPatients] = useState([]);
  const [labOrders, setLabOrders] = useState([]);

  const [searchPatientId,setSearchPatientId] = useState("");
  const [searchAppointmentId,setSearchAppointmentId] = useState("");

  const [activePage, setActivePage] = useState("DASHBOARD");

  /* ================= FETCH TESTS ================= */

  const fetchTests = useCallback(async () => {
    try {

      const res = await api.get("/lab-orders", authHeader);

      setTests(res.data);

    } catch {

      showAlert("error", "Failed to load lab tests ❌");

    }
  }, [authHeader]);


  /* ================= SEARCH LAB ORDERS ================= */

  const searchByPatient = async () => {

    if(!searchPatientId){
      showAlert("error","Enter Patient ID");
      return;
    }

    try{

      const res = await api.get(`/lab-orders/patient/${searchPatientId}`,authHeader);

      setLabOrders(res.data);

    }catch{

      showAlert("error","No lab orders found");

    }

  };


  const searchByAppointment = async () => {

    if(!searchAppointmentId){
      showAlert("error","Enter Appointment ID");
      return;
    }

    try{

      const res = await api.get(`/lab-orders/appointment/${searchAppointmentId}`,authHeader);

      setLabOrders(res.data);

    }catch{

      showAlert("error","No lab orders found");

    }

  };


  /* ================= INITIAL LOAD ================= */

  useEffect(() => {

    const loadData = async () => {

      try {

        const [staffRes, patientRes] = await Promise.all([
          api.get("/lab-staff/me", authHeader),
          api.get("/patients", authHeader)
        ]);

        setStaff(staffRes.data);
        setPatients(patientRes.data);

        fetchTests();

      } catch {

        showAlert("error", "Failed to load dashboard ❌");

      }
    };

    loadData();

  }, [authHeader, fetchTests]);



  return (
    <div className="doctor-profile-container admin-dashboard">

      <Navbar />

      {/* ================= PROFILE ================= */}

      <div className="profile-card">

        <div className="profile-header">
          <h2>🧪 Lab Staff Profile</h2>
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

      {/* ================= ACTIONS ================= */}

      <div className="appointments-card">

        <h3>Actions</h3>

        <div className="action-row">

          <button
            className="action-btn lab-btn"
            onClick={() => setActivePage("TESTS")}
          >
            🧪 Lab Tests
          </button>

          <button
            className="action-btn patient-btn"
            onClick={() => setActivePage("PATIENTS")}
          >
            🧑 Patient List
          </button>

          <button
            className="action-btn lab-btn"
            onClick={() => setActivePage("LAB_ORDERS")}
          >
            📋 Lab Orders
          </button>

        </div>

      </div>

      {/* ================= TEST TABLE ================= */}

      {activePage === "TESTS" && (

        <DataTable
          title="Lab Tests List"
          headers={[
            "Test ID",
            "Test Name",
            "Category",
            "Price"
          ]}
          data={tests}
          renderRow={(t) => (
            <>
              <td>{t.id}</td>
              <td>{t.name}</td>
              <td>{t.category}</td>
              <td>₹ {t.price}</td>
            </>
          )}
        />

      )}

      {/* ================= PATIENT TABLE ================= */}

      {activePage === "PATIENTS" && (

        <DataTable
          title="Patients List"
          headers={[
            "ID",
            "Name",
            "Gender",
            "Mobile"
          ]}
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


      {/* ================= LAB ORDERS ================= */}

      {activePage === "LAB_ORDERS" && (

        <div className="appointments-card">

          <div className="table-header">

            <h3>Lab Orders</h3>

            <div className="lab-search-row">

              <input
                className="table-search"
                placeholder="Patient ID"
                value={searchPatientId}
                onChange={(e)=>setSearchPatientId(e.target.value)}
              />

              <button
                className="action-btn lab-btn"
                onClick={searchByPatient}
              >
                Search Patient
              </button>

              <input
                className="table-search"
                placeholder="Appointment ID"
                value={searchAppointmentId}
                onChange={(e)=>setSearchAppointmentId(e.target.value)}
              />

              <button
                className="action-btn lab-btn"
                onClick={searchByAppointment}
              >
                Search Appointment
              </button>

            </div>

          </div>

          {labOrders.length === 0 ? (

            <p>No lab orders found</p>

          ) : (

            <table className="appointments-table">

              <thead>

                <tr>
                  <th>Order ID</th>
                  <th>Patient</th>
                  <th>Status</th>
                  <th>Tests</th>
                </tr>

              </thead>

              <tbody>

                {labOrders.map((o) => (

                  <tr key={o.labOrderId}>

                    <td>{o.labOrderId}</td>

                    <td>{o.patientName}</td>

                    <td>{o.status}</td>

                    <td>

                      {o.tests.map((t,i)=>(
                        <div key={i}>{t}</div>
                      ))}

                    </td>

                  </tr>

                ))}

              </tbody>

            </table>

          )}

        </div>

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

    return data.filter((item) =>
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
          placeholder="Search..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />

      </div>

      {filteredData.length === 0 ? (

        <p>No data found</p>

      ) : (

        <table className="appointments-table">

          <thead>
            <tr>{headers.map((h) => <th key={h}>{h}</th>)}</tr>
          </thead>

          <tbody>

            {filteredData.map((item) => (
              <tr key={item.id}>{renderRow(item)}</tr>
            ))}

          </tbody>

        </table>

      )}

    </div>

  );

};