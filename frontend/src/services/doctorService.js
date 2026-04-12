import api from "./api";

/* ================= ADMIN / PUBLIC ================= */
// Backend: @GetMapping("/api/doctors")
export const getAllDoctors = () => api.get("/doctors");

// Backend: @GetMapping("/api/doctors/available")
export const getAvailableDoctors = () => api.get("/doctors/available");

/* ================= DOCTOR (OWN PROFILE) ================= */
// Backend: @GetMapping("/api/doctors/profile")
export const getDoctorProfile = () => api.get("/doctors/profile");

/* ================= DOCTOR SLOT BOOKING ================= */
// Backend: Check doctor availability for a date
// GET /api/availability/{doctorId}?date=yyyy-MM-dd
export const checkDoctorAvailability = (doctorId, date) =>
  api.get(`/availability/${doctorId}`, { params: { date } });

// Backend: Get booked slots for doctor on a date
// GET /api/appointments/{doctorId}?date=yyyy-MM-dd
export const getBookedSlots = (doctorId, date) =>
  api.get(`/appointments/${doctorId}`, { params: { date } })
     .then(res => res.data); // assuming api wrapper returns full response

// Backend: Book a slot
// POST /api/appointments/book
export const bookDoctorSlot = (doctorId, date, slot) =>
  api.post("/appointments/book", { doctorId, date, slot })
     .then(res => res.status === 200);


     
