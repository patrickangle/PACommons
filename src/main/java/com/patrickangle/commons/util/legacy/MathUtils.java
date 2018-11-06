/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util.legacy;

import com.patrickangle.commons.types.Point;
import java.awt.Dimension;

/**
 *
 * @author Patrick Angle
 */
@Deprecated(forRemoval = true)
public class MathUtils {
    public static boolean pointsAreWithinSharedRadius(Point.IntegerPoint a, Point.IntegerPoint b, int radius) {
        return MathUtils.distanceBetweenPoints(a, b) <= radius;
    }
    
    public static double distanceBetweenPoints(Point.IntegerPoint a, Point.IntegerPoint b) {
        return (Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2)));
    }
    
    public static double scaleForNestedDimensions(Dimension desiredDimension, Dimension currentDimension) {
        double scaleX = (double) desiredDimension.width / (double) currentDimension.width;
        double scaleY = (double) desiredDimension.height / (double) currentDimension.height;
        
        return Math.min(scaleX, scaleY);
    }
    
    public static int clamp(int minimum, int number, int maximum) {
        return Math.min(maximum, Math.max(minimum, number));
    }
    
    public static long clamp(long minimum, long number, long maximum) {
        return Math.min(maximum, Math.max(minimum, number));
    }
    
    public static float clamp(float minimum, float number, float maximum) {
        return Math.min(maximum, Math.max(minimum, number));
    }
    
    public static double clamp(double minimum, double number, double maximum) {
        return Math.min(maximum, Math.max(minimum, number));
    }
}
