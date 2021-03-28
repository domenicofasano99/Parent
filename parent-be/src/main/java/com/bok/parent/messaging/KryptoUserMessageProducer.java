package com.bok.parent.messaging;

import com.bok.parent.message.KryptoAccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KryptoUserMessageProducer {

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.krypto-users}")
    private String usersQueue;


    public void send(KryptoAccountCreationMessage userCreationMessage) {
        try {
            log.info("Attempting Send transfer to Topic: " + usersQueue);
            jmsTemplate.convertAndSend(usersQueue, userCreationMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }


}
