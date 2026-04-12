import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../../services/api";
import "../../styles/doctorConsultation.css";
import { showAlert } from "../../components/common/Alert.js";

export default function DoctorConsultation() {
  const { id } = useParams();
  const nav = useNavigate();

  const [appointment, setAppointment] = useState(null);
  const [loadingPage, setLoadingPage] = useState(true);
  const [loading, setLoading] = useState(false);

  const [medicineList, setMedicineList] = useState([]);
  const [labTestsList, setLabTestsList] = useState([]);

  const [selectedMedicine, setSelectedMedicine] = useState("");
  const [selectedLabTest, setSelectedLabTest] = useState("");

  const [medicines, setMedicines] = useState([]);
  const [labTests, setLabTests] = useState([]);

  const [labDepartments, setLabDepartments] = useState([]);
  const [selectedDepartment, setSelectedDepartment] = useState("");

  const [labSections, setLabSections] = useState([]);
  const [selectedSection, setSelectedSection] = useState("");

  const [form, setForm] = useState({
    chiefComplaint: "",
    pastHistory: "",
    currentMedication: "",
    diagnosis: "",
    notes: ""
  });

  /* ================= LOAD DATA ================= */
  useEffect(() => {
    const loadData = async () => {
      try {
        const appointmentRes = await api.get(`/appointments/${id}`);
        console.log("Full Appointment Data:", appointmentRes.data);
        setAppointment(appointmentRes.data);

        const deptRes = await api.get("/lab/departments");
        setLabDepartments(deptRes.data);

        const medRes = await api.get("/medicines");
        setMedicineList(medRes.data);

        if (appointmentRes.data.visitId) {
          const visitRes = await api.get(`/visits/${appointmentRes.data.visitId}`);
          setLabTests(visitRes.data.labTests || []);
          setMedicines(visitRes.data.medicines || []);
          setForm({
            chiefComplaint: visitRes.data.chiefComplaint || "",
            pastHistory: visitRes.data.pastHistory || "",
            currentMedication: visitRes.data.currentMedication || "",
            diagnosis: visitRes.data.diagnosis || "",
            notes: visitRes.data.notes || ""
          });
        }
      } catch (err) {
        console.error(err);
        if (err.response?.status === 401) {
          showAlert("error", "Session expired. Please log in again.");
          nav("/login");
          return;
        }
        showAlert("error", "Error loading data");
      } finally {
        setLoadingPage(false);
      }
    };

    loadData();
  }, [id, nav]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  /* ================= LAB TEST HIERARCHY ================= */
  const handleDeptChange = async (e) => {
    const departmentId = e.target.value;
    setSelectedDepartment(departmentId);
    setSelectedSection("");
    setLabTestsList([]);
    if (departmentId) {
      const res = await api.get(`/lab/departments/${departmentId}/sections`);
      setLabSections(res.data);
    } else setLabSections([]);
  };

  const handleSectionChange = async (e) => {
    const sectionId = e.target.value;
    setSelectedSection(sectionId);
    setLabTestsList([]);
    if (sectionId) {
      const res = await api.get(`/lab/sections/${sectionId}/tests`);
      setLabTestsList(res.data);
    }
  };

  const handleAddLabTest = () => {
    if (!selectedLabTest) {
      showAlert("warning", "Please select a test first");
      return;
    }
    const test = labTestsList.find(t => t.id === Number(selectedLabTest));
    if (!test) return;

    if (labTests.some(t => (typeof t === 'object' ? t.id === test.id : t === test.name))) {
      showAlert("warning", "Test already added");
      return;
    }

    setLabTests(prev => [...prev, test]);
    setSelectedLabTest("");
  };

  const handleRemoveLabTest = (identifier) => {
    setLabTests(prev => prev.filter(t => (typeof t === 'object' ? t.id !== identifier : t !== identifier)));
  };

  /* ================= MEDICINE LOGIC ================= */
  const handleAddMedicine = () => {
    if (!selectedMedicine) {
      showAlert("warning", "Please select a medicine first");
      return;
    }

    const med = medicineList.find(m => m.id === Number(selectedMedicine));
    if (!med) return;

    if (medicines.some(m => (typeof m === 'object' ? m.id === med.id : false))) {
      showAlert("warning", "Medicine already added");
      return;
    }

    setMedicines(prev => [...prev, { ...med, dose: "1-0-1", days: 5 }]);
    setSelectedMedicine("");
  };

  const updateMedicineDetails = (id, field, value) => {
    setMedicines(prev => prev.map(m => (typeof m === 'object' && m.id === id) ? { ...m, [field]: value } : m));
  };

  const removeMedicine = (identifier) => {
    setMedicines(prev => prev.filter(m => (typeof m === 'object' ? m.id !== identifier : m !== identifier)));
  };

  /* ================= SAVE VISIT ================= */
const startVisit = async () => {
  if (!form.chiefComplaint || !form.diagnosis) {
    showAlert("warning", "Reason for consultation and diagnosis are required");
    return;
  }

  const body = {
    appointmentId: appointment.id,
    patientId: appointment.patientId,
    doctorId: appointment.doctorId,
    department: appointment.doctorDepartment,
    chiefComplaint: form.chiefComplaint,
    pastHistory: form.pastHistory,
    currentMedication: form.currentMedication,
    diagnosis: form.diagnosis,
    notes: form.notes,
    labTestIds: labTests.filter(t => typeof t === 'object').map(t => t.id),
    medicines: medicines.filter(m => typeof m === 'object').map(m => ({
      medicineId: m.id,
      dose: m.dose,
      days: parseInt(m.days) || 0
    }))
  };

  try {
    setLoading(true);

    let res;

    // 🔥 MAIN LOGIC YAHI HAI
   if (appointment.visitId) {
  res = await api.put(`/visits/${appointment.visitId}`, body);
} else {
  res = await api.post("/visits/start", body);

    // ✅ ADD THIS
     setAppointment(prev => ({
       ...prev,
      visitId: res.data.id
}));
}

console.log("Appointment Data:", appointment);

    const visitId = res.data.id;

    showAlert("success", "Consultation saved successfully");

    nav(`/doctor-slip/${visitId}`);

  } catch (err) {
    console.error(err);
    showAlert("error", "Error saving visit");
  } finally {
    setLoading(false);
  }
};
  if (loadingPage) return <h3 className="consult-loading">Loading consultation...</h3>;
  if (!appointment) return <h3 className="consult-error">Appointment not found</h3>;

  return (
    <div className="consult-page">
      <div className="consult-container">
        <h2 className="consult-title">Medical Consultation</h2>

        <div className="consult-patient-box">
          <p><b>Patient:</b> {appointment.patientName}</p>
          <p><b>Doctor Name:</b> {appointment.doctorName}</p>
          <p><b>Department:</b> {appointment.doctorDepartment}</p>
          <p><b>Appointment ID:</b> {appointment.id}</p>
        </div>

        <div className="consult-form-grid">
          <div className="consult-field">
            <label>Chief Complaint *</label>
            <textarea name="chiefComplaint" value={form.chiefComplaint} onChange={handleChange}/>
          </div>

          <div className="consult-field">
            <label>Diagnosis *</label>
            <textarea name="diagnosis" value={form.diagnosis} onChange={handleChange}/>
          </div>

          <div className="consult-field">
            <label>Past History</label>
            <textarea name="pastHistory" value={form.pastHistory} onChange={handleChange}/>
          </div>

          <div className="consult-field">
            <label>Current Medication</label>
            <textarea name="currentMedication" value={form.currentMedication} onChange={handleChange}/>
          </div>

          <div className="consult-field full-width">
            <label>Notes</label>
            <textarea name="notes" value={form.notes} onChange={handleChange}/>
          </div>

          {/* LAB TESTS SECTION */}
          <div className="consult-field full-width">
            <label>Laboratory Tests</label>
            <div className="labtest-add">
              <select value={selectedDepartment} onChange={handleDeptChange}>
                <option value="">Select Department</option>
                {labDepartments.map(d => <option key={d.id} value={d.id}>{d.name}</option>)}
              </select>

              <select value={selectedSection} onChange={handleSectionChange}>
                <option value="">Select Section</option>
                {labSections.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
              </select>

              <select value={selectedLabTest} onChange={(e)=>setSelectedLabTest(e.target.value)}>
                <option value="">Select Test</option>
                {labTestsList.map(test => (
                  <option key={test.id} value={test.id}>{test.name || test.testName}</option>
                ))}
              </select>
              <button type="button" onClick={handleAddLabTest}>Add Test</button>
            </div>

            <ul className="labtest-list">
              {labTests.map((test, index) => (
                <li key={typeof test === 'object' ? test.id : index}>
                  {typeof test === 'object' ? (test.name || test.testName) : test}
                  <button type="button" onClick={() => handleRemoveLabTest(typeof test === 'object' ? test.id : test)}>Remove</button>
                </li>
              ))}
            </ul>
          </div>

          {/* MEDICINES SECTION */}
          <div className="consult-field full-width">
            <label>Medications</label>
            <div className="medicine-add-row">
              <select 
                className="medicine-select" 
                value={selectedMedicine} 
                onChange={(e) => setSelectedMedicine(e.target.value)}
              >
                <option value="">Select Medicine</option>
                {medicineList.map(m => (
                  <option key={m.id} value={m.id}>{m.medicineName}</option>
                ))}
              </select>
              <button className="btn-add-med" type="button" onClick={handleAddMedicine}>
                + Add Medicine
              </button>
            </div>

            {medicines.length > 0 && (
              <table className="medication-table">
                <thead>
                  <tr>
                    <th>Medicine Name</th>
                    <th>Dosage (M-A-N)</th>
                    <th>Days</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {medicines.map((med, index) => {
                    const isObject = typeof med === 'object' && med !== null;
                    return (
                      <tr key={isObject ? med.id : index}>
                        <td>{isObject ? med.medicineName : med}</td>
                        <td>
                          {isObject ? (
                            <input 
                              type="text" 
                              value={med.dose} 
                              placeholder="e.g. 1-0-1"
                              onChange={(e) => updateMedicineDetails(med.id, 'dose', e.target.value)}
                            />
                          ) : "-"}
                        </td>
                        <td>
                          {isObject ? (
                            <input 
                              type="number" 
                              value={med.days} 
                              className="days-input"
                              onChange={(e) => updateMedicineDetails(med.id, 'days', e.target.value)}
                            />
                          ) : "-"}
                        </td>
                        <td>
                          <button className="btn-remove" onClick={() => removeMedicine(isObject ? med.id : med)}>Remove</button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            )}
          </div>
        </div>

        <div className="consult-actions">
          <button className="btn-save" onClick={startVisit} disabled={loading}>
            {loading ? "Saving..." : "Save Consultation"}
          </button>
          <button className="btn-cancel" onClick={()=>nav(-1)} disabled={loading}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}