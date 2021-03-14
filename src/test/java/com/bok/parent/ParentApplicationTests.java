package com.bok.parent;

import com.bok.integration.UserCreationDTO;
import com.bok.parent.messaging.UserCreationMessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParentApplicationTests {


    @Autowired
    UserCreationMessageProducer userCreationMessageProducer;


    @Test
    void contextLoads() {
        UserCreationDTO dto = new UserCreationDTO();
        userCreationMessageProducer.send(dto);
    }

}
