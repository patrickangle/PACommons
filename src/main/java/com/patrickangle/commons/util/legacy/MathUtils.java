/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util.legacy;

import com.patrickangle.commons.types.Point;
import java.awt.Dimension;

/**
 * NOTE: All angle operations work in radians, but travel clockwise instead of counter-clock-wise.
 * @author Patrick Angle
 */
public class MathUtils {
    public static boolean pointsAreWithinSharedRadius(Point.IntegerPoint a, Point.IntegerPoint b, int radius) {
        return MathUtils.distanceBetweenPoints(a, b) <= radius;
    }
    
    public static double distanceBetweenPoints(Point.IntegerPoint a, Point.IntegerPoint b) {
        return Math.hypot(a.getX() - b.getX(), a.getY() - b.getY());
    }
    
    public static Point.DoublePoint vectorBetweenPoints(Point a, Point b) {
        double x = b.getX().doubleValue() - a.getX().doubleValue();
        double y = b.getY().doubleValue() - a.getY().doubleValue();
        
        return new Point.DoublePoint(x, y);
    }
    
    public static double angleFromVector(Point.DoublePoint vector) {
        return Math.atan2(vector.getY(), vector.getX());
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
