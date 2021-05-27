package com.bok.parent.be.helper;

import com.bok.parent.be.exception.TokenNotFoundException;
import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import com.bok.parent.model.Account;
import com.bok.parent.model.Token;
import com.bok.parent.repository.TokenRepository;
import com.bok.parent.be.utils.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class TokenHelper {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    JWTService jwtService;

    public Token findByTokenString(String token) {
        return tokenRepository.findByTokenString(token).orElseThrow(() -> new RuntimeException("Token not found"));
    }

    public Optional<Token> findOptionalByTokenString(String token) {
        return tokenRepository.findByTokenString(token);
    }


    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    public Boolean invalidateToken(String token) {
        Token tokenInfo = findByTokenString(token);
        tokenInfo.expired = true;
        tokenRepository.save(tokenInfo);
        return true;
    }

    public void invalidateToken(Token token) {
        token.expired = true;
        tokenRepository.save(token);
    }

    public void deleteExpiredTokens() {
        Integer deletedTokens = tokenRepository.deleteByExpiredIsTrue();
        log.info("Deleted {} expired tokens", deletedTokens);
    }

    public TokenInfoResponseDTO getTokenInfo(String token) {
        Token t = findByTokenString(token);
        return new TokenInfoResponseDTO(t.expiresAt);
    }

    public Optional<Token> getActiveToken(String email) {
        return tokenRepository.findByAccount_Credentials_EmailAndExpiredIsFalse(email);
    }

    public Long getAccountIdByTokenString(String token) {
        return tokenRepository.findByTokenString(token).orElseThrow(TokenNotFoundException::new).getAccount().getId();
    }

    public Token getTokenByTokenString(String token) {
        return tokenRepository.findByTokenString(token).orElseThrow(TokenNotFoundException::new);
    }

    public Token replaceOldToken(Token oldToken) {
        Token newToken = jwtService.create(oldToken.getAccount());
        invalidateToken(oldToken.getTokenString());
        saveToken(newToken);
        return newToken;
    }

    public Token create(Account account) {
        return jwtService.create(account);
    }

    public Token verify(String token) {
        return jwtService.verify(token);
    }
}
