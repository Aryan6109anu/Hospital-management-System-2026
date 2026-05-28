package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AdminReplyDTO;
import com.example.demo.dto.ContactMessageDTO;
import com.example.demo.entities.ContactMessage;
import com.example.demo.services.ContactService;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // ================= PUBLIC / PATIENT / DOCTOR =================
    @PostMapping
    public ContactMessage send(@RequestBody ContactMessageDTO dto) {
        return contactService.sendMessage(dto);
    }

    // ================= ADMIN INBOX =================
    @GetMapping("/admin")
    public List<ContactMessage> inbox() {
        return contactService.getAllMessages();
    }

    // ================= ADMIN GET SINGLE MESSAGE (🔥 MISSING PART) =================
    @GetMapping("/{id}")
    public ContactMessage getMessageById(@PathVariable Long id) {
        return contactService.getMessageById(id);
    }

    // ================= ADMIN REPLY =================
    @PostMapping("/admin/reply/{id}")
    public void reply(@PathVariable Long id,
                       @RequestBody AdminReplyDTO dto) {
        contactService.adminReply(id, dto.getAiReply());
    }

}
