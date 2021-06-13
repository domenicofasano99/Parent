package com.bok.parent;

import com.bok.parent.be.helper.AccountHelper;
import com.bok.parent.be.service.AccountService;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.model.Account;
import com.bok.parent.repository.AccountRepository;
import com.bok.parent.repository.AccountTemporaryDetailsRepository;
import com.bok.parent.repository.AuditLogRepository;
import com.bok.parent.repository.ConfirmationTokenRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Component
public class ModelTestUtil {

    public static final Faker faker = new Faker();

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    AccountService accountService;

    @Autowired
    AuditLogRepository auditLogRepository;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    AccountTemporaryDetailsRepository accountTemporaryDetailsRepository;

    public Account enableAccount(Account account) {
        account.setEnabled(true);
        return accountRepository.save(account);
    }

    public AccountRegistrationDTO createRegistrationDTO() {
        AccountRegistrationDTO registrationDTO = new AccountRegistrationDTO();
        registrationDTO.name = faker.name().name().replace(".", "");
        registrationDTO.surname = faker.name().lastName().replace(".", "");
        registrationDTO.birthdate = faker.date().birthday();
        registrationDTO.fiscalCode = "FFFFFF99F99F999F";
        registrationDTO.gender = faker.demographic().sex();
        registrationDTO.business = false;

        AccountRegistrationDTO.CredentialsDTO credentials = new AccountRegistrationDTO.CredentialsDTO();
        credentials.email = faker.internet().emailAddress();
        credentials.password = sha256Hex(faker.internet().password());
        registrationDTO.credentials = credentials;

        AccountRegistrationDTO.AddressDTO address = new AccountRegistrationDTO.AddressDTO();
        address.city = faker.address().city();
        address.country = faker.address().country();
        address.postalCode = faker.address().zipCode();
        address.county = "county";
        address.street = faker.address().streetAddress();
        address.houseNumber = faker.address().buildingNumber();
        registrationDTO.address = address;

        AccountRegistrationDTO.MobileDTO mobile = new AccountRegistrationDTO.MobileDTO();
        mobile.number = faker.phoneNumber().cellPhone();
        mobile.icc = faker.phoneNumber().extension();
        registrationDTO.mobile = mobile;

        return registrationDTO;
    }

    public AccountRegistrationDTO.CredentialsDTO createAccountWithCredentials() {

        AccountRegistrationDTO registrationDTO = createRegistrationDTO();

        accountService.register(registrationDTO);
        Account account = accountRepository.findByCredentials_Email(registrationDTO.credentials.email).orElseThrow(RuntimeException::new);
        enableAccount(account);
        return registrationDTO.credentials;
    }

    public void clearAll() {
        auditLogRepository.deleteAll();
        confirmationTokenRepository.deleteAll();
        accountTemporaryDetailsRepository.deleteAll();
        accountRepository.deleteAll();
    }
}
