package com.bok.parent.service;

import com.bok.parent.dto.AccountRegistrationDTO;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    String register(AccountRegistrationDTO accountRegistrationDTO);

    String verify(String verificationToken);
}
