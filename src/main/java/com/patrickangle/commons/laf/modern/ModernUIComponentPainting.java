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

import com.patrickangle.commons.Pair;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.CPK_BUTTON_DISABLED_BACKGROUND;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.CPK_BUTTON_ENABLED_HOVER_DEFAULTORSELECTED_BACKGROUND;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.CPK_BUTTON_ENABLED_HOVER_INFOCUS_BACKGROUND;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.CPK_BUTTON_ENABLED_HOVER_NORMAL_BACKGROUND;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.CPK_BUTTON_ENABLED_NORMAL_DEFAULTORSELECTED_BACKGROUND;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.CPK_BUTTON_ENABLED_NORMAL_INFOCUS_BACKGROUND;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.CPK_BUTTON_ENABLED_NORMAL_NORMAL_BACKGROUND;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.CPK_BUTTON_ENABLED_PRESSED_DEFAULTORSELECTED_BACKGROUND;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.CPK_BUTTON_ENABLED_PRESSED_INFOCUS_BACKGROUND;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.CPK_BUTTON_ENABLED_PRESSED_NORMAL_BACKGROUND;
import com.patrickangle.commons.util.Colors;
import com.patrickangle.commons.util.CompatibleImageUtil;
import com.patrickangle.commons.util.GraphicsHelpers;
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
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.WeakHashMap;
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

    public final static String CPK_COMPONENT_ENABLED_FOREGROUND = "CPK_COMPONENT_ENABLED_FOREGROUND";
    public final static String CPK_COMPONENT_DISABLED_FOREGROUND = "CPK_COMPONENT_DISABLED_FOREGROUND";

