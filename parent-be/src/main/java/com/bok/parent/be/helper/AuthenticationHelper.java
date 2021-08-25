package com.bok.parent.be.helper;

import com.bok.parent.be.exception.InvalidCredentialsException;
import com.bok.parent.be.security.JwtTokenUtil;
import com.bok.parent.be.service.TokenService;
import com.bok.parent.integration.dto.LastAccessInfoDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.model.AccessInfo;
import com.bok.parent.model.Account;
import com.bok.parent.model.Credentials;
import com.bok.parent.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.util.Objects.nonNull;

@Slf4j
@Component
public class AuthenticationHelper {
    @Autowired
    AccountHelper accountHelper;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuditHelper auditHelper;

    @Autowired
    PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(Account account, String password) {
        String email = account.getCredentials().getEmail();
        tokenService.revokeTokens(account);

        authenticate(email, password);
        Token token = tokenService.generate(account);
        token = tokenService.saveToken(token);
        accountHelper.addTokenToAccount(account, token);

        LoginResponseDTO response = new LoginResponseDTO();
        response.lastAccessInfo = getLastAccessInfoByAccountId(account.getId());
        response.token = token.getTokenString();
        response.passwordResetNeeded = account.getPasswordResetNeeded();
        log.info("User {} logged in", email);
        return response;
    }

    public Token loginNoPassword(String email) {
        final Account account = accountHelper.findByEmail(email);
        return tokenService.generate(account);
    }

    public Long extractAccountIdFromToken(String token) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        return accountHelper.getAccountIdByEmail(email);
    }

    private void authenticate(String email, String password) {
        try {
            Credentials c = accountHelper.getCredentialsByEmail(email);
            if (passwordEncoder.matches(password, c.getPassword())) {
                return;
            }
        } catch (AccountNotFoundException a) {
            throw new RuntimeException("Account not found");
        }
        log.info("user {} tried to log in with a wrong password", email);
        throw new InvalidCredentialsException("INVALID_CREDENTIALS");
    }

    private LastAccessInfoDTO getLastAccessInfoByAccountId(Long accountId) {
        AccessInfo accessInfo = auditHelper.findLastAccessInfo(accountId);
        LastAccessInfoDTO lastAccessInfo = new LastAccessInfoDTO();
        if (nonNull(accessInfo)) {
            lastAccessInfo.lastAccessDateTime = LocalDateTime.ofInstant(accessInfo.getTimestamp(), ZoneOffset.UTC);
            lastAccessInfo.lastAccessIP = accessInfo.getIpAddress();
        } else {
            lastAccessInfo.lastAccessDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
            lastAccessInfo.lastAccessIP = "";
        }
        return lastAccessInfo;
    }
}
