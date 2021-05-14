package com.bok.parent;

import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
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
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class ParentApplicationTests {

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

        AccountRegistrationDTO registrationDTO = modelTestUtil.createRegistrationDTO();

        registrationDTO.name = faker.name().name();
        registrationDTO.surname = faker.name().lastName();
        registrationDTO.birthdate = faker.date().birthday();
        registrationDTO.credentials.email = faker.internet().emailAddress();
        registrationDTO.credentials.password = faker.internet().password();
        accountService.register(registrationDTO);

        Account account = accountRepository.findByEmail(registrationDTO.credentials.email).orElseThrow(RuntimeException::new);
        assertNotNull(account);
        assertEquals(account.getEmail(), registrationDTO.credentials.email);
        assertFalse(account.getEnabled());
    }

    @Test
    public void accountCreation_enabledTest() {

        AccountRegistrationDTO registrationDTO = modelTestUtil.createRegistrationDTO();

        AccountRegistrationDTO.CredentialsDTO credentials = new AccountRegistrationDTO.CredentialsDTO();
        credentials.email = faker.internet().emailAddress();
        credentials.password = faker.internet().password();

        registrationDTO.credentials = credentials;
        accountService.register(registrationDTO);

        Account account = accountRepository.findByEmail(registrationDTO.credentials.email).orElseThrow(RuntimeException::new);
        modelTestUtil.enableAccount(account);
        assertNotNull(account);
        assertEquals(account.getEmail(), registrationDTO.credentials.email);
        assertTrue(account.getEnabled());

    }

    @Test
    public void successfulLoginTest() {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = account.email;
        loginDTO.password = account.password;
        LoginResponseDTO login = securityService.login(loginDTO);
        log.debug(login.token);
        assertNotNull(login.token);
    }

    @Test
    public void failedLoginTest() {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = account.email;
        loginDTO.password = "wrongpassword";
        assertThrows(WrongCredentialsException.class, () -> securityService.login(loginDTO));
    }

    @Test
    public void loginToUnverifiedAccountTest() {
        AccountRegistrationDTO.CredentialsDTO credentials = modelTestUtil.createAccountWithCredentials();
        Account account = accountRepository.findByEmail(credentials.email).orElseThrow(RuntimeException::new);
        account.setEnabled(false);
        accountRepository.save(account);

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = credentials.email;
        loginDTO.password = "wrongpassword";
        assertThrows(WrongCredentialsException.class, () -> securityService.login(loginDTO));
    }

    @Test
    public void accountWithFakeNameAttempt() {
        AccountRegistrationDTO registrationDTO = modelTestUtil.createRegistrationDTO();
        registrationDTO.name = "Aless34andro";
        registrationDTO.surname = faker.name().lastName() + ".#";
        assertThrows(IllegalArgumentException.class, () -> accountService.register(registrationDTO));
    }

    @Test
    public void accountWithBadEmailAttempt() {
        AccountRegistrationDTO registrationDTO = modelTestUtil.createRegistrationDTO();
        registrationDTO.credentials.email = "bad_email!ò@@aaaa";
        registrationDTO.credentials.password = faker.internet().password();
        assertThrows(IllegalArgumentException.class, () -> accountService.register(registrationDTO));
    }

}
