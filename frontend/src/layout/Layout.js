// Layout.js
import React from "react";
import { Outlet } from "react-router-dom";
import Navbar from "../components/common/Navbar";
import "../styles/layout.css";  // contains wellspring-bg and overlay styles

export default function Layout() {
  return (
    <div className="layout-hero">
      <Navbar />

      {/* BACKGROUND & OVERLAY */}
      <div className="wellspring-bg" aria-hidden="true"></div>
      <div className="wellspring-overlay" aria-hidden="true"></div>

      {/* PAGE CONTENT */}
      <div className="layout-content">
        <Outlet />
      </div>
    </div>
  );
}
