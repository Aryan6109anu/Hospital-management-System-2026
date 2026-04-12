import api from "./api";

// ============================
// Patient/Admin: Book appointment
// ============================
export const createAppointment = (appointment) =>
  api.post("/appointments/book", appointment);

// ============================
// Admin: Get all appointments
// ============================
export const getAllAppointments = () =>
  api.get("/appointments");

// ============================
// Patient: Get own appointments
// ============================
export const getPatientAppointments = () =>
  api.get("/appointments/patient/me");

// ============================
// Doctor: Get own appointments (JWT based)
// ============================
export const getDoctorAppointments = () =>
  api.get("/appointments/doctor/me");

// ============================  
// Patient: Cancel own appointment
// ============================
export const cancelAppointment = (appointmentId) =>
  api.put(`/appointments/cancel/patient/${appointmentId}`);

// ============================
// Admin: Cancel any appointment
// ============================
export const adminCancelAppointment = (appointmentId) =>
  api.patch(`/appointments/cancel/admin/${appointmentId}`);

// ============================
// Admin: Update appointment by ID
// ============================

export const updateAppointmentByAdmin = async (appointmentId, data) => {
  return await api.put(`/appointments/walkin/${appointmentId}`, data);
};