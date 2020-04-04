package com.ibm.certificationsserver.util;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CustomPasswordEncoder implements PasswordEncoder {
    private static byte[] getEncodedHash(String rawPassword) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToBase64(byte[] hash) {
        return Base64.getEncoder().encodeToString(hash);
    }

    public String encode(String rawPassword) {
        try {
            return bytesToBase64(getEncodedHash(rawPassword));
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return encode(rawPassword.toString());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}