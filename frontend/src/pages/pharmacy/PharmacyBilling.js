import React, { useEffect, useState } from "react";
import api from "../../services/api";
import { showAlert } from "../common/Alert.js";

export default function PharmacyBilling() {
  const [prescriptions, setPrescriptions] = useState([]);

  useEffect(() => {
    fetchPrescriptions();
  }, []);

  const fetchPrescriptions = async () => {
    try {
      const res = await api.get("/pharmacy/prescriptions");
      setPrescriptions(res.data);
    } catch (err) {
      console.error("Error fetching prescriptions", err);
    }
  };

const dispenseMedicine = async (id) => {
  try {
    await api.put(`/pharmacy/prescriptions/${id}/dispense`);
    showAlert("success", "Dawai de di gayi hai!");
    fetchPrescriptions();
  } catch (err) {
    // 🛑 PROBLEM YAHAN THI: Aapne yahan "Payment failed" likha hoga
    // ISKO AISE BADLO:
    
    const backendMessage = err.response?.data?.message; 
    
    // Agar backend se message mila (Stock khatam wala), toh wo dikhao
    // Nahi toh ek generic error dikhao
    const finalMsg = backendMessage || "Kuch gadbad ho gayi hai!";

    // Ab showAlert vahi message dikhayega jo console mein aa raha hai
    showAlert("error", finalMsg); 
    
    console.error("Original Error:", err);
  }
};

  return ( 
    <div>
      <h2>Pharmacy Billing</h2>

      <table border="1" cellPadding="10" width="100%">
        <thead>
          <tr>
            <th>Prescription ID</th>
            <th>Patient</th>
            <th>Medicines</th>
            <th>Total Price</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {prescriptions.map((p) => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{p.patientName}</td>
              <td>
                {p.medicines.map((m, index) => (
                  <div key={index}>
                    {m.name} - {m.quantity}
                  </div>
                ))}
              </td>
              <td>₹ {p.totalAmount}</td>
              <td>{p.status}</td>
              <td>
                {p.status !== "DISPENSED" && (
                  <button onClick={() => dispenseMedicine(p.id)}>
                    Dispense
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );   
}

