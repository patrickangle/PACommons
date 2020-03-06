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
package com.patrickangle.commons.laf.modern.ui;

import com.patrickangle.commons.laf.modern.ModernLookAndFeel;
import com.patrickangle.commons.laf.modern.ModernShapedComponentUI;
import com.patrickangle.commons.laf.modern.borders.ModernComponentShadowFocusBorder;
import com.patrickangle.commons.laf.modern.util.GraphicsUtils;
import com.patrickangle.commons.laf.modern.util.PaintingUtils;
import com.patrickangle.commons.laf.modern.util.ShapeUtils;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author patrickangle
 */
public class ModernButtonUI extends BasicButtonUI implements ModernShapedComponentUI {

    protected static final Border DEFAULT_BORDER = new ModernComponentShadowFocusBorder();
    protected static final int MINIMUM_HEIGHT = 19;
    protected static final int MINIMUM_LEADING_TRAILING_AREA = 15;

    public static enum Style {
        Normal, Toolbar, Attached;

        public static final String Key = Style.class.getName();

        public static Style from(JComponent c) {
            Object style = c.getClientProperty(Key);
            if (style instanceof Style) {
                return (Style) style;
            } else {
                return Normal;
            }
        }
    }

    public static enum Segment {
        Only, First, Middle, Last;

        public static final String Key = Segment.class.getName();

        public static Segment from(JComponent c) {
            Object segment = c.getClientProperty(Key);
            if (segment instanceof Segment) {
                return (Segment) segment;
            } else {
                return Only;
            }
        }
    }

    private final AbstractButton button;

    public ModernButtonUI(AbstractButton button) {
        this.button = button;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        button.setRolloverEnabled(true);
        button.setOpaque(false);
    }

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
    }

    @Override
    public void paint(Graphics graphics, JComponent c) {
        if (((AbstractButton) c).isBorderPainted()) {
            paintShape(graphics, (AbstractButton) c, getShape(c));
        }
        super.paint(graphics, c);
    }

    protected void paintText(Graphics graphics, AbstractButton button, Rectangle textRect, String text) {
        Graphics2D g = GraphicsUtils.configureGraphics(graphics);

        PaintingUtils.paintText(g, button, textRect, text, getTextShiftOffset());

        g.dispose();
    }

    protected void paintShape(Graphics graphics, AbstractButton button, Shape shape) {
        Graphics2D g = GraphicsUtils.configureGraphics(graphics);

        // Paint fill
        g.translate(button.getBorder().getBorderInsets(button).left, button.getBorder().getBorderInsets(button).top);
        g.setPaint(ModernLookAndFeel.colors.componentPaint(button));
        g.fill(shape);

        g.dispose();
    }

    public static Border getDefaultBorder() {
        return DEFAULT_BORDER;
    }

    public Shape getShape(JComponent c) {
        float width = c.getWidth() - c.getBorder().getBorderInsets(c).left - c.getBorder().getBorderInsets(c).right;
        float height = c.getHeight() - c.getBorder().getBorderInsets(c).top - c.getBorder().getBorderInsets(c).bottom;

        switch (Style.from(c)) {
            case Attached:
                switch (Segment.from(c)) {
                    case First:
                        return ShapeUtils.roundedRectangle(0, 0, width, height, 3, List.of(ShapeUtils.Corner.TopRight));
                    case Middle:
                        return ShapeUtils.roundedRectangle(0, 0, width, height, 3, List.of());
                    case Last:
                        return ShapeUtils.roundedRectangle(0, 0, width, height, 3, List.of(ShapeUtils.Corner.BottomRight));
                    case Only:
                    default:
                        return ShapeUtils.roundedRectangle(0, 0, width, height, 3, List.of(ShapeUtils.Corner.TopRight, ShapeUtils.Corner.BottomRight));
                }
            case Normal:
            case Toolbar:
            default:
                switch (Segment.from(c)) {
                    case First:
                        return ShapeUtils.roundedRectangle(0, 0, width, height, 3, List.of(ShapeUtils.Corner.TopLeft, ShapeUtils.Corner.BottomLeft));
                    case Middle:
                        return ShapeUtils.roundedRectangle(0, 0, width, height, 3, List.of());
                    case Last:
                        return ShapeUtils.roundedRectangle(0, 0, width, height, 3, List.of(ShapeUtils.Corner.TopRight, ShapeUtils.Corner.BottomRight));
                    case Only:
                    default:
                        return ShapeUtils.roundedRectangle(0, 0, width, height, 3);
                }
        }
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        switch (Style.from(c)) {
            case Attached:
                return super.getMinimumSize(c);
            case Normal:
            case Toolbar:
            default:
                return new Dimension(
                super.getMinimumSize(c).width + (MINIMUM_LEADING_TRAILING_AREA * 2),
                Math.max(MINIMUM_HEIGHT, super.getMinimumSize(c).height) + c.getBorder().getBorderInsets(c).top + c.getBorder().getBorderInsets(c).bottom
        );
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        switch (Style.from(c)) {
            case Attached:
                return super.getPreferredSize(c);
            case Normal:
            case Toolbar:
            default:
                return new Dimension(
                        super.getPreferredSize(c).width + (MINIMUM_LEADING_TRAILING_AREA * 2),
                        Math.max(MINIMUM_HEIGHT, super.getPreferredSize(c).height) + c.getBorder().getBorderInsets(c).top + c.getBorder().getBorderInsets(c).bottom
                );
        }
    }

    public static ComponentUI createUI(JComponent component) {
        return new ModernButtonUI((AbstractButton) component);
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ButtonUI", ModernButtonUI.class.getName());
        defaults.put("ToggleButtonUI", ModernButtonUI.class.getName());
        defaults.put("Button.border", DEFAULT_BORDER);
        defaults.put("ToggleButton.border", DEFAULT_BORDER);
    }
}
