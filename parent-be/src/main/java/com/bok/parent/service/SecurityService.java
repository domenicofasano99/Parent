package com.bok.parent.service;

import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.KeepAliveRequestDTO;
import com.bok.parent.integration.dto.KeepAliveResponseDTO;
import com.bok.parent.integration.dto.LoginResponseDTO;
import com.bok.parent.integration.dto.TokenExpirationRequestDTO;
import com.bok.parent.integration.dto.TokenExpirationResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    LoginResponseDTO login(AccountLoginDTO accountLoginDTO);

    Long extractAccountId(String token);

    TokenExpirationResponseDTO tokenInfo(TokenExpirationRequestDTO tokenExpirationRequestDTO);

    KeepAliveResponseDTO keepAlive(KeepAliveRequestDTO keepAliveRequestDTO);
}
