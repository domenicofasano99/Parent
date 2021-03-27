package com.bok.parent.service;

import com.bok.parent.dto.LoginUser;
import com.bok.parent.helper.UserHelper;
import com.bok.parent.model.User;
import com.bok.parent.utils.JWTAuthenticationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    JWTAuthenticationHelper jwtAuthenticationHelper;
    @Autowired
    private UserHelper userHelper;

    @Override
    public User register(LoginUser loginUser) {
        return userHelper.register(loginUser);
    }
}