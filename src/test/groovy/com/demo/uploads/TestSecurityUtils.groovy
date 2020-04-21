package com.demo.uploads

import com.demo.uploads.config.PasswordHelper
import com.demo.uploads.feature.user.UserE
import com.demo.uploads.feature.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TestSecurityUtils {

    public static final String PASSWORD = 'Secret123!'
    public static final String EMAIL = "admin@admin.io"

    @Autowired
    private UserRepository userRepository

    @Autowired
    private PasswordHelper passwordHelper

    void ensureUserExists(String email, String password) {
        if (userRepository.findByEmail(email) == null) {
            userRepository.save(UserE.builder().email(email).passwordHash(passwordHelper.generateHash(password)).build())
        }
    }
}
