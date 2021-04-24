package com.bok.parent;

import com.bok.parent.dto.AccountRegistrationDTO;
import com.bok.parent.helper.AccountHelper;
import com.bok.parent.model.Account;
import com.bok.parent.repository.AccountConfirmationTokenRepository;
import com.bok.parent.repository.AccountRepository;
import com.bok.parent.repository.AuditLogRepository;
import com.bok.parent.repository.TemporaryUserRepository;
import com.bok.parent.service.AccountService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    AccountConfirmationTokenRepository accountConfirmationTokenRepository;

    @Autowired
    TemporaryUserRepository temporaryUserRepository;

    public Account enableAccount(Account account) {
        account.setEnabled(true);
        return accountRepository.save(account);
    }

    public AccountDetails createAccountWithCredentials() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        AccountRegistrationDTO registrationDTO = new AccountRegistrationDTO();
        registrationDTO.name = faker.name().name();
        registrationDTO.surname = faker.name().lastName();
        registrationDTO.birthdate = faker.date().birthday();
        registrationDTO.email = email;
        registrationDTO.password = password;
        accountService.register(registrationDTO);
        Account account = accountRepository.findByEmail(registrationDTO.email).orElseThrow(RuntimeException::new);
        enableAccount(account);
        return new AccountDetails(email, password);
    }

    public void clearAll() {
        auditLogRepository.deleteAll();
        accountConfirmationTokenRepository.deleteAll();
        temporaryUserRepository.deleteAll();
        accountRepository.deleteAll();
    }
}
