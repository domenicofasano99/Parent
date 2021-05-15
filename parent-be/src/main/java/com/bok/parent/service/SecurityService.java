package com.bok.parent.service;

import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    LoginResponseDTO login(AccountLoginDTO accountLoginDTO);

    Long extractAccountId(String token);
}
