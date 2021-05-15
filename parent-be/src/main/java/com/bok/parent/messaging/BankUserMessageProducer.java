package com.bok.parent.messaging;

import com.bok.parent.integration.message.AccountCreationMessage;
import com.bok.parent.integration.message.AccountDeletionMessage;
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

    @Value("${active-mq.bank-account-deletion}")
    private String accountDeletionQueue;


    public void send(AccountCreationMessage userCreationMessage) {
        try {
            log.info("Attempting Send user creation message to queue: " + usersQueue);
            jmsTemplate.convertAndSend(usersQueue, userCreationMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

    public void send(AccountDeletionMessage accountDeletionMessage) {
        try {
            log.info("Attempting send account deletion to queue: " + accountDeletionQueue);
            jmsTemplate.convertAndSend(accountDeletionQueue, accountDeletionMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
