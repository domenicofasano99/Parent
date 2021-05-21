package com.bok.parent.messaging;


import com.bok.parent.helper.ReportHelper;
import com.bok.parent.integration.message.AccountCreationMessage;
import com.bok.parent.integration.message.ReportMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportMessageConsumer {

    @Autowired
    ReportHelper reportHelper;

    @JmsListener(destination = "${active-mq.reports}")
    public void accountCreationListener(ReportMessage message) {
        log.info("Received message from parent: " + message.toString());
        reportHelper.handle(message);
    }


}