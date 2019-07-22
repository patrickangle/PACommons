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

import com.patrickangle.commons.laf.modern.ModernShapedComponentUI;
import com.patrickangle.commons.laf.modern.ModernUIComponentPainting;
import com.patrickangle.commons.laf.modern.ModernUIUtilities;
import com.patrickangle.commons.util.GraphicsHelpers;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author patrickangle
 */
public class ModernButtonUI extends BasicButtonUI implements ModernShapedComponentUI {

    public static final String JBUTTON_TYPE_KEY = "JButton.buttonType";
    public static final String JBUTTON_TYPE_NORMAL_VALUE = "normal";
    public static final String JBUTTON_TYPE_QUIET_VALUE = "quiet";
    public static final String JBUTTON_TYPE_HELP_VALUE = "help";

    public static final String JBUTTON_SEGMENT_POSITION_KEY = "JButton.segmentPosition";
    public static final String JBUTTON_SEGMENT_POSITION_ONLY_VALUE = "only";
    public static final String JBUTTON_SEGMENT_POSITION_FIRST_VALUE = "first";
    public static final String JBUTTON_SEGMENT_POSITION_MIDDLE_VALUE = "middle";
    public static final String JBUTTON_SEGMENT_POSITION_LAST_VALUE = "last";

    private static final int CORNER_DIAMETER = 8;
    private static final int GENERAL_PADDING = 4;
    private static final int LEADING_AND_TRAILING_PADDING = 12;

