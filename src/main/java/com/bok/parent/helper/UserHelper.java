package com.bok.parent.helper;

import com.bok.integration.UserCreationDTO;
import com.bok.integration.parent.dto.AuthenticationResponseDTO;
import com.bok.parent.dto.UserDTO;
import com.bok.parent.exception.UserException;
import com.bok.parent.messaging.MessageProducer;
import com.bok.parent.model.User;
import com.bok.parent.repository.UserRepository;
import com.bok.parent.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.bok.parent.exception.UserException.UserExceptionCode.*;

@Component
@Slf4j
public class UserHelper implements UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageProducer messageProducer;

    @Override
    public UserDetails loadUserByUsername(String s) {
        List<SimpleGrantedAuthority> roles;
        User user = userRepository.findByUsername(s);
        if (Objects.nonNull(user)) {
            if (user.getEnabled()) {
                roles = Arrays.asList(new SimpleGrantedAuthority(user.getRole()));
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
            } else {
                throw new UserException(USER_NOT_ENABLED);
            }
        }
        throw new UserException(USER_NOT_FOUND);
    }

    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.email)) {
            throw new UserException(EMAIL_ALREADY_EXISTS);
        } else if (userRepository.existsByUsername(userDTO.username)) {
            throw new UserException(USERNAME_ALREADY_EXISTS);
        } else {
            User u = userRepository.save(User.builder()
                    .username(userDTO.username)
                    .password(bcryptEncoder.encode(userDTO.password))
                    .role(userDTO.role.toUpperCase())
                    .enabled(true)
                    .email(userDTO.role)
                    .build());
            messageProducer.sendUserModify(UserCreationDTO.builder()
                    .id(u.getId())
                    .email(u.getEmail())
                    .build());
            return u;
        }
    }

    public User updateUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.username)) {
            User u = userRepository.findById(userDTO.id).orElseThrow(() -> new UserException(BAD_ID_PROVIDED));
            u.setUsername(userDTO.username);
            u.setPassword(userDTO.password);
            u.setEmail(userDTO.email);
            if (userRepository.existsByEmail(userDTO.email)) {
                throw new UserException(EMAIL_ALREADY_EXISTS);
            } else if (userRepository.existsByUsername(userDTO.username)) {
                throw new UserException(USERNAME_ALREADY_EXISTS);
            } else {
                userRepository.save(u);
                return u;
            }
        }
        throw new UserException(USER_NOT_EXISTS);
    }

    public AuthenticationResponseDTO authenticate(UserDTO userDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userDTO.username, userDTO.password));
        } catch (DisabledException e) {
            throw new UserException(UserException.UserExceptionCode.USER_NOT_ENABLED);
        } catch (BadCredentialsException e) {
            throw new UserException(UserException.UserExceptionCode.INVALID_CREDENTIALS);
        }
        UserDetails userdetails = loadUserByUsername(userDTO.username);
        String token = jwtUtil.generateToken(userdetails);
        String role = userRepository.findByUsername(userDTO.username).getRole();
        return AuthenticationResponseDTO.builder()
                .token(token)
                .role(role)
                .build();
    }
}
