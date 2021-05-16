package com.bok.parent.service.implementation;

import com.bok.parent.helper.SecurityHelper;
import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.KeepAliveRequestDTO;
import com.bok.parent.integration.dto.KeepAliveResponseDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.LogoutResponseDTO;
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
    public Long extractAccountId(String token) {
        return securityHelper.extractAccountId(token);
    }

    @Override
    public TokenInfoResponseDTO tokenInfo(String token) {
        return securityHelper.getTokenInfo(token);
    }

    @Override
    public KeepAliveResponseDTO keepAlive(KeepAliveRequestDTO keepAliveRequestDTO) {
        ValidationUtils.nonNull(keepAliveRequestDTO.token);
        return securityHelper.keepAlive();
    }

    @Override
    public LogoutResponseDTO logout(String token) {
         return securityHelper.logout(token);
    }
}
