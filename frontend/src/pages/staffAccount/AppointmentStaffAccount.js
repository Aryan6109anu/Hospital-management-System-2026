// src/pages/staffAccount/AppointmentAccount.js
import React, { useEffect, useState, useCallback, useMemo } from "react";
import api from "../../services/api";
import "../../styles/appointmentStaffAccount.css";
import "../../styles/global.css";
import "../../styles/table.css";
import AppointmentSlip from "./AppointmentSlip";
import Navbar from "../../components/common/Navbar";
import { showAlert } from "../../components/common/Alert";
import { formatDisplayDate, formatTimeRange } from "../../utils/dateTimeUtils";

/* ================= MODALS ================= */
import WalkinAppointmentBookModal from "../../components/appointments/WalkinAppointmentBookModal";
import AppointmentUpdateModal from "../../components/appointments/AppointmentUpdateModal";

/* ================= MODAL CONSTANTS ================= */
const MODAL = {
  APPOINTMENT_BOOK: "APPOINTMENT_BOOK",
  APPOINTMENT_EDIT: "APPOINTMENT_EDIT",
};

/* ================= APPOINTMENT STAFF DASHBOARD ================= */
export default function AppointmentStaffAccount() {
  const token = localStorage.getItem("token");
  const authHeader = useMemo(() => ({ headers: { Authorization: `Bearer ${token}` } }), [token]);

  const [staff, setStaff] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [patients, setPatients] = useState([]);
  const [doctors, setDoctors] = useState([]);

  const [activeModal, setActiveModal] = useState(null);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [activePage, setActivePage] = useState("DASHBOARD");
  const [appointmentFilter, setAppointmentFilter] = useState("TODAY");
const [selectedDate, setSelectedDate] = useState("");

const filteredAppointments = useMemo(() => {

  const today = new Date().toISOString().split("T")[0];

  if (appointmentFilter === "TODAY") {
    return appointments.filter(a => a.appointmentDate === today);
  }

  if (appointmentFilter === "DATE" && selectedDate) {
    return appointments.filter(a => a.appointmentDate === selectedDate);
  }

  return appointments;

}, [appointments, appointmentFilter, selectedDate]);

  /* ================= FETCH APPOINTMENTS ================= */
  const fetchAppointments = useCallback(async () => {
    try {
      const res = await api.get("/appointments", authHeader);
      setAppointments(res.data);
    } catch {
      showAlert("error", "Failed to load appointments ❌");
    }
  }, [authHeader]);

  /* ================= INITIAL LOAD ================= */
  useEffect(() => {
    const loadData = async () => {
      try {
        const [staffRes, doctorsRes, patientsRes] = await Promise.all([
          api.get("/appointment-staff/me", authHeader),
          api.get("/doctors", authHeader),
          api.get("/patients", authHeader),
        ]);

        setStaff(staffRes.data);
        setDoctors(doctorsRes.data);
        setPatients(patientsRes.data);
        fetchAppointments();
      } catch {
        showAlert("error", "Failed to load dashboard ❌");
      }
    };
    loadData();
  }, [authHeader, fetchAppointments]);

  /* ================= MODAL HANDLERS ================= */
  const openModal = (type, appointment = null) => {
    setActiveModal(type);
    if (appointment) setSelectedAppointment(appointment);
  };

  const closeModal = () => {
    setActiveModal(null);
    setSelectedAppointment(null);
  };

  /* ================= UPDATE APPOINTMENT ================= */
  const handleUpdate = async (formData) => {
    try {
      await api.put(`/appointments/update/walk-in/${selectedAppointment.id}`, formData, authHeader);
      showAlert("success", "Appointment updated ✅");
      fetchAppointments();
      closeModal();
    } catch {
      showAlert("error", "Update failed ❌");
    }
  };

  return (
    <div className="doctor-profile-container admin-dashboard">
      {/* ================= NAVBAR ================= */}
      <Navbar openModal={openModal} MODAL={MODAL} />

      {/* ================= PROFILE ================= */}
      <div className="profile-card">
        <div className="profile-header">
          <h2>📌 Appointment Staff Profile</h2>
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
          <button className="action-btn appointment-btn" onClick={() => openModal(MODAL.APPOINTMENT_BOOK)}>
            📅 Book Walk-in Appointment
          </button>
          <button className="action-btn appointment-btn" onClick={() => setActivePage("APPOINTMENTS")}>
            📋 Appointments List
          </button>
          <button className="action-btn patient-btn" onClick={() => setActivePage("PATIENTS")}>
            🧑‍🤝‍🧑 Patient List
          </button>
          <button className="action-btn doctor-btn" onClick={() => setActivePage("DOCTORS")}>
            🩺 Doctor List
          </button>
        </div>
      </div>

     {/* ================= APPOINTMENTS TABLE ================= */}
{activePage === "APPOINTMENTS" && (

  <div className="appointments-card">

    {/* FILTER BAR */}
    <div className="appointment-filter-bar">

      <button
        className={`filter-btn ${appointmentFilter === "TODAY" ? "active" : ""}`}
        onClick={() => setAppointmentFilter("TODAY")}
      >
        📅 Today
      </button>

      <button
        className={`filter-btn ${appointmentFilter === "ALL" ? "active" : ""}`}
        onClick={() => setAppointmentFilter("ALL")}
      >
        📋 All
      </button>

      <button
        className={`filter-btn ${appointmentFilter === "DATE" ? "active" : ""}`}
        onClick={() => setAppointmentFilter("DATE")}
      >
        📆 Date Wise
      </button>

      {appointmentFilter === "DATE" && (
        <input
          type="date"
          className="date-picker"
          value={selectedDate}
          onChange={(e) => setSelectedDate(e.target.value)}
        />
      )}

    </div>

    <DataTable
      title="Appointments List"
      headers={[
        "Appointment ID",
        "Patient",
        "Doctor",
        "Disease",
        "Date",
        "Time",
        "Status",
        "Action"
      ]}
      data={filteredAppointments}
      renderRow={(a) => (
        <>
          <td>{a.id}</td>
          <td>{a.patient?.name || a.patientName}</td>
          <td>{a.doctor?.name || a.doctorName}</td>
          <td>{a.disease || "-"}</td>
          <td>{formatDisplayDate(a.appointmentDate)}</td>
          <td>{formatTimeRange(a.slotStartTime, a.slotEndTime)}</td>

          <td>
            <span className={a.status === "CANCELLED" ? "status-inactive" : "status-active"}>
              {a.status}
            </span>
          </td>

          <td>
  <div className="table-actions-cell">

    <button
      className="btn-primary"
      onClick={() => openModal(MODAL.APPOINTMENT_EDIT, a)}
    >
      ✏ Edit
    </button>

    <button
      className="btn-print"
      onClick={() => {
        setSelectedAppointment(a);
        setTimeout(() => {
          window.print();
          setSelectedAppointment(null);
        }, 500);
      }}
    >
      🖨 Print
    </button>

  </div>
</td>
        </>
      )}
    />

  </div>
)}

         {/* ================= APPOINTMENT SLIP ================= */}
                     {selectedAppointment && (
                            <AppointmentSlip appointment={selectedAppointment} />
           )}

      {/* ================= PATIENTS TABLE ================= */}
      {activePage === "PATIENTS" && (
        <DataTable
          title="Patients List"
           headers={["Patient ID", "Name", "Gender", "Mobile", "Address"]}
          data={patients}
          renderRow={(p) => (
            <>
              <td>{p.id}</td>
              <td>{p.name}</td>
              <td>{p.gender}</td>
              <td>{p.mobile}</td>
              <td>{p.address}</td>
            </>
          )}
        />
      )}

     {/* ================= DOCTORS TABLE ================= */}
                {activePage === "DOCTORS" && (
  <DataTable
    title="Doctors List"
    headers={["ID", "Name", "Specialization", "Experience", "Visiting Hour", "Visiting Day", "Available"]}
    data={doctors}
    renderRow={(d) => {

      const formatTime = (time) => {
        if (!time) return "-";
        const [h, m] = time.split(":");
        let hour = parseInt(h, 10);
        const ampm = hour >= 12 ? "PM" : "AM";
        hour = hour % 12 || 12;
        return `${hour}:${m} ${ampm}`;
      };

      const visitingHour =
        d.schedule?.startTime && d.schedule?.endTime
          ? `${formatTime(d.schedule.startTime)} - ${formatTime(d.schedule.endTime)}`
          : "-";

      const visitingDays =
        d.schedule?.days?.length > 0
          ? d.schedule.days.join(", ")
          : "-";

      const today = new Date().toLocaleString("en-US", { weekday: "long" }).toUpperCase();
      const availableToday = d.available && d.schedule?.days?.includes(today);

      return (
        <>
          <td>{d.id}</td>
          <td>{d.name}</td>
          <td>{d.specialization}</td>
          <td>{d.experience} yrs</td>

          <td>{visitingHour}</td>
          <td>{visitingDays}</td>

          <td>
            <span className={availableToday ? "status-active" : "status-inactive"}>
              {availableToday ? "Available" : "Not Available"}
            </span>
          </td>
        </>
      );
    }}
  />
)}
      {/* ================= MODALS ================= */}
      {activeModal === MODAL.APPOINTMENT_BOOK && (
        <WalkinAppointmentBookModal doctors={doctors} onClose={closeModal} onSaved={fetchAppointments} />
      )}
      {activeModal === MODAL.APPOINTMENT_EDIT && selectedAppointment && (
        <AppointmentUpdateModal appointment={selectedAppointment} doctors={doctors} onClose={closeModal} onUpdate={handleUpdate} />
      )}
    </div>
  );
}

/* ================= SMALL REUSABLE COMPONENTS ================= */
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
          type="text"
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
            <tr>{headers.map((h) => <th key={h}>{h}</th>)}</tr>
          </thead>
          <tbody>{filteredData.map((item) => <tr key={item.id}>{renderRow(item)}</tr>)}</tbody>
        </table>
      )}
    </div>
  );
};
