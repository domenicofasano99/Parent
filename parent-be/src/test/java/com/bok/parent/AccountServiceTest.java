package com.bok.parent;

import com.bok.parent.be.helper.AccountHelper;
import com.bok.parent.be.helper.MessageHelper;
import com.bok.parent.be.helper.SecurityHelper;
import com.bok.parent.be.service.AccountService;
import com.bok.parent.be.service.SecurityService;
import com.bok.parent.be.service.bank.BankService;
import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.AccountRegistrationResponseDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.PasswordResetRequestDTO;
import com.bok.parent.model.Account;
import com.bok.parent.model.Credentials;
import com.bok.parent.model.TemporaryAccount;
import com.bok.parent.repository.AccessInfoRepository;
import com.bok.parent.repository.AccountRepository;
import com.bok.parent.repository.TemporaryAccountRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
    TemporaryAccountRepository temporaryAccountRepository;

    @Autowired
    AccessInfoRepository accessInfoRepository;

    @Autowired
    MessageHelper messageHelper;

    @Autowired
    SecurityHelper securityHelper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Mock
    BankService bankService;

    @Autowired
    @InjectMocks
    AccountHelper accountHelper;

    @Autowired
    SecurityService securityService;

    @BeforeEach
    public void setup() {
        modelTestUtil.clearAll();
        when(bankService.checkCreation(anyString(), anyString(), anyBoolean())).thenReturn(true);
    }

    @Test
    public void createAccountUsingService() {
        AccountRegistrationDTO request = new AccountRegistrationDTO();
        request.birthdate = Date.from(ZonedDateTime.now().minusYears(20).toInstant());
        request.name = "Christian";
        request.middleName = "Gennaro";
        request.surname = "Faraone";
        request.fiscalCode = "FFFFFF99F99F999F";
        request.gender = "M";
        request.mobile = new AccountRegistrationDTO.MobileDTO("+39", "3334445599");
        request.address = new AccountRegistrationDTO.AddressDTO("1", "Via delle vie", "Gioia Tauro", "Reggio Calabria", "Italia", "89029");
        String email = "ciao@ciao.com";
        request.credentials = new AccountRegistrationDTO.CredentialsDTO(email);
        request.business = false;

        AccountRegistrationResponseDTO response = accountService.register(request);
        assertEquals("registered", response.status);

        TemporaryAccount ta = temporaryAccountRepository.findByEmail("ciao@ciao.com").orElseThrow(RuntimeException::new);
        assertTrue(accountService.verify(ta.getConfirmationToken()));

        Account a = accountRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        assertTrue(a.isPasswordResetNeeded());
        assertTrue(securityHelper.checkForPasswordResetNeeded(a.getId()));

        String newEncryptedPassword = passwordEncoder.encode(sha256Hex("newPassword"));
        accountHelper.setNewPassword(a, newEncryptedPassword);

        AccountLoginDTO loginRequest = new AccountLoginDTO(email, sha256Hex("newPassword"));
        LoginResponseDTO loginResponse = securityService.login(loginRequest);
        assertFalse(loginResponse.passwordResetNeeded);

        a = accountRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        assertFalse(a.isPasswordResetNeeded());

        assertFalse(securityService.passwordResetNeeded(loginResponse.token));


    }

    @Test
    public void accountWithFakeNameAttempt() {
        AccountRegistrationDTO registrationDTO = new AccountRegistrationDTO();
        registrationDTO.name = "Aless34andro";
        registrationDTO.surname = faker.name().lastName() + ".#";
        registrationDTO.birthdate = faker.date().birthday();
        registrationDTO.fiscalCode = "FFFFFF99F99F999F";
        registrationDTO.gender = faker.demographic().sex();
        registrationDTO.business = false;

        AccountRegistrationDTO.CredentialsDTO credentials = new AccountRegistrationDTO.CredentialsDTO();
        credentials.email = faker.internet().emailAddress();
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
        assertThrows(IllegalArgumentException.class, () -> accountService.register(registrationDTO));
    }

    @Test
    public void accountWithBadEmailAttempt() {
        AccountRegistrationDTO registrationDTO = new AccountRegistrationDTO();
        registrationDTO.name = faker.name().name().replace(".", "");
        registrationDTO.surname = faker.name().lastName().replace(".", "");
        registrationDTO.birthdate = faker.date().birthday();
        registrationDTO.fiscalCode = "FFFFFF99F99F999F";
        registrationDTO.gender = faker.demographic().sex();
        registrationDTO.business = false;

        registrationDTO.credentials = new AccountRegistrationDTO.CredentialsDTO();
        //fake email
        registrationDTO.credentials.email = "bad_email!ò@@aaaa";

        registrationDTO.address = new AccountRegistrationDTO.AddressDTO();
        registrationDTO.address.city = faker.address().city();
        registrationDTO.address.country = faker.address().country();
        registrationDTO.address.postalCode = faker.address().zipCode();
        registrationDTO.address.county = "county";
        registrationDTO.address.street = faker.address().streetAddress();
        registrationDTO.address.houseNumber = faker.address().buildingNumber();


        registrationDTO.mobile = new AccountRegistrationDTO.MobileDTO();
        registrationDTO.mobile.number = faker.phoneNumber().cellPhone();
        registrationDTO.mobile.icc = faker.phoneNumber().extension();

        assertThrows(IllegalArgumentException.class, () -> accountService.register(registrationDTO));
    }

    @Test
    public void resetPasswordTest() {
        Credentials credentials = modelTestUtil.createAccountWithCredentials();
        String email = credentials.getEmail();

        Account a = accountRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        PasswordResetRequestDTO requestDTO = new PasswordResetRequestDTO();
        requestDTO.email = email;
        accountService.resetPassword(requestDTO);
        Account aa = accountRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        assertNotEquals(a.getCredentials().getPassword(), aa.getCredentials().getPassword());
    }

    @Test
    public void resetPasswordTest_failNoAccount() {

        String email = "email@domain.com";
        PasswordResetRequestDTO requestDTO = new PasswordResetRequestDTO();
        requestDTO.email = email;
        assertThrows(RuntimeException.class, () -> accountService.resetPassword(requestDTO));
    }
}
