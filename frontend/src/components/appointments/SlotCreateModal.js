import React, { useState } from "react";
import api from "../../services/api";

export default function SlotCreateModal({ doctorId, onClose, onSaved }) {
  const [date, setDate] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  const createSlot = async () => {
    await api.post("/slots", {
      doctorId,
      date,
      startTime,
      endTime,
    });
    alert("Slot created");
    onSaved();
    onClose();
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <h3>Create Slot</h3>

        <input type="date" onChange={(e) => setDate(e.target.value)} />
        <input type="time" onChange={(e) => setStartTime(e.target.value)} />
        <input type="time" onChange={(e) => setEndTime(e.target.value)} />

        <button onClick={createSlot}>Create</button>
        <button onClick={onClose}>Cancel</button>
      </div>  
    </div>
  );
}