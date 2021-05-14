package com.bok.parent.mail.service;

import com.bok.parent.integration.message.EmailMessage;
import com.bok.parent.mail.service.interfaces.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender emailSender;

    @Value("${spring.mail.from}")
    String from;

    public void sendEmail(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailMessage.to);
        message.setSubject(emailMessage.subject);
        message.setText(emailMessage.text);
        emailSender.send(message);
    }
}