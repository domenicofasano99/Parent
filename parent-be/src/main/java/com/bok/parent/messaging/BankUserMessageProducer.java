package com.bok.parent.messaging;

import com.bok.parent.message.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BankUserMessageProducer {

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.bank-users}")
    private String usersQueue;


    public void send(AccountCreationMessage userCreationMessage) {
        try {
            log.info("Attempting Send user creation message to queue: " + usersQueue);
            jmsTemplate.convertAndSend(usersQueue, userCreationMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
