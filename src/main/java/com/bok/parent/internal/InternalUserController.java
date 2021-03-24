package com.bok.parent.internal;

import com.bok.parent.repository.UserRepository;
import com.bok.parent.security.annotations.InternalOnly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/internal")
public class InternalUserController {

    @Autowired
    UserRepository userRepository;

    @InternalOnly
    @GetMapping("/email/{userId}")
    public String getEmail(@PathVariable("userId") Long userId) {
        return userRepository.findById(userId).get().getEmail();
    }
}
