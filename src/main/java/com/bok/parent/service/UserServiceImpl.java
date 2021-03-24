package com.bok.parent.service;

import com.bok.parent.dto.UserDTO;
import com.bok.parent.helper.UserHelper;
import com.bok.parent.model.User;
import com.bok.parent.utils.JWTAuthenticationHandler;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserHelper userHelper;

    @Autowired
    JWTAuthenticationHandler jwtAuthenticationHandler;

    @Override
    public User createUser(UserDTO userDTO) {
        Preconditions.checkArgument(Objects.nonNull(userDTO.password));
        Preconditions.checkArgument(Objects.nonNull(userDTO.email));
        return userHelper.createUser(userDTO);
    }

    @Override
    public Object login(UserDTO user) {
        try {
            return jwtAuthenticationHandler.login(user.email, user.password);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
        }
    }
}