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
package com.patrickangle.commons.laf.modern.icons;

import com.patrickangle.commons.laf.modern.ModernLookAndFeel;
import com.patrickangle.commons.laf.modern.util.GraphicsUtils;
import com.patrickangle.commons.laf.modern.util.PaintingUtils;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;
import javax.swing.Icon;

/**
 *
 * @author patrickangle
 */
public class ModernCompactButtonArrowIcon implements Icon {

    private static final int WIDTH = 5;
    private static final int HEIGHT = 2;

    private static final int PADDING = 1;

    private static final Shape ARROW_DOWN_SHAPE = createDownArrowShape();
    private static final Shape ARROW_UP_SHAPE = createUpArrowShape();
    private static final Stroke SELECTION_STROKE = new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private final boolean up;
    
    public ModernCompactButtonArrowIcon(boolean up) {
        this.up = up;
    }
    
    @Override
    public void paintIcon(Component c, Graphics graphics, int x, int y) {
        AbstractButton component = (AbstractButton) c;
        Graphics2D g = GraphicsUtils.configureGraphics(graphics);

        g.translate(x + PADDING, y + PADDING);

        g.setPaint(ModernLookAndFeel.colors.componentNormalTextPaint(component));
        g.setStroke(SELECTION_STROKE);
        g.draw(up ? ARROW_UP_SHAPE : ARROW_DOWN_SHAPE);

        g.dispose();
    }

    @Override
    public int getIconWidth() {
        return WIDTH + (PADDING * 2);
    }

    @Override
    public int getIconHeight() {
        return HEIGHT + (PADDING * 2);
    }

    private static Shape createDownArrowShape() {
        Path2D.Float s = new Path2D.Float();
        s.moveTo(0, 0);
        s.lineTo(((float) WIDTH) / 2, HEIGHT);
        s.lineTo(WIDTH, 0);
        return s;
    }

    private static Shape createUpArrowShape() {
        Path2D.Float s = new Path2D.Float();
        s.moveTo(0, HEIGHT);
        s.lineTo(((float) WIDTH) / 2, 0);
        s.lineTo(WIDTH, HEIGHT);
        return s;
    }
}
