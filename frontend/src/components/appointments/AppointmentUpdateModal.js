// src/components/modals/AppointmentUpdateModal.js
import React, { useState, useEffect } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "../styles/appointmentUpdate.css";
import api from "../../services/api";
import { showAlert } from "../common/Alert.js";
import {
  checkDoctorAvailability,
  getAvailableSlots
} from "../../services/doctorScheduleService";

export default function AppointmentUpdateModal({
  appointment,
  doctors = [],
  onClose,
  onSaved = () => {}   
}) {

  const [form, setForm] = useState({
    disease: "",
    department: "",
    appointmentDate: null,
    slotStartTime: "",
    doctorId: "",
  });
  // 👇 ADD THIS
   const isReady =form.department && form.doctorId &&form.appointmentDate;
  const [loading, setLoading] = useState(false);
  const [showSlots, setShowSlots] = useState(false);
  const [slots, setSlots] = useState([]);
  const [availableDates, setAvailableDates] = useState([]);
  const [availabilityError, setAvailabilityError] = useState("");

  const departments = [...new Set(doctors.map(d => d.department))];

  const formatDate = (date) => {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const formatTime = (t) => {
    if (!t) return "";
    return t.split(":").slice(0, 2).join(":");
  };

  const checkAvailabilityAndLoadSlots = React.useCallback(async (doctorId, date) => {
    try {
      setAvailabilityError("");
      setSlots([]);

      const isoDate = formatDate(date);
      const availabilityRes = await checkDoctorAvailability(doctorId, isoDate);

      if (!availabilityRes.data) {
        const msg = "Doctor is not available on this date";
        setAvailabilityError(msg);
        showAlert("warning", msg);   // SweetAlert
        return;
      }

      const slotRes = await getAvailableSlots(doctorId, isoDate);
      const data = Array.isArray(slotRes.data) ? slotRes.data : [];

      setSlots(data);

      if (data.length === 0 || data.every(s => s.booked)) {
          const msg = "All slots are full for this day";
        setAvailabilityError(msg);
        showAlert("warning", msg);   // SweetAlert
      }
    } catch (err) {
      console.error(err);
      setSlots([]);
      const msg = "Failed to load slots";
      setAvailabilityError(msg);
      showAlert("error", msg);       // SweetAlert
    }
  }, []);

  useEffect(() => {
    if (!appointment) return;

    const updatedForm = {
      patientName: appointment.patient?.name || "",
      mobile: appointment.patient?.mobile || "",
      gender: appointment.patient?.gender || "",
      age: appointment.patient?.age || "",
      address: appointment.patient?.address || "",
      disease: appointment.disease || "",
      department: appointment.doctor?.department || "",
      doctorId: appointment.doctor?.id || "",
      appointmentDate: appointment.appointmentDate
        ? new Date(appointment.appointmentDate)
        : null,
      slotStartTime: appointment.slotStartTime || ""
    };

    setForm(updatedForm);

    if (updatedForm.doctorId) {
      loadAvailableDates(updatedForm.doctorId);
    }

    if (updatedForm.doctorId && updatedForm.appointmentDate) {
      checkAvailabilityAndLoadSlots(
        updatedForm.doctorId,
        updatedForm.appointmentDate
      );
    }
  }, [appointment, checkAvailabilityAndLoadSlots]);

  const handleDoctorChange = (e) => {
    const doctorId = e.target.value;

    setForm(prev => ({
      ...prev,
      doctorId,
      appointmentDate: null,
      slotStartTime: ""
    }));

    setSlots([]);
    setAvailabilityError("");
    setAvailableDates([]);

    if (doctorId) {
      loadAvailableDates(doctorId);
    }
  };

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

  const handleSubmit = async () => {
    if (availabilityError) {
      alert(availabilityError);
      return;
    }

    if (!form.doctorId || !form.appointmentDate || !form.slotStartTime) {
      showAlert("warning", "❌ Doctor, Date and Slot are required");
      return;
    }

    try {
      setLoading(true);

      await api.put(`appointments/update/walk-in/${appointment.id}`, {
        disease: form.disease,
        doctorId: form.doctorId,
        appointmentDate: formatDate(form.appointmentDate),
        slotStartTime: form.slotStartTime 
      });

      showAlert("success", "✅ Appointment Updated");
      onSaved();
      onClose();
    } catch (err) {
      console.error(err);
      showAlert("error", "❌ Update failed");
    } finally {
      setLoading(false);
    }
  };

  const patient = appointment?.patient || {};
  const patientId = appointment?.patientId || patient.id || "";
  const patientName = patient.name || appointment?.patientName || "";
  const mobile = patient.mobile || appointment?.patientMobile || "";
  const gender = patient.gender || appointment?.patientGender || "";
  const age = patient.age || appointment?.patientAge || "";
  const address = patient.address || appointment?.patientAddress || "";

  return (
    <div className="modal-overlay">
      <div className="modal-card wide">
        <h3>Update Appointment (Admin)</h3>

        <div className="form-grid readonly-section">
          <div><label>Patient ID</label><input value={patientId} readOnly /></div>
          <div><label>Patient Name</label><input value={patientName} readOnly /></div>
          <div><label>Mobile</label><input value={mobile} readOnly /></div>
          <div><label>Gender</label><input value={gender} readOnly /></div>
          <div><label>Age</label><input value={age} readOnly /></div>
          <div><label>Address</label><input value={address} readOnly /></div>
        </div>

        <div className="form-grid two-column">

          <input
            placeholder="Disease"
            value={form.disease}
            onChange={e => setForm({ ...form, disease: e.target.value })}
          />

          <select
            value={form.department}
            onChange={e =>
              setForm({
                ...form,
                department: e.target.value,
                doctorId: "",
                appointmentDate: "",
                slotStartTime: ""
              })
            }
          >
            <option value="">Select Department</option>
            {departments.map(dep => (
              <option key={dep} value={dep}>
                {dep.replaceAll("_", " ")}
              </option>
            ))}
          </select>

          <select
            value={form.doctorId}
            onChange={handleDoctorChange}
            disabled={!form.department}
          >
            <option value="">Select Doctor</option>
            {doctors
              .filter(d => d.department === form.department && d.available)
              .map(d => (
                <option key={d.id} value={d.id}>
                  {d.name} ({d.specialization})
                </option>
              ))}
          </select>

          <DatePicker
            selected={form.appointmentDate}
            includeDates={availableDates.length > 0 ? availableDates : undefined}
            onChange={date => {
              setForm(prev => ({ ...prev, appointmentDate: date, slotStartTime: "" }));
              if (form.doctorId && date) {
                checkAvailabilityAndLoadSlots(form.doctorId, date);
              }
            }}
            placeholderText={
              availableDates.length > 0
                ? "Select available date"
                : "No available dates"
            }
            dateFormat="yyyy-MM-dd"
            className="date-picker"
            withPortal
          />

                             {/* SLOT SELECTOR BUTTON (FIXED) */}
                            <button
                                   type="button"
                                   disabled={!isReady}
                                   className={`slot-button 
                                   ${isReady ? "ready" : ""} 
                                   ${form.slotStartTime ? "slot-selected-btn" : ""}
                             `}
                         onClick={() => {
                               if (isReady) setShowSlots(true);
                                   }}            >
                         {form.slotStartTime || "Select Slot"}
                            </button>
 
        </div>

        {showSlots && (
          <div className="slot-overlay">
            <div className="slot-popup">
              <h4>Select Available Slot</h4>

              {availabilityError && <p className="no-slot">{availabilityError}</p>}
              {slots.length === 0 && !availabilityError && (
                <p className="no-slot">No slots</p>
              )}

              <div className="slot-grid">
                {slots.map((s, i) => {
                  const isBooked = s.booked;
                  const cleanStart = formatTime(s.startTime);
                  const cleanEnd = formatTime(s.endTime);
                  const isSelected = form.slotStartTime === cleanStart;

                  return (
                    <div
                      key={i}
                      className={`slot-box ${
                        isBooked ? "slot-booked" : "slot-free"
                      } ${isSelected ? "slot-selected" : ""}`}
                      onClick={() => {
                        if (!isBooked) {
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

              <button className="btn-close" onClick={() => setShowSlots(false)}>
                Close
              </button>
            </div>
          </div>
        )}

        <div className="form-grid full-width" style={{ marginTop: "15px" }}>
          <button type="submit" onClick={handleSubmit} disabled={loading}>
            {loading ? "Updating..." : "Update Appointment"}
          </button>
          <button type="cancel" onClick={onClose} disabled={loading}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}
