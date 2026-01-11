package com.yarukov.backend.service;

import com.yarukov.backend.model.User;
import com.yarukov.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            return false;
        }
        String encodedPassword = passwordEncoder.encode(password);
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(encodedPassword);
        userRepository.save(newUser);

        return true;
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}