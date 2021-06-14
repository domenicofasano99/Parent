package com.bok.parent.be.service.implementation;

import com.bok.parent.be.helper.SecurityHelper;
import com.bok.parent.be.helper.TokenHelper;
import com.bok.parent.be.service.SecurityService;
import com.bok.parent.be.utils.ValidationUtils;
import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.KeepAliveResponseDTO;
import com.bok.parent.integration.dto.LastAccessInfoDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.LogoutResponseDTO;
import com.bok.parent.integration.dto.PasswordChangeRequestDTO;
import com.bok.parent.integration.dto.PasswordChangeResponseDTO;
import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    SecurityHelper securityHelper;

    @Autowired
    TokenHelper tokenHelper;

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
        return securityHelper.changePassword(token, passwordChangeRequestDTO);
    }

    @Override
    public boolean checkTokenValidity(String token) {
        return tokenHelper.checkTokenValidity(token);
    }

    @Override
    public void checkIpAddress(Long accountId, String remoteAddr) {
        securityHelper.checkIpAddress(accountId, remoteAddr);
    }
}
