package com.example.authserver.service;

import com.example.authserver.entity.User;
import com.example.authserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;

    public User getByName(String name) {
        log.info("Getting user: name={}", name);
        User user = repository.findByName(name);
        log.info("Getting user: user={}", user);
        return user;
    }

}
