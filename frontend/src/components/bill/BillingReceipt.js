import React from 'react';
import "../../styles/slip.css"; 
import "../../styles/BillingReceipt.css"; 

const BillingReceipt = ({ billData }) => {
  if (!billData) return <p className="no-print">Loading Bill Data...</p>;

  // 1. Calculations
  const pharmacyTotal = billData.medicines?.reduce((sum, m) => 
    sum + (Number(m.price || 0) * Number(m.quantity || 0)), 0) || 0;

  const labTotal = billData.labTests?.reduce((sum, l) => 
    sum + Number(l.price || 0), 0) || 0;

  const finalCalculatedTotal = billData.grandTotal || (pharmacyTotal + labTotal);

  // 🔍 2. Mapping from your Backend DTO
  const pId = billData.patientId || billData.visitId || "N/A";
  const bId = billData.billId || "000";
  const deptName = billData.department || "General OPD";

  const handlePrint = () => {
    const printContent = document.getElementById("printable-area").innerHTML;
    const printWindow = window.open('', '', 'width=900,height=800');

    printWindow.document.write(`
      <html>
        <head>
          <title>SpringWell Hospital - ${billData.patientName}</title>
          <style>
            body { font-family: 'Segoe UI', Arial, sans-serif; padding: 20px; color: #000; }
            .opd-slip { margin-bottom: 50px; border: 1px solid #000; padding: 30px; background: #fff; }
            .slip-header { text-align: center; border-bottom: 2px solid #000; padding-bottom: 10px; }
            .hospital-name { font-size: 26px; font-weight: bold; margin: 0; }
            .slip-badge { display: inline-block; padding: 5px 15px; background: #000; color: #fff; font-weight: bold; margin-top: 10px; font-size: 13px; }
            .slip-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin: 20px 0; border: 1px solid #ddd; padding: 15px; }
            .slip-grid div { font-size: 14px; line-height: 1.8; }
            table { width: 100%; border-collapse: collapse; margin-top: 15px; }
            th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
            th { background: #f2f2f2; }
            .total-row { font-weight: bold; font-size: 16px; border-top: 2px solid #000; }
            .page-break { page-break-after: always; display: block; }
            @media print { 
               body { padding: 0; margin: 0; }
               .opd-slip { border: 1px solid #000; margin-bottom: 0; }
            }
          </style>
        </head>
        <body>${printContent}</body>
      </html>
    `);

    printWindow.document.close();
    printWindow.focus();
    setTimeout(() => { printWindow.print(); printWindow.close(); }, 500);
  };

  return (
    <div className="receipt-wrapper">
      <div className="no-print" style={{ textAlign: 'center', marginBottom: '30px' }}>
        <h2 style={{color: '#0f172a'}}>SpringWell Hospital Bill ✅</h2>
        <button onClick={handlePrint} className="print-btn" style={{padding: '12px 40px', cursor: 'pointer', fontWeight: '800', background: '#000', color: '#fff', borderRadius: '5px', border: 'none'}}>
          🖨 PRINT ALL 3 SLIPS
        </button>
      </div>

      <div id="printable-area">
        
        {/* SLIP 1: MAIN HOSPITAL RECEIPT */}
        <div className="opd-slip page-break">
            <div className="slip-header">
              <h3 className="hospital-name">SPRINGWELL HOSPITAL</h3>
              <p>Ranchi, Jharkhand</p>
              <div className="slip-badge">HOSPITAL COPY</div>
            </div>

            <div className="slip-grid">
              <div><strong>Bill No:</strong> #{bId}</div>
              <div><strong>Patient ID:</strong> {pId}</div>
              <div><strong>Patient Name:</strong> {billData.patientName}</div>
              <div><strong>Department:</strong> {deptName}</div>
              <div><strong>Doctor:</strong> {billData.doctorName}</div>
              <div><strong>Date:</strong> {new Date().toLocaleDateString()}</div>
              <div><strong>Status:</strong> {billData.paid ? "PAID" : "UNPAID"}</div>
            </div>

            <table>
              <thead>
                <tr><th>Description</th><th align="right">Amount</th></tr>
              </thead>
              <tbody>
                <tr><td>Pharmacy Total Charges</td><td align="right">₹{pharmacyTotal.toFixed(2)}</td></tr>
                <tr><td>Laboratory & Investigation Charges</td><td align="right">₹{labTotal.toFixed(2)}</td></tr>
                <tr className="total-row"><td>GRAND TOTAL</td><td align="right">₹{finalCalculatedTotal.toFixed(2)}</td></tr>
              </tbody>
            </table>
            <div style={{marginTop: '50px', display: 'flex', justifyContent: 'space-between'}}>
                <p>Patient Sign</p>
                <p>Authorized Cashier</p>
            </div>
        </div>

        {/* SLIP 2: PHARMACY BILL (Only if medicines exist) */}
        {billData.medicines?.length > 0 && (
          <div className="opd-slip page-break">
            <div className="slip-header">
              <h3 className="hospital-name">SPRINGWELL HOSPITAL</h3>
              <p>PHARMACY CASH MEMO</p>
              <div className="slip-badge" style={{background: '#15803d'}}>PHARMACY COPY</div>
            </div>
            <div className="slip-grid">
              <div><strong>Patient Name:</strong> {billData.patientName}</div>
              <div><strong>Patient ID:</strong> {pId}</div>
              <div><strong>Bill No:</strong> #{bId}</div>
              <div><strong>Date:</strong> {new Date().toLocaleDateString()}</div>
            </div>
            <table>
              <thead>
                <tr><th>Medicine Name</th><th>Qty</th><th align="right">Subtotal</th></tr>
              </thead>
              <tbody>
                {billData.medicines.map((m, i) => (
                  <tr key={i}>
                    <td>{m.medicineName}</td>
                    <td>{m.quantity}</td>
                    <td align="right">₹{(Number(m.price) * Number(m.quantity)).toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <h4 style={{textAlign: 'right', marginTop: '15px'}}>Pharmacy Total: ₹{pharmacyTotal.toFixed(2)}</h4>
            <p style={{marginTop: '20px'}}>Pharmacist Signature: _________________</p>
          </div>
        )}

        {/* SLIP 3: LAB BILL (Only if tests exist) */}
        {billData.labTests?.length > 0 && (
          <div className="opd-slip page-break">
            <div className="slip-header">
              <h3 className="hospital-name">SPRINGWELL HOSPITAL</h3>
              <p>LABORATORY SERVICES</p>
              <div className="slip-badge" style={{background: '#1d4ed8'}}>LABORATORY COPY</div>
            </div>
            <div className="slip-grid">
              <div><strong>Patient Name:</strong> {billData.patientName}</div>
              <div><strong>Patient ID:</strong> {pId}</div>
              <div><strong>Dept:</strong> {deptName}</div>
              <div><strong>Bill No:</strong> #{bId}</div>
            </div>
            <table>
              <thead>
                <tr><th>Test Name</th><th align="right">Price</th></tr>
              </thead>
              <tbody>
                {billData.labTests.map((t, i) => (
                  <tr key={i}>
                    <td>{t.testName}</td>
                    <td align="right">₹{Number(t.price).toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <h4 style={{textAlign: 'right', marginTop: '15px'}}>Lab Total: ₹{labTotal.toFixed(2)}</h4>
            <p style={{marginTop: '20px'}}>Lab In-charge Signature: _________________</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default BillingReceipt;