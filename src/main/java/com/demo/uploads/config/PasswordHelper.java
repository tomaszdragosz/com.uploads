package com.demo.uploads.config;

import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

@Component
public class PasswordHelper implements PasswordEncoder {

    private static final String ALGORITHM = "SHA-512";
    private static final String SALT = "nzincwzemifyjf";

    @SneakyThrows
    public String generateHash(String notEncoded) {
        if (notEncoded == null) {
            return null;
        }
        MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
        messageDigest.update(notEncoded.getBytes());
        messageDigest.update(SALT.getBytes());
        return DatatypeConverter.printHexBinary(messageDigest.digest());
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return generateHash(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
