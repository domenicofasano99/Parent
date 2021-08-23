package com.bok.parent.be.helper;

import com.bok.bank.integration.grpc.BankGrpc;
import com.bok.parent.be.exception.AccountException;
import com.bok.parent.be.exception.EmailAlreadyExistsException;
import com.bok.parent.be.service.bank.BankService;
import com.bok.parent.be.utils.ValidationUtils;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.AccountRegistrationResponseDTO;
import com.bok.parent.integration.dto.PasswordResetResponseDTO;
import com.bok.parent.integration.message.AccountCreationMessage;
import com.bok.parent.integration.message.AccountDeletionMessage;
import com.bok.parent.integration.message.EmailMessage;
import com.bok.parent.model.Account;
import com.bok.parent.model.Credentials;
import com.bok.parent.model.TemporaryAccount;
import com.bok.parent.model.Token;
import com.bok.parent.repository.AccessInfoRepository;
import com.bok.parent.repository.AccountRepository;
import com.bok.parent.repository.TemporaryAccountRepository;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Component
@Slf4j
public class AccountHelper {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TemporaryAccountRepository temporaryAccountRepository;
    @Autowired
    MessageHelper messageHelper;
    @Autowired
    ValidationUtils validationUtils;
    @Autowired
    AccessInfoRepository accessInfoRepository;
    @GrpcClient("bank")
    BankGrpc.BankBlockingStub bankBlockingStub;
    @GrpcClient("bank")
    BankGrpc.BankFutureStub bankFutureStub;
    @Value("${server.baseUrl}")
    String baseUrl;
    @Autowired
    BankService bankService;

    public static String generatePassword(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public Long getAccountIdByEmail(String email) {
        return accountRepository.findAccountIdByEmail(email);
    }

    @Transactional
    public AccountRegistrationResponseDTO register(AccountRegistrationDTO request) {

        if (!bankService.checkCreation(request.fiscalCode, request.vatNumber, request.business)) {
            throw new RuntimeException("Found an account with same fiscalCode or vatNumber in bank system");
        }

        if (accountRepository.existsByCredentials_Email(request.credentials.email)) {
            throw new EmailAlreadyExistsException("Account already registered.");
        }
        if (temporaryAccountRepository.existsByEmail(request.credentials.email)) {
            throw new RuntimeException("An account request is pending for this email, check your inbox!");
        }

        TemporaryAccount temporaryAccount = new TemporaryAccount(request.name,
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
                request.gender);


        temporaryAccount = saveOrUpdate(temporaryAccount);
        sendTemporaryAccountConfirmationEmail(temporaryAccount);
        log.info("User {} registered", request.credentials.email);
        return new AccountRegistrationResponseDTO("registered");
    }

    private void sendTemporaryAccountConfirmationEmail(TemporaryAccount temporaryAccount) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.to = temporaryAccount.getEmail();
        emailMessage.subject = "BOK Account Verification";
        emailMessage.body = "Click on the link to verify your BOK account: \n" +
                baseUrl + "/verify/" + temporaryAccount.getConfirmationToken() +
                "\nThis link will expire in 24 hours; after that you will have to create another account from scratch." +
                "\nAfter confirming the account you will receive an email with a temporary password that we suggest you should change at your first log in" +
                "\n\nThe BOK Team.";

        messageHelper.send(emailMessage);
    }

    public Account saveOrUpdate(Account account) {
        return accountRepository.save(account);
    }

    public TemporaryAccount saveOrUpdate(TemporaryAccount temporaryAccount) {
        return temporaryAccountRepository.save(temporaryAccount);
    }

    @Cacheable("email_accounts")
    public Account findByEmail(String email) {
        return accountRepository.findByCredentials_Email(email).orElseThrow(() -> new AccountException("Account not found or not verified"));
    }

    private void notifyServices(TemporaryAccount temporaryAccount, Long accountId) {
        log.info("Notifying services about the account {} creation", temporaryAccount);
        messageHelper.send(generateAccountCreationMessage(temporaryAccount, accountId));
        temporaryAccountRepository.delete(temporaryAccount);
    }

