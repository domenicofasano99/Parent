package com.bok.parent.be.security;

import com.bok.parent.be.utils.encryption.CustomEncryption;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

    private final CustomEncryption customEncryption = CustomEncryption.getInstance();

    @SneakyThrows
    @Override
    public String encode(CharSequence rawPassword) {
        return customEncryption.encrypt(rawPassword.toString());
    }

    @SneakyThrows
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return customEncryption.validate(rawPassword.toString(), encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }
}
