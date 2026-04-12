import React, { useState } from "react";
import api from "../../services/api";
import "../../styles/slot.css";
import { showAlert } from "../common/Alert";

export default function DoctorUnavailableModal({
  doctorId,
  onClose,
  onSaved
}) {

  const [form, setForm] = useState({
    fromDate: "",
    toDate: "",
    reason: ""
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    if (!form.fromDate || !form.toDate) {
      alert("❌ From & To date required");
      return;
    }

    if (form.fromDate > form.toDate) {
      alert("❌ From date cannot be after To date");
      return;
    }

    try {
      await api.post(
        `/admin/doctors/${doctorId}/unavailable`,
        form
      );

      showAlert("success", "✅ Doctor marked unavailable");
      onSaved();
      onClose();

    } catch (err) {
      showAlert("error", "❌ Failed to mark unavailable");
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">

        <h3>Mark Doctor Unavailable</h3>

        <label>From Date</label>
        <input
          type="date"
          name="fromDate"
          value={form.fromDate}
          onChange={handleChange}
        />

        <label>To Date</label>
        <input
          type="date"
          name="toDate"
          value={form.toDate}
          onChange={handleChange}
        />

        <label>Reason</label>
        <textarea
          name="reason"
          value={form.reason}
          onChange={handleChange}
          placeholder="Leave / Emergency / Holiday"
        />

        <div style={{ marginTop: "15px" }}>
          <button onClick={handleSave}>Save</button>
          <button onClick={onClose}>Cancel</button>
        </div>

      </div>
    </div>
  );
}