    @Transactional
    public Boolean verify(String confirmationToken) throws RuntimeException {
        log.info("Verifying account with confirmation token: {}", confirmationToken);
        TemporaryAccount ta = temporaryAccountRepository.findByConfirmationToken(confirmationToken)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Account account = new Account();
        String generatePassword = generatePassword(10);
        account.setCredentials(new Credentials(ta.getEmail(), passwordEncoder.encode(sha256Hex(generatePassword)), true));
        account.setRole(Account.Role.USER);
        account = accountRepository.save(account);

        log.info("Account {} successfully verified!", account.getCredentials().getEmail());
        notifyServices(ta, account.getId());
        sendWelcomeEmail(account.getCredentials().getEmail(), ta.getName(), generatePassword);
        return Boolean.TRUE;
    }

    private AccountCreationMessage generateAccountCreationMessage(TemporaryAccount userData, Long accountId) {
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
        message.accountId = accountId;

        return message;
    }

    public PasswordResetResponseDTO recover(String email) {

        Account account = findByEmail(email);
        String generatedPassword = generatePassword(8);
        Credentials credentials = new Credentials(email, passwordEncoder.encode(sha256Hex(generatedPassword)), true);
        account.setCredentials(credentials);
        accountRepository.save(account);
        messageHelper.send(generatePasswordResetEmail(account.getCredentials().getEmail(), generatedPassword));
        return new PasswordResetResponseDTO("Password reset correctly");
    }

    public EmailMessage generatePasswordResetEmail(String email, String password) {
        EmailMessage mail = new EmailMessage();
        mail.to = email;
        mail.subject = "BOK account password reset";
        mail.body = "Hello BOK user, \n " +
                "your account password has been reset, you can now login using the following credentials:\n" +
                "email: " + email + "\n" +
                "password: " + password + "\n" +
                "at the first login you will be asked to reset this password\n\n" +
                "best regards\n" +
                "the bok team";
        return mail;
    }

    private void sendWelcomeEmail(String email, String firstName, String generatedPlainPassword) {
        EmailMessage mail = new EmailMessage();
        mail.to = email;
        mail.subject = firstName + " welcome to BOK!";
        mail.body = "Hello " + firstName + ", \n " +
                "Thanks for verifying your account, you can now login using the following credentials:\n" +
                "email: " + email + "\n" +
                "password: " + generatedPlainPassword + "\n" +
                "at the first login you will be asked to reset this password\n\n" +
                "Best regards\n" +
                "The bok team";
        messageHelper.send(mail);
    }

    public String delete(String email) {
        Account a = accountRepository.findByCredentials_Email(email).orElseThrow(() -> new RuntimeException("Account not found"));
        temporaryAccountRepository.deleteByEmail(email);
        accessInfoRepository.deleteByAccount(a);
        accountRepository.delete(a);
        messageHelper.send(new AccountDeletionMessage(a.getId()));

        EmailMessage deletionEmail = new EmailMessage();
        deletionEmail.to = email;
        deletionEmail.body = "You account has been successfully deleted.";
        deletionEmail.subject = "Account deletion request";
        messageHelper.send(deletionEmail);

        return email + " deleted";
    }

    @Cacheable("id_account")
    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountException("Account not found"));
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void deleteUnconfirmedAccounts() {
        List<TemporaryAccount> temporaryAccounts = temporaryAccountRepository.findAll();
        List<TemporaryAccount> toDelete = new ArrayList<>();
        for (TemporaryAccount ta : temporaryAccounts) {
            if (ta.getCreationTimestamp().isBefore(ta.getCreationTimestamp().minus(1, ChronoUnit.DAYS))) {
                toDelete.add(ta);
            }
        }
        temporaryAccountRepository.deleteAll(toDelete);
        log.info("Deleted {} unconfirmed accounts", toDelete.size());
    }

    public boolean setNewPassword(Account account, String newPassword) {
        try {
            String email = account.getCredentials().getEmail();
            Credentials newCredentials = new Credentials(email, newPassword, false);
            account.setCredentials(newCredentials);
            accountRepository.save(account);
            evictSingleCredentialCache(email);
            return true;
        } catch (Exception e) {
            log.error("An exception occurred while setting new password for account {}", account.getId());
        }
        return false;
    }

    //@CacheEvict(value = "email_credentials", key = "#email")
    public void evictSingleCredentialCache(String email) {
    }


    public void addTokenToAccount(Account account, Token token) {
        account.addToken(token);
        accountRepository.save(account);
    }


    //@Cacheable(value = "email_credentials", key = "#email")
    public Credentials getCredentialsByEmail(String email) throws AccountNotFoundException {
        return accountRepository.findByCredentials_Email(email).orElseThrow(AccountNotFoundException::new).getCredentials();
    }
}
