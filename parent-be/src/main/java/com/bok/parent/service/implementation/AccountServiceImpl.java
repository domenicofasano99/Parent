package com.bok.parent.service.implementation;

import com.bok.parent.dto.AccountRegistrationDTO;
import com.bok.parent.helper.AccountHelper;
import com.bok.parent.helper.JWTAuthenticationHelper;
import com.bok.parent.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    JWTAuthenticationHelper jwtAuthenticationHelper;

    @Autowired
    private AccountHelper accountHelper;

    @Override
    public String register(AccountRegistrationDTO accountRegistrationDTO) {
        return accountHelper.register(accountRegistrationDTO);
    }

    @Override
    public String verify(String verificationToken) {
        log.info("Verifying user with token{}", verificationToken);
        return accountHelper.verify(verificationToken);
    }
}