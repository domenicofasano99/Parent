package com.bok.parent.security;

import com.bok.parent.utils.JWTAuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    JWTAuthenticationHandler jwtAuthenticationHandler;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        Object token = authentication.getCredentials();
        return Optional
                .ofNullable(token)
                .flatMap(t -> Optional.of(jwtAuthenticationHandler.authenticateByToken(String.valueOf(t)))
                        .map(u -> User.builder()
                                .username(u.getEmail())
                                .password(u.getPassword())
                                .roles(u.getRole().toString())
                                .build()))
                .orElseThrow(() -> new BadCredentialsException("Invalid authentication token=" + token));
    }
}