    private static final Insets BUTTON_INSETS = new Insets(GENERAL_PADDING, GENERAL_PADDING + LEADING_AND_TRAILING_PADDING, GENERAL_PADDING, GENERAL_PADDING + LEADING_AND_TRAILING_PADDING);

    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static ComponentUI createUI(JComponent c) {
        return new ModernButtonUI();
    }
    
    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);

        b.setRolloverEnabled(true);
        b.setOpaque(false);

    }

    @Override
    public void paint(Graphics graphics, JComponent component) {
        paintShape(graphics, component, getShape(component));
        super.paint(graphics, component);
    }

    @Override
    protected void paintText(Graphics g, AbstractButton component, Rectangle textRect, String text) {
        if (JBUTTON_TYPE_HELP_VALUE.equals(component.getClientProperty(JBUTTON_TYPE_KEY))) {
            if (component.getFont().getSize() != 14 || !component.getFont().isBold()) {
                // Help buttons have a bold ? mark, and no other text.
                component.setFont(component.getFont().deriveFont(Font.BOLD, 14f));
            }

            ModernUIComponentPainting.paintComponentText(g, component, textRect, "?", getTextShiftOffset());
        } else {
            ModernUIComponentPainting.paintComponentText(g, component, textRect, text, getTextShiftOffset());
        }
    }

    public void paintShape(Graphics graphics, JComponent component, Shape buttonRect) {
        final AbstractButton button = (AbstractButton) component;

        final Graphics2D g = (Graphics2D) graphics.create();
        GraphicsHelpers.enableAntialiasing(g);
        GraphicsHelpers.enableStrokeNormalization(g);

        final ButtonModel buttonModel = button.getModel();

        switch ((String) ModernUIUtilities.clientPropertyOrDefault(component, JBUTTON_TYPE_KEY, JBUTTON_TYPE_NORMAL_VALUE)) {
            case JBUTTON_TYPE_QUIET_VALUE:
                if (button.isEnabled() && (buttonModel.isPressed() || buttonModel.isRollover())) {
                    ModernUIComponentPainting.paintComponentBackgroundFill(g, button, buttonRect);
                }
                break;

            case JBUTTON_TYPE_HELP_VALUE:
                ModernUIComponentPainting.paintComponentShadowOrFocus(g, component, buttonRect);
                ModernUIComponentPainting.paintComponentBackgroundFill(g, button, buttonRect);
                ModernUIComponentPainting.paintComponentBorderHighlight(g, button, buttonRect);
                break;

            case JBUTTON_TYPE_NORMAL_VALUE:
            default:
                ModernUIComponentPainting.paintComponentShadowOrFocus(g, component, buttonRect);
                ModernUIComponentPainting.paintComponentBackgroundFill(g, button, buttonRect);
                ModernUIComponentPainting.paintComponentBorderHighlight(g, button, buttonRect);
                break;

        }

//        if ((component.getClientProperty(JBUTTON_TYPE_KEY) == null || (boolean) component.getClientProperty(JBUTTON_TYPE_KEY) == false)) {
//            ModernUIComponentPainting.paintComponentShadowOrFocus(g, component, buttonRect);
//        }
//        
//        
//        
//        if ((component.getClientProperty(JBUTTON_TYPE_KEY) == null || component.getClientProperty(JBUTTON_TYPE_KEY).equals(g)) || button.isEnabled() && (buttonModel.isPressed() || buttonModel.isRollover())) {
//            ModernUIComponentPainting.paintComponentBackgroundFill(g, button, buttonRect);
//        }
//        if (component.getClientProperty(JBUTTON_TYPE_KEY) == null || component.getClientProperty(JBUTTON_TYPE_KEY) == false) {
//            ModernUIComponentPainting.paintComponentBorderHighlight(g, button, buttonRect);
//        }
        g.dispose();
    }

    public static ModernBasicBorder getDefaultBorder() {
        return new ModernBasicBorder(BUTTON_INSETS);
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ButtonUI", ModernButtonUI.class.getName());
        defaults.put("ToggleButtonUI", ModernButtonUI.class.getName());
        defaults.put("Button.border", ModernButtonUI.getDefaultBorder());
    }

    @Override
    public Shape getShape(JComponent c) {
        final Insets buttonBorderInsets = c.getBorder().getBorderInsets(c);

        Shape buttonRect = new RoundRectangle2D.Double(
                buttonBorderInsets.left - BUTTON_INSETS.left,
                buttonBorderInsets.top - BUTTON_INSETS.top,
                c.getWidth() - buttonBorderInsets.left - buttonBorderInsets.right + BUTTON_INSETS.left + BUTTON_INSETS.right,
                c.getHeight() - buttonBorderInsets.top - buttonBorderInsets.bottom + BUTTON_INSETS.top + BUTTON_INSETS.bottom,
                CORNER_DIAMETER,
                CORNER_DIAMETER);

        if (JBUTTON_TYPE_HELP_VALUE.equals(c.getClientProperty(JBUTTON_TYPE_KEY))) {
            int dims = Math.min(c.getWidth(), c.getHeight());
            int insetDiffs = Math.max(-buttonBorderInsets.left - buttonBorderInsets.right + BUTTON_INSETS.left + BUTTON_INSETS.right, -buttonBorderInsets.top - buttonBorderInsets.bottom + BUTTON_INSETS.top + BUTTON_INSETS.bottom);
            int origin = Math.max(buttonBorderInsets.left - BUTTON_INSETS.left, buttonBorderInsets.top - BUTTON_INSETS.top);
            int originX = origin + ((c.getWidth() - dims) / 2);
            buttonRect = new RoundRectangle2D.Double(
                    originX, origin,
                    dims + insetDiffs, dims + insetDiffs,
                    dims, dims
            );
        } else {

            String segmentValue = (String) c.getClientProperty(JBUTTON_SEGMENT_POSITION_KEY);
            if (segmentValue != null && !segmentValue.equals(JBUTTON_SEGMENT_POSITION_ONLY_VALUE)) {
                switch (segmentValue) {
                    case JBUTTON_SEGMENT_POSITION_FIRST_VALUE:
                        Shape addonRect = new Rectangle2D.Double(
                                buttonRect.getBounds().x + (buttonRect.getBounds().width / 2),
                                buttonRect.getBounds().y,
                                (buttonRect.getBounds().width / 2) + buttonBorderInsets.right,
                                buttonRect.getBounds().height);

                        Area compositeShape = new Area(buttonRect);
                        compositeShape.add(new Area(addonRect));

                        buttonRect = compositeShape;

                        break;
                    case JBUTTON_SEGMENT_POSITION_MIDDLE_VALUE:
                        Shape addonRect2 = new Rectangle2D.Double(
                                buttonRect.getBounds().x - buttonBorderInsets.left,
                                buttonRect.getBounds().y,
                                buttonRect.getBounds().width + buttonBorderInsets.left + buttonBorderInsets.right,
                                buttonRect.getBounds().height);

                        Area compositeShape2 = new Area(buttonRect);
                        compositeShape2.add(new Area(addonRect2));

                        buttonRect = compositeShape2;
                        break;
                    case JBUTTON_SEGMENT_POSITION_LAST_VALUE:
                        Shape addonRect3 = new Rectangle2D.Double(
                                buttonRect.getBounds().x - buttonBorderInsets.left,
                                buttonRect.getBounds().y,
                                buttonRect.getBounds().width / 2,
                                buttonRect.getBounds().height);

                        Area compositeShape3 = new Area(buttonRect);
                        compositeShape3.add(new Area(addonRect3));

                        buttonRect = compositeShape3;
                        break;
                }
            } else {
                // Do nothing, the shape is already correct.
            }
        }
        return buttonRect;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
//        if (JBUTTON_TYPE_HELP_VALUE.equals(c.getClientProperty(JBUTTON_TYPE_KEY))) {
//            return new Dimension (28, 28);
//        } else {
        return new Dimension(super.getPreferredSize(c).width, 28);
//        }
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
//        if (JBUTTON_TYPE_HELP_VALUE.equals(c.getClientProperty(JBUTTON_TYPE_KEY))) {
//            return new Dimension (28, 28);
//        } else {
        return new Dimension(super.getMinimumSize(c).width, 28);
//        }

    }
}
