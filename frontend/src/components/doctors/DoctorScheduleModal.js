import React, { useState, useEffect } from "react";
import api from "../../services/api";
import "../styles/doctorschedule.css";
import { showAlert } from "../common/Alert";

// ✅ Import global utils
import { formatTimeAMPM} from "../../utils/dateTimeUtils";

export default function DoctorScheduleModal({ doctorId, onClose, onSaved }) {

  const days = [
    { label: "Monday", value: "MONDAY" },
    { label: "Tuesday", value: "TUESDAY" },
    { label: "Wednesday", value: "WEDNESDAY" },
    { label: "Thursday", value: "THURSDAY" },
    { label: "Friday", value: "FRIDAY" },
    { label: "Saturday", value: "SATURDAY" },
    { label: "Sunday", value: "SUNDAY" },
  ];

  const [form, setForm] = useState({
    workingDays: [],
    startTime: "",
    endTime: "",
    slotDuration: 10,
    maxPatientsPerDay: 20,
  });

  // ================= Load existing schedule =================
  useEffect(() => {
    const loadSchedule = async () => {
      try {
        const res = await api.get(`/doctor-schedules/${doctorId}`);
        if (res.data) {
          setForm({
            workingDays: res.data.days || [],
            startTime: res.data.startTime || "",
            endTime: res.data.endTime || "",
            slotDuration: res.data.slotDuration || 10,
            maxPatientsPerDay: res.data.maxPatientsPerDay || 20,
          });
        }
      } catch (err) {
        showAlert("error", "❌ Failed to load schedule");
      }
    };
    loadSchedule();
  }, [doctorId]);

  // ================= Handlers =================
  const handleCheckboxChange = (e) => {
    const { value, checked } = e.target;
    setForm((prev) =>
      checked
        ? { ...prev, workingDays: [...prev.workingDays, value] }
        : { ...prev, workingDays: prev.workingDays.filter((d) => d !== value) }
    );
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSave = async () => {
    // Validation
    if (form.workingDays.length === 0 || !form.startTime || !form.endTime) {
      showAlert("error", "❌ Please fill all required fields");
      return;
    }

    try {
      await api.post("/doctor-schedules", {
        doctorId,
        days: form.workingDays,
        startTime: form.startTime, // backend expects 24h format
        endTime: form.endTime,     // backend expects 24h format
        slotDuration: form.slotDuration,
        maxPatientsPerDay: form.maxPatientsPerDay,
      });

      showAlert("success", "✅ Schedule saved successfully");
      onSaved();
      onClose();
    } catch (err) {
      console.error(err);
      showAlert("error", "❌ Failed to save schedule");
    }
  };

  // ================= UI =================
  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <h3>🗓️ Set Doctor Schedule</h3>

        <div className="modal-form-grid">

          {/* ===== Working Days ===== */}
          <div className="form-field full-width">
            <label>Working Days</label>
            <div className="day-selector">
              {days.map((d) => (
                <label
                  key={d.value}
                  className={`day-chip ${form.workingDays.includes(d.value) ? "active" : ""}`}
                >
                  <input
                    type="checkbox"
                    value={d.value}
                    checked={form.workingDays.includes(d.value)}
                    onChange={handleCheckboxChange}
                  />
                  {d.label}
                </label>
              ))}
            </div>
          </div>

          {/* ===== Start Time ===== */}
          <div className="form-field">
            <label>Start Time</label>
            <input
              type="time"
              name="startTime"
              value={form.startTime}
              onChange={handleChange}
            />
            <div className="time-display">
              {form.startTime && `(${formatTimeAMPM(form.startTime)})`}
            </div>
          </div>

          {/* ===== End Time ===== */}
          <div className="form-field">
            <label>End Time</label>
            <input
              type="time"
              name="endTime"
              value={form.endTime}
              onChange={handleChange}
            />
            <div className="time-display">
              {form.endTime && `(${formatTimeAMPM(form.endTime)})`}
            </div>
          </div>

          {/* ===== Slot Duration ===== */}
          <div className="form-field">
            <label>Slot Duration (min)</label>
            <input
              type="number"
              name="slotDuration"
              value={form.slotDuration}
              min={5}
              max={120}
              onChange={handleChange}
            />
          </div>

          {/* ===== Max Patients ===== */}
          <div className="form-field">
            <label>Max Patients Per Day</label>
            <input
              type="number"
              name="maxPatientsPerDay"
              value={form.maxPatientsPerDay}
              min={1}
              max={100}
              onChange={handleChange}
            />
          </div>

        </div>

        {/* ===== Buttons ===== */}
        <div className="modal-actions">
          <button className="btn-secondary" onClick={onClose}>Cancel</button>
          <button className="btn-primary" onClick={handleSave}>Save Schedule</button>
        </div>
      </div>
    </div>
  );
}