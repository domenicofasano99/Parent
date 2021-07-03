package com.bok.parent.be.helper;

import com.bok.parent.be.exception.TokenNotFoundException;
import com.bok.parent.be.security.JwtTokenUtil;
import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import com.bok.parent.model.Account;
import com.bok.parent.model.Token;
import com.bok.parent.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenHelper {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    AuthenticationHelper authenticationHelper;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserDetailsService userDetailsService;

    public Token findByTokenString(String token) {
        return tokenRepository.findByTokenString(token).orElseThrow(TokenNotFoundException::new);
    }

    private Token saveToken(Token token) {
        return tokenRepository.saveAndFlush(token);
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void deleteExpiredTokens() {
        Integer deletedTokens = tokenRepository.deleteByExpirationBefore(Instant.now());
        log.info("Deleted {} expired tokens", deletedTokens);
    }

    public TokenInfoResponseDTO getTokenInfo(String token) {
        Token t = findByTokenString(token);
        return new TokenInfoResponseDTO(t.getExpiration());
    }

    public Token replaceOldToken(Token oldToken) {
        Token newToken = authenticationHelper.loginNoPassword(oldToken.getAccount().getCredentials().getEmail());
        revoke(oldToken);
        return saveToken(newToken);
    }

    public void revoke(Token token) {
        tokenRepository.delete(token);
    }

    @Async
    @Transactional
    public void revokeTokens(Account account) {
        tokenRepository.deleteAll(account.getTokens().stream().filter(t -> t.getIssuedAt().isBefore(Instant.now())).collect(Collectors.toList()));
    }

    public Token generate(Account account) {
        return saveToken(jwtTokenUtil.generateToken(account));
    }

    public boolean revoke(String token) {
        return tokenRepository.deleteByTokenString(token).equals(1);
    }

    public boolean checkTokenValidity(String token) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return jwtTokenUtil.validateToken(token, userDetails);
    }
}
