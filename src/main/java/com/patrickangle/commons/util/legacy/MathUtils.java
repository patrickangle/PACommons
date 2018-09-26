/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util.legacy;

import com.patrickangle.commons.types.Point;

/**
 *
 * @author Patrick Angle
 */
@Deprecated(forRemoval = true)
public class MathUtils {
    @Deprecated(forRemoval = true)
    public static boolean pointsAreWithinSharedRadius(Point.IntegerPoint a, Point.IntegerPoint b, int radius) {
        return MathUtils.distanceBetweenPoints(a, b) <= radius;
    }
    
    @Deprecated(forRemoval = true)
    public static double distanceBetweenPoints(Point.IntegerPoint a, Point.IntegerPoint b) {
        return (Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2)));
    }
}
