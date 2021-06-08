package com.bok.parent.be.helper;

import com.bok.parent.be.exception.InvalidCredentialsException;
import com.bok.parent.be.exception.UserNotEnabledException;
import com.bok.parent.be.security.JwtTokenUtil;
import com.bok.parent.model.Account;
import com.bok.parent.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationHelper {
    @Autowired
    AccountHelper accountHelper;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenHelper tokenHelper;

    public Token login(Account account, String password) {
        String email = account.getCredentials().getEmail();

        authenticate(email, password);
        final Token token = jwtTokenUtil.generateToken(account);
        return tokenHelper.saveToken(token);
    }

    public Token loginNoPassword(String email) {
        final Account account = accountHelper.findByEmail(email);
        final Token token = jwtTokenUtil.generateToken(account);

        return tokenHelper.saveToken(token);
    }

    public void checkTokenValidity(String token) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        jwtTokenUtil.validateToken(token, userDetails);
    }

    public Long extractAccountIdFromToken(String token) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        return accountHelper.getAccountIdByEmail(email);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new UserNotEnabledException("USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }
    }
}
