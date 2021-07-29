package com.bok.parent.be.service.implementation;

import com.bok.parent.be.helper.TokenHelper;
import com.bok.parent.be.service.TokenService;
import com.bok.parent.integration.dto.TokenInfoResponseDTO;
import com.bok.parent.model.Account;
import com.bok.parent.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenHelper tokenHelper;

    @Override
    public boolean checkTokenValidity(String token) {
        return tokenHelper.checkTokenValidity(token);
    }

    @Override
    public Token findByTokenString(String token) {
        return tokenHelper.findByTokenString(token);
    }

    @Override
    public Token replaceOldToken(Token oldToken) {
        return tokenHelper.replaceOldToken(oldToken);
    }

    @Override
    public void revoke(Token token) {
        tokenHelper.revoke(token);
    }

    @Override
    public boolean revoke(String token) {
        return tokenHelper.revoke(token);
    }

    @Override
    public void revokeTokens(Account account) {
        tokenHelper.revokeTokens(account);
    }

    @Override
    public TokenInfoResponseDTO getTokenInfo(String token) {
        return tokenHelper.getTokenInfo(token);
    }

    @Override
    public Token generate(Account account) {
        return tokenHelper.generate(account);
    }

    @Override
    public Token saveToken(Token token) {
        return tokenHelper.save(token);
    }
}
