package com.bok.parent.messaging;

import com.bok.integration.UserCreationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageProducer implements Producer<UserCreationDTO>{

    @Autowired
    JmsTemplate queueJmsTemplate;

    @Value("${active-mq.users-queue}")
    private String usersQueue;

    /**
     * this method will send a message to the queue that will be listened by
     * krypto. the message will be sent each time a new user is created into the system
     * @param userCreationDTO represents the just created user
     **/
    @Override
    public void produce(UserCreationDTO userCreationDTO) {
        try {
            log.info("Attempting Send transfer to Topic: " + usersQueue);
            queueJmsTemplate.convertAndSend(usersQueue, userCreationDTO);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
