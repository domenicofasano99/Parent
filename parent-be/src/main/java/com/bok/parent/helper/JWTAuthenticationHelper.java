package com.bok.parent.helper;

import com.bok.parent.exception.BokException;
import com.bok.parent.helper.AccountHelper;
import com.bok.parent.model.Account;
import com.bok.parent.utils.CryptoUtils;
import com.bok.parent.utils.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public String login(String email, String password) throws BadCredentialsException {
        return accountHelper
                .findByEmail(email)
                .filter(user -> user.getEnabled() && cryptoUtils.checkPassword(password, user.getPassword()))
                .map(user -> jwtService.create(email))
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));
    }

    public Account authenticateByToken(String token) {
        try {
            Object username = jwtService.verify(token).get(EMAIL);
            return Optional.ofNullable(username)
                    .flatMap(name -> accountHelper.findByEmail(String.valueOf(name)))
                    .filter(Account::getEnabled)
                    .orElseThrow(() -> new UsernameNotFoundException("Account '" + username + "' not found."));
        } catch (BokException e) {
            throw new BadCredentialsException("Invalid JWT token.", e);
        }
    }


    public Long extractAccountIdFromToken(String token) {
        String email = (String) jwtService.verify(token).get(EMAIL);
        return accountHelper.findIdByEmail(email);
    }
}
