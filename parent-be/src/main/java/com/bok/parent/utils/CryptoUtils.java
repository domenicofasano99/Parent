package com.bok.parent.utils;

import com.bok.parent.utils.encryption.JWTEncryption;
import com.bok.parent.utils.encryption.PasswordEncryption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@Slf4j
public class CryptoUtils {

    @Autowired
    JWTEncryption jwtEncryption;
    @Autowired
    PasswordEncryption passwordEncryption;
    @Value("${jwt.security.secret}")
    private String jwtSecret;

    public String encryptPassword(String plainPassword) {
        try {
            return passwordEncryption.generatePasswordHash(plainPassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Password encrypt error {0}", e);
            return null;
        }
    }

    public boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            return passwordEncryption.validatePassword(plainPassword, hashedPassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Password check error {0}", e);
            return false;
        }
    }

    public String encryptToken(String plainToken) {
        return jwtEncryption.encrypt(plainToken, jwtSecret);
    }

    public String decryptToken(String encryptedToken) {
        return jwtEncryption.decrypt(encryptedToken, jwtSecret);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // For encrypting user password
    }

}
