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
package com.patrickangle.commons.laf.modern.util;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 *
 * @author patrickangle
 */
public class ShapeUtils {
    public static enum Corner {
        TopLeft, TopRight, BottomLeft, BottomRight;
    }

    public static Shape roundedRectangle(float x, float y, float width, float height, float cornerRadius) {
        return new RoundRectangle2D.Float(x, y, width, height, cornerRadius * 2, cornerRadius * 2);
    }
    
    public static Shape roundedRectangle(float x, float y, float width, float height, float cornerRadius, List<Corner> roundedCorners) {
        Area base = new Area(roundedRectangle(x, y, width, height, cornerRadius));
        
        if (!roundedCorners.contains(Corner.TopLeft)) {
            base.add(new Area(new Rectangle2D.Float(x, y, cornerRadius * 2, cornerRadius * 2)));
        }
        
        if (!roundedCorners.contains(Corner.TopRight)) {
            base.add(new Area(new Rectangle2D.Float(x + width - (cornerRadius * 2), y, cornerRadius * 2, cornerRadius * 2)));
        }
        
        if (!roundedCorners.contains(Corner.BottomLeft)) {
            base.add(new Area(new Rectangle2D.Float(x, y + height - (cornerRadius * 2), cornerRadius * 2, cornerRadius * 2)));
        }
        
        if (!roundedCorners.contains(Corner.BottomRight)) {
            base.add(new Area(new Rectangle2D.Float(x + width - (cornerRadius * 2), y + height - (cornerRadius * 2), cornerRadius * 2, cornerRadius * 2)));
        }
        
        return base;
    }
}
