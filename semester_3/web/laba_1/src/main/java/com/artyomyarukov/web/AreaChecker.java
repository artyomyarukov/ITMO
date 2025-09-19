package com.artyomyarukov.web;

public class AreaChecker {
    public static boolean checkHit(double x, double y, double r) {
        // Check if the point is within the quarter circle
        if (x <= 0 && y <= 0 && (x * x + y * y <= r * r)) {
            return true;
        }
        // Check if the point is within the rectangle
        if (x <= 0 && y >= 0 && x >= -r && y <= r/2) {
            return true;
        }
        // Check if the point is within the triangle
        if (x >= 0 && y >= 0 && y<=r/2 && x<=r) {
            return true;
        }
        return false;
    }
}
