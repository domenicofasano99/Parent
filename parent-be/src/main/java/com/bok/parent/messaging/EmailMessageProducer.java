package com.bok.parent.messaging;

import com.bok.integration.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailMessageProducer {
    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.emails-queue}")
    private String emailsQueue;


    public void send(EmailMessage emailMessage) {
        try {
            log.info("Attempt to send account creation message to queue: " + emailsQueue);
            jmsTemplate.convertAndSend(emailsQueue, emailMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
