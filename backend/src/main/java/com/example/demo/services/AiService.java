package com.example.demo.services;

import org.springframework.stereotype.Service;

@Service
public class AiService {

    public String generateReply(String message) {

        return """
        Hello,

        Thank you for contacting our hospital.
        Your message has been received successfully.
        Our team will contact you shortly.

        Regards,
        Hospital Support Team
        """;
    }
}
