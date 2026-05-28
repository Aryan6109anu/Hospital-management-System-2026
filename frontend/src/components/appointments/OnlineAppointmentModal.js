/**********************************************************
 * ONLINE APPOINTMENT MODAL (UPDATED FINAL FILE)
 * Flow:
 * Role → Doctors → Department → Doctor → Date → Slot → Book
 **********************************************************/

import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/onlineAppointment.css";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { formatDate, formatTimeAMPM } from "../../utils/dateTimeUtils";
import  {showAlert} from "../common/Alert.js";
import api from "../../services/api";
/* ================== SERVICES (API CALLS) ================== */
import { createAppointment } from "../../services/appointmentService";
import { getAllDoctors} from "../../services/doctorService";
import {
  checkDoctorAvailability,
  getAvailableSlots
} from "../../services/doctorScheduleService";
import { getMyPatientProfile } from "../../services/patientService";

export default function OnlineAppointmentModal() {
  /* ================== ROUTING ================== */
  const navigate = useNavigate();

  /* ================== STATES ================== */
  const [role, setRole] = useState("");
  const [doctors, setDoctors] = useState([]);
  const [patient, setPatient] = useState({});
  const [showSlots, setShowSlots] = useState(false);
  const [slots, setSlots] = useState([]);
  const [availableDates, setAvailableDates] = useState([]);
  const [availabilityError, setAvailabilityError] = useState("");

  /* ================== FORM STATE ================== */
  const [form, setForm] = useState({
    department: "",
    doctorId: "",
    patientId: "",
    disease: "",
    appointmentDate: null,
    slotStartTime: ""
  });

  /* ================== DERIVED ================== */
  const departments = [...new Set(doctors.map(d => d.department))].filter(Boolean); 

  /* =========================================================
     INITIAL LOAD
     ========================================================= */
  useEffect(() => {
    const storedRole = localStorage.getItem("role");
    setRole(storedRole);

    loadDoctors();

    if (storedRole === "ROLE_PATIENT") {
      loadPatientInfo();
    }
  }, []);

  /* =========================================================
     LOAD DOCTORS
     ========================================================= */
  const loadDoctors = async () => {
    try {
      const res = await getAllDoctors();
      setDoctors(res.data || []);
    } catch (err) {
      console.error(err);
      showAlert("error", "Failed to load doctors");
    }
  };

  /* =========================================================
     LOAD PATIENT PROFILE
     ========================================================= */
  const loadPatientInfo = async () => {
    try {
      const res = await getMyPatientProfile();
      const data = res.data || {};
      setPatient(data);
      setForm(prev => ({ ...prev, patientId: data.id }));
    } catch (err) {
      console.error(err);
      showAlert("error", "Failed to load patient info");
    }
  };

  /* =========================================================
     LOAD AVAILABLE DATES
     ========================================================= */
  const loadAvailableDates = async (doctorId) => {
    try {
      const res = await api.get(`/doctor-schedules/${doctorId}/available-dates`);
      const dates = (res.data || []).map(d => {
        const parts = d.split("-");
        return new Date(parts[0], parts[1] - 1, parts[2]);
      });
      setAvailableDates(dates);
    } catch (err) {
      console.error(err);
      setAvailableDates([]);
    }
  };

  /* =========================================================
     CHECK AVAILABILITY + LOAD SLOTS
     ========================================================= */
  const checkAvailabilityAndLoadSlots = async (doctorId, date) => {
    try {
      setAvailabilityError("");
      setSlots([]);

      const isoDate = formatDate(date);
      const availabilityRes = await checkDoctorAvailability(doctorId, isoDate);

      if (!availabilityRes.data) {
        const msg = "Doctor not available on this date";  
        setAvailabilityError(msg);
        showAlert("error", msg); 
        return;
      }

      const slotRes = await getAvailableSlots(doctorId, isoDate);
      const data = Array.isArray(slotRes.data) ? slotRes.data : [];

      setSlots(data);

      if (data.length === 0 || data.every(s => s.booked)) {
        const msg = "All slots are full for this day";  
        setAvailabilityError(msg);
        showAlert("warning", msg);
      }
    } catch {
      const msg = "Failed to load slots";
      setAvailabilityError(msg);  
      showAlert("error", msg);
    }
  };

  /* =========================================================
     DOCTOR CHANGE HANDLER
     ========================================================= */
  const handleDoctorChange = (e) => {
    const doctorId = e.target.value;

    setForm(prev => ({
      ...prev,
      doctorId,
      appointmentDate: null,
      slotStartTime: ""
    }));

    setSlots([]);
    setAvailableDates([]);
    setAvailabilityError("");

    if (doctorId) loadAvailableDates(doctorId);
  };

  /* =========================================================
     FORM VALIDATION
     ========================================================= */
  const validateForm = () => {
    if (!form.disease) return "Enter disease";
    if (!form.department) return "Select department";
    if (!form.doctorId) return "Select doctor";
    if (!form.appointmentDate) return "Select date";
    if (!form.slotStartTime) return "Select time slot";
    return null;
  };

  /* =========================================================
     SUBMIT APPOINTMENT
     ========================================================= */
  const handleSubmit = async (e) => {
    e.preventDefault();

    const error = validateForm();
    if (error) {
      alert("❌ " + error);
      return;
    }

    try {
      await createAppointment({
        doctorId: form.doctorId,
        patientId: role === "ROLE_ADMIN" ? null : form.patientId,
        disease: form.disease,
        slotDate: formatDate(form.appointmentDate),
        slotStartTime: form.slotStartTime
      });

      showAlert("success", "✅ Appointment Booked");
      navigate(-1);
    } catch {
      showAlert("error","❌ Booking failed");
    }
  };

  /* =========================================================
     CANCEL
     ========================================================= */
  const handleClose = () => {
    navigate(-1);
  };

  /* =========================================================
     UI JSX
     ========================================================= */
  return (
    <div className="oa-overlay">
      <div className="oa-card">

        {/* ===== TITLE ===== */}
        <h2 className="oa-title">
          {role === "ROLE_ADMIN"
            ? "Walk-in Appointment"
            : "Book Online Appointment"}
        </h2>

        {/* ===== ERROR ===== */}
        {availabilityError && (
          <div className="oa-error">{availabilityError}</div>
        )}

        {/* ===== PATIENT INFO ===== */}
        {role === "ROLE_PATIENT" && (
          <div className="oa-patient-grid">
            <div className="oa-field">
              <label className="oa-label">ID</label>
              <input
                className="oa-input oa-input-readonly"
                value={patient.id || ""}
                readOnly
              />
            </div>

            <div className="oa-field">
              <label className="oa-label">Name</label>
              <input
                className="oa-input oa-input-readonly"
                value={patient.name || ""}
                readOnly
              />
            </div>

            <div className="oa-field">
              <label className="oa-label">Age</label>
              <input
                className="oa-input oa-input-readonly"
                value={patient.age || ""}
                readOnly
              />
            </div>

            <div className="oa-field">
              <label className="oa-label">Gender</label>
              <input
                className="oa-input oa-input-readonly"
                value={patient.gender || ""}
                readOnly
              />
            </div>

            <div className="oa-field">
              <label className="oa-label">Mobile</label>
              <input
                className="oa-input oa-input-readonly"
                value={patient.mobile || ""}
                readOnly
              />
            </div>

            <div className="oa-field">
              <label className="oa-label">Address</label>
              <input
                className="oa-input oa-input-readonly"
                value={patient.address || ""}
                readOnly
              />
            </div>
          </div>
        )}

        {/* ===== FORM ===== */}
        <form className="oa-form-grid" onSubmit={handleSubmit}>

          <div className="oa-field">
            <label className="oa-label">Disease</label>
            <input
              className="oa-input"
              value={form.disease}
              onChange={e =>
                setForm(prev => ({ ...prev, disease: e.target.value }))
              }
            />
          </div>

          <div className="oa-field">
            <label className="oa-label">Department</label>
            <select
              className="oa-select"
              value={form.department}
              onChange={e =>
                setForm(prev => ({
                  ...prev,
                  department: e.target.value,
                  doctorId: "",
                  appointmentDate: null,
                  slotStartTime: ""
                }))
              }
            >
              <option value="">Select Department</option>
              {departments.map(dep => (
                <option key={dep} value={dep}>
                  {dep.replaceAll("_", " ")}
                </option>
              ))}
            </select>
          </div>

          <div className="oa-field">
            <label className="oa-label">Doctor</label>
            <select
              className="oa-select"
              value={form.doctorId}
              onChange={handleDoctorChange}
              disabled={!form.department}
            >
              <option value="">Select Doctor</option>
              {doctors
                .filter(d => d.department === form.department )
                .map(d => (
                  <option key={d.id} value={d.id}>
                    {d.name} ({d.specialization})
                  </option>
                ))}
            </select>
          </div>

          <div className="oa-field">
            <label className="oa-label">Appointment Date</label>
            <DatePicker
              selected={form.appointmentDate}
              includeDates={availableDates.length > 0 ? availableDates : undefined}
              onChange={date => {
                setForm(prev => ({ ...prev, appointmentDate: date, slotStartTime: "" }));
                if (form.doctorId && date) {
                  checkAvailabilityAndLoadSlots(form.doctorId, date);
                }
              }} 
              placeholderText="Select appointment date"
              dateFormat="yyyy-MM-dd"
              className="date-picker"
              withPortal
            />
          </div>

          <div className="oa-field oa-slot-field">
            <label className="oa-label">Time Slot</label>
            <button
              type="button"
              className={`oa-slot-btn ${
                form.department && form.doctorId && form.appointmentDate ? "ready" : ""
              } ${form.slotStartTime ? "slot-selected-btn" : ""}`}
              disabled={!(form.department && form.doctorId && form.appointmentDate)}
              onClick={() => setShowSlots(true)}
            >
              {form.slotStartTime || "Select Slot"}
            </button>
          </div>

          {/* ===== ACTIONS ===== */}
          <div className="oa-form-actions">
            <button type="submit" className="oa-submit-btn">
              Book Appointment
            </button>

            <button
              type="button"
              className="oa-cancel-btn"
              onClick={handleClose}
            >
              Cancel
            </button>
          </div>
        </form>

        {/* ===== SLOT POPUP ===== */}
        {showSlots && (
          <div className="slot-overlay">
            <div className="slot-popup">

              <h4 className="slot-title">Select Available Slot</h4>

              {availabilityError && (
                <p className="slot-message error">
                  {availabilityError}
                </p>
              )}

              {slots.length === 0 && !availabilityError && (
                <p className="slot-message">
                  No slots
                </p>
              )}

              <div className="slot-grid">
                {slots.map((s, i) => { 
                  const cleanStart = formatTimeAMPM(s.startTime);
                  const cleanEnd = formatTimeAMPM(s.endTime);
                  const isSelected = form.slotStartTime === cleanStart; // <-- Fixed logic here
                  
                  return (
                    <div
                      key={i}
                      className={`slot-box ${
                        s.booked ? "slot-booked" : "slot-free"
                      } ${isSelected ? "slot-selected" : ""}`} // <-- Dynamic active class added
                      onClick={() => {
                        if (!s.booked) {
                          setForm(prev => ({
                            ...prev,
                            slotStartTime: cleanStart
                          }));
                          setShowSlots(false);
                        }
                      }}
                    >
                      {cleanStart} - {cleanEnd}
                    </div>
                  );
                })}
              </div>

              <button
                className="btn-close"
                onClick={() => setShowSlots(false)}
              >
                Close
              </button>

            </div>
          </div>
        )}
      </div>
    </div>
  );
}