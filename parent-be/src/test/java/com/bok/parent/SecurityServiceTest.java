package com.bok.parent;

import com.bok.parent.exception.TokenAuthenticationException;
import com.bok.parent.exception.WrongCredentialsException;
import com.bok.parent.helper.TokenHelper;
import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.LogoutResponseDTO;
import com.bok.parent.integration.dto.TokenExpirationRequestDTO;
import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import com.bok.parent.model.Account;
import com.bok.parent.model.Token;
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

import java.time.Instant;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class SecurityServiceTest {

    public static final Faker faker = new Faker();

    @Autowired
    ModelTestUtil modelTestUtil;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SecurityService securityService;

    @Autowired
    ValidationUtils validationUtils;

    @Autowired
    TokenHelper tokenHelper;

    @Before
    public void setup() {
        modelTestUtil.clearAll();
        Mockito.when(ValidationUtils.validateEmail(anyString())).thenReturn(true);
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
        Account account = accountRepository.findByCredentials_Email(credentials.email).orElseThrow(RuntimeException::new);
        account.setEnabled(false);
        accountRepository.save(account);

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = credentials.email;
        loginDTO.password = "wrongpassword";
        assertThrows(WrongCredentialsException.class, () -> securityService.login(loginDTO));
    }


    @Test
    public void checkTokenInfoTest() {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = account.email;
        loginDTO.password = account.password;
        LoginResponseDTO loginResponse = securityService.login(loginDTO);
        log.debug(loginResponse.token);
        assertNotNull(loginResponse.token);


        TokenExpirationRequestDTO requestDTO = new TokenExpirationRequestDTO();
        requestDTO.token = loginResponse.token;
        TokenInfoResponseDTO response = securityService.tokenInfo(loginResponse.token);
        assertTrue(response.expirationDate.isAfter(Instant.now()));

    }

    @Test
    public void testLogout(){
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = account.email;
        loginDTO.password = account.password;
        LoginResponseDTO loginResponse = securityService.login(loginDTO);
        log.debug(loginResponse.token);
        assertNotNull(loginResponse.token);


        securityService.logout(loginResponse.token);
        Token token = tokenHelper.findByTokenString(loginResponse.token);
        assertTrue(token.expired);
    }

}
