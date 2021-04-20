package com.bok.parent.helper;

import com.bok.integration.EmailMessage;
import com.bok.parent.message.BankAccountCreationMessage;
import com.bok.parent.message.KryptoAccountCreationMessage;
import com.bok.parent.messaging.BankUserMessageProducer;
import com.bok.parent.messaging.EmailMessageProducer;
import com.bok.parent.messaging.KryptoUserMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHelper {

    @Autowired
    BankUserMessageProducer bankUserMessageProducer;

    @Autowired
    KryptoUserMessageProducer kryptoUserMessageProducer;

    @Autowired
    EmailMessageProducer emailMessageProducer;

    public void send(BankAccountCreationMessage userCreationMessage) {
        bankUserMessageProducer.send(userCreationMessage);
    }

    public void send(KryptoAccountCreationMessage userCreationMessage) {
        kryptoUserMessageProducer.send(userCreationMessage);
    }

    public void send(EmailMessage emailMessage) {
        emailMessageProducer.send(emailMessage);
    }
}
