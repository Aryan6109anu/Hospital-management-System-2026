import React, { useEffect, useState, useCallback, useMemo } from "react";
import api from "../../services/api";
import "../../styles/global.css";
import "../../styles/table.css";
import "../../styles/billingStaffAccount.css";
import Navbar from "../../components/common/Navbar";
import { showAlert } from "../../components/common/Alert";
import BillingReceipt from "../../components/bill/BillingReceipt";

export default function BillingStaffAccount() {
  const token = localStorage.getItem("token");
  const authHeader = useMemo(() => ({
    headers: { Authorization: `Bearer ${token}` }
  }), [token]);

  const [staff, setStaff] = useState(null);
  const [bills, setBills] = useState([]);
  const [patients, setPatients] = useState([]); // 👈 Ye ab niche use ho raha hai
  const [activePage, setActivePage] = useState("DASHBOARD");
  const [visitIdSearch, setVisitIdSearch] = useState(""); 

  const [editableMedicines, setEditableMedicines] = useState([]);
  const [editableTests, setEditableTests] = useState([]);
  const [selectedBill, setSelectedBill] = useState(null);
  const [showReceipt, setShowReceipt] = useState(false);

  /* ================= FINAL PAYMENT LOGIC ================= */
  const handlePayAndGenerate = async (visitId, mode = "CASH") => {
    const selectedMeds = editableMedicines.filter(m => m.selected);
    const selectedTst = editableTests.filter(t => t.selected);

    try {
      const res = await api.post(`/billing/pay/${visitId}?mode=${mode}`, {
        medicines: selectedMeds,
        labTests: selectedTst
      }, authHeader);

      setBills(prev => [res.data, ...prev.filter(b => b.visitId !== visitId)]);
      showAlert("success", "Payment Received & Bill Finalized! ✅");
      setActivePage("BILLS"); 
    } catch (err) {
      const errorMsg = err.response?.data?.message || "Process failed. ❌";
      showAlert("error", errorMsg);
    }
  };

  /* ================= FETCH LOGIC ================= */
 const fetchPrescriptionForBilling = useCallback(async (id) => {
  if (!id) return showAlert("Info", "Please enter Visit ID");
  try {
    const res = await api.get(`/billing/visit/${id}`, authHeader);
    const data = res.data;
    
    console.log("Backend Data Check:", data); // 👈 Ye line console mein check karo!

    setEditableMedicines((data.medicines || []).map(m => ({ 
      ...m, 
      // 🔥 Ye line check karo, agar 'medicineName' nahi hai toh 'name' uthayega
      medicineName: m.medicineName || m.name || (m.medicine && m.medicine.medicineName) || "Unknown Med", 
      selected: true 
    })));
    
    setEditableTests((data.labTests || []).map(t => ({ 
      ...t, 
      testName: t.testName || t.name || (t.labTest && t.labTest.name) || "Unknown Test", 
      selected: true 
    })));
    
    setActivePage("EDITOR");
  } catch (err) {
    showAlert("error", "Data load nahi ho raha ❌");
  }
}, [authHeader]);
  /* ================= INITIAL LOAD ================= */
  useEffect(() => {
    const loadData = async () => {
      try {
        const [staffRes, patientRes] = await Promise.all([
          api.get("/billing-staff/me", authHeader),
          api.get("/patients", authHeader)
        ]);
        setStaff(staffRes.data);
        setPatients(patientRes.data); // 👈 Data set ho raha hai
      } catch { showAlert("error", "Auth Error ❌"); }
    };
    loadData();
  }, [authHeader]);

  const calculateTotal = () => {
    const medTotal = editableMedicines.filter(m => m.selected).reduce((s, m) => s + m.price * m.quantity, 0);
    const testTotal = editableTests.filter(t => t.selected).reduce((s, t) => s + t.price, 0);
    return medTotal + testTotal;
  };

  return (
    <div className="doctor-profile-container admin-dashboard">
      <Navbar />

      {showReceipt && selectedBill && (
        <div className="receipt-modal-overlay no-print">
          <div className="receipt-modal-content">
            <button className="close-btn" onClick={() => setShowReceipt(false)}>✖ Close</button>
            <BillingReceipt billData={selectedBill} />
          </div>
        </div>
      )}

      <div className="profile-card">
        <div className="profile-header"><h2>💳 Staff Dashboard</h2></div>
        {staff && (
          <div className="profile-body">
            <ProfileField label="Name" value={staff.fullName} />
            <ProfileField label="Email" value={staff.email} />
            <ProfileField label="Mobile" value={staff.mobile} />
          </div>
        )}
      </div>

      <div className="appointments-card action-center">
        <div className="search-box-row">
          <input 
            type="number" placeholder="Enter Visit ID..." 
            className="main-search-input" value={visitIdSearch}
            onChange={(e) => setVisitIdSearch(e.target.value)}
          />
        </div>
        <div className="action-grid">
          <button className="nav-btn btn-blue" onClick={() => fetchPrescriptionForBilling(visitIdSearch)}>📥 Fetch & Bill</button>
          <button className="nav-btn btn-green" onClick={() => setActivePage("BILLS")}>📋 Recent Bills</button>
          <button className="nav-btn btn-orange" onClick={() => setActivePage("PATIENTS")}>🧑 Patients</button>
          <button className="nav-btn btn-red" onClick={() => {setVisitIdSearch(""); setActivePage("DASHBOARD")}}>🧹 Reset</button>
        </div>
      </div>

      {/* BILL EDITOR */}
      {activePage === "EDITOR" && (
        <div className="editor-master-box fade-in">
          <div className="editor-header-main">
            <div className="editor-title-group">
              <h2>🧾 Invoice Generator</h2>
              <span className="visit-badge">Visit ID: {visitIdSearch}</span>
            </div>
            <div className="live-total-box">
              <label>TOTAL PAYABLE</label>
              <span className="total-amount">₹{calculateTotal().toLocaleString()}</span>
            </div>
          </div>

          <div className="editor-section">
            <h3>💊 Medicines</h3>
            <table className="editor-table-awesome">
              <thead>
                <tr><th>Action</th><th>Item Name</th><th>Price</th><th>Qty</th><th>Total</th></tr>
              </thead>
              <tbody>
                {editableMedicines.map((m, i) => (
                  <tr key={`m-${i}`} className={m.selected ? "row-active" : "row-removed"}>
                    <td>
                      <button className={m.selected ? "remove-item-btn" : "add-item-btn"} onClick={() => {
                        const arr = [...editableMedicines]; arr[i].selected = !arr[i].selected; setEditableMedicines(arr);
                      }}>{m.selected ? "✖" : "➕"}</button>
                    </td>
                    <td className="name-col">{m.medicineName}</td>
                    <td>₹{m.price}</td>
                    <td>
                      <input type="number" min="1" value={m.quantity} disabled={!m.selected} onChange={(e) => {
                        const arr = [...editableMedicines]; arr[i].quantity = Math.max(1, +e.target.value); setEditableMedicines(arr);
                      }} style={{width: '60px'}} />
                    </td>
                    <td className="subtotal-col">₹{(m.price * m.quantity).toLocaleString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="editor-section" style={{marginTop: '20px'}}>
            <h3>🧪 Lab Tests</h3>
            <table className="editor-table-awesome">
              <thead>
                <tr><th>Action</th><th>Test Name</th><th>Price</th></tr>
              </thead>
              <tbody>
                {editableTests.map((t, i) => (
                  <tr key={`t-${i}`} className={t.selected ? "row-active" : "row-removed"}>
                    <td>
                      <button className={t.selected ? "remove-item-btn" : "add-item-btn"} onClick={() => {
                        const arr = [...editableTests]; arr[i].selected = !arr[i].selected; setEditableTests(arr);
                      }}>{t.selected ? "✖" : "➕"}</button>
                    </td>
                    <td className="name-col">{t.testName}</td>
                    <td>₹{t.price}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="editor-footer-actions">
            <button className="btn-cancel-final" onClick={() => setActivePage("DASHBOARD")}>✖ Cancel</button>
            <button className="btn-generate-final" style={{backgroundColor: '#22c55e'}} 
              onClick={() => handlePayAndGenerate(visitIdSearch, "CASH")}>
              💰 Pay & Generate 🖨️
            </button>
          </div>
        </div>
      )}

      {/* BILLS TABLE */}
      {activePage === "BILLS" && (
        <DataTable title="Recent Invoices" 
          headers={["Bill ID", "Patient", "Total", "Status", "Action"]}
          data={bills}
          renderRow={(b) => (
            <>
              <td>#{b.billId}</td>
              <td>{b.patientName}</td>
              <td><strong>₹{b.grandTotal}</strong></td>
              <td><span className={`status-pill ${b.paid ? "paid" : "unpaid"}`}>{b.paid ? "PAID" : "UNPAID"}</span></td>
              <td><button className="print-action-btn" onClick={() => { setSelectedBill(b); setShowReceipt(true); }}>🖨 Print</button></td>
            </>
          )}
        />
      )}

      {/* PATIENTS TABLE (Warning Solve karne ke liye ye use ho raha hai) */}
      {activePage === "PATIENTS" && (
        <DataTable title="Registered Patients" 
          headers={["ID", "Name", "Gender", "Mobile", "City"]}
          data={patients} // 👈 'patients' variable yahan use ho gaya!
          renderRow={(p) => (
            <><td>{p.id}</td><td>{p.name}</td><td>{p.gender}</td><td>{p.mobile}</td><td>{p.address}</td></>
          )}
        />
      )}
    </div>
  );
}

const ProfileField = ({ label, value }) => (
  <div className="profile-field"><label>{label}</label><span>{value || "-"}</span></div>
);

const DataTable = ({ title, headers, data, renderRow }) => {
  const [search, setSearch] = useState("");
  const filteredData = useMemo(() => {
    return data.filter(item => Object.values(item).join(" ").toLowerCase().includes(search.toLowerCase()));
  }, [data, search]);

  return (
    <div className="appointments-card fade-in">
      <div className="table-header"><h3>{title}</h3><input className="table-search" placeholder="Filter..." onChange={(e) => setSearch(e.target.value)} /></div>
      <table className="appointments-table">
        <thead><tr>{headers.map(h => <th key={h}>{h}</th>)}</tr></thead>
        <tbody>{filteredData.map((item, i) => <tr key={i}>{renderRow(item)}</tr>)}</tbody>
      </table>
    </div>
  );
}; 