import React, { useEffect, useState, useCallback } from "react";
import api from "../../services/api";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { formatDate, formatTimeAMPM } from "../../utils/dateTimeUtils";
import { showAlert } from "../common/Alert";
import {
  checkDoctorAvailability,
  getAvailableSlots
} from "../../services/doctorScheduleService";
import "../styles/walkinAppointment.css";

export default function WalkinAppointmentBookModal({
  doctors = [],
  onClose,
  onSaved,
  appointment
}) {

  const [allDoctors, setAllDoctors] = useState([]);
  const [showSlots, setShowSlots] = useState(false);
  const [slots, setSlots] = useState([]);
  const [availableDates, setAvailableDates] = useState([]);
  const [availabilityError, setAvailabilityError] = useState("");
  

  const [form, setForm] = useState({
    patientName: "",
    mobile: "",
    gender: "",
    age: "",
    address: "",
    disease: "",
    department: "",
    doctorId: "",
    appointmentDate: "",
    slotStartTime: ""
  });

  /* ================= LOAD DOCTORS ================= */
  useEffect(() => {
    if (doctors && doctors.length > 0) {
      setAllDoctors(doctors);
    } else {
      api.get("/doctors")
        .then(res => {
          setAllDoctors(res.data || []);
        })
        .catch(err => {
          console.error(err);
          setAllDoctors([]);
        });
    }
  }, [doctors]);

  const departments = [...new Set(allDoctors.map(d => d.department))];
  
  /* ================= LOAD AVAILABLE DATES ================= */
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

  /* ================= CHECK AVAILABILITY ================= */
  const checkAvailabilityAndLoadSlots = useCallback(async (doctorId, date) => {
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
    } catch (err) {
      console.error(err);
      const msg = "Failed to load slots";
      setAvailabilityError(msg);
      showAlert("error", msg);
    }
  }, []);

  /* ================= DOCTOR CHANGE ================= */
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



 /* ================= SUBMIT (Fixed for AM/PM Backend) ================= */
 const handleSubmit = async () => {
  if (!form.department || !form.doctorId || !form.appointmentDate || !form.slotStartTime) {
    showAlert("error", "❌ Department, Doctor, Date and Slot are required");
    return;
  }

  // ✅ Step 1: Selected slot se End Time nikalna
  const selectedSlotData = slots.find( s => formatTimeAMPM(s.startTime) === form.slotStartTime);
  const finalEndTime = selectedSlotData ? formatTimeAMPM(selectedSlotData.endTime) : "";

  try {
    await api.post("/appointments/walk-in", {
      patientName: form.patientName,
      mobile: form.mobile,
      gender: form.gender,
      age: parseInt(form.age),
      address: form.address,
      disease: form.disease,
      doctorId: form.doctorId,
      appointmentDate: formatDate(form.appointmentDate),
      slotStartTime: form.slotStartTime, // Ye pehle se "02:30 PM" hai
      slotEndTime: finalEndTime          // ✅ Ye field add ki gayi hai
    });

    showAlert("success", "✅ Appointment Booked");
    onSaved && onSaved();
    onClose();
  } catch (err) {
    console.error("Booking Error:", err);
    // Agar slot available nahi hai toh backend wala message dikhayein
    const errorMsg = err.response?.data?.message || "Check Format";
    showAlert("error", "❌ Booking failed: " + errorMsg);
  }
};
  /* ================= JSX ================= */
  return (
    <div className="modal-overlay">
      <div className="modal-card wide">
        <h3>Book Walk-in Appointment</h3>

        <div className="form-grid">

          <input
            placeholder="Patient Name"
            value={form.patientName}
            onChange={e => setForm({ ...form, patientName: e.target.value })}
          />

          <input
            placeholder="Mobile"
            value={form.mobile}
            onChange={e => setForm({ ...form, mobile: e.target.value })}
          />

          <select
            value={form.gender}
            onChange={e => setForm({ ...form, gender: e.target.value })}
          >
            <option value="">Gender</option>
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
            <option value="OTHER">Other</option>
          </select>

          <input
            type="number"
            placeholder="Age"
            value={form.age}
            onChange={e => setForm({ ...form, age: e.target.value })}
          />

          <input
            placeholder="Address"
            value={form.address}
            className="full-width"
            onChange={e => setForm({ ...form, address: e.target.value })}
          />

          <input
            placeholder="Disease"
            value={form.disease}
            onChange={e => setForm({ ...form, disease: e.target.value })}
          />

          {/* ================= DEPARTMENT ================= */}
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
                {dep?.replaceAll("_", " ")}
              </option>
            ))}
          </select>

          {/* ================= DOCTOR ================= */}
          <select
            value={form.doctorId}
            onChange={handleDoctorChange}
            disabled={!form.department}
          >
            <option value="">Select Doctor</option>
            {allDoctors
              .filter(d => d.department === form.department && d.available)
              .map(d => (
                <option key={d.id} value={d.id}>
                  {d.name} ({d.specialization})
                </option>
              ))}
          </select>

          {/* ================= DATE ================= */}
          <DatePicker
            selected={form.appointmentDate}
            includeDates={availableDates.length > 0 ? availableDates : undefined}
            onChange={date => {
              setForm(prev => ({ ...prev, appointmentDate: date, slotStartTime: "" }));
              if (form.doctorId && date) {
                checkAvailabilityAndLoadSlots(form.doctorId, date);
              }
            }}
            placeholderText={availableDates.length > 0 ? "Select available date" : "No available dates"}
            dateFormat="yyyy-MM-dd"
            className="date-picker full-width"
            withPortal
          />

          {availabilityError && (
            <div className="message full-width">{availabilityError}</div>
          )}

          <button
            type="button"
            className={`slot-button ${
              form.department && form.doctorId && form.appointmentDate
                ? "ready"
                : ""
            } ${form.slotStartTime ? "slot-selected-btn" : ""}`}
            disabled={!(form.department && form.doctorId && form.appointmentDate)}
            onClick={() => setShowSlots(true)}
          >
            {form.slotStartTime || "Select Slot"}
          </button>

          {showSlots && (
            <div className="slot-overlay">
              <div className="slot-popup">
                <h4>Select Available Slot</h4>

                {slots.length === 0 && (
                  <p className="no-slot">No slots</p>
                )}

                <div className="slot-grid">
                  {slots.map((s, i) => {
                    const isBooked = s.booked;
                     const cleanStart = formatTimeAMPM(s.startTime);
                    const cleanEnd = formatTimeAMPM(s.endTime);
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

                <button
                  className="btn-close"
                  onClick={() => setShowSlots(false)}
                >
                  Close
                </button>
              </div>
            </div>
          )}

          <div
            className="form-grid full-width"
            style={{ marginTop: "15px" }}
          >
            <button type="submit" onClick={handleSubmit}>
              Book
            </button>

            <button
              type="button"
              className="btn-cancel"
              onClick={onClose}
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}