package com.bok.parent.be.utils.encryption;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import static java.util.Objects.isNull;

@Slf4j
public class CustomEncryption {
    private static CustomEncryption instance;
    private static SecretKeyFactory skf;

    @SneakyThrows
    private CustomEncryption() {
        skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    }

    public static CustomEncryption getInstance() {
        if (isNull(instance)) {
            instance = new CustomEncryption();
        }
        return instance;
    }


    private byte[] getSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException ex) {
            log.error("Error in getSalt method : {}", ex.getCause());
            throw new RuntimeException("Error");
        }

    }

    public String encrypt(String plainPassword) {
        int iterations = 1000;
        char[] chars = plainPassword.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        try {
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + ":" + toHex(salt) + ":" + toHex(hash);
        } catch (InvalidKeySpecException ex) {
            log.error("Error while encrypting");
            throw new RuntimeException();
        }

    }

    private String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public boolean validate(String plainPassword, String hashedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = hashedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(plainPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
