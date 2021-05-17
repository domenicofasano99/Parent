package com.bok.parent.service.implementation;

import com.bok.parent.helper.SecurityHelper;
import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.KeepAliveResponseDTO;
import com.bok.parent.integration.dto.LastAccessInfoDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.LogoutResponseDTO;
import com.bok.parent.integration.dto.PasswordChangeRequestDTO;
import com.bok.parent.integration.dto.PasswordChangeResponseDTO;
import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import com.bok.parent.service.SecurityService;
import com.bok.parent.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    SecurityHelper securityHelper;

    @Override
    public LoginResponseDTO login(AccountLoginDTO accountLoginDTO) {
        ValidationUtils.nonNull(accountLoginDTO.email);
        ValidationUtils.nonNull(accountLoginDTO.password);
        return securityHelper.login(accountLoginDTO);
    }

    @Override
    public Long getAccountId(String token) {
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
}
