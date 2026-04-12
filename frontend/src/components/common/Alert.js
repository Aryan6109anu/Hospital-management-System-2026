import Swal from "sweetalert2";

export const showAlert = (type, message) => {
  return Swal.fire({
    icon: type,
    title:
      type === "success"
        ? "Success"
        : type === "error"
        ? "Error"
        : type === "warning"
        ? "Warning"
        : "Info",
    text: message,
    confirmButtonColor:
      type === "success"
        ? "#22c55e"
        : type === "error"
        ? "#ef4444"
        : type === "warning"
        ? "#f59e0b"
        : "#3b82f6",
    // YE LINE ADD KARO TAAKI MODAL KE UPAR DIKHE
    didOpen: () => {
      const container = Swal.getContainer();
      if (container) {
        container.style.zIndex = "100000"; // Modal ke 9999 se bada
      }
    }
  });
};