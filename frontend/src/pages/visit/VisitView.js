import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../../services/api";
import "../../styles/visit.css";

export default function VisitView() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [visit, setVisit] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchVisit = async () => {
      try {
        const res = await api.get(`/visits/${id}`);
        setVisit(res.data);
      } catch {
        alert("Failed to load visit");
      } finally {
        setLoading(false);
      }
    };
    fetchVisit();
  }, [id]);

  if (loading) return <h3>Loading visit...</h3>;
  if (!visit) return <h3>Visit not found</h3>;
  
  console.log("Full Visit Data:", visit);

  return (
<div className="visit-page">
      <div className="visit-card">
        <h1>SPRINGWELL HOSPITAL</h1>

        <div className="visit-info-grid">
          {/* ✅ Teeno IDs yahan display ho rahi hain */}
          <div><strong>Visit ID :</strong> {visit.id}</div>
          <div><strong>Appointment ID :</strong> {visit.appointmentId || "-"}</div>
          <div><strong>Patient ID :</strong> {visit.patientId || "-"}</div>
          
          <div><strong>Patient Name :</strong> {visit.patientName}</div>
          <div><strong>Doctor :</strong> {visit.doctorName}</div>
          <div><strong>Department :</strong> {visit.doctorDepartment}</div>
          <div><strong>Date :</strong> {visit.visitDate ? new Date(visit.visitDate).toLocaleString() : "-"}</div>
        </div>

          <div className="visit-section">
          <h4>Chief Complaint</h4>
          <p>{visit.chiefComplaint}</p>
        </div>

        <div className="visit-section">
          <h4>Past History</h4>
          <p>{visit.pastHistory}</p>
        </div>

        <div className="visit-section">
          <h4>Diagnosis</h4>
          <p>{visit.diagnosis}</p>
        </div>

        <div className="visit-section">
          <h4>Doctor Notes</h4>
          <p>{visit.notes}</p>
        </div>

        

        {/* LAB TESTS SECTION - FIXED */}
        <div className="visit-section">
          <h4>Lab Tests</h4>
          {visit.labTests && visit.labTests.length > 0 ? (
            <ul>
              {visit.labTests.map((t, index) => (
                <li key={index}>
                  {/* Agar backend se Object aaye toh t.name, warna direct string t */}
                  {typeof t === "object" ? (t.name || t.testName) : t}
                </li>
              ))}
            </ul>
          ) : (
            <p>No lab tests added for this visit.</p>
          )}
        </div>

        {/* MEDICINES SECTION - FIXED */}
        <div className="visit-section">
          <h4>Medicines</h4>
          {visit.medicines && visit.medicines.length > 0 ? (
            <ul>
              {visit.medicines.map((m, index) => (
                <li key={index}>
                  {/* Backend String bhej raha hai ("Paracetamol 1-0-1 (5 days)"), 
                      isliye direct m ko print karenge */}
                  {typeof m === "object" ? `${m.medicineName || m.name} (${m.dose} - ${m.days} days)` : m}
                </li>
              ))}
            </ul>
          ) : (
            <p>No medicines added for this visit.</p>
          )}
        </div>

        <div className="visit-actions">
          <button onClick={() => navigate(-1)}>Back</button>
          <button className="btn-edit" onClick={() => navigate(`/visits/edit/${id}`)}>
            Edit Visit
          </button>
        </div>
      </div>
    </div>
  );
}


