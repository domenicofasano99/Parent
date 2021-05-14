package com.bok.parent.mail.messaging.consumer;

import com.bok.parent.integration.message.EmailMessage;
import com.bok.parent.mail.service.interfaces.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailConsumer {


    @Autowired
    EmailService emailService;

    @JmsListener(destination = "${active-mq.mail}")
    public void onSendEmailMessage(EmailMessage emailMessage) {
        log.info("Received Message: " + emailMessage.toString());
        emailService.sendEmail(emailMessage);
    }


}
