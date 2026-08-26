package com.yarukov.backend.controller;

import com.yarukov.backend.dto.PointRequest;
import com.yarukov.backend.model.Point;
import com.yarukov.backend.model.User;
import com.yarukov.backend.repository.PointRepository;
import com.yarukov.backend.service.AuthService;
import com.yarukov.backend.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.context.i18n.LocaleContextHolder;

@RestController
@RequestMapping("/api/points")
@CrossOrigin(origins = "http://localhost:3000")
public class PointController {

    @Autowired
    private PointService pointService;

    @Autowired
    private AuthService authService;

    @Autowired
    private
    PointRepository pointRepository;

    @Autowired
    private MessageSource messageSource;

    @PostMapping
    public ResponseEntity<Point> addPoint(@Valid @RequestBody PointRequest request) {
        User user = getCurrentUser();
        return ResponseEntity.ok(pointService.savePoint(request, user));
    }

    @GetMapping
    public ResponseEntity<List<Point>> getPoints() {
        User user = getCurrentUser();
        return ResponseEntity.ok(pointRepository.findAllByOwnerOrderByExecutionTimeDesc(user));
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return authService.findByUsername(username)
                .orElseThrow(() ->
                        {
                            String msg = messageSource.getMessage(
                                    "user.not.found",
                                    null,
                                    LocaleContextHolder.getLocale()
                            );
                            return new RuntimeException(msg);
                        });
        }
}