package com.yarukov.backend.controller;


import com.yarukov.backend.model.Point;
import com.yarukov.backend.model.User;
import com.yarukov.backend.repository.PointRepository;
import com.yarukov.backend.service.AuthService;
import com.yarukov.backend.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PointControllerTest {

    @Mock
    private PointRepository pointRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    PointController pointController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPointsSuccessful(){
        String username = "testUser";
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        User user = new User();
        user.setUsername(username);
        List<Point> mockPoints = Arrays.asList(new Point(), new Point());
        when(authService.findByUsername(username)).thenReturn(Optional.of(user));
        when(pointRepository.findAllByOwnerOrderByExecutionTimeDesc(user)).thenReturn(mockPoints);
        ResponseEntity<List<Point>> response = pointController.getPoints();
        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(mockPoints, response.getBody());

    }




}
