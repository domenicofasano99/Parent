package com.bok.parent.controller;

import com.bok.integration.parent.dto.AuthenticationResponseDTO;
import com.bok.parent.dto.UserDTO;
import com.bok.parent.model.User;
import com.bok.parent.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/authenticate")
    public AuthenticationResponseDTO authenticate(@RequestBody UserDTO userDTO) {
        return userServiceImpl.authenticate(userDTO);
    }

    @PostMapping
    public User register(@RequestBody UserDTO userDTO) {
        return userServiceImpl.createUser(userDTO);
    }
}
