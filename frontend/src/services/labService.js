import api from "./api";

/* ================= GET ALL LAB TESTS ================= */

export const getAllLabTests = () =>
  api.get("/lab/tests").then(res => res.data);

/* ================= CREATE LAB ORDER ================= */

export const createLabOrder = (data) =>
  api.post("/lab-orders", data).then(res => res.data);

/* ================= SEARCH BY PATIENT ================= */

export const getLabOrdersByPatient = (patientId) =>
  api.get(`/lab-orders/patient/${patientId}`).then(res => res.data);

/* ================= SEARCH BY APPOINTMENT ================= */

export const getLabOrdersByAppointment = (appointmentId) =>
  api.get(`/lab-orders/appointment/${appointmentId}`).then(res => res.data);