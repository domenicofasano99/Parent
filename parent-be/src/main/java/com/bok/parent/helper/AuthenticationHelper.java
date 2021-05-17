package com.bok.parent.helper;

import com.bok.parent.exception.AccountException;
import com.bok.parent.model.Account;
import com.bok.parent.model.Token;
import com.bok.parent.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class AuthenticationHelper {
    @Autowired
    AccountHelper accountHelper;

    @Autowired
    CryptoUtils cryptoUtils;

    @Autowired
    TokenHelper tokenHelper;

    public String login(Account account, String password) {
        checkPassword(account, password);

        Optional<Token> oldToken = tokenHelper.getActiveToken(account.getCredentials().getEmail());
        oldToken.ifPresent(value -> tokenHelper.invalidateToken(value));
        Token token = tokenHelper.create(account);
        token = tokenHelper.saveToken(token);

        return token.getTokenString();
    }


    public Account authenticateByToken(String token) {
        String email = tokenHelper.verify(token).account.getCredentials().getEmail();
        return Optional.ofNullable(email)
                .flatMap(name -> accountHelper.findByEmail(name))
                .filter(Account::getEnabled)
                .orElseThrow(() -> new AccountException("Account '" + email + "' not found."));
    }

    public Long extractAccountIdFromToken(String token) {
        return tokenHelper.verify(token).account.getId();
    }

    private void checkPassword(Account account, String password) {
        cryptoUtils.checkPassword(password, account.getCredentials().getPassword());
    }
}
