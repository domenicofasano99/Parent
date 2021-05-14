package com.bok.parent.helper;

import com.bok.integration.parent.LoginResponseDTO;
import com.bok.integration.parent.dto.AccountLoginDTO;
import com.bok.parent.model.AccessInfo;
import com.bok.parent.utils.Constants;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Component
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

        return response;
    }

    /***
     * Cache is used here to speedup the retrieval process, since this bean is not accessible from the outside
     * it's considered a safe process to do.
     * Extracting the userId from the token is an expensive operation since a query is needed to do so.
     * Caching the result will improve the speed by a lot since there will be probably multiple requests from the same
     * user in a defined period of time.
     * @param token the token that arrives from the request sent by the client
     * @return the ID of the account that has made the request
     */
    @Cacheable(value = Constants.TOKENS, unless = "#result == null")
    public Long extractAccountId(String token) {
        return jwtAuthenticationHelper.extractAccountIdFromToken(token);
    }
}
