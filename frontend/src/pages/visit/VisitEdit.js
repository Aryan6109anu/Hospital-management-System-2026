import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../../services/api";
import "../../styles/visit.css";
import { showAlert } from "../../components/common/Alert";

export default function VisitEdit() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    chiefComplaint: "",
    pastHistory: "",
    diagnosis: "",
    currentMedication: "",
    notes: "",
    patientName: "",
    patientId: "",      // ✅ Added
    appointmentId: "",  // ✅ Added
    doctorName: "",
    doctorDepartment: "",
    visitDate: ""
  });

  // Data Lists
  const [labDepartments, setLabDepartments] = useState([]);
  const [labSections, setLabSections] = useState([]);
  const [labTestsList, setLabTestsList] = useState([]);
  const [medicinesList, setMedicinesList] = useState([]);

  // Selection States
  const [selectedDepartment, setSelectedDepartment] = useState("");
  const [selectedSection, setSelectedSection] = useState("");
  const [selectedLabTest, setSelectedLabTest] = useState("");
  const [selectedMedicine, setSelectedMedicine] = useState("");

  // Added Items
  const [labTests, setLabTests] = useState([]);
  const [medicines, setMedicines] = useState([]);
  
  const [loading, setLoading] = useState(true);

  /* ================= LOAD DATA ================= */
  useEffect(() => {
    const loadData = async () => {
      try {
        const [visitRes, deptsRes, medsRes] = await Promise.all([
          api.get(`/visits/${id}`),
          api.get("/lab/departments"),
          api.get("/medicines")
        ]);

        const data = visitRes.data;
        setForm({
          chiefComplaint: data.chiefComplaint || "",
          pastHistory: data.pastHistory || "",
          diagnosis: data.diagnosis || "",
          currentMedication: data.currentMedication || "",
          notes: data.notes || "",
          patientName: data.patientName || "",
          patientId: data.patientId || "",           // ✅ Map from backend
          appointmentId: data.appointmentId || "", // ✅ Map from backend
          doctorName: data.doctorName || "",
          doctorDepartment: data.doctorDepartment || "",
          visitDate: data.visitDate || ""
        });

        setLabDepartments(deptsRes.data || []);
        setMedicinesList(medsRes.data || []);
        setLabTests(data.labTests || []);
        setMedicines(data.medicines || []);
        
      } catch (err) {
        showAlert("error", "Failed to load visit data");
      } finally {
        setLoading(false);
      }
    };
    loadData();
  }, [id]);

  /* ================= LAB HIERARCHY HANDLERS ================= */
  const handleDeptChange = async (e) => {
    const deptId = e.target.value;
    setSelectedDepartment(deptId);
    setSelectedSection("");
    setLabTestsList([]);
    if (deptId) {
      const res = await api.get(`/lab/departments/${deptId}/sections`);
      setLabSections(res.data);
    } else setLabSections([]);
  };

  const handleSectionChange = async (e) => {
    const secId = e.target.value;
    setSelectedSection(secId);
    setLabTestsList([]);
    if (secId) {
      const res = await api.get(`/lab/sections/${secId}/tests`);
      setLabTestsList(res.data);
    }
  };

  const handleAddLabTest = () => {
    if (!selectedLabTest) {
      showAlert("warning", "Select a lab test first");
      return;
    }
    const test = labTestsList.find(t => t.id === Number(selectedLabTest));
    if (!test) return;

    if (labTests.some(t => typeof t === 'object' ? t.id === test.id : t === (test.name || test.testName))) {
      showAlert("warning", "Test already added");
      return;
    }

    setLabTests(prev => [...prev, test]);
    setSelectedLabTest("");
  };

  const handleRemoveLabTest = (identifier) => {
    setLabTests(prev => prev.filter(t => typeof t === 'object' ? t.id !== identifier : t !== identifier));
  };

  /* ================= MEDICINE HANDLERS ================= */
  const handleAddMedicine = () => {
    if (!selectedMedicine) {
      showAlert("warning", "Select a medicine first");
      return;
    }
    const med = medicinesList.find(m => m.id === Number(selectedMedicine));
    if (!med) return;

    if (medicines.some(m => typeof m === 'object' ? m.id === med.id : false)) {
      showAlert("warning", "Medicine already added");
      return;
    }

    setMedicines(prev => [...prev, { ...med, dose: "1-0-1", days: 5 }]);
    setSelectedMedicine("");
  };

  const updateMedicineDetails = (medId, field, value) => {
    setMedicines(prev => prev.map(m => (typeof m === 'object' && m.id === medId) ? { ...m, [field]: value } : m));
  };

  /* ================= SAVE VISIT ================= */
  const updateVisit = async () => {
    try {
      const body = {
        chiefComplaint: form.chiefComplaint,
        pastHistory: form.pastHistory,
        diagnosis: form.diagnosis,
        currentMedication: form.currentMedication,
        notes: form.notes,
        labTestIds: labTests.filter(t => typeof t === 'object').map(t => t.id),
        medicines: medicines.filter(m => typeof m === 'object').map(m => ({
          medicineId: m.id,
          dose: m.dose || "1-0-1",
          days: parseInt(m.days) || 5
        }))
      };

      await api.put(`/visits/${id}`, body);
      showAlert("success", "Visit updated successfully");
      navigate(-1);
    } catch (err) {
      showAlert("error", "Failed to update visit");
    }
  };

  if (loading) return <h3 className="visit-loading">Loading...</h3>;

  return (
    <div className="visit-page">
      <div className="visit-card">
        <div className="visit-header">
          <div className="hospital-brand"><h1>WELLSPRING HOSPITAL</h1></div>
          <div className="visit-meta">
            <p><b>Visit ID :</b> {id}</p> {/* ✅ URL parameter is Visit ID */}
            <p><b>Appointment ID :</b> {form.appointmentId}</p> {/* ✅ From state */}
            <p><b>Date :</b> {form.visitDate ? new Date(form.visitDate).toLocaleDateString() : "-"}</p>
          </div>
        </div>

        <div className="visit-info-grid">
          <div><label>Patient Name :</label> <span>{form.patientName}</span></div>
          <div><label>Patient ID :</label> <span>{form.patientId}</span></div> {/* ✅ Display Patient ID */}
          <div><label>Doctor :</label> <span>{form.doctorName}</span></div>
          <div><label>Department :</label> <span>{form.doctorDepartment}</span></div>
        </div>

        <Editable title="Chief Complaint" name="chiefComplaint" value={form.chiefComplaint} onChange={(e)=>setForm({...form, chiefComplaint: e.target.value})}/>
        <Editable title="Diagnosis" name="diagnosis" value={form.diagnosis} onChange={(e)=>setForm({...form, diagnosis: e.target.value})}/>
        <Editable title="Past History" name="pastHistory" value={form.pastHistory} onChange={(e)=>setForm({...form, pastHistory: e.target.value})}/>
        <Editable title="Notes" name="notes" value={form.notes} onChange={(e)=>setForm({...form, notes: e.target.value})}/>

        {/* LAB TESTS HIERARCHY */}
        <div className="visit-section">
          <h4>Laboratory Tests</h4>
          <div className="labtest-add">
            <select value={selectedDepartment} onChange={handleDeptChange}>
              <option value="">Select Dept</option>
              {labDepartments.map(d => <option key={d.id} value={d.id}>{d.name}</option>)}
            </select>
            <select value={selectedSection} onChange={handleSectionChange}>
              <option value="">Select Section</option>
              {labSections.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
            </select>
            <select value={selectedLabTest} onChange={(e) => setSelectedLabTest(e.target.value)}>
              <option value="">Select Test</option>
              {labTestsList.map(t => <option key={t.id} value={t.id}>{t.name || t.testName}</option>)}
            </select>
            <button type="button" onClick={handleAddLabTest}>Add</button>
          </div>
          <ul className="visit-list">
            {labTests.map((t, idx) => (
              <li key={typeof t === 'object' ? t.id : idx}>
                {typeof t === 'object' ? (t.name || t.testName) : t}
                <button type="button" onClick={() => handleRemoveLabTest(typeof t === 'object' ? t.id : t)}>Remove</button>
              </li>
            ))}
          </ul>
        </div>

              {/* ================= MEDICINES SECTION ================= */}
<div className="visit-section">
  <h4>Medicines</h4>
  <div className="labtest-add">
    <select value={selectedMedicine} onChange={(e) => setSelectedMedicine(e.target.value)}>
      <option value="">Select Medicine</option>
      {medicinesList.map(m => (
        <option key={m.id} value={m.id}>{m.medicineName}</option>
      ))}
    </select>
    <button type="button" onClick={handleAddMedicine}>Add</button>
  </div>
  
  {medicines.length > 0 && (
    <div className="med-list-container">
      {/* Column Headers */}
      <div className="list-header">
        <div className="col-name">Medicine Name</div>
        <div className="col-dose">Dose</div>
        <div className="col-days">Days</div>
        <div className="col-action">Action</div>
      </div>

      <ul className="visit-list">
        {medicines.map((m, idx) => {
          const isObj = typeof m === 'object' && m !== null;
          
          let displayValues = { name: m, dose: "-", days: "-" };
          if (!isObj && typeof m === 'string') {
            const parts = m.match(/(.+) (.+?) \((.+?) days\)/);
            if (parts) {
              displayValues = { name: parts[1], dose: parts[2], days: parts[3] };
            }
          }

          return (
            <li key={isObj ? (m.id || idx) : `saved-med-${idx}`} className="bullet-item">
              {/* Name Column */}
              <div className="col-name item-name">
                {isObj ? m.medicineName : displayValues.name}
              </div>

              {/* Dose Column */}
              <div className="col-dose">
                {isObj ? (
                  <input 
                    className="inline-input"
                    value={m.dose} 
                    placeholder="1-0-1"
                    onChange={(e) => updateMedicineDetails(m.id, 'dose', e.target.value)}
                  />
                ) : (
                  <span className="saved-badge">{displayValues.dose}</span>
                )}
              </div>

              {/* Days Column */}
              <div className="col-days">
                {isObj ? (
                  <div className="day-input-group">
                    <input 
                      type="number" 
                      className="inline-input days-width"
                      value={m.days} 
                      onChange={(e) => updateMedicineDetails(m.id, 'days', e.target.value)}
                    />
                    <span className="unit-txt">days</span>
                  </div>
                ) : (
                  <span className="saved-badge">{displayValues.days} days</span>
                )}
              </div>

              {/* Action Column */}
              <div className="col-action">
                <button 
                  className="btn-remove-item" 
                  type="button"
                  onClick={() => setMedicines(medicines.filter((_, i) => i !== idx))}
                >
                  Remove
                </button>
              </div>
            </li>
          );
        })}
      </ul>
    </div>
  )}
</div>
        <div className="visit-actions">
  <button className="btn-edit" onClick={() => navigate(-1)}>Cancel</button>

  <button className="btn-save" onClick={updateVisit}>
    Save Changes
  </button>

  <button
    className="btn-print"
    onClick={() => window.open(`/doctor-slip/${id}`, "_blank")}
   >
    🖨 Print Slip
  </button>
</div>
      </div>
    </div>
  );
}

function Editable({ title, name, value, onChange }) {
  return (
    <div className="visit-section">
      <h4>{title}</h4>
      <textarea name={name} value={value} onChange={onChange} className="visit-textarea" />
    </div>
  );
}