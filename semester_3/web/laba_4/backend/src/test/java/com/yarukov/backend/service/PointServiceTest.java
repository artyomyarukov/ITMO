package com.yarukov.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PointServiceTest {

    private PointService pointService;
    @BeforeEach
    void Setup(){
        pointService = new PointService();
    }


    @Test
    void calculateHit(){
        assertTrue(pointService.calculateHit(0.1,0.1,1));
    }
}
