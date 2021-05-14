package com.bok.parent.service;

import com.bok.integration.parent.LoginResponseDTO;
import com.bok.integration.parent.dto.AccountLoginDTO;
import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    LoginResponseDTO login(AccountLoginDTO accountLoginDTO);

    Long extractAccountId(String token);
}
