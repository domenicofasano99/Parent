package com.bok.parent;

import com.bok.parent.be.exception.AccountException;
import com.bok.parent.be.exception.InvalidCredentialsException;
import com.bok.parent.be.helper.TokenHelper;
import com.bok.parent.be.service.AccountService;
import com.bok.parent.be.service.SecurityService;
import com.bok.parent.be.utils.ValidationUtils;
import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.LastAccessInfoDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.TokenExpirationRequestDTO;
import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import com.bok.parent.model.Account;
import com.bok.parent.model.Token;
import com.bok.parent.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
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
import java.util.Random;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class SecurityServiceTest {

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
        Assertions.assertNotNull(login.token);
    }

    @Test
    public void failedLoginTest() {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = account.email;
        loginDTO.password = "wrongpassword";
        assertThrows(InvalidCredentialsException.class, () -> securityService.login(loginDTO));
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
        Assertions.assertNotNull(loginResponse.token);


        TokenExpirationRequestDTO requestDTO = new TokenExpirationRequestDTO();
        requestDTO.token = loginResponse.token;
        TokenInfoResponseDTO response = securityService.tokenInfo(loginResponse.token);
        Assertions.assertTrue(response.expirationDate.isAfter(Instant.now()));

    }

    @Test
    public void testLogout() {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = account.email;
        loginDTO.password = account.password;
        LoginResponseDTO loginResponse = securityService.login(loginDTO);
        log.debug(loginResponse.token);
        Assertions.assertNotNull(loginResponse.token);


        securityService.logout(loginResponse.token);
        Token token = tokenHelper.findByTokenString(loginResponse.token);
        Assertions.assertTrue(token.getExpired());
    }

    @Test
    public void testDifferentTokensForDifferentLogins() {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();

        AccountLoginDTO loginDTO = new AccountLoginDTO();
        loginDTO.email = account.email;
        loginDTO.password = account.password;

        LoginResponseDTO loginResponse = securityService.login(loginDTO);
        Assertions.assertNotNull(loginResponse.token);

        Token firstToken = tokenHelper.getActiveToken(loginDTO.email).orElseThrow(RuntimeException::new);
        firstToken.setExpired(true);
        tokenHelper.saveToken(firstToken);

        loginResponse = securityService.login(loginDTO);
        Token secondToken = tokenHelper.getActiveToken(loginDTO.email).orElseThrow(RuntimeException::new);

        Assertions.assertNotEquals(firstToken.getTokenString(), secondToken.getTokenString());

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
        Assertions.assertNotEquals(firstToken, secondToken);
        Assertions.assertNotEquals(firstToken, thirdToken);
    }


    @Test
    public void testLastAccessInfo() {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();
        AccountLoginDTO loginDTO = new AccountLoginDTO(account.email, account.password);

        String token = securityService.login(loginDTO).getToken();

        LastAccessInfoDTO lastAccessInfo = securityService.lastAccessInfo(token);
        assertThat(lastAccessInfo.lastAccessIP, is(""));
        Assertions.assertTrue(lastAccessInfo.lastAccessDateTime.isBefore(LocalDateTime.now()));
    }

    @Test
    public void testMultipleLogins() throws InterruptedException {
        AccountRegistrationDTO.CredentialsDTO account = modelTestUtil.createAccountWithCredentials();
        AccountLoginDTO loginDTO = new AccountLoginDTO(account.email, account.password);

        Random random = new Random();
        List<String> tokenList = new ArrayList<>();
        for (int c = 0; c < 100; c++) {
            String token = securityService.login(loginDTO).token;
            Thread.sleep(random.nextInt(100));
            assertThat(tokenList, not(containsInAnyOrder(Collections.singletonList(token))));
            tokenList.add(token);
        }
    }

    @Test
    public void passwordHashTest() {
        String password = "password";
        String hashedPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
        String sha256hex = sha256Hex(password);
        Assertions.assertEquals(hashedPassword, sha256hex);
    }

}
