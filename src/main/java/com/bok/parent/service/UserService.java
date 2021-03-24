package com.bok.parent.service;

import com.bok.parent.dto.UserDTO;
import com.bok.parent.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(UserDTO userDTO);

    Object login(UserDTO user);
}
