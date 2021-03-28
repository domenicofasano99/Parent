package com.bok.parent.controller;

import com.bok.parent.dto.LoginAccount;
import com.bok.parent.dto.RegisterAccount;
import com.bok.parent.model.Account;
import com.bok.parent.service.SecurityService;
import com.bok.parent.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    AccountService accountService;

    @Autowired
    SecurityService securityService;

    @PostMapping("/login")
    public Object login(@RequestBody LoginAccount user) {
        return securityService.login(user);
    }


    @PostMapping("/register")
    public Account register(@RequestBody RegisterAccount registerAccount) {
        return accountService.register(registerAccount);
    }
}
