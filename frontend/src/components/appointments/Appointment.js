import React, { useCallback, useEffect, useState } from "react";
import {
  cancelAppointment,
  adminCancelAppointment,
  getAllAppointments,
  getDoctorAppointments,
  getPatientAppointments
} from "../../services/appointmentService.js";
import "../../styles/appointment.css";
import "../../styles/global.css"
import "../../styles/table.css"
import Layout from "../../layout/Layout.js";
import Swal from "sweetalert2";
import { formatDisplayDate, formatTimeRange } from "../../utils/dateTimeUtils";

export default function Appointment() {

  const [appointments, setAppointments] = useState([]);

  const role = localStorage.getItem("role");

  const isAdmin   = role === "ADMIN"   || role === "ROLE_ADMIN";
  const isPatient = role === "PATIENT" || role === "ROLE_PATIENT";
  const isDoctor  = role === "DOCTOR"  || role === "ROLE_DOCTOR";
  const isAppointment = role === "APPOINTMENT" || role === "ROLE_APPOINTMENT";

  // =============================
  // LOAD APPOINTMENTS (ROLE BASED)
  // =============================
  const loadAppointments = useCallback(async () => {
    try {
      let res;

   if (isAdmin) {
  res = await getAllAppointments();
   } else if (isPatient) {
  res = await getPatientAppointments();
    } else if (isDoctor) {
  res = await getDoctorAppointments();
   } else if (isAppointment) {
  res = await getAllAppointments();  // Appointment staff sees all
    } else {
  throw new Error("Invalid role");
 }

      setAppointments(res.data);

    } catch (err) {
      console.error(err);
      Swal.fire("Error", "Failed to load appointments", "error");
    }
  }, [isAdmin, isPatient, isDoctor,isAppointment]);

  useEffect(() => {
    loadAppointments();
  }, [loadAppointments]);

  // =============================
  // CANCEL APPOINTMENT
  // =============================
  const handleCancel = async (appointment) => {

    // 🔒 Already cancelled
    if (appointment.status === "CANCELLED") {
      Swal.fire({
        icon: "info",
        title: "Already Cancelled",
        text: "This appointment is already cancelled",
        timer: 1600,
        showConfirmButton: false,
      });
      return;
    }

    // ❓ Confirmation
    const confirm = await Swal.fire({
      title: "Cancel Appointment?",
      text: "Are you sure you want to cancel this appointment?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      confirmButtonText: "Yes, Cancel",
    });

    if (!confirm.isConfirmed) return;

    try {
      if (isAdmin || isAppointment) {
        await adminCancelAppointment(appointment.id);
      } else {
        await cancelAppointment(appointment.id);
      }

      await Swal.fire({
        icon: "success",
        title: "Appointment Cancelled",
        text: "Appointment cancelled successfully",
        timer: 1700,
        showConfirmButton: false,
      });

      loadAppointments();

    } catch (err) {
      Swal.fire("Failed", "Unable to cancel appointment", "error");
    }
  };

  return (
    <div className="appointment-container">
      <Layout />

      <div className="appointment-main">
        <h1>Appointment List</h1>

        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>#</th>
                <th>Patient</th>
                <th>Doctor</th>
                <th>Date</th>
                <th>Time</th>
                <th>Specialization</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody>
              {appointments.length === 0 ? (
                <tr>
                  <td colSpan="8" style={{ textAlign: "center", padding: "20px" }}>
                    No appointments found 😔
                  </td>
                </tr>
              ) : (
                appointments.map((a, i) => (
                  <tr key={a.id}>
                    <td>{i + 1}</td>
                    <td>{a.patientName}</td>
                    <td>{a.doctorName}</td>
                    <td>{formatDisplayDate(a.appointmentDate)}</td>
                    <td>{formatTimeRange(a.slotStartTime, a.slotEndTime)}</td>
                    <td>{a.specialization}</td>
                    <td>
                      <span className={`status ${a.status?.toLowerCase()}`}>
                        {a.status}
                      </span>
                    </td>
                    <td>
                      {(isAdmin || isPatient || isAppointment) && (
                        <button
                          className="delete-btn"
                          disabled={a.status === "CANCELLED"}
                          onClick={() => handleCancel(a)}
                        >
                          {a.status === "CANCELLED" ? "Cancelled" : "Cancel"}
                        </button>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

      </div>
    </div>
  );
}
