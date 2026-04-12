package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ContactMessageDTO;
import com.example.demo.entities.ContactMessage;
import com.example.demo.repository.ContactMessageRepository;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactMessageRepository repo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AiService aiService;

    // ================= USER SENDS MESSAGE =================
    @Override
    public ContactMessage sendMessage(ContactMessageDTO dto) {

        ContactMessage msg = new ContactMessage();
        msg.setName(dto.name);
        msg.setEmail(dto.email);
        msg.setRole(dto.role);
        msg.setMessage(dto.message);
        msg.setCreatedAt(LocalDateTime.now());

        // 🤖 AI AUTO REPLY
        String aiReply = aiService.generateReply(dto.message);

        String aiMail =
                "Dear " + dto.name + ",\n\n" +
                "Greetings from SpringWell Hospital.\n\n" +
                aiReply + "\n\n" +
                "This message serves as an automated acknowledgement of your request. " +
        	    "Our team will carefully review the details and reach out to you at the earliest.\n\n" +
                "Warm Regards,\n" +
                "SpringWell Hospital Support Team\n" +
                "📧 support@springwellhospital.com\n" +
                "📞 +91-98765-43210";

        msg.setAiReply(aiMail);
        msg.setReplied(false);
        repo.save(msg);

        // 📧 ONLY AI MAIL GOES
        emailService.sendMail(
                dto.email,
                "SpringWell Hospital - We Received Your Message",
                aiMail
        );

        return msg;
    }

    // ================= ADMIN REPLY =================
    @Override
    public void adminReply(Long id, String reply) {

        ContactMessage msg = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        msg.setAdminReply(reply);
        msg.setReplied(true);
        repo.save(msg);

        // 🧑 ONLY ADMIN MAIL (NO AI HERE)
        String adminMail =
                "Dear " + msg.getName() + ",\n\n" +
                "Greetings from SpringWell Hospital.\n\n" +
                reply + "\n\n" +
                "If you need any further assistance, please reply to this email.\n\n" +
                "Warm Regards,\n" +
                "SpringWell Hospital Support Team\n" +
                "📧 support@springwellhospital.com\n" +
                "📞 +91-98765-43210\n\n" +
                "This is an official communication from SpringWell Hospital.";

        // 📧 ONLY ADMIN MAIL GOES
        emailService.sendMail(
                msg.getEmail(),
                "Re: SpringWell Hospital Support #" + msg.getId(),
                adminMail
        );
    }

    @Override
    public List<ContactMessage> getAllMessages() {
        return repo.findAll();
    }

    @Override
    public ContactMessage getMessageById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }
}
