package com.bok.parent.be.service.implementation;

import com.bok.parent.be.helper.AccountHelper;
import com.bok.parent.be.helper.AuthenticationHelper;
import com.bok.parent.be.helper.EmailHelper;
import com.bok.parent.be.service.AccountService;
import com.bok.parent.be.service.bank.BankService;
import com.bok.parent.be.utils.ValidationUtils;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.integration.dto.AccountRegistrationResponseDTO;
import com.bok.parent.integration.dto.PasswordResetRequestDTO;
import com.bok.parent.integration.dto.PasswordResetResponseDTO;
import com.bok.parent.integration.dto.VerificationResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    AuthenticationHelper authenticationHelper;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    EmailHelper emailHelper;

    @Autowired
    BankService bankService;

    @Override
    public AccountRegistrationResponseDTO register(AccountRegistrationDTO registrationDTO) {
        ValidationUtils.nonNull(registrationDTO);
        ValidationUtils.nonNull(registrationDTO.credentials);
        ValidationUtils.nonNull(registrationDTO.credentials.email);
        ValidationUtils.nonNull(registrationDTO.credentials.password);
        ValidationUtils.checkEmail(registrationDTO.credentials.email, "The given email is not valid!");
        ValidationUtils.nonNull(registrationDTO.name);
        ValidationUtils.validateName(registrationDTO.name);
        ValidationUtils.nonNull(registrationDTO.surname);
        ValidationUtils.validateSurname(registrationDTO.surname);
        ValidationUtils.nonNull(registrationDTO.birthdate);

        if (registrationDTO.birthdate.after(java.sql.Date.valueOf(LocalDate.now().minus(18L, ChronoUnit.YEARS)))) {
            throw new IllegalArgumentException("User must be 18 or over to register");
        }
        ValidationUtils.nonNull(registrationDTO.business);
        if (registrationDTO.business) {
            if (registrationDTO.vatNumber == null && registrationDTO.fiscalCode == null) {
                log.info("Rejected register request due to vatNumber and fiscalCode not given, {}", registrationDTO);
                throw new IllegalArgumentException("Account is business but no vatNumber or fiscalCode were given.");
            }
        } else {
            ValidationUtils.nonNull(registrationDTO.gender);
            ValidationUtils.nonNull(registrationDTO.fiscalCode);
        }

        ValidationUtils.nonNull(registrationDTO.mobile);
        ValidationUtils.nonNull(registrationDTO.mobile.icc);
        ValidationUtils.nonNull(registrationDTO.mobile.number);

        ValidationUtils.nonNull(registrationDTO.address);
        ValidationUtils.nonNull(registrationDTO.address.houseNumber);
        ValidationUtils.nonNull(registrationDTO.address.street);
        ValidationUtils.nonNull(registrationDTO.address.city);
        ValidationUtils.nonNull(registrationDTO.address.county);
        ValidationUtils.nonNull(registrationDTO.address.country);
        ValidationUtils.nonNull(registrationDTO.address.postalCode);

        if (!bankService.checkCreation(registrationDTO.fiscalCode, registrationDTO.vatNumber, registrationDTO.business)) {
            throw new RuntimeException("Found duplicate account in bank system");
        }

        return accountHelper.register(registrationDTO);
    }

    @Override
    public VerificationResponseDTO verify(UUID confirmationToken) {
        ValidationUtils.nonNull(confirmationToken, "Verification token cannot be null");
        return accountHelper.verify(confirmationToken);
    }

    @Override
    public PasswordResetResponseDTO resetPassword(PasswordResetRequestDTO passwordResetRequestDTO) {
        ValidationUtils.nonNull(passwordResetRequestDTO);
        ValidationUtils.nonNull(passwordResetRequestDTO.email);
        return accountHelper.recover(passwordResetRequestDTO.email);
    }

    @Override
    public String delete(String email) {
        return accountHelper.delete(email);
    }
}