package com.bok.parent.be.helper;

import com.bok.parent.be.exception.TokenNotFoundException;
import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import com.bok.parent.model.Token;
import com.bok.parent.repository.TokenRepository;
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
    AuthenticationHelper authenticationHelper;

    public Token findByTokenString(String token) {
        return tokenRepository.findByTokenString(token).orElseThrow(() -> new RuntimeException("Token not found"));
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
        return new TokenInfoResponseDTO(t.getExpiration());
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
        oldToken.getAccount().getCredentials().getEmail();
        Token newToken = authenticationHelper.loginNoPassword(oldToken.getAccount().getCredentials().getEmail());
        invalidateToken(oldToken.getTokenString());
        return saveToken(newToken);
    }

    public void revokeTokenByAccountId(Long accountId) {
        Token t = tokenRepository.findByAccount_Id(accountId).orElseThrow(RuntimeException::new);
        tokenRepository.delete(t);
    }
}
