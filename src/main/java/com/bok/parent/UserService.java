package com.bok.parent;

import com.bok.parent.exception.*;
import com.bok.parent.model.AccountUserDetails;
import com.bok.parent.model.Role;
import com.bok.parent.model.User;
import com.bok.parent.repository.RoleRepository;
import com.bok.parent.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenManager jwtTokenManager;

    @Autowired
    TotpManager totpManager;

    @Autowired
    RoleRepository roleRepository;

    public String loginUser(String username, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userRepository.findByUsername(username).get();
        if (user.isMfa()) {
            return "";
        }

        return jwtTokenManager.generateToken(authentication);
    }

    public String verify(String username, String code) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("username %s", username)));

        if (!totpManager.verifyCode(code, user.getSecret())) {
            throw new BadRequestException("Code is incorrect");
        }

        return Optional.of(user)
                .map(AccountUserDetails::new)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()))
                .map(jwtTokenManager::generateToken)
                .orElseThrow(() ->
                        new InternalServerException("unable to generate access token"));
    }

    public User registerUser(User user, Role role) {
        log.info("registering user {}", user.getUsername());

        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("username {} already exists.", user.getUsername());

            throw new UsernameAlreadyExistsException(
                    String.format("username %s already exists", user.getUsername()));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("email {} already exists.", user.getEmail());

            throw new EmailAlreadyExistsException(
                    String.format("email %s already exists", user.getEmail()));
        }

        Optional<Role> or = roleRepository.findByName(role.getName());
        if (!or.isPresent()) {
            role = roleRepository.save(new Role(role.getName()));
        }
        Role r = role;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(r);
        user.setRoles(roleSet);

        if (user.isMfa()) {
            user.setSecret(totpManager.generateSecret());
        }

        return userRepository.save(user);
    }

    public List<User> findAll() {
        log.info("retrieving all users");
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        log.info("retrieving user {}", username);
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        log.info("retrieving user {}", id);
        return userRepository.findById(id);
    }
}