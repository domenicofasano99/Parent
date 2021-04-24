package com.bok.parent;

import com.bok.parent.dto.AccountRegistrationDTO;
import com.bok.parent.model.Account;
import com.bok.parent.repository.AccountRepository;
import com.bok.parent.service.AccountService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest
class ParentApplicationTests {
    public static final Faker faker = new Faker();

    @Autowired
    ModelTestUtil modelTestUtil;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void contextLoads() {
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

}
