package com.bok.parent.service.implementation;

import com.bok.integration.parent.dto.AccountRegistrationDTO;
import com.bok.parent.helper.AccountHelper;
import com.bok.parent.helper.JWTAuthenticationHelper;
import com.bok.parent.service.AccountService;
import com.bok.parent.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    JWTAuthenticationHelper jwtAuthenticationHelper;

    @Autowired
    AccountHelper accountHelper;

    @Override
    public String register(AccountRegistrationDTO registrationDTO) {
        ValidationUtils.nonNull(registrationDTO);
        ValidationUtils.nonNull(registrationDTO.name);
        ValidationUtils.nonNull(registrationDTO.surname);
        ValidationUtils.nonNull(registrationDTO.birthdate);

        if(registrationDTO.birthdate.after(java.sql.Date.valueOf(LocalDate.now().minus(18L, ChronoUnit.YEARS)))){
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
        ValidationUtils.nonNull(registrationDTO.credentials);
        ValidationUtils.nonNull(registrationDTO.credentials.email);
        ValidationUtils.nonNull(registrationDTO.credentials.password);

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

        return accountHelper.register(registrationDTO);
    }

    @Override
    public String verify(String verificationToken) {
        ValidationUtils.nonNull(verificationToken, "Verification token cannot be null");
        log.info("Verifying user with token{}", verificationToken);
        return accountHelper.verify(verificationToken);
    }
}