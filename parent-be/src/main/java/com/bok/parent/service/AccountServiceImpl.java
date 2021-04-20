package com.bok.parent.service;

import com.bok.parent.dto.RegisterAccount;
import com.bok.parent.helper.AccountHelper;
import com.bok.parent.model.Account;
import com.bok.parent.utils.JWTAuthenticationHelper;
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
    public Account register(RegisterAccount registerAccount) {
        return accountHelper.register(registerAccount);
    }

    @Override
    public String confirm(String confirmationToken) {
        return accountHelper.confirmAccount(confirmationToken);
    }
}