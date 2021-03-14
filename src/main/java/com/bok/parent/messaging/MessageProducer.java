package com.bok.parent.messaging;

import com.bok.integration.UserCreationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageProducer {

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.users-queue}")
    private String usersQueue;


    public void send(UserCreationDTO userCreationDTO) {
        try {
            log.info("Attempting Send transfer to Topic: " + usersQueue);
            jmsTemplate.convertAndSend(usersQueue, userCreationDTO);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }


}
