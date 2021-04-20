package com.bok.parent.controller;

import com.bok.parent.dto.AccountLoginDTO;
import com.bok.parent.dto.AccountRegistrationDTO;
import com.bok.parent.service.AccountService;
import com.bok.parent.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    AccountService accountService;

    @Autowired
    SecurityService securityService;

    @PostMapping("/login")
    public Object login(@RequestBody AccountLoginDTO user) {
        return securityService.login(user);
    }


    @PostMapping("/register")
    public String register(@RequestBody AccountRegistrationDTO accountRegistrationDTO) {
        return accountService.register(accountRegistrationDTO);
    }

    @GetMapping("/verify")
    public String verify(@RequestParam("verificationToken") String verificationToken) {
        return accountService.verify(verificationToken);
    }
}
