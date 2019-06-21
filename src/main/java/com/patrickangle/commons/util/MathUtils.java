/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util;

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

    public static double rerange(double number, double inputMinimum, double inputMaximum, double outputMinimum, double outputMaximum) {
        double clampedNumber = MathUtils.clamp(inputMinimum, number, inputMaximum);
        double normalizedClampedNumber = (clampedNumber - inputMinimum) / (inputMaximum - inputMinimum);
        double clampedOutputNumber = (normalizedClampedNumber * (outputMaximum - outputMinimum)) + outputMinimum;
        return clampedOutputNumber;
    }
    
    public static float rerange(float number, float inputMinimum, float inputMaximum, float outputMinimum, float outputMaximum) {
        float clampedNumber = MathUtils.clamp(inputMinimum, number, inputMaximum);
        float normalizedClampedNumber = (clampedNumber - inputMinimum) / (inputMaximum - inputMinimum);
        float clampedOutputNumber = (normalizedClampedNumber * (outputMaximum - outputMinimum)) + outputMinimum;
        return clampedOutputNumber;
    }
    
    public static int rerange(int number, int inputMinimum, int inputMaximum, int outputMinimum, int outputMaximum) {
        int clampedNumber = MathUtils.clamp(inputMinimum, number, inputMaximum);
        int normalizedClampedNumber = (clampedNumber - inputMinimum) / (inputMaximum - inputMinimum);
        int clampedOutputNumber = (normalizedClampedNumber * (outputMaximum - outputMinimum)) + outputMinimum;
        return clampedOutputNumber;
    }
    
    public static long rerange(long number, long inputMinimum, long inputMaximum, long outputMinimum, long outputMaximum) {
        long clampedNumber = MathUtils.clamp(inputMinimum, number, inputMaximum);
        long normalizedClampedNumber = (clampedNumber - inputMinimum) / (inputMaximum - inputMinimum);
        long clampedOutputNumber = (normalizedClampedNumber * (outputMaximum - outputMinimum)) + outputMinimum;
        return clampedOutputNumber;
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

    public static long roundUpToPowerOfTwo(long x) {
        return (long) Math.pow(2, (int) (Math.log(x - 1) / Math.log(2)) + 1);
    }

    public static double roundUpToPowerOfTwo(double x) {
        return Math.pow(2, (int) (Math.log(x - 1) / Math.log(2)) + 1);
    }

    public static int sign(int number) {
        if (number > -0) {
            return 1;
        } else if (number < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public static int sign(long number) {
        if (number > -0) {
            return 1;
        } else if (number < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public static int sign(float number) {
        if (number > -0f) {
            return 1;
        } else if (number < 0f) {
            return -1;
        } else {
            return 0;
        }
    }

    public static int sign(double number) {
        if (number > -0.0) {
            return 1;
        } else if (number < 0.0) {
            return -1;
        } else {
            return 0;
        }
    }
}
