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
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.MINIMUM_LEADING_TRAILING_AREA;
import com.patrickangle.commons.laf.modern.util.GraphicsUtils;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

/**
 *
 * @author patrickangle
 */
public class ModernTextFieldUI extends BasicTextFieldUI implements ModernShapedComponentUI {

    protected static final Border DEFAULT_BORDER = new ModernComponentShadowFocusBorder();
    protected static final int MINIMUM_HEIGHT = 19;
    
    protected JTextComponent editor;
//    protected static final int MINIMUM_LEADING_TRAILING_AREA = 15;

//    private static final int GENERAL_PADDING = 4;
//
//    private static final Insets TEXT_FIELD_INSETS = new Insets(GENERAL_PADDING, GENERAL_PADDING, GENERAL_PADDING, GENERAL_PADDING);
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

    @Override
    public void installUI(JComponent c) {
        editor = (JTextComponent) c;
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

    @Override
    protected void paintBackground(Graphics g) {
    }
    
    protected Rectangle getVisibleEditorRect() {
        Rectangle alloc = editor.getBounds();
        if ((alloc.width > 0) && (alloc.height > 0)) {
            alloc.x = alloc.y = 0;
            Insets insets = editor.getInsets();
            alloc.x += insets.left + 4;
            alloc.y += insets.top;
            alloc.width -= insets.left + insets.right + 8;
            alloc.height -= insets.top + insets.bottom;
            return alloc;
        }
        return null;
    }

    public void paintShape(Graphics graphics, JTextComponent component, Shape shape) {

        Graphics2D g = GraphicsUtils.configureGraphics(graphics);

        g.setStroke(new BasicStroke(0.5f));
        g.translate(component.getBorder().getBorderInsets(component).left, component.getBorder().getBorderInsets(component).top);
        if (component.isEnabled()) {
            g.setPaint(ModernLookAndFeel.colors.textAreaNormalBackgroundPaint(component));
        } else {
            g.setPaint(ModernLookAndFeel.colors.textAreaDisabledBackgroundPaint(component));
        }

        g.fill(shape);

        if (component.isEnabled()) {
            g.setPaint(ModernLookAndFeel.colors.textAreaNormalBorderPaint(component));
        } else {
            g.setPaint(ModernLookAndFeel.colors.textAreaDisabledBorderPaint(component));
        }

        g.draw(shape);

        if (component.isEnabled()) {
            g.setPaint(ModernLookAndFeel.colors.textAreaNormalBaselinePaint(component));
        } else {
            g.setPaint(ModernLookAndFeel.colors.textAreaDisabledBaselinePaint(component));
        }
        g.clip(shape);
        g.drawLine(shape.getBounds().x, shape.getBounds().height, shape.getBounds().width, shape.getBounds().height);

//        if (component.isEnabled()) {
//            g.setPaint(ModernLookAndFeel.colors.textAreaNormalPaint(component));
//        } else {
//            g.setPaint(ModernLookAndFeel.colors.textAreaDisabledBaselinePaint(component));
//        }
//        
        g.dispose();
    }

    public static Border getDefaultBorder() {
        return DEFAULT_BORDER;
    }

//    public static void paintComponentBackgroundFill(Graphics2D g, JTextComponent component, Shape buttonShape) {
//        if (component.isEnabled()) {
//            // Component is enabled
//            g.setColor(UIManager.getColor(PRIMARY_DARK_COLOR_KEY));
//        } else {
//            // Component is disabled
//            g.setColor(UIManager.getColor(PRIMARY_ULTRA_DARK_COLOR_KEY));
//        }
//
//        g.fill(buttonShape);
//    }
//
//    public static void paintComponentBorderHighlight(Graphics2D g, JTextComponent component, Shape buttonShape) {
//        g.setStroke(new BasicStroke(0.25f));
//
//        if (component.hasFocus()) {
//            g.setColor(UIManager.getColor(ACCENT_HIGHLIGHT_COLOR_KEY));
//            g.draw(buttonShape);
//        } else {
//            g.setColor(UIManager.getColor(PRIMARY_MEDIUM_COLOR_KEY));
//            g.draw(buttonShape);
//        }
//    }
//    public static boolean isSearchField(Component c) {
//        return c instanceof JTextField && "search".equals(((JTextField) c).getClientProperty("JTextField.variant"));
//    }
    @Override
    public Shape getShape(JComponent c) {
        float width = c.getWidth() - c.getBorder().getBorderInsets(c).left - c.getBorder().getBorderInsets(c).right;
        float height = c.getHeight() - c.getBorder().getBorderInsets(c).top - c.getBorder().getBorderInsets(c).bottom;

        return new Rectangle2D.Float(0, 0, width, height);
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        return new Dimension(
                super.getMinimumSize(c).width + (MINIMUM_LEADING_TRAILING_AREA * 2),
                Math.max(MINIMUM_HEIGHT, super.getMinimumSize(c).height) + c.getBorder().getBorderInsets(c).top + c.getBorder().getBorderInsets(c).bottom
        );
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(
                super.getPreferredSize(c).width + (MINIMUM_LEADING_TRAILING_AREA * 2),
                Math.max(MINIMUM_HEIGHT, super.getPreferredSize(c).height) + c.getBorder().getBorderInsets(c).top + c.getBorder().getBorderInsets(c).bottom
        );
    }

    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static ComponentUI createUI(JComponent c) {
        return new ModernTextFieldUI();
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("TextFieldUI", ModernTextFieldUI.class.getName());
        defaults.put("TextField.border", ModernTextFieldUI.getDefaultBorder());

        defaults.put("TextField.foreground", ModernLookAndFeel.colors.textAreaNormalTextColor());
        defaults.put("TextField.inactiveForeground", ModernLookAndFeel.colors.textAreaDisabledTextColor());
        defaults.put("TextField.selectionBackground", ModernLookAndFeel.colors.textAreaSelectionHighlightColor());
        defaults.put("TextField.selectionForeground", ModernLookAndFeel.colors.textAreaNormalTextColor());
        defaults.put("TextField.caretForeground", ModernLookAndFeel.colors.textAreaNormalTextColor());

        defaults.put("FormattedTextFieldUI", ModernTextFieldUI.class.getName());
        defaults.put("FormattedTextField.caretForeground", ModernLookAndFeel.colors.textAreaNormalTextColor());
    }
}
