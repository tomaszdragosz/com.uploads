package com.demo.uploads.feature.user;

import com.demo.uploads.config.PasswordHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordHelper passwordHelper;

    public UserE createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        return repository.save(UserE.builder()
                .email(createUserDto.getEmail())
                .passwordHash(passwordHelper.generateHash(createUserDto.getPassword()))
                .build());
    }

    public UserE findByEmail(String email){
        return repository.findByEmail(email);
    }
}
