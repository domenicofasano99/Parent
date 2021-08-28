package com.bok.parent.be.helper;

import com.bok.parent.be.messaging.BankUserMessageProducer;
import com.bok.parent.be.messaging.EmailMessageProducer;
import com.bok.parent.be.messaging.KryptoUserMessageProducer;
import com.bok.parent.integration.message.AccountClosureMessage;
import com.bok.parent.integration.message.AccountCreationMessage;
import com.bok.parent.integration.message.EmailMessage;
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

    public void send(AccountClosureMessage accountClosureMessage) {
        kryptoUserMessageProducer.send(accountClosureMessage);
        bankUserMessageProducer.send(accountClosureMessage);
    }
}
