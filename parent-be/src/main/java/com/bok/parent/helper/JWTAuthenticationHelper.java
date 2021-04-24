package com.bok.parent.helper;

import com.bok.parent.exception.AccountException;
import com.bok.parent.exception.WrongCredentialsException;
import com.bok.parent.model.Account;
import com.bok.parent.utils.CryptoUtils;
import com.bok.parent.utils.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.bok.parent.utils.Constants.EMAIL;

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
                .filter(user -> user.getEnabled() && cryptoUtils.checkPassword(password, user.getPassword()))
                .map(user -> jwtService.create(email))
                .orElseThrow(() -> new WrongCredentialsException("Invalid email or password."));
    }

    public Account authenticateByToken(String token) {
        Object username = jwtService.verify(token).get(EMAIL);
        return Optional.ofNullable(username)
                .flatMap(name -> accountHelper.findByEmail(String.valueOf(name)))
                .filter(Account::getEnabled)
                .orElseThrow(() -> new AccountException("Account '" + username + "' not found."));
    }


    public Long extractAccountIdFromToken(String token) {
        String email = (String) jwtService.verify(token).get(EMAIL);
        return accountHelper.findIdByEmail(email);
    }
}
