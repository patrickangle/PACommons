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
package com.patrickangle.commons.awt.strokes;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;

/**
 * A stroke that has all the properties of a BasicStroke, with the additional
 * ability to stroke the outside or inside of shapes instead of centered on the
 * edge.
 *
 * @author patrickangle
 */
public class EdgeStroke implements Stroke {

    public static enum Align {
        Center, Inside, Outside;
    }

    public static enum Cap {
        Square(BasicStroke.CAP_SQUARE),
        Round(BasicStroke.CAP_ROUND),
        Butt(BasicStroke.CAP_BUTT);

        private final int awtValue;

        Cap(int awtValue) {
            this.awtValue = awtValue;
        }

        public int getAwtValue() {
            return awtValue;
        }
    }

    public static enum Join {
        Miter(BasicStroke.JOIN_MITER),
        Bevel(BasicStroke.JOIN_BEVEL),
        Round(BasicStroke.JOIN_ROUND);

        private final int awtValue;

        Join(int awtValue) {
            this.awtValue = awtValue;
        }

        public int getAwtValue() {
            return awtValue;
        }
    }

    private final BasicStroke stroke;
    private final Align align;

    public EdgeStroke(BasicStroke stroke, Align align) {
        this.stroke = stroke;
        this.align = align;
    }
    
    public EdgeStroke(float width, Align align) {
        // If the alignment is not center, double the width of the underlying stroke.
        this.stroke = new BasicStroke(align != Align.Center ? width * 2f : width);
        this.align = align;
    }

    public EdgeStroke(float width, Align align, Cap cap, Join join) {
        this.stroke = new BasicStroke(align != Align.Center ? width * 2f : width, cap.getAwtValue(), join.getAwtValue());
        this.align = align;
    }

    public EdgeStroke(float width, Align align, Cap cap, Join join, float miterLimit) {
        this.stroke = new BasicStroke(align != Align.Center ? width * 2f : width, cap.getAwtValue(), join.getAwtValue(), miterLimit);
        this.align = align;
    }

    public EdgeStroke(float width, Align align, Cap cap, Join join, float miterLimit, float[] dash, float dashPhase) {
        this.stroke = new BasicStroke(align != Align.Center ? width * 2f : width, cap.getAwtValue(), join.getAwtValue(), miterLimit, dash, dashPhase);
        this.align = align;
    }

    public Align getAlign() {
        return align;
    }

    public float getLineWidth() {
        if (align == Align.Center) {
            return stroke.getLineWidth();
        } else {
            return stroke.getLineWidth() / 2f;
        }
    }

    public int getEndCap() {
        return stroke.getEndCap();
    }

    public int getLineJoin() {
        return stroke.getLineJoin();
    }

    public float getMiterLimit() {
        return stroke.getMiterLimit();
    }

    public float[] getDashArray() {
        return stroke.getDashArray();
    }

    public float getDashPhase() {
        return stroke.getDashPhase();
    }

    @Override
    public Shape createStrokedShape(Shape p) {
        Area stroke = new Area(this.stroke.createStrokedShape(p));
        switch (align) {
            case Inside:
                stroke.intersect(new Area(p));
                return stroke;
            case Outside:
                stroke.subtract(new Area(p));
                return stroke;
            case Center:
            default:
                return stroke;
        }
    }

}
