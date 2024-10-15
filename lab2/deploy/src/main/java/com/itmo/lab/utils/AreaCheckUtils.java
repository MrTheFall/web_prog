package com.itmo.lab.utils;

import java.util.Arrays;
import java.util.List;

public class AreaCheckUtils {

    public static String validatePrecision(String yStr, double y) {
        String cleanedYStr = cleanTrailingZeros(String.valueOf(y));
        
        if (!cleanTrailingZeros(yStr).equals(cleanedYStr)) {
            return "Precision error in 'y' value: " + yStr;
        }
        return null;

    }

    public static String cleanTrailingZeros(String value) {
        if (value.contains(".")) {
            value = value.replaceAll("0+$", "");
            value = value.replaceAll("\\.$", "");
        }
        return value;
    }

    public static boolean isValidValues(int x, double y, int r) {
        List<Integer> validXValues = Arrays.asList(-4, -3, -2, -1, 0, 1, 2, 3, 4);
        List<Integer> validRValues = Arrays.asList(1, 2, 3, 4, 5);
        return validXValues.contains(x) && y >= -5.0 && y <= 5.0 && validRValues.contains(r);
    }

    public static boolean checkArea(int x, double y, int r) {
        return (x >= -r && x <= 0 && y <= r / 2 && y >= 0) || // Rectangle
               (x >= 0 && y >= x / 2 - r / 2 && y <= 0) ||     // Triangle
               (x >= 0 && y >= 0 && (x * x + y * y <= r * r)); // Circle
    }
}
