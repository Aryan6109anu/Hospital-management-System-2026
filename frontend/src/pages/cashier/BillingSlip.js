import React, { useRef } from "react";
import { useReactToPrint } from "react-to-print";

export default function BillingSlip({ data }) {
  const componentRef = useRef();

  const handlePrint = useReactToPrint({
    content: () => componentRef.current,
  });

  return (
    <div>
      <button onClick={handlePrint}>Print Slip</button>

      <div ref={componentRef} className="bill-container">
        <h2>Hospital Billing Slip</h2>
        <p>Patient: {data.patientName}</p>
        <p>Date: {new Date().toLocaleDateString()}</p>

        <table>
          <thead>
            <tr>
              <th>Item</th>
              <th>Price</th>
            </tr>
          </thead>
          <tbody>
            {data.items.map((item, index) => (
              <tr key={index}>
                <td>{item.name}</td>
                <td>₹ {item.price}</td>
              </tr>
            ))}
          </tbody>
        </table>

        <h3>Total: ₹ {data.totalAmount}</h3>
      </div>
    </div>
  );
}