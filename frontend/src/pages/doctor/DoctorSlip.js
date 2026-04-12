import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../../services/api";
import "../../styles/slip.css";

export default function DoctorSlip() {
  const { id } = useParams();
  const [visit, setVisit] = useState(null);

  useEffect(() => {
    const fetchVisit = async () => {
      try {
        const res = await api.get(`/visits/${id}`);
        setVisit(res.data);
      } catch (err) {
        console.error("Fetch Error:", err);
      }
    };
    fetchVisit();
  }, [id]);

  if (!visit) return <div className="loading">Loading...</div>;

  return (
    <div className="opd-page-wrapper">
      <div className="slip-container" id="printable-area">
        {/* HEADER SECTION */}
        <div className="simple-header">
          <img src="/assets/logo.png" alt="logo" className="header-logo" />
          <div className="header-info">
            <h2 className="hospital-name">WELLSPRING HOSPITAL</h2>
            <p>Near XYZ Chowk, Ranchi, Jharkhand - 834001</p>
            <div className="header-date-time">
              <strong>DATE:</strong> {visit.visitDate ? new Date(visit.visitDate).toLocaleDateString() : "-"} | 
              <strong> TIME:</strong> {visit.visitDate ? new Date(visit.visitDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : "-"}
            </div>
          </div>
        </div>

        {/* DETAILS SECTION (Fields & Values Side-by-Side) */}
        <div className="slip-grid">
          <div className="grid-item"><strong>Appointment ID:</strong> <span>{visit.appointmentId || "N/A"}</span></div>
          <div className="grid-item"><strong>Patient ID:</strong> <span>{visit.patientId || "N/A"}</span></div>
          <div className="grid-item"><strong>Patient Name:</strong> <span>{visit.patientName}</span></div>
          <div className="grid-item"><strong>Age / Gender:</strong> <span>{visit.age || "25"} / {visit.gender || "M"}</span></div>
          <div className="grid-item"><strong>Consultant:</strong> <span>Dr. {visit.doctorName}</span></div>
          <div className="grid-item"><strong>Department:</strong> <span>{visit.doctorDepartment}</span></div>
          <div className="grid-item"><strong>Visit Status:</strong> <span>CONSULTED</span></div>
          <div className="grid-item"><strong>Visit Type:</strong> <span>OPD VISIT</span></div>
        </div>

        <hr className="compact-hr" />

        {/* DIAGNOSIS SECTION */}
        <div className="slip-section">
          <p className="diag-text">
            <strong>Clinical Diagnosis:</strong> <span>{visit.diagnosis || "General Consultation"}</span>
          </p>
        </div>

        {/* FOOTER SECTION */}
       {/* FOOTER SECTION */}
        <div className="slip-footer">
          {/* Note section ko ek div wrapper mein rakha hai height aur look ke liye */}
          <div className="note-container">
            <p className="note-text">
              <strong>Note:</strong> This is a computer-generated OPD slip. 
              Please present this at the billing counter and pharmacy. 
              Valid for today only.
            </p>
          </div>

          <div className="signature">
            <div className="sign-line"></div>
            <p>Consultant Signature</p>
            <p><strong>Dr. {visit.doctorName}</strong></p>
          </div>
        </div>
      </div>

      <div className="no-print-zone">
        <button onClick={() => window.print()} className="print-main-btn">🖨 Print Slip</button>
      </div>
    </div>
  );
}