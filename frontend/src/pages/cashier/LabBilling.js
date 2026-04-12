import { useState } from "react";
import axios from "axios";

export default function LabBilling(){

  const [orderId, setOrderId] = useState("");
  const [bill, setBill] = useState(null);

  const fetchBill = () => {
    axios.get(`http://localhost:8080/cashier/lab-order/${orderId}`)
      .then(res => setBill(res.data));
  };

  const pay = () => {
    axios.post(`http://localhost:8080/cashier/lab-order/${orderId}/pay`)
      .then(() => alert("Paid Successfully"));
  };

  return (
    <div>
      <h2>Lab Billing</h2>

      <input
        value={orderId}
        onChange={(e)=>setOrderId(e.target.value)}
        placeholder="Enter Order ID"
      />

      <button onClick={fetchBill}>Generate Bill</button>

      {bill && (
        <div>
          <p>Total: {bill.totalAmount}</p>
          <p>GST: {bill.gstAmount}</p>
          <p>Grand Total: {bill.grandTotal}</p>

          <button onClick={pay}>Pay</button>
        </div>
      )}
    </div>
  );
}