import { Navigate } from "react-router-dom";

export default function PrivateRoute({ children, roleRequired }) {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  // 🔴 Not logged in
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  // 🔹 Normalize role → always ROLE_XXX
  const normalize = (r) =>
    r?.startsWith("ROLE_") ? r : `ROLE_${r}`;

  const userRole = normalize(role);

  // 🔹 If no role restriction, allow access
  if (!roleRequired) {
    return children;
  }

  // 🔹 Support single or multiple roles
  const allowedRoles = Array.isArray(roleRequired)
    ? roleRequired.map(normalize)
    : [normalize(roleRequired)];

  // 🔹 Check access
  if (!allowedRoles.includes(userRole)) {
    return <Navigate to="/" replace />;
  }

  return children;
}