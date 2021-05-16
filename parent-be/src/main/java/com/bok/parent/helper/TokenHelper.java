package com.bok.parent.helper;

import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import com.bok.parent.model.Token;
import com.bok.parent.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenHelper {

    @Autowired
    TokenRepository tokenRepository;

    public Token findByTokenString(String token) {
        return tokenRepository.findByTokenString(token).orElseThrow(() -> new RuntimeException("Token not found"));
    }


    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public Boolean invalidateToken(String token) {
        Token tokenInfo = findByTokenString(token);
        tokenInfo.expired = true;
        tokenRepository.save(tokenInfo);
        return true;
    }

    public void deleteExpiredTokens() {
        Integer deletedTokens = tokenRepository.deleteByExpiredIsTrue();
        log.info("Deleted {} expired tokens", deletedTokens);
    }

    public TokenInfoResponseDTO getTokenInfo(String token) {
        Token t = findByTokenString(token);
        return new TokenInfoResponseDTO(t.expiresAt);
    }
}
