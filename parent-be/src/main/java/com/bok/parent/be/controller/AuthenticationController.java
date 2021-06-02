package com.bok.parent.be.controller;

import com.bok.parent.be.audit.LoginAudit;
import com.bok.parent.be.audit.PasswordResetAudit;
import com.bok.parent.be.audit.RegisterAudit;
import com.bok.parent.be.service.AccountService;
import com.bok.parent.be.service.SecurityService;
import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.AccountRegistrationResponseDTO;
import com.bok.parent.integration.dto.KeepAliveResponseDTO;
import com.bok.parent.integration.dto.LastAccessInfoDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.LogoutResponseDTO;
import com.bok.parent.integration.dto.PasswordChangeRequestDTO;
import com.bok.parent.integration.dto.PasswordChangeResponseDTO;
import com.bok.parent.integration.dto.PasswordResetRequestDTO;
import com.bok.parent.integration.dto.PasswordResetResponseDTO;
import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import com.bok.parent.integration.dto.VerificationResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpHeaders;
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
    @ApiOperation(value = "Login method with email and password")
    public LoginResponseDTO login(@RequestBody AccountLoginDTO user, HttpServletRequest request) {
        return securityService.login(user);
    }

    @RegisterAudit
    @PostMapping("/register")
    @ApiOperation(value = "Registration methods")
    public AccountRegistrationResponseDTO register(@RequestBody AccountRegistrationDTO accountRegistrationDTO, HttpServletRequest request) {
        return accountService.register(accountRegistrationDTO);
    }

    @PostMapping("/tokenInfo")
    @ApiOperation(value = "Returns token infos, accessible only after login")
    public TokenInfoResponseDTO tokenInfo(HttpServletRequest request) {
        return securityService.tokenInfo(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
    }

    @GetMapping("/verify")
    @ApiOperation(value = "Used to verify the account with the link sent via email")
    public VerificationResponseDTO verify(@RequestParam("verificationToken") String verificationToken) {
        return accountService.verify(verificationToken);
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

    @PostMapping("/keepAlive")
    @ApiOperation(value = "Requests a keepAlive to prevent user from being disconnected while using the platform, a new token will be issue only 1 minute before the expiration of the old one, otherwise null will be returned.")
    public KeepAliveResponseDTO keepAlive(HttpServletRequest request) {
        return securityService.keepAlive(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
    }

    @PostMapping("/logout")
    public LogoutResponseDTO logout(HttpServletRequest request) {
        return securityService.logout(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
    }

    @GetMapping("/lastAccessInfo")
    public LastAccessInfoDTO lastAccessInfo(HttpServletRequest request) {
        return securityService.lastAccessInfo(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
    }

    @PostMapping("/changePassword")
    public PasswordChangeResponseDTO changePassword(HttpServletRequest request, @RequestBody PasswordChangeRequestDTO passwordChangeRequestDTO) {
        return securityService.changePassword(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7), passwordChangeRequestDTO);
    }
}
