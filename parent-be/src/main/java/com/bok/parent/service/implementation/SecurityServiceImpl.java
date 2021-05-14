package com.bok.parent.service.implementation;

import com.bok.integration.parent.LoginResponseDTO;
import com.bok.integration.parent.dto.AccountLoginDTO;
import com.bok.parent.helper.SecurityHelper;
import com.bok.parent.service.SecurityService;
import com.bok.parent.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    SecurityHelper securityHelper;

    @Override
    public LoginResponseDTO login(AccountLoginDTO accountLoginDTO) {
        ValidationUtils.nonNull(accountLoginDTO.email);
        ValidationUtils.nonNull(accountLoginDTO.password);
        return securityHelper.login(accountLoginDTO);
    }

    @Override
    public Long extractAccountId(String token) {
        return securityHelper.extractAccountId(token);
    }
}
