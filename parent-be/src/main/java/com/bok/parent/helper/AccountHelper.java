package com.bok.parent.helper;

import com.bok.parent.integration.dto.AccountRegistrationResponseDTO;
import com.bok.parent.integration.dto.PasswordResetResponseDTO;
import com.bok.parent.integration.dto.VerificationResponseDTO;
import com.bok.parent.integration.message.AccountCreationMessage;
import com.bok.parent.integration.message.EmailMessage;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.exception.EmailAlreadyExistsException;
import com.bok.parent.model.Account;
import com.bok.parent.model.AccountTemporaryDetails;
import com.bok.parent.model.ConfirmationToken;
import com.bok.parent.model.Credentials;
import com.bok.parent.repository.AccessInfoRepository;
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
import java.util.Random;


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

    @Autowired
    AccessInfoRepository accessInfoRepository;

    @Value("${server.baseUrl}")
    String baseUrl;

    @Transactional
    public AccountRegistrationResponseDTO register(AccountRegistrationDTO request) {

        if (accountRepository.existsByCredentials_Email(request.credentials.email)) {
            throw new EmailAlreadyExistsException("Account already registered.");
        }
        Account account = new Account();
        account.setCredentials(new Credentials(request.credentials.email, cryptoUtils.encryptPassword(request.credentials.password)));
        account.setEnabled(false);
        account.setRole(Account.Role.USER);
        account = accountRepository.save(account);

        AccountTemporaryDetails accountTemporaryDetails = new AccountTemporaryDetails(request.name,
                request.middleName,
                request.surname,
                request.credentials.email,
                request.birthdate,
                request.business,
                request.fiscalCode,
                request.vatNumber,
                request.mobile.icc,
                request.mobile.number,
                request.address.houseNumber,
                request.address.street,
                request.address.city,
                request.address.county,
                request.address.country,
                request.address.postalCode,
                request.gender,
                account);


        saveAccountInformations(accountTemporaryDetails);
        sendAccountConfirmationEmail(account);
        log.info("User {} registered", request.credentials.email);
        return new AccountRegistrationResponseDTO("registered");
    }

    private void sendAccountConfirmationEmail(Account account) {
        ConfirmationToken confirmationToken = new ConfirmationToken(account);
        accountConfirmationTokenRepository.save(confirmationToken);
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.to = account.getCredentials().getEmail();
        emailMessage.subject = "BOK Account Verification";
        emailMessage.text = "Click on the link to verify your BOK account: \n" +
                baseUrl + "/verify?verificationToken=" + confirmationToken.getConfirmationToken() +
                "\n\nThe BOK Team.";

        messageHelper.send(emailMessage);
    }

    @Transactional
    public void saveAccountInformations(AccountTemporaryDetails accountTemporaryDetails) {
        temporaryUserRepository.save(accountTemporaryDetails);
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByCredentials_Email(email);
    }

    public Optional<Account> findByEmailAndEnabled(String email) {
        return accountRepository.findByCredentials_EmailAndEnabledIsTrue(email);
    }

    @Cacheable(value = Constants.IDS, unless = "#result == null")
    public Long findIdByEmail(String email) {
        return accountRepository.findIdByEmail(email);
    }

    private void notifyServices(Account account) {
        log.info("Notifying services about the account {} creation", account);
        AccountTemporaryDetails accountTemporaryDetails = temporaryUserRepository.findByAccount(account).orElseThrow(() -> new RuntimeException("Couldn't find account " + account.getId()));
        messageHelper.send(generateAccountCreationMessage(accountTemporaryDetails));
        temporaryUserRepository.deleteByAccount_Id(account.getId());
    }

    @Transactional
    public VerificationResponseDTO verify(String accountConfirmationToken) {
        ConfirmationToken token = accountConfirmationTokenRepository.findByConfirmationToken(accountConfirmationToken);
        Preconditions.checkArgument(Objects.nonNull(token));

        Optional<Account> accountOptional = accountRepository.findByCredentials_Email(token.getAccount().getCredentials().getEmail());
        if (!accountOptional.isPresent()) {
            log.warn("Couldn't find token {} related account.", accountConfirmationToken);
            accountConfirmationTokenRepository.delete(token);
            throw new RuntimeException("Error while activating you account, try registering again or contact customer care.");
        }
        Account account = accountOptional.get();
        account.setEnabled(true);
        accountRepository.save(account);
        log.info("Account {} successfully verified!", account.getCredentials().getEmail());
        accountConfirmationTokenRepository.delete(token);
        notifyServices(account);

        return new VerificationResponseDTO("Your account has been confirmed, you can now login to the user area.");
    }

    private AccountCreationMessage generateAccountCreationMessage(AccountTemporaryDetails userData) {
        AccountCreationMessage message = new AccountCreationMessage();
        message.name = userData.getName();
        message.middleName = userData.getMiddleName();
        message.surname = userData.getSurname();
        message.email = userData.getEmail();
        message.birthdate = userData.getBirthdate();
        message.business = userData.getBusiness();
        message.fiscalCode = userData.getFiscalCode();
        message.vatNumber = userData.getVatNumber();
        message.icc = userData.getIcc();
        message.mobile = userData.getMobile();
        message.houseNumber = userData.getHouseNumber();
        message.street = userData.getStreet();
        message.city = userData.getCity();
        message.county = userData.getCounty();
        message.country = userData.getCountry();
        message.postalCode = userData.getPostalCode();
        message.gender = userData.getGender();
        message.accountId = userData.getAccount().getId();

        return message;
    }

    public PasswordResetResponseDTO recover(String email) {

        Account account = findByEmail(email).orElseThrow(() -> new RuntimeException("Account not found."));
        String generatedPassword = generatePassword(8);
        Credentials credentials = new Credentials(email, cryptoUtils.encryptPassword(generatedPassword));
        account.setCredentials(credentials);
        accountRepository.save(account);
        messageHelper.send(generatePasswordResetEmail(account.getCredentials().getEmail(), generatedPassword));
        return new PasswordResetResponseDTO("Password reset correctly");
    }


    public static String generatePassword(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public EmailMessage generatePasswordResetEmail(String email, String password) {
        EmailMessage mail = new EmailMessage();
        mail.to = email;
        mail.subject = "BOK account password reset";
        mail.text = "Hello BOK user, \n " +
                "your account password has been reset, you can now login using the following credentials:\n" +
                "email: " + email + "\n" +
                "password: " + password + "\n" +
                "we suggest you change this password as soon as possible\n\n" +
                "best regards\n" +
                "the bok team";
        return mail;
    }

    public String delete(String email) {
        Account a = accountRepository.findByCredentials_Email(email).orElseThrow(()->new RuntimeException("Account not found"));
        temporaryUserRepository.deleteByAccount(a);
        accountConfirmationTokenRepository.deleteByAccount(a);
        accessInfoRepository.deleteByAccount(a);
        accountRepository.delete(a);
        return email + " deleted";
    }
}
