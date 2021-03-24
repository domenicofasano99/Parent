package com.bok.parent.utils;

import com.bok.parent.exception.BokException;
import com.bok.parent.helper.UserHelper;
import com.bok.parent.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JWTAuthenticationHandler {
    @Autowired
    UserHelper userHelper;

    @Autowired
    JWTService jwtService;

    @Autowired
    CryptoUtils cryptoUtils;

    public String login(String username, String password) throws BadCredentialsException {
        return userHelper
                .findByEmail(username)
                .filter(user -> user.getEnabled() && cryptoUtils.checkPassword(password, user.getPassword()))
                .map(user -> jwtService.create(username))
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password."));
    }

    public User authenticateByToken(String token) {
        try {
            Object username = jwtService.verify(token).get("username");
            return Optional.ofNullable(username)
                    .flatMap(name -> userHelper.findByEmail(String.valueOf(name)))
                    .filter(User::getEnabled)
                    .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found."));
        } catch (BokException e) {
            throw new BadCredentialsException("Invalid JWT token.", e);
        }
    }
}
