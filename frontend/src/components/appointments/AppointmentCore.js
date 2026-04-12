import React, { useState } from "react";
import DatePicker from "react-datepicker";
import {
  checkDoctorAvailability,
  getAvailableSlots
} from "../../services/doctorScheduleService";
import "../styles/appointmentCore.css"

export default function AppointmentCore({
  mode,
  doctors,
  form,
  setForm,
  availableDates,
  loadAvailableDates,
  onSubmit
}) {

  const [showSlots, setShowSlots] = useState(false);
  const [slots, setSlots] = useState([]);
  const [availabilityError, setAvailabilityError] = useState("");

  const checkAndLoadSlots = async (doctorId, date) => {
    try {
      setAvailabilityError("");
      setSlots([]);
      const isoDate = date.toISOString().split("T")[0];

      const availability = await checkDoctorAvailability(doctorId, isoDate);
      if (!availability.data) {
        setAvailabilityError("Doctor not available");
        return;
      }

      const slotRes = await getAvailableSlots(doctorId, isoDate);
      setSlots(slotRes.data || []);
    } catch {
      setAvailabilityError("Failed to load slots");
    }
  };

  return (
    <div className="ac-card">

      <h2 className="ac-title">
        {mode === "walkin" ? "Walk-in Appointment" : "Online Appointment"}
      </h2>

      {availabilityError && (
        <div className="ac-error">{availabilityError}</div>
      )}

      {/* FORM GRID */}
      <div className="ac-grid">

        {mode === "walkin" && (
          <>
            <div className="ac-field">
              <label>Patient Name</label>
              <input
                value={form.patientName}
                onChange={e =>
                  setForm({ ...form, patientName: e.target.value })
                }
              />
            </div>

            <div className="ac-field">
              <label>Mobile Number</label>
              <input
                value={form.mobile}
                onChange={e =>
                  setForm({ ...form, mobile: e.target.value })
                }
              />
            </div>
          </>
        )}

        <div className="ac-field">
          <label>Disease / Problem</label>
          <input
            value={form.disease}
            onChange={e =>
              setForm({ ...form, disease: e.target.value })
            }
          />
        </div>

        <div className="ac-field">
          <label>Select Doctor</label>
          <select
            value={form.doctorId}
            onChange={e => {
              setForm({
                ...form,
                doctorId: e.target.value,
                appointmentDate: null,
                startTime: ""
              });
              loadAvailableDates(e.target.value);
            }}
          >
            <option value="">Select Doctor</option>
            {doctors.map(d => (
              <option key={d.id} value={d.id}>
                {d.name}
              </option>
            ))}
          </select>
        </div>

        <div className="ac-field">
          <label>Appointment Date</label>
          <DatePicker
            selected={form.appointmentDate}
            includeDates={availableDates}
            placeholderText="Select available date"
            className="ac-datepicker"
            onChange={date => {
              setForm({
                ...form,
                appointmentDate: date,
                startTime: ""
              });
              if (form.doctorId) {
                checkAndLoadSlots(form.doctorId, date);
              }
            }}
          />
        </div>

        <div className="ac-field">
          <label>Time Slot</label>
          <button
            type="button"
            className="ac-slot-btn"
            onClick={() => setShowSlots(true)}
          >
            {form.startTime || "Select Slot"}
          </button>
        </div>

      </div>

      <button className="ac-submit-btn" onClick={onSubmit}>
        Book Appointment
      </button>

      {/* SLOT POPUP */}
      {showSlots && (
        <div className="ac-slot-overlay">
          <div className="ac-slot-popup">

            <h4>Select Slot</h4>

            <div className="ac-slot-grid">
              {slots.map((slot, i) => (
                <div
                  key={i}
                  className={`ac-slot-box ${
                    slot.booked ? "ac-slot-booked" : "ac-slot-free"
                  }`}
                  onClick={() => {
                    if (!slot.booked) {
                      setForm({
                        ...form,
                        startTime: slot.startTime
                      });
                      setShowSlots(false);
                    }
                  }}
                >
                  {slot.startTime} - {slot.endTime}
                </div>
              ))}
            </div>

            <button
              className="ac-btn-close"
              onClick={() => setShowSlots(false)}
            >
              Close
            </button>

          </div>
        </div>
      )}

    </div>
  );
}
