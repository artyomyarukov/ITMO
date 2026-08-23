package com.yarukov.backend.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PointServiceTest {

    private PointService pointService;
    @BeforeEach
    void Setup(){
        pointService = new PointService();
    }


    @Test
    void testRectangleSuccess() {
        assertTrue(pointService.calculateHit(0.1, 0.1, 1));
    }
    @Test
    void testRectangleBorder() {
        assertTrue(pointService.calculateHit(0.5, 1, 1));
    }
    @Test
    void testRectangleFail() {
        assertFalse(pointService.calculateHit(1, 0.5, 1));
    }

    @Test
    void testCircleSuccess() {
        assertTrue(pointService.calculateHit(-0.5, 0.5, 1));
    }

    @Test
    void testCircleBorder() {
        assertTrue(pointService.calculateHit(-1, 0, 1));
    }

    @Test
    void testCircleFail() {

        assertFalse(pointService.calculateHit(-1, 1, 1));
    }

    @Test
    void testTriangleSuccess() {
        assertTrue(pointService.calculateHit(0.1, -0.1, 1));
    }

    @Test
    void testTriangleBorder() {
        assertTrue(pointService.calculateHit(0.25, -0.5, 1));
    }

    @Test
    void testTriangleFail() {
        assertFalse(pointService.calculateHit(0.6, -0.1, 1));
    }




}
