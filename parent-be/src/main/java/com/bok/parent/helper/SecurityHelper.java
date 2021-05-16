package com.bok.parent.helper;

import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.TokenExpirationRequestDTO;
import com.bok.parent.integration.dto.TokenExpirationResponseDTO;
import com.bok.parent.model.AccessInfo;
import com.bok.parent.utils.Constants;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Component
@Slf4j
public class SecurityHelper {

    @Autowired
    JWTAuthenticationHelper jwtAuthenticationHelper;

    @Autowired
    AuditHelper auditHelper;

    public LoginResponseDTO login(AccountLoginDTO accountLoginDTO) {
        Preconditions.checkArgument(Objects.nonNull(accountLoginDTO.password));
        Preconditions.checkArgument(Objects.nonNull(accountLoginDTO.email));
        String token = jwtAuthenticationHelper.login(accountLoginDTO.email, accountLoginDTO.password);

        AccessInfo accessInfo = auditHelper.findLastAccessInfo(accountLoginDTO.email);
        LoginResponseDTO response = new LoginResponseDTO();
        response.token = token;
        if (Objects.nonNull(accessInfo)) {
            response.lastAccessDateTime = LocalDateTime.ofInstant(accessInfo.getTimestamp(), ZoneOffset.UTC);
            response.lastAccessIP = accessInfo.getIpAddress();
        }
        log.info("User {} logged in", accountLoginDTO.email);
        return response;
    }

    public Long extractAccountId(String token) {
        return jwtAuthenticationHelper.extractAccountIdFromToken(token);
    }

    public TokenExpirationResponseDTO tokenInfo(TokenExpirationRequestDTO tokenExpirationRequestDTO) {
        return jwtAuthenticationHelper.tokenExpirationInfo(tokenExpirationRequestDTO.token);
    }
}
