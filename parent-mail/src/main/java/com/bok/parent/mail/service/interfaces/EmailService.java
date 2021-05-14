package com.bok.parent.mail.service.interfaces;

import com.bok.parent.integration.message.EmailMessage;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    public void sendEmail(EmailMessage emailMessage);
}
