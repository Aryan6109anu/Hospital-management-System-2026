import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getMessageById, adminReply } from "../../services/api";
import { showAlert } from "../../components/common/Alert";
import "../../styles/adminInbox.css";

export default function AdminReply() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [message, setMessage] = useState({});
  const [reply, setReply] = useState("");

  useEffect(() => {
    const loadMessage = async () => {
      const res = await getMessageById(id);
      setMessage(res.data);
      setReply(""); // EMPTY – admin should type fresh
    };
    loadMessage();
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await adminReply(id, { aiReply: reply });
      showAlert("success", "Reply sent successfully ✅");
      navigate("/admin/inbox");
    } catch {
      showAlert("error", "Failed to send reply ❌");
    }
  };

  return (
    <div className="admin-container">
      <h2>✉️ Admin Reply</h2>

      <div className="mail-card">
        <div className="mail-meta">
          <div><b>Name:</b> {message.name}</div>
          <div><b>Email:</b> {message.email}</div>
        </div>

        <div className="mail-section">
          <div className="mail-section-title">Patient Message</div>
          <div className="mail-text">{message.message}</div>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="mail-section">
            <div className="mail-section-title">Your Reply</div>
            <textarea
              className="reply-box"
              value={reply}
              onChange={(e) => setReply(e.target.value)}
              placeholder="Type professional reply here..."
              required
            />
          </div>

          <div className="mail-actions">
            <button type="submit">Send Reply</button>
          </div>
        </form>
      </div>
    </div>
  );
}
