package com.bok.parent.controller;

import com.bok.integration.parent.PasswordRecoveryResponseDTO;
import com.bok.integration.parent.PasswordResetRequestDTO;
import com.bok.parent.audit.LoginAudit;
import com.bok.parent.audit.RegisterAudit;
import com.bok.integration.parent.dto.AccountLoginDTO;
import com.bok.integration.parent.dto.AccountRegistrationDTO;
import com.bok.parent.service.AccountService;
import com.bok.parent.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {

    @Autowired
    AccountService accountService;

    @Autowired
    SecurityService securityService;

    @LoginAudit
    @PostMapping("/login")
    public Object login(@RequestBody AccountLoginDTO user, HttpServletRequest request) {
        return securityService.login(user);
    }

    @RegisterAudit
    @PostMapping("/register")
    public String register(@RequestBody AccountRegistrationDTO accountRegistrationDTO, HttpServletRequest request) {
        return accountService.register(accountRegistrationDTO);
    }

    @GetMapping("/verify")
    public String verify(@RequestParam("verificationToken") String verificationToken) {
        return accountService.verify(verificationToken);
    }

    @PostMapping
    public PasswordRecoveryResponseDTO recoverPassword(@RequestBody PasswordResetRequestDTO passwordResetRequestDTO){
        return accountService.recover(passwordResetRequestDTO);
    }
}
