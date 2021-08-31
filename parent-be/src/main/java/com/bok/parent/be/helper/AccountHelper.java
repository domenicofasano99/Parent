package com.bok.parent.be.helper;

import com.bok.parent.be.exception.AccountException;
import com.bok.parent.be.exception.EmailAlreadyExistsException;
import com.bok.parent.be.service.bank.BankService;
import com.bok.parent.be.utils.ValidationUtils;
import com.bok.parent.integration.dto.AccountClosureDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.AccountRegistrationResponseDTO;
import com.bok.parent.integration.dto.PasswordResetResponseDTO;
import com.bok.parent.integration.message.AccountClosureMessage;
import com.bok.parent.integration.message.AccountCreationMessage;
import com.bok.parent.integration.message.EmailMessage;
import com.bok.parent.model.Account;
import com.bok.parent.model.Credentials;
import com.bok.parent.model.TemporaryAccount;
import com.bok.parent.model.Token;
import com.bok.parent.repository.AccessInfoRepository;
import com.bok.parent.repository.AccountRepository;
import com.bok.parent.repository.TemporaryAccountRepository;
import com.bok.parent.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    TokenRepository tokenRepository;

    @Value("${server.baseUrl}")
    String baseUrl;

    @Autowired
    BankService bankService;

    public static String generatePassword() {
        int len = 10;
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
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
                "https://api.bok.faraone.ovh/verify/" + temporaryAccount.getConfirmationToken() +
                "\nThis link will expire in 24 hours; after that you will have to create another account from scratch." +
                "\nAfter confirming the account you will receive an email with a temporary password that we suggest you should change at your first log in" +
                "\n\nThe BOK Team.";

        messageHelper.send(emailMessage);
    }

    public Account saveOrUpdate(Account account) {
        Account a = accountRepository.save(account);
        return a;
    }

    public TemporaryAccount saveOrUpdate(TemporaryAccount temporaryAccount) {
        return temporaryAccountRepository.save(temporaryAccount);
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new AccountException("Account not found or not verified"));
    }

    private void notifyServices(TemporaryAccount temporaryAccount, Long accountId) {
        log.info("Notifying services about the account {} creation", temporaryAccount);
        messageHelper.send(generateAccountCreationMessage(temporaryAccount, accountId));
        temporaryAccountRepository.delete(temporaryAccount);
    }

    /**
     * Method caled when the user clicks on the verification link
     *
     * @param confirmationToken from the email
     * @return
     * @throws RuntimeException
     */
    @Transactional
    public Boolean verifyAccount(String confirmationToken) throws RuntimeException {
        log.info("Verifying account with confirmation token: {}", confirmationToken);
        TemporaryAccount ta = temporaryAccountRepository.findByConfirmationToken(confirmationToken)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Account account = new Account();
        String generatePassword = generatePassword();
        account.setCredentials(new Credentials(ta.getEmail(), passwordEncoder.encode(sha256Hex(generatePassword))));
        account.setPasswordResetNeeded(true);
        account.setRole(Account.Role.USER);
        account = accountRepository.saveAndFlush(account);

        log.info("Account {} successfully verified!", account.getCredentials().getEmail());
        notifyServices(ta, account.getId());
        sendWelcomeEmail(account.getCredentials().getEmail(), ta.getName(), generatePassword);
        temporaryAccountRepository.deleteByEmail(ta.getEmail());
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

    /**
     * This method is used to recover a forgot password. A new password is generated and sent via email to the user.
     * The password sent is temporary and needs to be changed asap
     *
     * @param email of the user
     * @return the DTO in response containing a message for the user
     */
    public PasswordResetResponseDTO recover(String email) {
        log.info("user {} is recovering his password", email);
        Account account = findByEmail(email);
        String generatedPassword = generatePassword();
        Credentials credentials = new Credentials(email, passwordEncoder.encode(sha256Hex(generatedPassword)));
        account.setCredentials(credentials);
        account.setPasswordResetNeeded(true);
        accountRepository.saveAndFlush(account);
        messageHelper.send(generatePasswordResetEmail(account.getCredentials().getEmail(), generatedPassword));
        return new PasswordResetResponseDTO("Password reset correctly");
    }

    /**
     * Generates the email for the password reset
     *
     * @param email    of the user
     * @param password (temporary)
     * @return the prepared EmailMessae, ready to be sent to the broker
     */
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

    /**
     * This method send the welcome email to the user, it is triggered after the email verification, containing the username and the temporary password
     *
     * @param email                  of the user
     * @param firstName              of the user (entered in the subject and the body to avoid spam filters)
     * @param generatedPlainPassword for the first login
     */
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

    /**
     * Method used to perform the account closure, it sends 2 messages to both krypto and bank to trigger the account deletion on those systems too
     *
     * @param accountClosureDTO containing email and IBAN for the funds to be sent before account deletion
     * @return a message to be shown to the user in the business console
     */
    public String closeAccount(AccountClosureDTO accountClosureDTO) {
        String email = accountClosureDTO.email;
        String iban = accountClosureDTO.IBAN;
        Account a = accountRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Account not found"));
        tokenRepository.deleteAllByAccountId(a.getId());
        accessInfoRepository.deleteByAccount(a);
        accountRepository.delete(a);

        messageHelper.send(new AccountClosureMessage(a.getId(), iban));

        EmailMessage deletionEmail = new EmailMessage();
        deletionEmail.to = email;
        deletionEmail.body = "You account has been successfully closed.";
        deletionEmail.subject = "Account closure request.";
        messageHelper.send(deletionEmail);

        return email + " deleted";
    }

    /**
     * Find the given account by using the email to qury the db or, if not found, throws an AccountException
     *
     * @param accountId of the account to be retrieved
     * @return the retrieved account
     */
    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountException("Account not found"));
    }

    /**
     * This method is triggered every 5 minutes, it checks if there is any unconfirmed account older than 24 hours and if so
     * it deletes that
     */
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

    /**
     * This methods sets a new password for a given Account
     *
     * @param account     where to set the new password
     * @param newPassword already encoded password
     * @return true if everything went good or false if there was an error
     */
    public boolean setNewPassword(Account account, String newPassword) {
        try {
            String email = account.getCredentials().getEmail();
            Credentials newCredentials = new Credentials(email, newPassword);
            account.setCredentials(newCredentials);
            account.setPasswordResetNeeded(false);
            accountRepository.saveAndFlush(account);
            return true;
        } catch (Exception e) {
            log.error("An exception occurred while setting new password for account {}", account.getId());
        }
        return false;
    }


    /**
     * Adds a token to a given account
     *
     * @param account ok
     * @param token
     */
    public void addTokenToAccount(Account account, Token token) {
        account.addToken(token);
        accountRepository.save(account);
    }


    /**
     * Returns, given the email, the corresponding Credentials
     *
     * @param email of the account whose credentials are needed
     * @return Credentials object
     * @throws AccountNotFoundException if no Account is found
     */
    public Credentials getCredentialsByEmail(String email) throws AccountNotFoundException {
        return accountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new).getCredentials();
    }
}
