package com.bok.parent;

import com.bok.parent.exception.AccountException;
import com.bok.parent.exception.WrongCredentialsException;
import com.bok.parent.helper.TokenHelper;
import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.LastAccessInfoDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotEquals;
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
        assertThrows(AccountException.class, () -> securityService.login(loginDTO));
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
    public void testLogout() {
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

    @Test
    public void testDifferentTokensForDifferentLogins() {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = account.email;
        loginDTO.password = account.password;

        LoginResponseDTO loginResponse = securityService.login(loginDTO);
        assertNotNull(loginResponse.token);

        Token firstToken = tokenHelper.getActiveToken(loginDTO.email).orElseThrow(RuntimeException::new);
        firstToken.setExpired(true);
        tokenHelper.saveToken(firstToken);

        loginResponse = securityService.login(loginDTO);
        Token secondToken = tokenHelper.getActiveToken(loginDTO.email).orElseThrow(RuntimeException::new);

        assertNotEquals(firstToken.tokenString, secondToken.tokenString);

    }

    @Test
    public void testMultipleTokensAreDifferent() throws InterruptedException {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();
        AccountLoginDTO loginDTO = new AccountLoginDTO(account.email, account.password);

        //first login
        String firstToken = securityService.login(loginDTO).getToken();
        //wait some time before another login
        Thread.sleep(500);
        //second login
        String secondToken = securityService.login(loginDTO).getToken();
        //wait some time before another login
        Thread.sleep(500);
        //third login
        String thirdToken = securityService.login(loginDTO).getToken();
        assertNotEquals(firstToken, secondToken);
        assertNotEquals(firstToken, thirdToken);
    }


    @Test
    public void testLastAccessInfo() {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();
        AccountLoginDTO loginDTO = new AccountLoginDTO(account.email, account.password);

        String token = securityService.login(loginDTO).getToken();

        LastAccessInfoDTO lastAccessInfo = securityService.lastAccessInfo(token);
        assertThat(lastAccessInfo.lastAccessIP, is(""));
        assertTrue(lastAccessInfo.lastAccessDateTime.isBefore(LocalDateTime.now()));
    }

    @Test
    public void testMultipleLogins() {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();
        AccountLoginDTO loginDTO = new AccountLoginDTO(account.email, account.password);

        List<String> tokenList = new ArrayList<>();
        for (int c = 0; c < 100; c++) {
            String token = securityService.login(loginDTO).token;
            assertThat(tokenList, not(containsInAnyOrder(Collections.singletonList(token))));
            tokenList.add(token);
        }

    }

}
