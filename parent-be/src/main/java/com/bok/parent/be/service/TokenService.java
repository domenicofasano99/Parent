package com.bok.parent.be.service;

import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import com.bok.parent.model.Account;
import com.bok.parent.model.Token;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {

    boolean checkTokenValidity(String token);

    Token findByTokenString(String token);

    Token replaceOldToken(Token oldToken);

    void revoke(Token token);

    boolean revoke(String token);

    void revokeTokens(Account account);

    TokenInfoResponseDTO getTokenInfo(String token);

    Token generate(Account account);

    Token saveToken(Token token);
}
