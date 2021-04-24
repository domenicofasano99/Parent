package com.bok.parent;

import com.bok.parent.dto.AccountLoginDTO;
import com.bok.parent.dto.AccountRegistrationDTO;
import com.bok.parent.exception.WrongCredentialsException;
import com.bok.parent.model.Account;
import com.bok.parent.repository.AccountRepository;
import com.bok.parent.service.AccountService;
import com.bok.parent.service.SecurityService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@Slf4j
class ParentApplicationTests {
    public static final Faker faker = new Faker();

    @Autowired
    ModelTestUtil modelTestUtil;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SecurityService securityService;

    @Test
    void contextLoads() {
    }

    @Before
    public void clearAll() {
        modelTestUtil.clearAll();
    }


    @Test
    public void accountCreation_butNotEnabledTest() {

        AccountRegistrationDTO registrationDTO = new AccountRegistrationDTO();

        registrationDTO.name = faker.name().name();
        registrationDTO.surname = faker.name().lastName();
        registrationDTO.birthdate = faker.date().birthday();
        registrationDTO.email = faker.internet().emailAddress();
        registrationDTO.password = faker.internet().password();
        accountService.register(registrationDTO);

        Account account = accountRepository.findByEmail(registrationDTO.email).orElseThrow(RuntimeException::new);
        assertNotNull(account);
        assertEquals(account.getEmail(), registrationDTO.email);
        assertFalse(account.getEnabled());
    }

    @Test
    public void accountCreation_enabledTest() {
        AccountRegistrationDTO registrationDTO = new AccountRegistrationDTO();

        registrationDTO.name = faker.name().name();
        registrationDTO.surname = faker.name().lastName();
        registrationDTO.birthdate = faker.date().birthday();
        registrationDTO.email = faker.internet().emailAddress();
        registrationDTO.password = faker.internet().password();
        accountService.register(registrationDTO);

        Account account = accountRepository.findByEmail(registrationDTO.email).orElseThrow(RuntimeException::new);
        modelTestUtil.enableAccount(account);
        assertNotNull(account);
        assertEquals(account.getEmail(), registrationDTO.email);
        assertTrue(account.getEnabled());

    }

    @Test
    public void successfulLoginTest() {
        AccountDetails account = modelTestUtil.createAccountWithCredentials();

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = account.email;
        loginDTO.password = account.password;
        String token = securityService.login(loginDTO);
        log.debug(token);
        assertNotNull(token);
    }

    @Test
    public void failedLoginTest() {
        AccountDetails account = modelTestUtil.createAccountWithCredentials();

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = account.email;
        loginDTO.password = "wrongpassword";
        assertThrows(WrongCredentialsException.class, () -> securityService.login(loginDTO));
    }

    @Test
    public void loginToUnverifiedAccountTest() {
        AccountDetails credentials = modelTestUtil.createAccountWithCredentials();
        Account account = accountRepository.findByEmail(credentials.email).orElseThrow(RuntimeException::new);
        account.setEnabled(false);
        accountRepository.save(account);

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = credentials.email;
        loginDTO.password = "wrongpassword";
        assertThrows(WrongCredentialsException.class, () -> securityService.login(loginDTO));
    }

}
