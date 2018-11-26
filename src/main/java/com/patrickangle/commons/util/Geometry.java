/*
 * Patrick Angle Commons Library
 * Copyright 2018 Patrick Angle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrickangle.commons.util;

import java.awt.geom.Point2D;


/**
 *
 * @author patrickangle
 */
public class Geometry {    
    public static Point2D offsetPoint(Point2D p, double offsetX, double offsetY) {
        return new Point2D.Double(p.getX() + offsetX, p.getY() + offsetY);
    }
    
    public static Point2D offsetPoint(Point2D p, Point2D offset) {
        return Geometry.offsetPoint(p, offset.getX(), offset.getY());
    }
    
    public static Point2D scalePoint(Point2D p, double scaleX, double scaleY) {
        return new Point2D.Double(p.getX() * scaleX, p.getY() * scaleY);
    }
    
    public static Point2D scalePoint(Point2D p, double scale) {
        return Geometry.scalePoint(p, scale, scale);
    }
    
    public static Point2D scalePoint(Point2D p, Point2D scale) {
        return Geometry.scalePoint(p, scale.getX(), scale.getY());
    }
    
    public static Point2D rotatePoint(Point2D p, double degrees) {
        double x = ((p.getX() * Math.cos(-Math.toRadians(degrees))) - (p.getY() * Math.sin(-Math.toRadians(degrees))));
        double y = ((p.getY() * Math.cos(-Math.toRadians(degrees))) - (p.getX() * Math.sin(-Math.toRadians(degrees))));
        
        return new Point2D.Double(x, y);
    }
    
    public static Point2D shearPoint(Point2D p, double shearX, double shearY) {
        double x = (p.getX() + (shearX * p.getY()));
        double y = (p.getY() + (shearY * p.getX()));
            
        return new Point2D.Double(x, y);
    }
    
    public static Point2D shearPoint(Point2D p, Point2D shear) {
        return Geometry.shearPoint(p, shear.getX(), shear.getY());
    }
}
