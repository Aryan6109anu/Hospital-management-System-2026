import React, { useEffect, useState } from "react";
import { getAllLabTests, createLabOrder } from "../../services/labService";
import { showAlert } from "../common/Alert";

export default function DoctorLabTestSelectModal({
  appointmentId,
  patientId,
  onClose
}) {

  const [tests,setTests] = useState([]);
  const [selected,setSelected] = useState([]);

  useEffect(() => {

    const loadTests = async () => {
      const data = await getAllLabTests();
      setTests(data);
    };

    loadTests();

  },[]);

  const toggleTest = (id) => {

    if(selected.includes(id)){
      setSelected(selected.filter(t => t !== id));
    } else {
      setSelected([...selected,id]);
    }

  };

  const handleSave = async () => {

    if(selected.length === 0){
      showAlert("error","Select at least one test");
      return;
    }

    try{

      await createLabOrder({
        patientId,
        appointmentId,
        labTestIds: selected
      });

      showAlert("success","Lab tests ordered successfully");

      onClose();

    }catch(e){
      showAlert("error","Failed to create lab order");
    }

  };

  return (

    <div className="modal-overlay">

      <div className="modal-card">

        <h3>🧪 Select Lab Tests</h3>

        <div className="lab-test-list">

          {tests.map(t => (

            <label key={t.id} className="lab-test-item">

              <input
                type="checkbox"
                checked={selected.includes(t.id)}
                onChange={() => toggleTest(t.id)}
              />

              {t.name} (₹{t.price})

            </label>

          ))}

        </div>

        <div className="modal-actions">

          <button className="btn-primary" onClick={handleSave}>
            Save Tests
          </button>

          <button className="btn-secondary" onClick={onClose}>
            Cancel
          </button>

        </div>

      </div>

    </div>

  );

}