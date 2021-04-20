package com.bok.parent.service;

import com.bok.parent.dto.RegisterAccount;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    String register(RegisterAccount registerAccount);

    String verify(String verificationToken);
}
