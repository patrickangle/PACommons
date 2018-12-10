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

import com.patrickangle.commons.laf.modern.ModernUIUtilities;
import com.patrickangle.commons.util.Colors;
import com.patrickangle.commons.util.GraphicsHelpers;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 *
 * @author patrickangle
 */
public class ModernButtonUI extends BasicButtonUI {
    private static final int CORNER_DIAMETER = 8;
    private static final int GENERAL_PADDING = 4;
    private static final int LEADING_AND_TRAILING_PADDING = 12;
    
    private static final Insets BUTTON_INSETS = new Insets(GENERAL_PADDING, GENERAL_PADDING + LEADING_AND_TRAILING_PADDING, GENERAL_PADDING, GENERAL_PADDING + LEADING_AND_TRAILING_PADDING);

    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static ComponentUI createUI(JComponent c) {
        return new ModernButtonUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
    }

    @Override
    public void paint(Graphics graphics, JComponent component) {
        final Insets buttonBorderInsets  = component.getBorder().getBorderInsets(component);
        
        Shape buttonRect = new RoundRectangle2D.Double(
                buttonBorderInsets.left - BUTTON_INSETS.left,
                buttonBorderInsets.top - BUTTON_INSETS.top,
                component.getWidth() - buttonBorderInsets.left - buttonBorderInsets.right + BUTTON_INSETS.left + BUTTON_INSETS.right,
                component.getHeight() - buttonBorderInsets.top - buttonBorderInsets.bottom + BUTTON_INSETS.top + BUTTON_INSETS.bottom,
                CORNER_DIAMETER,
                CORNER_DIAMETER);
        
        paintShape(graphics, component, buttonRect);
    }

    @Override
    protected void paintText(Graphics graphics, JComponent component, Rectangle textRect, String text) {
        final AbstractButton button = (AbstractButton) component;
        
        final Graphics2D g = (Graphics2D) graphics.create();
        
        if (button.isEnabled()) {
            // Button is enabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));
        } else {
            // Button is disabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
        }
        
        int mnemonicIndex = button.getDisplayedMnemonicIndex();
        FontMetrics metrics = component.getFontMetrics(component.getFont());
        BasicGraphicsUtils.drawStringUnderlineCharAt(component, g, text, mnemonicIndex, textRect.x + getTextShiftOffset(), textRect.y + metrics.getAscent() + getTextShiftOffset());
        
        g.dispose();
    }
    
    public void paintShape(Graphics graphics, JComponent component, Shape buttonRect) {
        final AbstractButton button = (AbstractButton) component;
        
        final Graphics2D g = (Graphics2D) graphics.create();
        GraphicsHelpers.enableAntialiasing(g);

        ModernUIUtilities.paintButtonShadowOrGlow(g, button, buttonRect);
        ModernUIUtilities.paintButtonBackgroundFill(g, button, buttonRect);
        ModernUIUtilities.paintButtonBorderHighlight(g, button, buttonRect);
                
        g.dispose();
        super.paint(graphics, component);
    }
    
    public static ModernButtonBorder getDefaultBorder() {
        return new ModernButtonBorder(BUTTON_INSETS);
    }
    
    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ButtonUI", ModernButtonUI.class.getName());
        defaults.put("Button.border", ModernButtonUI.getDefaultBorder());
    }
}
