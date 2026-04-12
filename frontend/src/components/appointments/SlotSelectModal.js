import React, { useEffect, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import { formatTimeAMPM, formatDate } from "../../utils/dateTimeUtils";
import {
  getAvailableSlots   // ✅ EXISTING & VALID
} from "../../services/doctorScheduleService";
import "../styles/slotSelect.css";

export default function SlotSelectModal({ doctors, onClose }) {
  const [doctorId, setDoctorId] = useState("");
  const [selectedDates, setSelectedDates] = useState([]);
  const [existingSlots, setExistingSlots] = useState([]);
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  /* ================= FETCH EXISTING SLOTS ================= */
  useEffect(() => {
    if (!doctorId) return;

    setSelectedDates([]);

    const today = formatDate(new Date());

    getAvailableSlots(doctorId, today)
      .then(res => {
        setExistingSlots(res.data || []);
      })
      .catch(err => {
        console.error("Slot fetch error", err);
        setExistingSlots([]);
      });
  }, [doctorId]);

  /* ================= DATE CLICK ================= */
  const handleDateClick = (info) => {
    const date = info.dateStr;

    const booked = existingSlots.find(
      s => s.slotDate === date && s.booked
    );

    if (booked) {
      alert("This date is already booked ❌");
      return;
    }

    setSelectedDates(prev =>
      prev.includes(date)
        ? prev.filter(d => d !== date)
        : [...prev, date]
    );
  };

  /* ================= TEMP SAVE (NO BULK API YET) ================= */
  const handleSubmit = () => {
    if (!doctorId || selectedDates.length === 0 || !startTime || !endTime) {
      alert("Doctor, date and time required ❌");
      return;
    }

    console.log("TEMP SLOT DATA 👉", {
      doctorId,
      selectedDates,
      startTime,
      endTime
    });

    alert("Frontend OK ✅ (Bulk API pending)");
    onClose();
  };

  /* ================= CALENDAR COLORS ================= */
  const calendarEvents = [
    ...existingSlots.map(s => ({
      date: s.slotDate,
      display: "background",
      backgroundColor: s.booked ? "#dc2626" : "#2563eb"
    })),
    ...selectedDates.map(date => ({
      date,
      display: "background",
      backgroundColor: "#22c55e"
    }))
  ];

  return (
    <div className="modal-backdrop">
      <div className="modal-content calendar-modal">
        <h3>Doctor Slot Calendar</h3>

        {/* Doctor Select */}
        <label>Select Doctor</label>
        <select value={doctorId} onChange={e => setDoctorId(e.target.value)}>
          <option value="">-- Select Doctor --</option>
          {doctors.map(d => (
            <option key={d.id} value={d.id}>{d.name}</option>
          ))}
        </select>

        {/* Calendar */}
        <FullCalendar
          plugins={[dayGridPlugin, interactionPlugin]}
          initialView="dayGridMonth"
          events={calendarEvents}
          dateClick={handleDateClick}
          height={320}
          selectable
          showNonCurrentDates={false}
        />

        {/* Time */}
        <div className="time-row">
          <div>
            <label>Start Time</label>
          <input
               type="time"
               value={startTime}
                  onChange={e => setStartTime(formatTimeAMPM(e.target.value))}
                 />
          </div>
          <div>
            <label>End Time</label>
            <input
              type="time"
              value={endTime}
              onChange={e => setEndTime(formatTimeAMPM(e.target.value))}
            />
          </div>
        </div>

        {/* Actions */}
        <div className="modal-actions">
          <button className="btn-primary" onClick={handleSubmit}>
            Save (Temp)
          </button>
          <button className="btn-secondary" onClick={onClose}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}
