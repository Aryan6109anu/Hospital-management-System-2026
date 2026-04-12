import React, { useEffect, useState } from "react";
import api from "../../services/api";
import DoctorUnavailableModal from "./DoctorUnavailableModal";
import SlotCreateModal from "../appointments/SlotCreateModal";
import { formatDisplayDate } from "../../utils/dateTimeUtils";
import "../styles/calendar.css";


export default function DoctorAvailabilityCalendar({ doctors = [] }) {

  const [selectedDoctor, setSelectedDoctor] = useState("");
  const [calendarData, setCalendarData] = useState([]);
  const [activeDate, setActiveDate] = useState(null);

  const [showUnavailableModal, setShowUnavailableModal] = useState(false);
  const [showSlotModal, setShowSlotModal] = useState(false);

  /* ================= LOAD CALENDAR ================= */
  useEffect(() => {
    if (!selectedDoctor) return;

    api.get(`/admin/doctors/${selectedDoctor}/calendar`)
      .then(res => setCalendarData(res.data))
      .catch(() => setCalendarData([]));

  }, [selectedDoctor]);

  /* ================= STATUS COLOR ================= */
  const getStatusClass = (day) => {
    if (day.unavailable) return "calendar-red";
    if (day.totalSlots === 0) return "calendar-gray";
    if (day.bookedSlots >= day.totalSlots) return "calendar-yellow";
    return "calendar-green";
  };

  return (
    <div className="calendar-container">

      <h2>Doctor Availability Calendar</h2>

      {/* ===== Doctor Select ===== */}
      <select
        value={selectedDoctor}
        onChange={(e) => setSelectedDoctor(e.target.value)}
      >
        <option value="">Select Doctor</option>
        {doctors.map(d => (
          <option key={d.id} value={d.id}>
            {d.name} ({d.specialization})
          </option>
        ))}
      </select>

      {!selectedDoctor && (
        <p>Select a doctor to view calendar</p>
      )}

      {/* ===== Calendar Grid ===== */}
      <div className="calendar-grid">
        {calendarData.map(day => (
          <div
            key={day.date}
            className={`calendar-cell ${getStatusClass(day)}`}
            onClick={() => setActiveDate(day.date)}
          >
            <span>{formatDisplayDate(day.date)}</span>
          </div>
        ))}
      </div>

      {/* ===== ACTION BUTTONS ===== */}
      {activeDate && (
        <div className="calendar-actions">
          <h4>Selected Date: {formatDisplayDate(activeDate)}</h4>

          <button onClick={() => setShowSlotModal(true)}>
            ⏰ Manage Slots
          </button>

          <button onClick={() => setShowUnavailableModal(true)}>
            ❌ Mark Unavailable
          </button>

          <button onClick={() => setActiveDate(null)}>
            Close
          </button>
        </div>
      )}

      {/* ===== MODALS ===== */}
      {showUnavailableModal && (
        <DoctorUnavailableModal
          doctorId={selectedDoctor}
          date={activeDate}
          onClose={() => setShowUnavailableModal(false)}
          onSaved={() => setSelectedDoctor(selectedDoctor)}
        />
      )}

      {showSlotModal && (
        <SlotCreateModal
          doctorId={selectedDoctor}
          date={activeDate}
          onClose={() => setShowSlotModal(false)}
          onSaved={() => setSelectedDoctor(selectedDoctor)}
        />
      )}
    </div>
  );
}
