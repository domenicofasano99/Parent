package com.bok.parent.service;

import com.bok.parent.dto.RegisterAccount;
import com.bok.parent.model.Account;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    String register(RegisterAccount registerAccount);

    String confirm(String confirmationToken);
}
