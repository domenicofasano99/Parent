package com.bok.parent.be.controller;

import com.bok.parent.be.audit.PasswordResetAudit;
import com.bok.parent.be.audit.RegisterAudit;
import com.bok.parent.be.service.AccountService;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.AccountRegistrationResponseDTO;
import com.bok.parent.integration.dto.PasswordResetRequestDTO;
import com.bok.parent.integration.dto.PasswordResetResponseDTO;
import com.bok.parent.integration.dto.VerificationResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AccountController {


    final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RegisterAudit
    @PostMapping("/register")
    @ApiOperation(value = "Registration methods")
    public AccountRegistrationResponseDTO register(@RequestBody AccountRegistrationDTO accountRegistrationDTO, HttpServletRequest request) {
        return accountService.register(accountRegistrationDTO);
    }

    @GetMapping("/verify")
    @ApiOperation(value = "Used to verify the account with the link sent via email")
    public VerificationResponseDTO verify(@RequestParam("verificationToken") String confirmationToken) {
        return accountService.verify(confirmationToken);
    }

    @PasswordResetAudit
    @PostMapping("/resetPassword")
    @ApiOperation(value = "Resets the account password and sends a new secure password via email")
    public PasswordResetResponseDTO resetPassword(@RequestBody PasswordResetRequestDTO passwordResetRequestDTO) {
        return accountService.resetPassword(passwordResetRequestDTO);
    }

    @Deprecated
    @ApiOperation(value = "Completely deletes an account from the platform, ONLY FOR INTERNAL USE")
    @DeleteMapping("/delete")
    public String deleteByEmail(@RequestParam("email") String email) {
        return accountService.delete(email);
    }
}
