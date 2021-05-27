package com.bok.parent.be.helper;

import com.bok.parent.integration.message.AccountCreationMessage;
import com.bok.parent.integration.message.AccountDeletionMessage;
import com.bok.parent.integration.message.EmailMessage;
import com.bok.parent.be.messaging.BankUserMessageProducer;
import com.bok.parent.be.messaging.EmailMessageProducer;
import com.bok.parent.be.messaging.KryptoUserMessageProducer;
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

    public void send(AccountCreationMessage accountCreationMessage) {
        kryptoUserMessageProducer.send(accountCreationMessage);
        bankUserMessageProducer.send(accountCreationMessage);
    }

    public void send(EmailMessage emailMessage) {
        emailMessageProducer.send(emailMessage);
    }

    public void send(AccountDeletionMessage accountDeletionMessage) {
        kryptoUserMessageProducer.send(accountDeletionMessage);
        bankUserMessageProducer.send(accountDeletionMessage);
    }
}
