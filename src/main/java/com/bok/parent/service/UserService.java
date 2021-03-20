package com.bok.parent.service;

import com.bok.integration.parent.dto.AuthenticationResponseDTO;
import com.bok.parent.dto.UserDTO;
import com.bok.parent.model.User;

public interface UserService {
    User createUser(UserDTO userDTO);

    AuthenticationResponseDTO authenticate(UserDTO userDTO);
}
