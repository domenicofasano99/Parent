package com.bok.parent.service;

import com.bok.parent.dto.LoginUser;
import com.bok.parent.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User register(LoginUser userDTO);
}
