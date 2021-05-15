package com.bok.parent;

import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.PasswordResetRequestDTO;
import com.bok.parent.model.Account;
import com.bok.parent.repository.AccountRepository;
import com.bok.parent.service.AccountService;
import com.bok.parent.service.SecurityService;
import com.bok.parent.utils.ValidationUtils;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static reactor.core.publisher.Mono.when;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class AccountServiceTest {

    public static final Faker faker = new Faker();

    @Autowired
    ModelTestUtil modelTestUtil;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SecurityService securityService;

    @Before
    public void setup() {
        modelTestUtil.clearAll();
        Mockito.when(ValidationUtils.validateEmail(anyString())).thenReturn(true);
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

    @Test
    public void resetPasswordTest() {
        AccountRegistrationDTO.CredentialsDTO credentials = modelTestUtil.createAccountWithCredentials();
        String email = credentials.email;

        Account a = accountRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        PasswordResetRequestDTO requestDTO = new PasswordResetRequestDTO();
        requestDTO.email = email;
        accountService.resetPassword(requestDTO);
        Account aa = accountRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        assertNotEquals(a.getPassword(), aa.getPassword());
    }
}
