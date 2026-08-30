package com.yarukov.backend.service;

import com.yarukov.backend.model.User;
import com.yarukov.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    @InjectMocks
    AuthService authService;

    String username;
    String password;
    String encodedPassword;

    @BeforeEach
    void setUp(){
        username = "username";
        password = "password";
        encodedPassword = "encoded_hash_xyz";
    }

    @Test

    void testSuccesRegister(){
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        assertTrue(authService.register(username,password));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        when(userRepository.existsByUsername(username)).thenReturn(true);
        assertFalse(authService.register(username, password));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPasswordHash(encodedPassword);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        Optional<User> result = authService.login(username, password);
        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
    }

    @Test
    void testLoginWrongPassword() {
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPasswordHash(encodedPassword);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrong_pass", encodedPassword)).thenReturn(false);
        Optional<User> result = authService.login(username, "wrong_pass");
        assertTrue(result.isEmpty());
    }

}
