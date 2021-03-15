package com.bok.parent.service;

import com.bok.integration.parent.dto.AuthenticationResponseDTO;
import com.bok.parent.dto.UserDTO;
import com.bok.parent.helper.UserHelper;
import com.bok.parent.model.User;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserHelper userHelper;

    @Override
    public User createUser(UserDTO userDTO) {
        Preconditions.checkArgument(Objects.nonNull(userDTO.username));
        Preconditions.checkArgument(Objects.nonNull(userDTO.password));
        Preconditions.checkArgument(Objects.nonNull(userDTO.role));
        Preconditions.checkArgument(Objects.nonNull(userDTO.email));
        return userHelper.createUser(userDTO);
    }

    @Override
    public AuthenticationResponseDTO authenticate(UserDTO userDTO) {
        Preconditions.checkArgument(Objects.nonNull(userDTO.username));
        Preconditions.checkArgument(Objects.nonNull(userDTO.password));
        return userHelper.authenticate(userDTO);
    }
}