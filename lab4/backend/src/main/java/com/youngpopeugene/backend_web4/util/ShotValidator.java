package com.example.AreaChecker.util;

public class ShotValidator {
    public static boolean isHit(double x, double y, double r){
        return (x >= -r && x <= 0 && y <= r / 2 && y >= 0) || // Rectangle
               (x >= 0 && y >= x / 2 - r / 2 && y <= 0) ||     // Triangle
               (x >= 0 && y >= 0 && (x * x + y * y <= r * r)); // Circle
    }
}
