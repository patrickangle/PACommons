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

import com.patrickangle.commons.awt.strokes.EdgeStroke;
import com.patrickangle.commons.laf.modern.ModernLookAndFeel;
import com.patrickangle.commons.util.Colors;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 *
 * @author patrickangle
 */
public class PaintingUtils {
    public static final int FOCUSABLE_COMPONENT_INSET_SIZE = 3;

    public static void paintShadow(Graphics2D g, JComponent c, Shape shape) {
        float alpha = (float) ModernLookAndFeel.colors.componentShadowColor(c).getAlpha() / 255f / (float) FOCUSABLE_COMPONENT_INSET_SIZE;
        g.setColor(Colors.transparentColor(ModernLookAndFeel.colors.componentShadowColor(c), Math.max(0f, Math.min(alpha, 1f))));
        for (float i = .5f; i <= FOCUSABLE_COMPONENT_INSET_SIZE / 2f; i+=.5f) {
            g.setStroke(new EdgeStroke(i, EdgeStroke.Align.Outside));
            g.draw(shape);
        }
    }

    public static void paintText(Graphics2D g, JComponent c, Rectangle textRect, String text, int textShiftOffset) {
        g.setPaint(ModernLookAndFeel.colors.componentNormalTextPaint(c));
        FontMetrics metrics = c.getFontMetrics(c.getFont());
        BasicGraphicsUtils.drawString(c, g, text, textRect.x + textShiftOffset, textRect.y + textShiftOffset + metrics.getAscent());
    }

    public static void paintFocusRing(Graphics2D g, JComponent c, Shape shape) {
        if (c instanceof AbstractButton) {
            if (!((AbstractButton) c).isFocusPainted()) {
                return;
            }
        }
        g.setPaint(ModernLookAndFeel.colors.componentFocusRingPaint(c));
        g.setStroke(new EdgeStroke(FOCUSABLE_COMPONENT_INSET_SIZE, EdgeStroke.Align.Outside, EdgeStroke.Cap.Round, EdgeStroke.Join.Round));
        g.draw(shape);
    }
}
