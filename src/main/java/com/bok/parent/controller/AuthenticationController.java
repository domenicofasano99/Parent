package com.bok.parent.controller;

import com.bok.parent.dto.UserDTO;
import com.bok.parent.model.User;
import com.bok.parent.security.annotations.AllowLogged;
import com.bok.parent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Object login(@RequestBody UserDTO user) {
        return userService.login(user);
    }


    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }
}
