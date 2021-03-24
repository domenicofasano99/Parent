package com.bok.parent.utils;

import com.bok.parent.exception.BokException;
import com.bok.parent.helper.UserHelper;
import com.bok.parent.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.bok.parent.utils.Constants.EMAIL;

@Component
public class JWTAuthenticationHelper {
    @Autowired
    UserHelper userHelper;

    @Autowired
    JWTService jwtService;

    @Autowired
    CryptoUtils cryptoUtils;

    public String login(String email, String password) throws BadCredentialsException {
        return userHelper
                .findByEmail(email)
                .filter(user -> user.getEnabled() && cryptoUtils.checkPassword(password, user.getPassword()))
                .map(user -> jwtService.create(email))
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));
    }

    public User authenticateByToken(String token) {
        try {
            Object username = jwtService.verify(token).get(EMAIL);
            return Optional.ofNullable(username)
                    .flatMap(name -> userHelper.findByEmail(String.valueOf(name)))
                    .filter(User::getEnabled)
                    .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found."));
        } catch (BokException e) {
            throw new BadCredentialsException("Invalid JWT token.", e);
        }
    }


    public Long extractUserIdFromToken(String token) {
        String email = (String) jwtService.verify(token).get(EMAIL);
        return userHelper.findIdByEmail(email);
    }
}
