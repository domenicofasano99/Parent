package com.bok.parent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles("test")
public class GrpcTest {

    @Autowired
    ModelTestUtil modelTestUtil;

    @BeforeEach
    public void clear() {
        modelTestUtil.clearAll();
    }

    @Test
    public void placeHolder() {

    }
}
