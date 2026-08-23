package com.yarukov.backend.controller;

import com.yarukov.backend.config.JwtUtils;
import com.yarukov.backend.dto.AuthRequest;
import com.yarukov.backend.model.User;
import com.yarukov.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")

@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MessageSource messageSource;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        boolean success = authService.register(request.getUsername(), request.getPassword());
        String msg;
        if (success) {
            msg = messageSource.getMessage("auth.register.succes",null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", msg));
        }
        msg = messageSource.getMessage("auth.register.error",null,LocaleContextHolder.getLocale());
        return ResponseEntity.badRequest()
                .body(Map.of("error", msg));
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
        String msg =  messageSource.getMessage("auth.login.error",null,LocaleContextHolder.getLocale());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", msg));
    }
}