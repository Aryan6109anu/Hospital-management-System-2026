import React, { useState, useEffect } from "react";
import { getAllDoctors } from "../../services/doctorService"; // backend fetch
import api from "../../services/api"; // generic API calls
import { showAlert } from "../common/Alert"; // SweetAlert for feedback

// WorkingDay enum options
const workingDaysOptions = [
  "MONDAY",
  "TUESDAY",
  "WEDNESDAY",
  "THURSDAY",
  "FRIDAY",
  "SATURDAY",
  "SUNDAY",
];

const DoctorSlotBooking = () => {
  const [doctorsList, setDoctorsList] = useState([]);
  const [selectedDoctor, setSelectedDoctor] = useState("");
  const [selectedDays, setSelectedDays] = useState([]);
  const [startTime, setStartTime] = useState("09:00");
  const [endTime, setEndTime] = useState("17:00");
  const [maxPatients, setMaxPatients] = useState(10);
  const [message, setMessage] = useState("");

  // ✅ Fetch doctors from backend
  useEffect(() => {
    getAllDoctors()
      .then((res) => {
        if (Array.isArray(res.data)) {
          setDoctorsList(res.data);
        } else {
          setDoctorsList([]);
        }
      })
      .catch((err) => {
        showAlert("error", "Unable to load doctors. Please try again later.");
        setDoctorsList([]);
      });
  }, []);

  // ✅ Handle checkbox for working days
  const handleDayToggle = (day) => {
    if (selectedDays.includes(day)) {
      setSelectedDays(selectedDays.filter((d) => d !== day));
    } else {
      setSelectedDays([...selectedDays, day]);
    }
  };

  // ✅ Handle form submit
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedDoctor) {
      const msg = "Please select a doctor";
      setMessage(msg);
      showAlert("info", msg);  
      return;
    }
    if (selectedDays.length === 0) {
      const msg = "Please select at least one working day!";
      setMessage(msg);
      showAlert("info", msg);
      return;
    }

    const payload = {
      doctorId: selectedDoctor,
      workingDays: selectedDays,
      startTime,
      endTime,
      maxPatientsPerDay: maxPatients,
    };

    try {
      await api.post("/doctor-schedule", payload); // backend endpoint
       const msg = "✅ Schedule saved successfully!";
       setMessage(msg);
      showAlert("success", msg);  
      // Reset form (optional)
      // setSelectedDoctor("");
      // setSelectedDays([]);
    } catch (err) {
      console.error(err);
      const msg = "❌ Error saving schedule";
      setMessage(msg);
      showAlert("error", msg);
    }
  };

  return (
    <div className="max-w-2xl mx-auto p-6 bg-white shadow-md rounded-md">
      <h2 className="text-2xl font-bold mb-4">Doctor Slot / Schedule Setup</h2>

      {message && <div className="mb-4 text-center text-lg">{message}</div>}

      <form onSubmit={handleSubmit} className="space-y-4">

        {/* ===== Select Doctor ===== */}
        <div>
          <label className="block mb-1 font-semibold">Select Doctor</label>
          <select
            value={selectedDoctor}
            onChange={(e) => setSelectedDoctor(e.target.value)}
            className="w-full border px-3 py-2 rounded-md"
          >
            <option value="">-- Choose Doctor --</option>
            {doctorsList.map((doc) => (
              <option key={doc.id} value={doc.id}>
                {doc.name}
              </option>
            ))}
          </select>
        </div>

        {/* ===== Working Days ===== */}
        <div>
          <label className="block mb-1 font-semibold">Select Working Days</label>
          <div className="flex flex-wrap gap-2">
            {workingDaysOptions.map((day) => (
              <label key={day} className="inline-flex items-center gap-1">
                <input
                  type="checkbox"
                  checked={selectedDays.includes(day)}
                  onChange={() => handleDayToggle(day)}
                  className="form-checkbox"
                />
                {day}
              </label>
            ))}
          </div>
        </div>

        {/* ===== Start & End Time ===== */}
        <div className="flex gap-4">
          <div className="flex-1">
            <label className="block mb-1 font-semibold">Start Time</label>
            <input
              type="time"
              value={startTime}
              onChange={(e) => setStartTime(e.target.value)}
              className="w-full border px-3 py-2 rounded-md"
            />
          </div>
          <div className="flex-1">
            <label className="block mb-1 font-semibold">End Time</label>
            <input
              type="time"
              value={endTime}
              onChange={(e) => setEndTime(e.target.value)}
              className="w-full border px-3 py-2 rounded-md"
            />
          </div>
        </div>

        {/* ===== Max Patients ===== */}
        <div>
          <label className="block mb-1 font-semibold">Max Patients per Day</label>
          <input
            type="number"
            value={maxPatients}
            onChange={(e) => setMaxPatients(e.target.value)}
            className="w-full border px-3 py-2 rounded-md"
            min={1}
          />
        </div>

        {/* ===== Submit ===== */}
        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition"
        >
          Save Schedule
        </button>
      </form>
    </div>
  );
};

export default DoctorSlotBooking;
