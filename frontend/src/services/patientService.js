import api from "./api";

/* ================= ADMIN ONLY ================= */
export const getAllPatients = () =>
  api.get("/patients");

/* ================= PATIENT (OWN PROFILE) ================= */
export const getMyPatientProfile = () =>
  api.get("/patients/me");

/* ================= PATIENT UPDATE ================= */
export const updateMyPatientProfile = (data) =>
  api.put("/patients/me", data);
