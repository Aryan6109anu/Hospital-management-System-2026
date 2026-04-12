/* ================= DATE FORMAT ================= */
export const formatDate = (date) => {
  if (!date) return "";

  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`; // backend safe
};


/* ================= DISPLAY DATE ================= */
export const formatDisplayDate = (date) => {
  if (!date) return "";

  // backend date safe conversion
  if (typeof date === "string" && date.includes("-")) {
    const [year, month, day] = date.split("-");
    return `${day}-${month}-${year}`;
  }

  const d = new Date(date);
  const day = String(d.getDate()).padStart(2, "0");
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const year = d.getFullYear();

  return `${day}-${month}-${year}`;
};


/* ================= TIME AM PM ================= */
export const formatTimeAMPM = (time) => {
  if (!time) return "";

  if (time.includes("AM") || time.includes("PM")) return time;

  let [hours, minutes] = time.split(":");
  hours = parseInt(hours, 10);

  const ampm = hours >= 12 ? "PM" : "AM";
  hours = hours % 12 || 12;

  const hh = hours < 10 ? `0${hours}` : hours;
  const mm = minutes.substring(0, 2);

  return `${hh}:${mm} ${ampm}`;
};


/* ================= TIME RANGE ================= */
export const formatTimeRange = (start, end) => {
  if (!start || !end) return "-";

  return `${formatTimeAMPM(start)} - ${formatTimeAMPM(end)}`;
};