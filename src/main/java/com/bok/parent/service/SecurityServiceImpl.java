package com.bok.parent.service;

import com.bok.parent.dto.LoginUser;
import com.bok.parent.helper.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    SecurityHelper securityHelper;

    @Override
    public Object login(LoginUser loginUser) {
        return securityHelper.login(loginUser);
    }
}
