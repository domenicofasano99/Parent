package com.bok.parent.be.service;

import com.bok.parent.integration.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    LoginResponseDTO login(AccountLoginDTO accountLoginDTO);

    Long getAccountId(String token);

    TokenInfoResponseDTO tokenInfo(String token);

    KeepAliveResponseDTO keepAlive(String token);

    LogoutResponseDTO logout(String token);

    LastAccessInfoDTO lastAccessInfo(String token);

    PasswordChangeResponseDTO changePassword(String token, PasswordChangeRequestDTO passwordChangeRequestDTO);

    Boolean checkTokenValidity(String token);

    Boolean passwordResetNeeded(String token);

    Boolean confirmCard(Long accountId, String confirmationToken);
}
