/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util.legacy;

import java.awt.Dimension;

/**
 * @author Patrick Angle
 */
public class MathUtils {    
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
