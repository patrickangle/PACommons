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
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.PRIMARY_DARK_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.PRIMARY_ULTRA_DARK_COLOR_KEY;
import com.patrickangle.commons.util.GraphicsHelpers;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

/**
 *
 * @author patrickangle
 */
public class ModernTextFieldUI extends BasicTextFieldUI implements ModernShapedComponentUI {

    private static final int GENERAL_PADDING = 4;

    private static final Insets TEXT_FIELD_INSETS = new Insets(GENERAL_PADDING, GENERAL_PADDING, GENERAL_PADDING, GENERAL_PADDING);

    private final FocusListener focusListener = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            getComponent().repaint();
        }

        @Override
        public void focusLost(FocusEvent e) {
            getComponent().repaint();
        }
    };

    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static ComponentUI createUI(JComponent c) {
        return new ModernTextFieldUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
        c.setBorder(getDefaultBorder());
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        final JTextComponent c = getComponent();
        c.addFocusListener(focusListener);
    }

    @Override
    protected void uninstallListeners() {
        final JTextComponent c = getComponent();
        c.removeFocusListener(focusListener);
        super.uninstallListeners();
    }

    @Override
    protected void paintSafely(Graphics g) {
        JTextComponent component = getComponent();
        
        paintShape(g, component, getShape(component));
        super.paintSafely(g);
    }

    public void paintShape(Graphics graphics, JTextComponent component, Shape buttonRect) {

        final Graphics2D g = (Graphics2D) graphics.create();
        GraphicsHelpers.enableAntialiasing(g);

        paintComponentBackgroundFill(g, component, buttonRect);
        paintComponentBorderHighlight(g, component, buttonRect);

        g.dispose();
    }

    public static Border getDefaultBorder() {
        return new ModernBasicBorder(TEXT_FIELD_INSETS);
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("TextFieldUI", ModernTextFieldUI.class.getName());
        defaults.put("TextField.border", ModernTextFieldUI.getDefaultBorder());

        defaults.put("TextField.foreground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("TextField.inactiveForeground", defaults.getColor(PRIMARY_MEDIUM_DARK_COLOR_KEY));
        defaults.put("TextField.selectionBackground", defaults.getColor(ACCENT_HIGHLIGHT_COLOR_KEY));
        defaults.put("TextField.selectionForeground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("TextField.caretForeground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        
        defaults.put("FormattedTextFieldUI", ModernTextFieldUI.class.getName());
        defaults.put("FormattedTextField.font", defaults.getFont("defaultFont"));
        defaults.put("FormattedTextField.caretForeground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
    }

    public static void paintComponentBackgroundFill(Graphics2D g, JTextComponent component, Shape buttonShape) {
        if (component.isEnabled()) {
            // Component is enabled
            g.setColor(UIManager.getColor(PRIMARY_DARK_COLOR_KEY));
        } else {
            // Component is disabled
            g.setColor(UIManager.getColor(PRIMARY_ULTRA_DARK_COLOR_KEY));
        }

        g.fill(buttonShape);
    }

    public static void paintComponentBorderHighlight(Graphics2D g, JTextComponent component, Shape buttonShape) {
        g.setStroke(new BasicStroke(0.25f));

        if (component.hasFocus()) {
            g.setColor(UIManager.getColor(ACCENT_HIGHLIGHT_COLOR_KEY));
            g.draw(buttonShape);
        } else {
            g.setColor(UIManager.getColor(PRIMARY_MEDIUM_COLOR_KEY));
            g.draw(buttonShape);
        }
    }

    public static boolean isSearchField(Component c) {
        return c instanceof JTextField && "search".equals(((JTextField) c).getClientProperty("JTextField.variant"));
    }

    @Override
    public Shape getShape(JComponent c) {
        Insets buttonBorderInsets = new Insets(0, 0, 0, 0);
        if (c.getBorder() instanceof ModernBasicBorder) {
            buttonBorderInsets = c.getBorder().getBorderInsets(c);
        }

        int cornerDiameter = 0;

        if (isSearchField(c)) {
            cornerDiameter = 6;
        }
        
        Shape buttonRect = new RoundRectangle2D.Double(
                buttonBorderInsets.left - TEXT_FIELD_INSETS.left,
                buttonBorderInsets.top - TEXT_FIELD_INSETS.top,
                c.getWidth() - buttonBorderInsets.left - buttonBorderInsets.right + TEXT_FIELD_INSETS.left + TEXT_FIELD_INSETS.right,
                c.getHeight() - buttonBorderInsets.top - buttonBorderInsets.bottom + TEXT_FIELD_INSETS.top + TEXT_FIELD_INSETS.bottom,
                cornerDiameter,
                cornerDiameter);
        
        return buttonRect;
    }
}
