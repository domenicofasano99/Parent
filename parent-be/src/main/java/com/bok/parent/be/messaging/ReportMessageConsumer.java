package com.bok.parent.be.messaging;


import com.bok.parent.be.helper.FileAttachmentHelper;
import com.bok.parent.integration.message.FileMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportMessageConsumer {

    @Autowired
    FileAttachmentHelper fileAttachmentHelper;

    @JmsListener(destination = "${queues.reports}")
    public void accountCreationListener(FileMessage message) {
        log.info("Received message from parent: " + message.toString());
        fileAttachmentHelper.handle(message);
    }


}