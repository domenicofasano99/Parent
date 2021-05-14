package com.bok.parent.mail.service.interfaces;

import com.bok.integration.EmailMessage;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    public void sendSimpleMessage(EmailMessage emailMessage);

    public void sendIt(String to);
}
