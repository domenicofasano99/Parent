package com.bok.parent.internal;

import com.bok.parent.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController("/internal")
public class InternalUserController {

    @Autowired
    AccountRepository accountRepository;

}
