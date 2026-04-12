import api from "./api";

// ADMIN dashboard
export const getDoctorCount = () =>
  api.get("/api/doctors").then(res => res.data.length);

export const getPatientCount = () =>
  api.get("/api/patients").then(res => res.data.length);

export const getAppointmentCount = () =>
  api.get("/api/appointments").then(res => res.data.length);
      