package com.bok.parent.be.security;

import com.bok.parent.be.exception.TokenAuthenticationException;
import com.bok.parent.be.helper.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
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
    AuthenticationHelper authenticationHelper;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        Object token = authentication.getCredentials();
        return Optional
                .ofNullable(token)
                .flatMap(t -> Optional.of(authenticationHelper.authenticateByToken(String.valueOf(t)))
                        .map(u -> User.builder()
                                .username(u.getCredentials().getEmail())
                                .password(u.getCredentials().getPassword())
                                .roles(u.getRole().toString())
                                .build()))
                .orElseThrow(() -> new TokenAuthenticationException("Invalid authentication token: " + token));
    }
}
