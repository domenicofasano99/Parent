package com.bok.parent.helper;

import com.bok.parent.exception.AccountException;
import com.bok.parent.exception.WrongCredentialsException;
import com.bok.parent.integration.dto.TokenExpirationResponseDTO;
import com.bok.parent.model.Account;
import com.bok.parent.utils.CryptoUtils;
import com.bok.parent.utils.JWTService;
import com.bok.parent.utils.encryption.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class JWTAuthenticationHelper {
    @Autowired
    AccountHelper accountHelper;

    @Autowired
    JWTService jwtService;

    @Autowired
    CryptoUtils cryptoUtils;

    public String login(String email, String password) {
        return accountHelper
                .findByEmailAndEnabled(email)
                .filter(account -> account.getEnabled() && cryptoUtils.checkPassword(password, account.getCredentials().getPassword()))
                .map(account -> jwtService.create(email, account.getId()))
                .orElseThrow(() -> new WrongCredentialsException("Invalid email or password."));
    }

    public Account authenticateByToken(String token) {
        String email = jwtService.verify(token).email;
        return Optional.ofNullable(email)
                .flatMap(name -> accountHelper.findByEmail(name))
                .filter(Account::getEnabled)
                .orElseThrow(() -> new AccountException("Account '" + email + "' not found."));
    }


    public Long extractAccountIdFromToken(String token) {
        return jwtService.verify(token).accountId;
    }

    public TokenExpirationResponseDTO tokenExpirationInfo(String token) {
        TokenInfo tokenInfo = jwtService.verify(token);
        log.info("expiration date: {}", tokenInfo.expiresAt);
        if (tokenInfo.expired) {
            return new TokenExpirationResponseDTO(true);
        }
        return new TokenExpirationResponseDTO(tokenInfo.expiresAt, false);
    }

}
