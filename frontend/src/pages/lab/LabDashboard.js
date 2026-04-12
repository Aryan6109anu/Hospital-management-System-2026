import React, { useEffect, useState } from "react";
import api from "../../services/api";

export default function LabDashboard() {

  const [orders, setOrders] = useState([]);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {

    try {

      const res = await api.get("/lab-orders");

      setOrders(res.data);

    } catch (err) {

      console.error("Error fetching lab orders", err);

    }

  };

  const markComplete = async (id) => {

    try {

      await api.put(`/lab-orders/${id}/complete`);

      alert("Test marked as completed");

      fetchOrders();

    } catch (err) {

      console.error("Error updating status", err);

    }

  };

  return (

    <div style={{ padding: "20px" }}>

      <h2>Lab Dashboard</h2>

      <table border="1" cellPadding="10" width="100%">

        <thead>

          <tr>
            <th>Order ID</th>
            <th>Patient</th>
            <th>Tests</th>
            <th>Status</th>
            <th>Action</th>
          </tr>

        </thead>

        <tbody>

          {orders.map((order) => (

            <tr key={order.id}>

              <td>{order.id}</td>

              <td>{order.patientId}</td>

              <td>

                {order.tests.map((t, index) => (
                  <div key={index}>{t.labTest.testName}</div>
                ))}

              </td>

              <td>{order.status}</td>

              <td>

                {order.status !== "COMPLETED" && (
                  <button onClick={() => markComplete(order.id)}>
                    Mark Complete
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