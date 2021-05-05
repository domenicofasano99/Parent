package com.bok.parent.helper;

import com.bok.integration.AccountCreationMessage;
import com.bok.integration.EmailMessage;
import com.bok.integration.parent.dto.AccountRegistrationDTO;
import com.bok.parent.exception.EmailAlreadyExistsException;
import com.bok.parent.model.Account;
import com.bok.parent.model.AccountTemporaryDetails;
import com.bok.parent.model.ConfirmationToken;
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

        if (accountRepository.existsByEmail(request.credentials.email)) {
            throw new EmailAlreadyExistsException("Account already registered.");
        }
        Account account = new Account();
        account.setEmail(request.credentials.email);
        account.setPassword(cryptoUtils.encryptPassword(request.credentials.password));
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
    public void saveAccountInformations(AccountTemporaryDetails accountTemporaryDetails) {
        temporaryUserRepository.save(accountTemporaryDetails);
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
        AccountTemporaryDetails accountTemporaryDetails = temporaryUserRepository.findByAccount(account).orElseThrow(() -> new RuntimeException("Couldn't find account " + account.getId()));
        messageHelper.send(generateAccountCreationMessage(accountTemporaryDetails));
        temporaryUserRepository.deleteByAccount_Id(account.getId());
    }

    @Transactional
    public String verify(String accountConfirmationToken) {
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
        notifyServices(account);

        return "Your account has been confirmed, you can now login to the user area.";
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
}
