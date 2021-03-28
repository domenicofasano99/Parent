package com.bok.parent.service;

import com.bok.parent.dto.LoginAccount;
import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    Object login(LoginAccount loginAccount);

    Long extractAccountId(String token);
}
