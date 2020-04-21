package com.demo.uploads.feature.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserE, Long> {
    UserE findByEmail(String email);
}
