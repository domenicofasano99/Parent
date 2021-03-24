package com.bok.parent.helper;

import com.bok.parent.dto.LoginUser;
import com.bok.parent.exception.EmailAlreadyExistsException;
import com.bok.parent.model.User;
import com.bok.parent.repository.UserRepository;
import com.bok.parent.utils.CryptoUtils;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;


@Component
@Slf4j
public class UserHelper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CryptoUtils cryptoUtils;

    public User register(LoginUser loginUser) {
        Preconditions.checkArgument(Objects.nonNull(loginUser.password));
        Preconditions.checkArgument(Objects.nonNull(loginUser.email));

        if (userRepository.existsByEmail(loginUser.email)) {
            throw new EmailAlreadyExistsException("User already registered.");
        }
        User user = new User();
        user.setEmail(loginUser.email);
        user.setPassword(cryptoUtils.encryptPassword(loginUser.password));
        user.setEnabled(true);
        user.setRole(User.Role.USER);
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Long findIdByEmail(String email) {
        return userRepository.findIdByEmail(email);
    }
}
