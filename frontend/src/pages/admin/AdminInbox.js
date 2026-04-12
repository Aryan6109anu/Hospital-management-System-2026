import { useEffect, useState } from "react";
import { Eye, EyeOff } from 'lucide-react';
import { getAllMessages } from "../../services/api";
import { useNavigate } from "react-router-dom";
import "../../styles/adminInbox.css";

export default function AdminInbox() {
  const [messages, setMessages] = useState([]);
  const [openId, setOpenId] = useState(null);
  const [showAiId, setShowAiId] = useState(null); // 🔥 which mail's AI reply to show
  const navigate = useNavigate();

  useEffect(() => {
    const loadMessages = async () => {
      const res = await getAllMessages();
      const sorted = res.data.sort(
        (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
      );
      setMessages(sorted);
    };
    loadMessages();
  }, []);

  const resolveRole = (role) => {
    switch (role) {
      case "ROLE_DOCTOR": return "Doctor";
      case "ROLE_PATIENT": return "Patient";
      case "ROLE_ADMIN": return "Admin";
      default: return "Guest";
    }
  };

  const formatTime = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString("en-IN", {
      day: "2-digit",
      month: "short",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const toggleOpen = (id) => {
    setOpenId(openId === id ? null : id);
    if (openId === id) setShowAiId(null); // close AI reply if mail closed
  };

  const toggleAiReply = (id) => {
    setShowAiId(showAiId === id ? null : id);
  };

  return (
    <div className="admin-container">
      <h2>📩 Admin Inbox</h2>

      <div className="inbox-list">
        {messages.map((msg) => {
          const isOpen = openId === msg.id;
          const isAiOpen = showAiId === msg.id;

          return (
            <div key={msg.id} className="mail-row">

              {/* ONE LINE HEADER */}
              <div className="mail-line" onClick={() => toggleOpen(msg.id)}>
                
                {/* Envelope Icon */}
                <div className={`mail-icon ${isOpen ? "open" : ""}`}>
                  <div className="envelope-body"></div>
                  <div className="envelope-top"></div>
                </div>

                {/* Name + Role */}
                <span className="mail-name">
                  {msg.name} ({resolveRole(msg.role)})
                </span>

                {/* Message preview */}
                <span className="mail-subject">
                  {msg.message.substring(0, 50)}...
                </span>

                {/* Time */}
                <span className="mail-time">{formatTime(msg.createdAt)}</span>
              </div>

              {/* EXPANDED MAIL */}
              {isOpen && (
                <div className="mail-expanded">
                  <p><b>From:</b> {msg.email}</p>
                  <p><b>Message:</b></p>
                  <div className="mail-box">{msg.message}</div>

                  {/* AI Toggle Icon */}
                      {/* AI Toggle Icon */}
{msg.aiReply && (
  <div 
    className="ai-toggle" 
    onClick={() => toggleAiReply(msg.id)}
    style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '8px' }}
  >
    <span>🤖 AI Reply</span>
    {isAiOpen ? (
      <Eye size={20} color="#4A90E2" strokeWidth={2.5} /> 
    ) : (
      <EyeOff size={20} color="#999" strokeWidth={2.5} />
    )}
  </div>
)}

                  {/* AI Reply Box */}
                  {isAiOpen && (
                    <div className="mail-box ai">{msg.aiReply}</div>
                  )}

                  {/* Admin Reply Button */}
                  <button
                    className="reply-btn"
                    onClick={() => navigate(`/admin/reply/${msg.id}`)}
                  >
                    Reply
                  </button>
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}