//    public final static String CPK_BUTTON_ENABLED_PRESSED_DEFAULTORSELECTED_BACKGROUND = "CPK_BUTTON_ENABLED_PRESSED_DEFAULTORSELECTED_BACKGROUND";
//    public final static String CPK_BUTTON_ENABLED_PRESSED_INFOCUS_BACKGROUND = "CPK_BUTTON_ENABLED_PRESSED_INFOCUS_BACKGROUND";
//    public final static String CPK_BUTTON_ENABLED_PRESSED_NORMAL_BACKGROUND = "CPK_BUTTON_ENABLED_PRESSED_NORMAL_BACKGROUND";
//
//    public final static String CPK_BUTTON_ENABLED_HOVER_DEFAULTORSELECTED_BACKGROUND = "CPK_BUTTON_ENABLED_HOVER_DEFAULTORSELECTED_BACKGROUND";
//    public final static String CPK_BUTTON_ENABLED_HOVER_INFOCUS_BACKGROUND = "CPK_BUTTON_ENABLED_HOVER_INFOCUS_BACKGROUND";
//    public final static String CPK_BUTTON_ENABLED_HOVER_NORMAL_BACKGROUND = "CPK_BUTTON_ENABLED_HOVER_NORMAL_BACKGROUND";
//
//    public final static String CPK_BUTTON_ENABLED_NORMAL_DEFAULTORSELECTED_BACKGROUND = "CPK_BUTTON_ENABLED_NORMAL_DEFAULTORSELECTED_BACKGROUND";
//    public final static String CPK_BUTTON_ENABLED_NORMAL_INFOCUS_BACKGROUND = "CPK_BUTTON_ENABLED_NORMAL_INFOCUS_BACKGROUND";
//    public final static String CPK_BUTTON_ENABLED_NORMAL_NORMAL_BACKGROUND = "CPK_BUTTON_ENABLED_NORMAL_NORMAL_BACKGROUND";
//
//    public final static String CPK_BUTTON_DISABLED_BACKGROUND = "CPK_BUTTON_DISABLED_BACKGROUND";
    private final static Map<Pair<Shape, Color>, BufferedImage> cachedShadows = new WeakHashMap<>();
    private final static Map<Pair<Shape, Color>, BufferedImage> cachedFills = new WeakHashMap<>();
    private final static Map<Pair<Shape, Color>, BufferedImage> cachedBorders = new WeakHashMap<>();

    public static void paintComponentBackgroundFill(Graphics2D g, AbstractButton button, Shape buttonShape) {
        final ButtonModel buttonModel = button.getModel();

        if (button.isEnabled()) {
            // Enabled
            if (buttonModel.isPressed()) {
                // Enabled + Pressed
                if (buttonIsDefaultOrSelected(button)) {

                    // Enabled + Pressed + Default or Selected
                    g.setColor(getClientPropertyOrDefault(
                            button,
                            CPK_BUTTON_ENABLED_PRESSED_DEFAULTORSELECTED_BACKGROUND,
                            UIManager.getColor(ModernUIUtilities.ACCENT_DARK_COLOR_KEY)
                    ));
                } else if (button.isFocusOwner()) {
                    // Enabled + Pressed + In Focus
                    g.setColor(getClientPropertyOrDefault(
                            button,
                            CPK_BUTTON_ENABLED_PRESSED_INFOCUS_BACKGROUND,
                            UIManager.getColor(ModernUIUtilities.ACCENT_DARK_COLOR_KEY)
                    ));
                } else {
                    // Enabled + Pressed + Normal
                    g.setColor(getClientPropertyOrDefault(
                            button,
                            CPK_BUTTON_ENABLED_PRESSED_NORMAL_BACKGROUND,
                            UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY)
                    ));
                }
            } else if (buttonModel.isRollover()) {
                // Enabled + Hover
                if (buttonIsDefaultOrSelected(button)) {
                    // Enabled + Hover + Default or Selected
                    g.setColor(getClientPropertyOrDefault(
                            button,
                            CPK_BUTTON_ENABLED_HOVER_DEFAULTORSELECTED_BACKGROUND,
                            UIManager.getColor(ModernUIUtilities.ACCENT_LIGHT_COLOR_KEY)
                    ));
                } else if (button.isFocusOwner()) {
                    // Enabled + Hover + In Focus
                    g.setColor(getClientPropertyOrDefault(
                            button,
                            CPK_BUTTON_ENABLED_HOVER_INFOCUS_BACKGROUND,
                            UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY)
                    ));
                } else {
                    // Enabled + Hover + Normal
                    g.setColor(getClientPropertyOrDefault(
                            button,
                            CPK_BUTTON_ENABLED_HOVER_NORMAL_BACKGROUND,
                            UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY)
                    ));
                }
            } else {
                // Enabled + Normal
                if (buttonIsDefaultOrSelected(button)) {
                    // Enabled + Normal + Default or Selected
                    g.setColor(getClientPropertyOrDefault(
                            button,
                            CPK_BUTTON_ENABLED_NORMAL_DEFAULTORSELECTED_BACKGROUND,
                            UIManager.getColor(ModernUIUtilities.ACCENT_MEDIUM_COLOR_KEY)
                    ));
                } else if (button.isFocusOwner()) {
                    // Enabled + Normal + In Focus
                    g.setColor(getClientPropertyOrDefault(
                            button,
                            CPK_BUTTON_ENABLED_NORMAL_INFOCUS_BACKGROUND,
                            UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY)
                    ));
                } else {
                    // Enabled + Normal + Normal
                    g.setColor(getClientPropertyOrDefault(
                            button,
                            CPK_BUTTON_ENABLED_NORMAL_NORMAL_BACKGROUND,
                            UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY)
                    ));
                }
            }
        } else {
            // Disabled
            g.setColor(getClientPropertyOrDefault(
                    button,
                    CPK_BUTTON_DISABLED_BACKGROUND,
                    UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY)
            ));
        }

        BufferedImage fill = cachedFills.computeIfAbsent(Pair.makePair(buttonShape, g.getColor()), (pair) -> {
            BufferedImage bi = CompatibleImageUtil.compatibleBufferedImage(
                    (int) (button.getWidth() * ModernUIUtilities.getDisplayScale()),
                    (int) (button.getHeight() * ModernUIUtilities.getDisplayScale()),
                    BufferedImage.TRANSLUCENT);
            Graphics2D g2 = bi.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
            g2.transform(ModernUIUtilities.getDisplayScaleTransform());

            g2.setColor(pair.b);
            g2.fill(pair.a);

            return bi;
        });
        g.drawImage(fill, ModernUIUtilities.getDisplayScaleInverseTransform(), button);
    }

    public static void paintComponentBorderHighlight(Graphics2D g, AbstractButton button, Shape buttonShape) {

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

        BufferedImage border = cachedBorders.computeIfAbsent(Pair.makePair(buttonShape, g.getColor()), (pair) -> {
            BufferedImage bi = CompatibleImageUtil.compatibleBufferedImage(
                    (int) (button.getWidth() * ModernUIUtilities.getDisplayScale()),
                    (int) (button.getHeight() * ModernUIUtilities.getDisplayScale()),
                    BufferedImage.TRANSLUCENT);
            Graphics2D g2 = bi.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
            g2.transform(ModernUIUtilities.getDisplayScaleTransform());

            g.setStroke(new BasicStroke(0.5f));
            g2.setColor(pair.b);
            g2.draw(pair.a);

            return bi;
        });
        g.drawImage(border, ModernUIUtilities.getDisplayScaleInverseTransform(), button);

//        g.draw(buttonShape);
    }

    public static void paintComponentShadowOrFocus(Graphics2D g, Component component, Shape shape) {//Graphics graphics, int x, int y, int width, int height) {
        if (component.isFocusOwner()) {
            g.setColor(Colors.transparentColor(UIManager.getColor(ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY), 0.5f));
        } else {
            g.setColor(Colors.transparentColor(UIManager.getColor(ModernUIUtilities.SHADOW_COLOR_KEY), 0.25f));
        }

        BufferedImage shadow = cachedShadows.computeIfAbsent(Pair.makePair(shape, g.getColor()), (pair) -> {
            BufferedImage bi = CompatibleImageUtil.compatibleBufferedImage(
                    (int) (component.getWidth() * ModernUIUtilities.getDisplayScale()),
                    (int) (component.getHeight() * ModernUIUtilities.getDisplayScale()),
                    BufferedImage.TRANSLUCENT);
            Graphics2D g2 = bi.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
            g2.transform(ModernUIUtilities.getDisplayScaleTransform());

            Color baseColor = pair.b;
//            g2.translate(3, 3);

//            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, baseColor.getAlpha() / 255.0f));
            int shadowWidth = 3;
            int sw = shadowWidth * 2;
            for (int i = sw; i >= 2; i -= 2) {
                float pct = (float) (sw - i) / (sw - 1);

                g2.setColor(Colors.transparentColor(baseColor, pct));

                g2.setStroke(new BasicStroke(i));
                g2.draw(pair.a);
            }
            g2.dispose();

            return bi; //To change body of generated lambdas, choose Tools | Templates.
        });

//        BufferedImage intermediate = CompatibleImageUtil.compatibleBufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TRANSLUCENT);
//        Graphics2D g3 = intermediate.createGraphics();
//        g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g3.setColor(g.getColor());
//        g3.fillRect(0, 0, component.getWidth(), component.getHeight());
//        g3.setComposite(AlphaComposite.DstIn);
//        g3.drawImage(shadowMask, 0, 0, component);
//        g3.dispose();
        g.drawImage(shadow, ModernUIUtilities.getDisplayScaleInverseTransform(), component);

//        Graphics2D g2 = (Graphics2D) g.create();
//        enableAntialiasing(g2);
//        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
//        g2.translate(0.0, 0.0);
//
//        Color baseColor = g2.getColor();
//
//        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, baseColor.getAlpha() / 255.0f));
//        int shadowWidth = 3;
//        int sw = shadowWidth * 2;
//        for (int i = sw; i >= 2; i -= 2) {
//            float pct = (float) (sw - i) / (sw - 1);
//
//            g2.setColor(Colors.transparentColor(baseColor, pct));
//
//            g2.setStroke(new BasicStroke(i));
//            g2.draw(shape);
//        }
//        g2.dispose();
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
            g.setColor(getClientPropertyOrDefault(
                    component,
                    CPK_COMPONENT_ENABLED_FOREGROUND,
                    UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY)
            ));
        } else {
            // Disabled
            g.setColor(getClientPropertyOrDefault(
                    component,
                    CPK_COMPONENT_DISABLED_FOREGROUND,
                    UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY)
            ));
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

    public static <T extends Object> T getClientPropertyOrDefault(JComponent c, Object clientPropertyKey, T defaultValue) {
        T returnValue = (T) c.getClientProperty(clientPropertyKey);
        if (returnValue == null) {
            returnValue = defaultValue;
        }
        return returnValue;
    }
}
