package com.bok.parent.be.service;

import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.AccountRegistrationResponseDTO;
import com.bok.parent.integration.dto.PasswordResetRequestDTO;
import com.bok.parent.integration.dto.PasswordResetResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    AccountRegistrationResponseDTO register(AccountRegistrationDTO accountRegistrationDTO);

    Boolean verify(String confirmationToken);

    PasswordResetResponseDTO resetPassword(PasswordResetRequestDTO passwordResetRequestDTO);

    String closeAccount(String email);
}
