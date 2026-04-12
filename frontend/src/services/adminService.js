import api from "./api";

/* ================= DOCTOR ================= */

// GET all doctors (ADMIN)
export const getAllDoctors = () =>
  api.get("/doctors");

// UPDATE doctor (ADMIN)
export const updateDoctorByAdmin = (id, data) =>
  api.put(`/doctors/${id}`, data);


/* ================= PATIENT ================= */

// GET all patients (ADMIN)
export const getAllPatients = () =>
  api.get("/patients");

// UPDATE patient (ADMIN)
export const updatePatientByAdmin = (id, data) =>
  api.put(`/patients/${id}`, data);
