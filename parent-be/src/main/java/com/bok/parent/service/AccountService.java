package com.bok.parent.service;

import com.bok.parent.integration.dto.PasswordRecoveryResponseDTO;
import com.bok.parent.integration.dto.PasswordResetRequestDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    String register(AccountRegistrationDTO accountRegistrationDTO);

    String verify(String verificationToken);

    PasswordRecoveryResponseDTO recover(PasswordResetRequestDTO passwordResetRequestDTO);
}
