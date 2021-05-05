package com.bok.parent.helper;

import com.bok.integration.parent.dto.AccountLoginDTO;
import com.bok.parent.utils.Constants;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SecurityHelper {

    @Autowired
    JWTAuthenticationHelper jwtAuthenticationHelper;

    public String login(AccountLoginDTO accountLoginDTO) {
        Preconditions.checkArgument(Objects.nonNull(accountLoginDTO.password));
        Preconditions.checkArgument(Objects.nonNull(accountLoginDTO.email));

        return jwtAuthenticationHelper.login(accountLoginDTO.email, accountLoginDTO.password);
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
