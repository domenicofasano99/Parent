package com.bok.parent.service;

import com.bok.parent.dto.AccountLoginDTO;
import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    String login(AccountLoginDTO accountLoginDTO);

    Long extractAccountId(String token);
}
