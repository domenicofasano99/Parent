package com.bok.parent.service;

import com.bok.parent.dto.LoginUser;
import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    Object login(LoginUser loginUser);
}
