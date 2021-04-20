package com.bok.parent.service.implementation;

import com.bok.parent.dto.AccountLoginDTO;
import com.bok.parent.helper.SecurityHelper;
import com.bok.parent.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    SecurityHelper securityHelper;

    @Override
    public Object login(AccountLoginDTO accountLoginDTO) {
        return securityHelper.login(accountLoginDTO);
    }

    @Override
    public Long extractAccountId(String token) {
        return securityHelper.extractAccountId(token);
    }
}
