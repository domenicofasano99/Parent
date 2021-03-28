package com.bok.parent.service;

import com.bok.parent.dto.LoginAccount;
import com.bok.parent.helper.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    SecurityHelper securityHelper;

    @Override
    public Object login(LoginAccount loginAccount) {
        return securityHelper.login(loginAccount);
    }

    @Override
    public Long extractAccountId(String token){
        return securityHelper.extractAccountId(token);
    }
}
