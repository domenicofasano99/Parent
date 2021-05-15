package com.bok.parent.controller;

import com.bok.parent.audit.LoginAudit;
import com.bok.parent.audit.PasswordResetAudit;
import com.bok.parent.audit.RegisterAudit;
import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.AccountRegistrationResponseDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.PasswordResetRequestDTO;
import com.bok.parent.integration.dto.PasswordResetResponseDTO;
import com.bok.parent.integration.dto.VerificationResponseDTO;
import com.bok.parent.service.AccountService;
import com.bok.parent.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public LoginResponseDTO login(@RequestBody AccountLoginDTO user, HttpServletRequest request) {
        return securityService.login(user);
    }

    @RegisterAudit
    @PostMapping("/register")
    public AccountRegistrationResponseDTO register(@RequestBody AccountRegistrationDTO accountRegistrationDTO, HttpServletRequest request) {
        return accountService.register(accountRegistrationDTO);
    }

    @GetMapping("/verify")
    public VerificationResponseDTO verify(@RequestParam("verificationToken") String verificationToken) {
        return accountService.verify(verificationToken);
    }

    @PasswordResetAudit
    @PostMapping("/resetPassword")
    public PasswordResetResponseDTO resetPassword(@RequestBody PasswordResetRequestDTO passwordResetRequestDTO) {
        return accountService.resetPassword(passwordResetRequestDTO);
    }


    @Deprecated
    @DeleteMapping("/delete")
    public String deleteByEmail(@RequestParam("email") String email) {
        return accountService.delete(email);
    }
}
