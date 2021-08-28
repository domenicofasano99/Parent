package com.bok.parent.be.controller;

import com.bok.parent.be.audit.PasswordResetAudit;
import com.bok.parent.be.audit.RegisterAudit;
import com.bok.parent.be.service.AccountService;
import com.bok.parent.integration.dto.AccountClosureDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.AccountRegistrationResponseDTO;
import com.bok.parent.integration.dto.PasswordResetRequestDTO;
import com.bok.parent.integration.dto.PasswordResetResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
public class AccountController {


    final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RegisterAudit
    @PostMapping("/register")
    @ApiOperation(value = "Register an account")
    public AccountRegistrationResponseDTO register(@RequestBody AccountRegistrationDTO accountRegistrationDTO, HttpServletRequest request) {
        return accountService.register(accountRegistrationDTO);
    }

    @GetMapping("/verify/{verificationToken}")
    @ApiOperation(value = "Used to verify the account with the link sent via email")
    public ModelAndView verify(@PathVariable("verificationToken") String confirmationToken) {
        Boolean verified = accountService.verify(confirmationToken);
        return new ModelAndView("redirect:" + "https://bok.faraone.ovh", Collections.singletonMap("verified", verified));
    }

    @PasswordResetAudit
    @PostMapping("/resetPassword")
    @ApiOperation(value = "Resets the account password and sends a new secure password via email")
    public PasswordResetResponseDTO resetPassword(@RequestBody PasswordResetRequestDTO passwordResetRequestDTO) {
        return accountService.resetPassword(passwordResetRequestDTO);
    }

    @ApiOperation(value = "Completely closes an account from the platform. Some data may remain stored for security purposes.")
    @DeleteMapping("/closeAccount")
    public String closeAccount(@RequestBody AccountClosureDTO accountClosureDTO) {
        return accountService.closeAccount(accountClosureDTO);
    }
}
