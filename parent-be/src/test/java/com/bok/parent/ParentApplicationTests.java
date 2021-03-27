package com.bok.parent;

import com.bok.parent.messaging.MessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParentApplicationTests {


    @Autowired
    MessageProducer messageProducer;


    @Test
    void contextLoads() {
//        UserCreationDTO dto = new UserCreationDTO();
//        messageProducer.send(dto);
    }

}
