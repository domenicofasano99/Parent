package com.bok.parent.controller;

import com.bok.parent.dto.LoginUser;
import com.bok.parent.model.User;
import com.bok.parent.service.SecurityService;
import com.bok.parent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    SecurityService securityService;

    @PostMapping("/login")
    public Object login(@RequestBody LoginUser user) {
        return securityService.login(user);
    }


    @PostMapping("/register")
    public User register(@RequestBody LoginUser loginUser) {
        return userService.register(loginUser);
    }
}
