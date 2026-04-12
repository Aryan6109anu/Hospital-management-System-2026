import api from "./api";

/* =======================================================
   COMMON ACCOUNT (All Roles)
======================================================= */

// Get logged-in user profile (based on token)
export const getMyAccount = () =>
  api.get("/account/profile").then(res => res.data);


/* =======================================================
   BILLING STAFF
======================================================= */

// Backend: GET /api/billing-staff
export const getAllBillingStaff = () =>
  api.get("/billing-staff").then(res => res.data);

// Backend: GET /api/billing-staff/{id}
export const getBillingStaffById = (id) =>
  api.get(`/billing-staff/${id}`).then(res => res.data);

// Backend: POST /api/billing-staff
export const createBillingStaff = (data) =>
  api.post("/billing-staff", data).then(res => res.data);

// Backend: PUT /api/billing-staff/{id}
export const updateBillingStaff = (id, data) =>
  api.put(`/billing-staff/${id}`, data).then(res => res.data);

// Backend: DELETE /api/billing-staff/{id}
export const deleteBillingStaff = (id) =>
  api.delete(`/billing-staff/${id}`).then(res => res.status === 200);


/* =======================================================
   PHARMACY STAFF
======================================================= */

// Backend: GET /api/pharmacy-staff
export const getAllPharmacyStaff = () =>
  api.get("/pharmacy-staff").then(res => res.data);

// Backend: GET /api/pharmacy-staff/{id}
export const getPharmacyStaffById = (id) =>
  api.get(`/pharmacy-staff/${id}`).then(res => res.data);

// Backend: POST /api/pharmacy-staff
export const createPharmacyStaff = (data) =>
  api.post("/pharmacy-staff", data).then(res => res.data);

// Backend: PUT /api/pharmacy-staff/{id}
export const updatePharmacyStaff = (id, data) =>
  api.put(`/pharmacy-staff/${id}`, data).then(res => res.data);

// Backend: DELETE /api/pharmacy-staff/{id}
export const deletePharmacyStaff = (id) =>
  api.delete(`/pharmacy-staff/${id}`).then(res => res.status === 200);


/* =======================================================
   APPOINTMENT STAFF
======================================================= */

// Backend: GET /api/appointment-staff
export const getAllAppointmentStaff = () =>
  api.get("/appointment-staff").then(res => res.data);

// Backend: GET /api/appointment-staff/{id}
export const getAppointmentStaffById = (id) =>
  api.get(`/appointment-staff/${id}`).then(res => res.data);

// Backend: POST /api/appointment-staff
export const createAppointmentStaff = (data) =>
  api.post("/appointment-staff", data).then(res => res.data);

// Backend: PUT /api/appointment-staff/{id}
export const updateAppointmentStaff = (id, data) =>
  api.put(`/appointment-staff/${id}`, data).then(res => res.data);

// Backend: DELETE /api/appointment-staff/{id}
export const deleteAppointmentStaff = (id) =>
  api.delete(`/appointment-staff/${id}`).then(res => res.status === 200);

/* =======================================================
   LAB STAFF
======================================================= */

// Backend: GET /api/lab-staff
export const getAllLabStaff = () =>
  api.get("/lab-staff").then(res => res.data);

// Backend: GET /api/lab-staff/{id}
export const getLabStaffById = (id) =>
  api.get(`/lab-staff/${id}`).then(res => res.data);

// Backend: POST /api/lab-staff
export const createLabStaff = (data) =>
  api.post("/lab-staff", data).then(res => res.data);

// Backend: PUT /api/lab-staff/{id}
export const updateLabStaff = (id, data) =>
  api.put(`/lab-staff/${id}`, data).then(res => res.data);

// Backend: DELETE /api/lab-staff/{id}
export const deleteLabStaff = (id) =>
  api.delete(`/lab-staff/${id}`).then(res => res.status === 200);


/* =======================================================
   LOGOUT
======================================================= */

export const logoutUser = () => {
  localStorage.removeItem("user");
  localStorage.removeItem("token");
  localStorage.removeItem("role");
};