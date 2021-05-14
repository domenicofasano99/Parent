package com.bok.parent.service;

import com.bok.integration.parent.PasswordRecoveryResponseDTO;
import com.bok.integration.parent.PasswordResetRequestDTO;
import com.bok.integration.parent.dto.AccountRegistrationDTO;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    String register(AccountRegistrationDTO accountRegistrationDTO);

    String verify(String verificationToken);

    PasswordRecoveryResponseDTO recover(PasswordResetRequestDTO passwordResetRequestDTO);
}
