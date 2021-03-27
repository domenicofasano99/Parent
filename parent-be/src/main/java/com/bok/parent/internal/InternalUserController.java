package com.bok.parent.internal;

import com.bok.parent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController("/internal")
public class InternalUserController {

    @Autowired
    UserRepository userRepository;

}
