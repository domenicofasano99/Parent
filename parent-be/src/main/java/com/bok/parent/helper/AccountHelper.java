package com.bok.parent.helper;

import com.bok.integration.EmailMessage;
import com.bok.parent.dto.RegisterAccount;
import com.bok.parent.exception.EmailAlreadyExistsException;
import com.bok.parent.message.KryptoAccountCreationMessage;
import com.bok.parent.model.Account;
import com.bok.parent.model.ConfirmationToken;
import com.bok.parent.model.TemporaryUser;
import com.bok.parent.repository.AccountConfirmationTokenRepository;
import com.bok.parent.repository.AccountRepository;
import com.bok.parent.repository.TemporaryUserRepository;
import com.bok.parent.utils.CryptoUtils;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;


@Component
@Slf4j
public class AccountHelper {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountConfirmationTokenRepository accountConfirmationTokenRepository;

    @Autowired
    TemporaryUserRepository temporaryUserRepository;

    @Autowired
    CryptoUtils cryptoUtils;

    @Autowired
    MessageHelper messageHelper;

    @Transactional
    public String register(RegisterAccount registerAccount) {
        Preconditions.checkArgument(Objects.nonNull(registerAccount.password));
        Preconditions.checkArgument(Objects.nonNull(registerAccount.email));
        Preconditions.checkArgument(Objects.nonNull(registerAccount.name));
        Preconditions.checkArgument(Objects.nonNull(registerAccount.surname));

        if (accountRepository.existsByEmail(registerAccount.email)) {
            throw new EmailAlreadyExistsException("Account already registered.");
        }
        Account account = new Account();
        account.setEmail(registerAccount.email);
        account.setPassword(cryptoUtils.encryptPassword(registerAccount.password));
        account.setEnabled(false);
        account.setRole(Account.Role.USER);
        account = accountRepository.save(account);
        saveTemporaryUserData(account, registerAccount.name, registerAccount.surname, null);
        sendAccountConfirmationEmail(account);
        return "registered";
    }

    private void sendAccountConfirmationEmail(Account account) {
        ConfirmationToken confirmationToken = new ConfirmationToken(account);
        accountConfirmationTokenRepository.save(confirmationToken);
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.to = account.getEmail();
        emailMessage.subject = "BOK Account Verification";
        emailMessage.text = "Click on the link to verify your BOK account: http://dev.faraone.ovh:8082/verify?verificationToken=" + confirmationToken.getConfirmationToken();

        messageHelper.send(emailMessage);
    }

    @Transactional
    public void saveTemporaryUserData(Account account, String name, String surname, Date birthdate) {
        TemporaryUser temporaryUser = new TemporaryUser();
        temporaryUser.setAccount(account);
        temporaryUser.setName(name);
        temporaryUser.setSurname(surname);
        temporaryUser.setBirthDate(birthdate);
        temporaryUserRepository.save(temporaryUser);
    }


    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Long findIdByEmail(String email) {
        return accountRepository.findIdByEmail(email);
    }

    private void notifyServices(Account account) {
        log.info("Notifying services about the account {} creation", account);
        KryptoAccountCreationMessage kryptoMessage = new KryptoAccountCreationMessage();
        kryptoMessage.accountId = account.getId();
        TemporaryUser userData = temporaryUserRepository.findByAccount_Id(account.getId()).orElseThrow(() -> new RuntimeException("Couldn't find account " + account));
        kryptoMessage.email = account.getEmail();
        kryptoMessage.name = userData.getName();
        kryptoMessage.surname = userData.getSurname();

        messageHelper.send(kryptoMessage);

        //here bank should be notified about the creation of the user
    }

    @Transactional
    public String verify(String accountConfirmationToken) {
        Preconditions.checkArgument(Objects.nonNull(accountConfirmationToken));
        ConfirmationToken token = accountConfirmationTokenRepository.findByConfirmationToken(accountConfirmationToken);
        Preconditions.checkArgument(Objects.nonNull(token));

        Optional<Account> accountOptional = accountRepository.findByEmail(token.getAccount().getEmail());
        if (!accountOptional.isPresent()) {
            log.warn("Couldn't find token {} related account.", accountConfirmationToken);
            accountConfirmationTokenRepository.delete(token);
            throw new RuntimeException("Error while activating you account, try registering again or contact customer care.");
        }
        Account account = accountOptional.get();
        account.setEnabled(true);
        accountRepository.save(account);
        log.info("Successfully verified account {}", account);
        temporaryUserRepository.deleteByAccount_Id(account.getId());
        accountConfirmationTokenRepository.delete(token);
        notifyServices(account);

        return "Your account has been confirmed, you can now login to the user area.";
    }
}
