package com.bok.parent.be.service.implementation;

import com.bok.parent.be.helper.SecurityHelper;
import com.bok.parent.be.service.SecurityService;
import com.bok.parent.be.service.TokenService;
import com.bok.parent.be.service.bank.BankService;
import com.bok.parent.be.utils.ValidationUtils;
import com.bok.parent.integration.dto.*;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    SecurityHelper securityHelper;

    @Autowired
    TokenService tokenService;

    @Autowired
    BankService bankService;

    @Override
    public LoginResponseDTO login(AccountLoginDTO accountLoginDTO) {
        ValidationUtils.nonNull(accountLoginDTO.email);
        ValidationUtils.nonNull(accountLoginDTO.password);
        return securityHelper.login(accountLoginDTO);
    }

    @Override
    public Long getAccountId(String token) {
        ValidationUtils.nonNull(token);
        return securityHelper.getAccountId(token);
    }

    @Override
    public TokenInfoResponseDTO tokenInfo(String token) {
        return securityHelper.getTokenInfo(token);
    }

    @Override
    public KeepAliveResponseDTO keepAlive(String token) {
        return securityHelper.keepAlive(token);
    }

    @Override
    public LogoutResponseDTO logout(String token) {
        return securityHelper.logout(token);
    }

    @Override
    public LastAccessInfoDTO lastAccessInfo(String token) {
        return securityHelper.lastAccessInfo(token);
    }

    @Override
    public PasswordChangeResponseDTO changePassword(String token, PasswordChangeRequestDTO passwordChangeRequestDTO) {
        Preconditions.checkNotNull(passwordChangeRequestDTO.oldPassword);
        Preconditions.checkNotNull(passwordChangeRequestDTO.newPassword);
        return securityHelper.changePassword(token, passwordChangeRequestDTO);
    }

    @Override
    public Boolean checkTokenValidity(String token) {
        return tokenService.checkTokenValidity(token);
    }

    @Override
    public Boolean passwordResetNeeded(String token) {
        return securityHelper.checkForPasswordResetNeeded(getAccountId(token));
    }

    @Override
    public Boolean confirmCard(Long accountId, String confirmationToken) {
        return bankService.confirmcard(accountId, confirmationToken);
    }
}
