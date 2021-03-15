package com.bok.parent.controller;

import com.bok.parent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/email/{userId}")
    public String getEmail(@PathVariable("userId") Long userId) {
        return userRepository.findById(userId).get().getEmail();
    }
}
