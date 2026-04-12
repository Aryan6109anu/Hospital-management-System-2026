import React from 'react';
import '../../styles/appointmentSlip.css';

const AppointmentSlip = ({ appointment }) => {
  if (!appointment) return null;

  // Aapke AppointmentResponse.java ke fields ke mutabiq mapping
  const age = appointment.patientAge || "N/A";
  const gender = appointment.patientGender || "N/A";
  const mobile = appointment.patientMobile || "N/A";
  const patientName = appointment.patientName || "N/A"; 

  const doctorName = appointment.doctorName || "N/A";
  
  // Department agar Object/Enum hai toh string check karega
  const deptValue = appointment.doctorDepartment || appointment.department || "General";
  const department = typeof deptValue === 'string' 
    ? deptValue.replace(/_/g, " ") 
    : "General";

  return (
    <div className="print-area">
      <div className="slip-header">
        <h2>Premium Hospital</h2>
      </div>

      <div className="slip-content">
        {/* Column 1: ID & Slot Info */}
        <div className="column">
          <div className="row">
            <span className="label">Token No:</span>
            <span className="value">{appointment.id}</span>
          </div>
          <div className="row">
            <span className="label">Appointment ID:</span>
            <span className="value">#{appointment.id}</span>
          </div>
          <div className="row">
            <span className="label">Date:</span>
            <span className="value">{appointment.slotDate || appointment.appointmentDate}</span>
          </div>
          <div className="row">
            <span className="label">Time:</span>
            <span className="value">{appointment.slotStartTime || appointment.startTime}</span>
          </div>
        </div>

        {/* Column 2: Patient Info */}
        <div className="column">
          <div className="row">
            <span className="label">Patient Name:</span>
            <span className="value">{patientName}</span>
          </div>
          <div className="row">
            <span className="label">Age:</span>
            <span className="value">{age}</span>
          </div>
          <div className="row">
            <span className="label">Gender:</span>
            <span className="value">{gender}</span>
          </div>
          <div className="row">
            <span className="label">Mobile:</span>
            <span className="value">{mobile}</span>
          </div>
        </div>

        {/* Column 3: Medical Info */}
        <div className="column">
          <div className="row">
            <span className="label">Department:</span>
            <span className="value">{department}</span>
          </div>
          <div className="row">
            <span className="label">Doctor:</span>
            <span className="value">Dr. {doctorName}</span>
          </div>
          <div className="row">
            <span className="label">Disease:</span>
            <span className="value">{appointment.disease || "N/A"}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AppointmentSlip;