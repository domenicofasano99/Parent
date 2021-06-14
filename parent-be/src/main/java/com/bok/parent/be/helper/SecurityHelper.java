package com.bok.parent.be.helper;

import com.bok.parent.be.utils.encryption.CustomEncryption;
import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.KeepAliveResponseDTO;
import com.bok.parent.integration.dto.LastAccessInfoDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.LogoutResponseDTO;
import com.bok.parent.integration.dto.PasswordChangeRequestDTO;
import com.bok.parent.integration.dto.PasswordChangeResponseDTO;
import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import com.bok.parent.model.AccessInfo;
import com.bok.parent.model.Account;
import com.bok.parent.model.Token;
import com.bok.parent.repository.AccessInfoRepository;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.util.Objects.nonNull;

@Component
@Slf4j
public class SecurityHelper {

    @Autowired
    AuthenticationHelper authenticationHelper;

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    AuditHelper auditHelper;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    AccessInfoRepository accessInfoRepository;

    public LoginResponseDTO login(AccountLoginDTO request) {
        Preconditions.checkArgument(nonNull(request.password));
        Preconditions.checkArgument(nonNull(request.email));

        Account account = accountHelper.findByEmail(request.email);

        LoginResponseDTO response = new LoginResponseDTO();
        Token token = authenticationHelper.login(account, request.password);
        response.lastAccessInfo = getLastAccessInfoByAccountId(account.getId());
        response.token = token.getTokenString();
        response.passwordResetNeeded = account.getCredentials().isResetNeeded();
        log.info("User {} logged in", request.email);
        return response;
    }

    public Long getAccountId(String token) {
        return authenticationHelper.extractAccountIdFromToken(token);
    }

    public TokenInfoResponseDTO getTokenInfo(String token) {
        return tokenHelper.getTokenInfo(token);
    }

    public KeepAliveResponseDTO keepAlive(String tokenString) {
        KeepAliveResponseDTO keepAliveResponse = new KeepAliveResponseDTO();
        Token token = tokenHelper.getTokenByTokenString(tokenString);
        if (token.getExpiration().isBefore(Instant.now().plusSeconds(120))) {
            keepAliveResponse.token = tokenHelper.replaceOldToken(token).getTokenString();
        }
        return keepAliveResponse;
    }

    public LogoutResponseDTO logout(String token) {
        return new LogoutResponseDTO(tokenHelper.invalidateToken(token));
    }

    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredToken() {
        tokenHelper.deleteExpiredTokens();
    }

    public LastAccessInfoDTO lastAccessInfo(String token) {
        Long accountId = tokenHelper.getAccountIdByTokenString(token);
        return getLastAccessInfoByAccountId(accountId);
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


    public PasswordChangeResponseDTO changePassword(String tokenString, PasswordChangeRequestDTO passwordChangeRequestDTO) {
        Token token = tokenHelper.findByTokenString(tokenString);
        Account account = token.getAccount();
        String newHashedPassword;
        newHashedPassword = CustomEncryption.getInstance().encrypt(passwordChangeRequestDTO.newPassword);

        boolean changed = accountHelper.changePassword(account, newHashedPassword);
        return new PasswordChangeResponseDTO(changed);
    }

    public void checkTokenValidity(String token) {
        authenticationHelper.checkTokenValidity(token);
    }

    public void checkIpAddress(Long accountId, String remoteAddr) {
        AccessInfo accessInfo = accessInfoRepository.findLastAccessInfoByAccountId(accountId).orElseThrow(() -> new RuntimeException("Error while authenticating by token"));
        if (!accessInfo.getIpAddress().equalsIgnoreCase(remoteAddr)) {
            tokenHelper.revokeTokenByAccountId(accountId);
            throw new RuntimeException("Hacking attempt or user IP address changed, revoking access token for security reasons.");
        }
    }
}
