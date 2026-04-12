package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.ContactMessageDTO;
import com.example.demo.entities.ContactMessage;

public interface ContactService {

    ContactMessage sendMessage(ContactMessageDTO dto);

    List<ContactMessage> getAllMessages();

    void adminReply(Long id, String reply);
    
    public ContactMessage getMessageById(Long id);

}
