import api from "./api";

/* =========================
   CREATE / UPDATE SCHEDULE
========================= */

// create schedule
export const createDoctorSchedule = (data) =>
  api.post("/doctor-schedules", data);

// update schedule
export const updateDoctorSchedule = (doctorId, data) =>
  api.put(`/doctor-schedules/${doctorId}`, data);


/* =========================
   GET SCHEDULE
========================= */

export const getDoctorSchedule = (doctorId) =>
  api.get(`/doctor-schedules/${doctorId}`);

export const getAllDoctorSchedules = () =>
  api.get("/doctor-schedules");


/* =========================
   DELETE
========================= */

export const deleteDoctorSchedule = (doctorId) =>
  api.delete(`/doctor-schedules/${doctorId}`);


/* =========================
   AVAILABILITY
========================= */

export const checkDoctorAvailability = (doctorId, date) =>
  api.get(`/doctor-schedules/${doctorId}/availability`, {
    params: { date }
  });


/* =========================
   SLOTS
========================= */

export const getAvailableSlots = (doctorId, date) =>
  api.get(`/doctor-schedules/${doctorId}/slots`, {
    params: { date }
  });


// Available dates
export const getAvailableDates = (doctorId) =>
  api.get(`/doctor-schedules/${doctorId}/available-dates`);
