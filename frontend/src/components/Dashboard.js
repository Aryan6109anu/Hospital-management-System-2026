import React, { useEffect, useState } from "react";
import Layout from "../layout/Layout";
import "../styles/dashboard.css";
import {
  getDoctorCount,
  getPatientCount,
  getAppointmentCount
} from "../services/dashboardService";

export default function Dashboard() {

  const role = localStorage.getItem("role") || "USER";

  const [doctorCount, setDoctorCount] = useState(0);
  const [patientCount, setPatientCount] = useState(0);
  const [appointmentCount, setAppointmentCount] = useState(0);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const doctors = await getDoctorCount();
      const patients = await getPatientCount();
      const appointments = await getAppointmentCount();

      setDoctorCount(doctors.data.length);
      setPatientCount(patients.data.length);
      setAppointmentCount(appointments.data.length);
    } catch (err) {
      console.error("Dashboard API Error", err);
    }
  };

  

  return (
    <Layout>
      <h1 className="dashboard-title">Hospital Dashboard</h1>
      <p className="dashboard-role">
        Logged in as: <b>{role}</b>
      </p>

      <div className="card-grid">

        <div className="card">
          <h2>{doctorCount}</h2>
          <p>Doctors</p>
        </div>

        <div className="card">
          <h2>{patientCount}</h2>
          <p>Patients</p>
        </div>

        <div className="card">
          <h2>{appointmentCount}</h2>
          <p>Appointments</p>
        </div>

        {role === "ADMIN" && (
          <div className="card admin-card">
            <h2>ADMIN</h2>
            <p>Panel</p>
          </div>
        )}

      </div>
    </Layout>
  );
}
