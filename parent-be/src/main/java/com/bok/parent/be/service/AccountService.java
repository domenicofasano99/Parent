package com.bok.parent.be.service;

import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.AccountRegistrationResponseDTO;
import com.bok.parent.integration.dto.PasswordResetRequestDTO;
import com.bok.parent.integration.dto.PasswordResetResponseDTO;
import com.bok.parent.integration.dto.VerificationResponseDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface AccountService {
    AccountRegistrationResponseDTO register(AccountRegistrationDTO accountRegistrationDTO);

    VerificationResponseDTO verify(UUID confirmationToken);

    PasswordResetResponseDTO resetPassword(PasswordResetRequestDTO passwordResetRequestDTO);

    String delete(String email);
}
