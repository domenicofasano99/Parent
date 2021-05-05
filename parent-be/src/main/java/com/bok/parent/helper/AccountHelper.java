package com.bok.parent.helper;

import com.bok.integration.AccountCreationMessage;
import com.bok.integration.EmailMessage;
import com.bok.integration.parent.dto.AccountRegistrationDTO;
import com.bok.parent.exception.EmailAlreadyExistsException;
import com.bok.parent.model.Account;
import com.bok.parent.model.ConfirmationToken;
import com.bok.parent.model.TemporaryAccount;
import com.bok.parent.repository.AccountConfirmationTokenRepository;
import com.bok.parent.repository.AccountRepository;
import com.bok.parent.repository.TemporaryUserRepository;
import com.bok.parent.utils.Constants;
import com.bok.parent.utils.CryptoUtils;
import com.bok.parent.utils.ValidationUtils;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
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

    @Autowired
    ValidationUtils validationUtils;

    @Value("${server.baseUrl}")
    String baseUrl;

    @Transactional
    public String register(AccountRegistrationDTO request) {
        Preconditions.checkArgument(Objects.nonNull(request.credentials.password));
        Preconditions.checkArgument(Objects.nonNull(request.credentials.email));
        Preconditions.checkArgument(validationUtils.validateEmail(request.credentials.email));
        Preconditions.checkArgument(Objects.nonNull(request.name));
        Preconditions.checkArgument(validationUtils.validateName(request.name));
        Preconditions.checkArgument(Objects.nonNull(request.surname));
        Preconditions.checkArgument(validationUtils.validateSurname(request.surname));
        Preconditions.checkArgument(Objects.nonNull(request.birthdate));

        if (accountRepository.existsByEmail(request.credentials.email)) {
            throw new EmailAlreadyExistsException("Account already registered.");
        }
        Account account = new Account();
        account.setEmail(request.credentials.email);
        account.setPassword(cryptoUtils.encryptPassword(request.credentials.password));
        account.setEnabled(false);
        account.setRole(Account.Role.USER);
        account = accountRepository.save(account);

        TemporaryAccount temporaryAccount = new TemporaryAccount(request.name,
                request.middleName,
                request.surname,
                request.credentials.email,
                request.birthdate,
                request.business,
                request.fiscalCode,
                request.vatNumber,
                request.mobile.icc,
                request.mobile.mobile,
                request.address.houseNumber,
                request.address.street,
                request.address.city,
                request.address.county,
                request.address.country,
                request.address.postalCode,
                request.gender.name(),
                account);


        saveTemporaryUserData(temporaryAccount);
        sendAccountConfirmationEmail(account);
        return "registered";
    }

    private void sendAccountConfirmationEmail(Account account) {
        ConfirmationToken confirmationToken = new ConfirmationToken(account);
        accountConfirmationTokenRepository.save(confirmationToken);
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.to = account.getEmail();
        emailMessage.subject = "BOK Account Verification";
        emailMessage.text = "Click on the link to verify your BOK account: \n" +
                baseUrl + "/verify?verificationToken=" + confirmationToken.getConfirmationToken() +
                "\n\nThe BOK Team.";

        messageHelper.send(emailMessage);
    }

    @Transactional
    public void saveTemporaryUserData(TemporaryAccount temporaryAccount) {
        temporaryUserRepository.save(temporaryAccount);
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> findByEmailAndEnabled(String email) {
        return accountRepository.findByEmailAndEnabledIsTrue(email);
    }

    @Cacheable(value = Constants.IDS, unless = "#result == null")
    public Long findIdByEmail(String email) {
        return accountRepository.findIdByEmail(email);
    }

    private void notifyServices(Account account) {
        log.info("Notifying services about the account {} creation", account);
        AccountCreationMessage creationCreationMessage = new AccountCreationMessage();
        creationCreationMessage.accountId = account.getId();

        TemporaryAccount userData = temporaryUserRepository.findByAccount(account).orElseThrow(() -> new RuntimeException("Couldn't find account " + account.getId()));
        creationCreationMessage.email = account.getEmail();
        creationCreationMessage.name = userData.getName();
        creationCreationMessage.surname = userData.getSurname();

        messageHelper.send(creationCreationMessage);

        temporaryUserRepository.deleteByAccount_Id(account.getId());
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
        accountConfirmationTokenRepository.delete(token);
        notifyServices();

        return "Your account has been confirmed, you can now login to the user area.";
    }

    private AccountCreationMessage generateAccountCreationMessage() {

    }
}
