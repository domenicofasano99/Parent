package com.bok.parent.helper;

import com.bok.parent.dto.LoginUser;
import com.bok.parent.utils.JWTAuthenticationHelper;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class SecurityHelper {

    @Autowired
    JWTAuthenticationHelper jwtAuthenticationHelper;

    public Object login(LoginUser loginUser) {
        Preconditions.checkArgument(Objects.nonNull(loginUser.password));
        Preconditions.checkArgument(Objects.nonNull(loginUser.email));
        try {
            return jwtAuthenticationHelper.login(loginUser.email, loginUser.password);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
        }
    }
}
