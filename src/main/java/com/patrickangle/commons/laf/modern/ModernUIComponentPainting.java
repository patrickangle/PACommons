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
package com.patrickangle.commons.laf.modern;

import com.patrickangle.commons.util.Colors;
import com.patrickangle.commons.util.GraphicsHelpers;
import static com.patrickangle.commons.util.GraphicsHelpers.enableAntialiasing;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 *
 * @author patrickangle
 */
public class ModernUIComponentPainting {

    public static void paintComponentBackgroundFill(Graphics2D g, AbstractButton button, Shape buttonShape) {
        final ButtonModel buttonModel = button.getModel();

        if (button.isEnabled()) {
            // Enabled
            if (buttonModel.isPressed()) {
                // Enabled + Pressed
                if (buttonIsDefaultOrSelected(button)) {
                    // Enabled + Pressed + Default or Selected
                    g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_DARK_COLOR_KEY));
                } else if (button.isFocusOwner()) {
                    // Enabled + Pressed + In Focus
                    g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_DARK_COLOR_KEY));
                } else {
                    // Enabled + Pressed + Normal
                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
                }
            } else if (buttonModel.isRollover()) {
                // Enabled + Hover
                if (buttonIsDefaultOrSelected(button)) {
                    // Enabled + Hover + Default or Selected
                    g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_LIGHT_COLOR_KEY));
                } else if (button.isFocusOwner()) {
                    // Enabled + Hover + In Focus
                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY));
                } else {
                    // Enabled + Hover + Normal
                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY));
                }
            } else {
                // Enabled + Normal
                if (buttonIsDefaultOrSelected(button)) {
                    // Enabled + Normal + Default or Selected
                    g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_MEDIUM_COLOR_KEY));
                } else if (button.isFocusOwner()) {
                    // Enabled + Normal + In Focus
                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
                } else {
                    // Enabled + Normal + Normal
                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
                }
            }
        } else {
            // Disabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
        }

        g.fill(buttonShape);
    }

    public static void paintComponentBorderHighlight(Graphics2D g, AbstractButton button, Shape buttonShape) {
        g.setStroke(new BasicStroke(0.5f));

        if (button.isEnabled()) {
            // Enabled
            if (buttonIsDefaultOrSelected(button)) {
                // Enabled + Default or Selected
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_MEDIUM_COLOR_KEY));
            } else if (button.isFocusOwner()) {
                // Enabled + In Focus
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY));
            } else {
                // Enabled + Normal
                g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY));
            }
        } else {
            // Disabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
        }
        
        g.draw(buttonShape);
    }

    public static void paintComponentShadowOrFocus(Graphics2D g, Component component, Shape shape) {//Graphics graphics, int x, int y, int width, int height) {
        if (component.isFocusOwner()) {
            g.setColor(Colors.transparentColor(UIManager.getColor(ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY), 0.5f));
        } else {
            g.setColor(Colors.transparentColor(UIManager.getColor(ModernUIUtilities.SHADOW_COLOR_KEY), 0.25f));
        }
        
        Graphics2D g2 = (Graphics2D) g.create();
        enableAntialiasing(g2);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g2.translate(0.0,0.0);

        Color baseColor = g2.getColor();
        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, baseColor.getAlpha() / 255.0f));
                int shadowWidth = 3;
        int sw = shadowWidth * 2;
        for (int i = sw; i >= 2; i -= 2) {
            float pct = (float) (sw - i) / (sw - 1);
            
            g2.setColor(Colors.transparentColor(baseColor, pct));
            
            g2.setStroke(new BasicStroke(i));
            g2.draw(shape);
        }
        g2.dispose();
    }

    public static void paintComponentText(Graphics g, JComponent component, Rectangle textRect, String text, int textShiftOffset) {
        Graphics2D g2 = (Graphics2D) g.create();
        GraphicsHelpers.enableAntialiasing(g2);
        ModernUIComponentPainting.paintComponentText(g2, component, textRect, text, textShiftOffset);
        g2.dispose();
    }

    public static void paintComponentText(Graphics2D g, JComponent component, Rectangle textRect, String text, int textShiftOffset) {
        if (component.isEnabled()) {
            // Enabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));
        } else {
            // Disabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
        }

        int mnemonicIndex = (component instanceof AbstractButton) ? ((AbstractButton) component).getDisplayedMnemonicIndex() : -1;
        FontMetrics metrics = component.getFontMetrics(component.getFont());
        BasicGraphicsUtils.drawStringUnderlineCharAt(component, g, text, mnemonicIndex, textRect.x + textShiftOffset, textRect.y + metrics.getAscent() + textShiftOffset);
    }

    public static void paintComponentCheckMark(Graphics2D g, Component button, Shape shape) {
        int x1 = shape.getBounds().width / 4;
        int y1 = shape.getBounds().height / 3;
        int x2 = x1 + shape.getBounds().width / 6;
        int y2 = y1 + shape.getBounds().height / 4;
        int x3 = shape.getBounds().width - 2;
        int y3 = -1;

        if (button.isEnabled()) {
            // Enabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));
        } else {
            // Disabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
        }

        g.setStroke(new BasicStroke(1.65f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x2, y2, x3, y3);
    }
    
//    public static void paintComponentDownArrow(Graphics2D g, Component component) {
//        g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
//        final int xU = Math.min(component.getWidth(), component.getHeight()) / 4;
//        final int yU = Math.min(component.getWidth(), component.getHeight()) / 4;
//        final Path2D.Double path = new Path2D.Double();
//        path.moveTo(xU + 1, yU + 2);
//        path.lineTo(3 * xU + 1, yU + 2);
//        path.lineTo(2 * xU + 1, 3 * yU);
//        path.lineTo(xU + 1, yU + 2);
//        path.closePath();
//        
//        if (component.isEnabled()) {
//            // Enabled
//            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));
//        } else {
//            // Disabled
//            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
//        }
//        
//        g.fill(path);
//    }

    public static void paintComponentRadioMark(Graphics2D g, Component component, Shape shape) {
        Shape radioMark = new Ellipse2D.Double(shape.getBounds().x + 4, shape.getBounds().y + 4, shape.getBounds().width - 8, shape.getBounds().height - 8);

        if (component.isEnabled()) {
            // Enabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));
        } else {
            // Disabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
        }
        
        g.fill(radioMark);
    }

    protected static boolean buttonIsDefaultOrSelected(AbstractButton button) {
        return (button instanceof JButton && ((JButton) button).isDefaultButton()) || (button instanceof JToggleButton && button.isSelected());
    }
}
