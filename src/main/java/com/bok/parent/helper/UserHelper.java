package com.bok.parent.helper;

import com.bok.parent.dto.UserDTO;
import com.bok.parent.model.User;
import com.bok.parent.repository.UserRepository;
import com.bok.parent.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@Slf4j
public class UserHelper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CryptoUtils cryptoUtils;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.email);
        user.setPassword(cryptoUtils.encryptPassword(userDTO.password));
        user.setEnabled(true);
        return userRepository.save(user);
    }
}
