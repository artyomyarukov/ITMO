package com.yarukov.backend.controller;

import com.yarukov.backend.config.JwtUtils;
import com.yarukov.backend.dto.AuthRequest;
import com.yarukov.backend.model.User;
import com.yarukov.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


@RestController
@RequestMapping("/api/auth")

@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {


    @Autowired
    private MessageSource messageSource;




    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        boolean success = authService.register(request.getUsername(), request.getPassword());
        if (success) {
            String msg = messageSource.getMessage(
                    "register.success",
                    null,
                    LocaleContextHolder.getLocale()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", msg));
        }

        String errorMsg = messageSource.getMessage(
                "register.error.username.exists",
                null,
                LocaleContextHolder.getLocale()
        );


        return ResponseEntity.badRequest()
                .body(Map.of("error", errorMsg));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<User> userOptional = authService.login(request.getUsername(), request.getPassword());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = jwtUtils.generateToken(user.getUsername());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", user.getUsername()
            ));
        }

        String errorMsg = messageSource.getMessage(
                "login.error.credentials",
                null,
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", errorMsg));
    }
}