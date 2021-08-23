package com.bok.parent.be.controller;

import com.bok.parent.be.audit.LoginAudit;
import com.bok.parent.be.audit.PasswordResetAudit;
import com.bok.parent.be.service.SecurityService;
import com.bok.parent.integration.dto.*;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SecurityController {

    final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @LoginAudit
    @PostMapping("/login")
    @ApiOperation(value = "Login method with email and password")
    public LoginResponseDTO login(@RequestBody AccountLoginDTO user, HttpServletRequest request) {
        return securityService.login(user);
    }

    @PostMapping("/tokenInfo")
    @ApiOperation(value = "Returns token infos, accessible only after login")
    public TokenInfoResponseDTO tokenInfo(HttpServletRequest request) {
        return securityService.tokenInfo(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
    }

    @PostMapping("/keepAlive")
    @ApiOperation(value = "Requests a keepAlive to prevent user from being disconnected while using the platform, a new token will be issue only 1 minute before the expiration of the old one, otherwise null will be returned.")
    public KeepAliveResponseDTO keepAlive(HttpServletRequest request) {
        return securityService.keepAlive(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
    }

    @GetMapping("/logout")
    @ApiOperation(value = "Requests logout")
    public LogoutResponseDTO logout(HttpServletRequest request) {
        return securityService.logout(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
    }

    @GetMapping("/passwordResetNeeded")
    @ApiOperation(value = "Returns true if a password reset is needed")
    public Boolean passwordResetNeeded(HttpServletRequest request) {
        return securityService.passwordResetNeeded(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
    }

    @GetMapping("/lastAccessInfo")
    public LastAccessInfoDTO lastAccessInfo(HttpServletRequest request) {
        return securityService.lastAccessInfo(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
    }

    @PostMapping("/changePassword")
    public PasswordChangeResponseDTO changePassword(HttpServletRequest request, @RequestBody PasswordChangeRequestDTO passwordChangeRequestDTO) {
        return securityService.changePassword(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7), passwordChangeRequestDTO);
    }

    @GetMapping("/cardConfirm/")
    public ModelAndView cardConfirm(@RequestParam("confirmationToken") String confirmationToken, @RequestParam("accountId") Long accountId) {
        securityService.confirmCard(accountId, confirmationToken);
        return new ModelAndView("redirect:" + "http://bok.faraone.ovh:90");
    }
}
