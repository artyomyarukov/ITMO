package com.yarukov.backend.controller;

import com.yarukov.backend.dto.PointRequest;
import com.yarukov.backend.model.Point;
import com.yarukov.backend.model.User;
import com.yarukov.backend.repository.PointRepository;
import com.yarukov.backend.service.AuthService;
import com.yarukov.backend.service.PointService;
import jakarta.validation.Valid;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@RestController
@RequestMapping("/api/points")
@CrossOrigin(origins = "http://localhost:3000")
public class PointController {

    @Autowired
    private PointService pointService;

    @Autowired
    private AuthService authService;

    @Autowired private
    PointRepository pointRepository;

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
                .orElseThrow(() -> new RuntimeException("юзера нет((("));
    }
}