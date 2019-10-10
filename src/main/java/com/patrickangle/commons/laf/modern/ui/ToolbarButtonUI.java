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

import com.patrickangle.commons.laf.modern.ui.icon.TemplateImageIcon;
import com.patrickangle.commons.util.AquaUtils;
import com.patrickangle.commons.util.Colors;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import sun.swing.SwingUtilities2;

/**
 *
 * @author patrickangle
 */
public class ToolbarButtonUI extends BasicButtonUI {

    private static class BackgroundTextPair<T> {

        private final T background;
        private final T text;

        public BackgroundTextPair(T background, T text) {
            this.background = background;
            this.text = text;
        }

        public T getBackground() {
            return background;
        }

        public T getText() {
            return text;
        }
    }

    private static class LightDarkPair<T> {

        private final T light;
        private final T dark;

        public LightDarkPair(T light, T dark) {
            this.light = light;
            this.dark = dark;
        }

        public T getLight() {
            return light;
        }

        public T getDark() {
            return dark;
        }
    }
    private static final int CORNER_DIAMETER = 8;

    private static final LightDarkPair<Color> BUTTON_BACKGROUND_NORMAL = new LightDarkPair<>(Colors.transparentColor(Color.WHITE, 0.12f), Colors.grey(0.43f));
    private static final LightDarkPair<Color> BUTTON_TEXT_NORMAL = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.62f), Colors.grey(1.0f));

    private static final LightDarkPair<Color> BUTTON_BACKGROUND_INACTIVE = new LightDarkPair<>(Colors.transparentColor(Color.WHITE, 0.0f), Colors.grey(0.25f));
    private static final LightDarkPair<Color> BUTTON_TEXT_INACTIVE = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.27f), Colors.grey(0.51f));

    private static final LightDarkPair<Color> BUTTON_BACKGROUND_PRESSED = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.02f), Colors.grey(0.54f));
    private static final LightDarkPair<Color> BUTTON_TEXT_PRESSED = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.75f), Colors.grey(1.0f));

    private static final LightDarkPair<Color> BUTTON_BACKGROUND_SHADOW_NORMAL = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.04f), Colors.grey(0.5f));
    private static final LightDarkPair<Color> BUTTON_BACKGROUND_SHADOW_INACTIVE = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.04f), Colors.grey(0.32f));

    // Special for the toggle-type buttons.
    private static final LightDarkPair<Color> TOGGLE_BUTTON_BACKGROUND_SELECTED = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.42f), Colors.grey(0.8f));
    private static final LightDarkPair<Color> TOGGLE_BUTTON_TEXT_SELECTED = new LightDarkPair<>(Colors.transparentColor(Color.WHITE, 1.0f), Colors.grey(0.25f));

    private static final LightDarkPair<Color> TOGGLE_BUTTON_BACKGROUND_SELECTED_PRESSED = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.50f), Colors.grey(0.94f));
    private static final LightDarkPair<Color> TOGGLE_BUTTON_TEXT_SELECTED_PRESSED = new LightDarkPair<>(Colors.transparentColor(Color.WHITE, 1.0f), Colors.grey(0.25f));

    private static final LightDarkPair<Color> TOGGLE_BUTTON_BACKGROUND_SELECTED_INACTIVE = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.07f), Colors.grey(0.36f));
    private static final LightDarkPair<Color> TOGGLE_BUTTON_TEXT_SELECTED_INACTIVE = new LightDarkPair<>(Colors.transparentColor(Color.WHITE, 0.25f), Colors.grey(0.2f));

    private static final BackgroundTextPair<LightDarkPair<Color>> BUTTON_NORMAL = new BackgroundTextPair<>(BUTTON_BACKGROUND_NORMAL, BUTTON_TEXT_NORMAL);
    private static final BackgroundTextPair<LightDarkPair<Color>> BUTTON_INACTIVE = new BackgroundTextPair<>(BUTTON_BACKGROUND_INACTIVE, BUTTON_TEXT_INACTIVE);
    private static final BackgroundTextPair<LightDarkPair<Color>> BUTTON_PRESSED = new BackgroundTextPair<>(BUTTON_BACKGROUND_PRESSED, BUTTON_TEXT_PRESSED);
    private static final BackgroundTextPair<LightDarkPair<Color>> TOGGLE_BUTTON_SELECTED = new BackgroundTextPair<>(TOGGLE_BUTTON_BACKGROUND_SELECTED, TOGGLE_BUTTON_TEXT_SELECTED);
    private static final BackgroundTextPair<LightDarkPair<Color>> TOGGLE_BUTTON_SELECTED_PRESSED = new BackgroundTextPair<>(TOGGLE_BUTTON_BACKGROUND_SELECTED_PRESSED, TOGGLE_BUTTON_TEXT_SELECTED_PRESSED);
    private static final BackgroundTextPair<LightDarkPair<Color>> TOGGLE_BUTTON_SELECTED_INACTIVE = new BackgroundTextPair<>(TOGGLE_BUTTON_BACKGROUND_SELECTED_INACTIVE, TOGGLE_BUTTON_TEXT_SELECTED_INACTIVE);

    private WindowAdapter windowFocusListener;

    private HierarchyListener buttonHierarchyListener;

    protected Window currentWindow;

    public static enum ButtonUIStyle {
        Default,
        SegmentedFirst,
        SegmentedMiddle,
        SegmentedLast,
        DropDown;

        public static final String KEY = ButtonUIStyle.class.getName();

        public static ButtonUIStyle forComponent(JComponent c) {
            ButtonUIStyle value = (ButtonUIStyle) c.getClientProperty(KEY);
            return value != null ? value : Default;
        }
    }

    public static enum ButtonUIIconType {
        Default,
        Template;

        public static final String KEY = ButtonUIIconType.class.getName();

        public static ButtonUIIconType forComponent(JComponent c) {
            ButtonUIIconType value = (ButtonUIIconType) c.getClientProperty(KEY);
            return value != null ? value : Default;
        }
    }

    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static ComponentUI createUI(JComponent c) {
        return new ToolbarButtonUI();
    }

    @Override
    public void installUI(JComponent c) {
        AbstractButton button = (AbstractButton) c;

        windowFocusListener = new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                c.repaint();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                c.repaint();
            }

            @Override
            public void windowActivated(WindowEvent e) {
                c.repaint();
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                c.repaint();
            }
        };

        buttonHierarchyListener = new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                updateFocusListener(button);
            }
        };

        super.installUI(button);
        button.setBorder(new EmptyBorder(4, 8, 4, 8));
        button.setMinimumSize(new Dimension(46, 24));
        button.setMaximumSize(new Dimension(-1, 24));
//        button.setPreferredSize(new Dimension(, 24));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected void installListeners(AbstractButton b) {
        super.installListeners(b); //To change body of generated methods, choose Tools | Templates.
        b.addHierarchyListener(buttonHierarchyListener);
    }

    @Override
    protected void uninstallListeners(AbstractButton b) {
        super.uninstallListeners(b); //To change body of generated methods, choose Tools | Templates.
        uninstallFocusListener();
        b.removeHierarchyListener(buttonHierarchyListener);
    }

    protected void updateFocusListener(AbstractButton b) {
        uninstallFocusListener();

        Window w = SwingUtilities.getWindowAncestor(b);
        if (w != null) {
            this.currentWindow = w;
            w.addWindowListener(windowFocusListener);
        }
    }

    protected void uninstallFocusListener() {
        if (currentWindow != null) {
            currentWindow.removeWindowListener(windowFocusListener);
        }
    }

    @Override
    public void paint(Graphics graphics, JComponent component) {
        final AbstractButton button = (AbstractButton) component;

        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(getShadowColor(button, AquaUtils.isSystemUsingDarkAppearance())); // TODO: Support light mode.
        g.fill(getShadowArea(button, AquaUtils.isSystemUsingDarkAppearance())); // TODO: Support light mode.

        g.setColor(getBackgroundColor(button, AquaUtils.isSystemUsingDarkAppearance())); // TODO: Support light mode.
        g.fill(getBackgroundArea(button, AquaUtils.isSystemUsingDarkAppearance())); // TODO: Support light mode.

        g.dispose();
        super.paint(graphics, component);
    }

    protected void paintText(Graphics graphics, JComponent component, Rectangle textRect, String text) {
        final AbstractButton button = (AbstractButton) component;

        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(getTextColor(button, AquaUtils.isSystemUsingDarkAppearance())); // TODO: Support light mode.

        FontMetrics metrics = SwingUtilities2.getFontMetrics(button, g);
        SwingUtilities2.drawStringUnderlineCharAt(button, g, text, -1,
                textRect.x + getTextShiftOffset(),
                textRect.y + metrics.getAscent() + getTextShiftOffset());

        g.dispose();
    }

    @Override
    protected void paintIcon(Graphics graphics, JComponent component, Rectangle iconRect) {
//        super.paintIcon(graphics, component, iconRect);

        if (ButtonUIIconType.forComponent(component) == ButtonUIIconType.Template) {
            final AbstractButton button = (AbstractButton) component;

            Icon icon = getIcon(button, AquaUtils.isSystemUsingDarkAppearance()); // TODO: Support light mode.

            if (icon != null) {
                int x = iconRect.x + ((iconRect.width - icon.getIconWidth()) / 2);
                int y = iconRect.y + ((iconRect.height - icon.getIconHeight()) / 2);

                icon.paintIcon(component, graphics, x, y);
            }
        } else {

        }

    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(Math.max(super.getPreferredSize(c).width, 46), 24);
    }

    protected static Icon getIcon(AbstractButton button, boolean isDarkAppearance) {
        // TODO: Support disabling the template image formatting.
        Icon icon = button.getIcon();
        if (icon == null) {
            return null;
        }

        return new TemplateImageIcon(icon, getTextColor(button, isDarkAppearance));

//        BufferedImage bi = CompatibleImageUtil.compatibleBufferedImage((int) (icon.getIconWidth() * ModernUIUtilities.getDisplayScale()), (int) (icon.getIconHeight() * ModernUIUtilities.getDisplayScale()), BufferedImage.TRANSLUCENT);
//        
//        Graphics2D g = bi.createGraphics();
//        g.setColor(getTextColor(button, isDarkAppearance));
//        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
//        g.setComposite(AlphaComposite.DstIn);
//        g.setTransform(ModernUIUtilities.getDisplayScaleTransform());
////        g.setTransform(ModernUIUtilities.getDisplayScaleTransform());
//        icon.paintIcon(button, g, 0, 0);
//        return new ImageIcon(bi);
    }

    protected static Area getShadowArea(AbstractButton button, boolean isDarkAppearance) {
        // TODO: Support segmented buttons.
        Area area = new Area(new RoundRectangle2D.Double(
                0,
                0,
                button.getWidth(),
                button.getHeight(),
                CORNER_DIAMETER,
                CORNER_DIAMETER));

        area.subtract(getBackgroundArea(button, isDarkAppearance));

        return area;
    }

    protected static Area getBackgroundArea(AbstractButton button, boolean isDarkAppearance) {
        // TODO: Support segmented buttons.
        Area area = new Area(new RoundRectangle2D.Double(
                0.5,
                isDarkAppearance ? 1.0 : 0.5,
                button.getWidth() - 1.0,
                button.getHeight() - 1.5,
                CORNER_DIAMETER - 1.0,
                CORNER_DIAMETER - 1.0));

        return area;
    }

    protected static Color getShadowColor(AbstractButton button, boolean isDarkAppearance) {
        Window owner = SwingUtilities.getWindowAncestor(button);
        boolean active = button.isEnabled() && (owner == null || owner.isActive());

        if (active) {
            if (isDarkAppearance) {
                return BUTTON_BACKGROUND_SHADOW_NORMAL.getDark();
            } else {
                return BUTTON_BACKGROUND_SHADOW_NORMAL.getLight();
            }
        } else {
            if (isDarkAppearance) {
                return BUTTON_BACKGROUND_SHADOW_INACTIVE.getDark();
            } else {
                return BUTTON_BACKGROUND_SHADOW_INACTIVE.getLight();
            }
        }

    }

    protected static Color getBackgroundColor(AbstractButton button, boolean isDarkAppearance) {
        if (isDarkAppearance) {
            return getColors(button).getBackground().getDark();
        } else {
            return getColors(button).getBackground().getLight();
        }
    }

    protected static Color getTextColor(AbstractButton button, boolean isDarkAppearance) {
        if (isDarkAppearance) {
            return getColors(button).getText().getDark();
        } else {
            return getColors(button).getText().getLight();
        }
    }

    protected static BackgroundTextPair<LightDarkPair<Color>> getColors(AbstractButton button) {
        // The button is active only if it is enabled and the window it is within is in focus.
        Window owner = SwingUtilities.getWindowAncestor(button);
        boolean active = button.isEnabled() && (owner == null || owner.isActive());

        if (button instanceof JToggleButton && ((JToggleButton) button).getModel().isSelected()) {
            // Special case for selected toggle buttons, as we handle their drawing specially.
            if (!active) {
                return TOGGLE_BUTTON_SELECTED_INACTIVE;
            } else if (button.getModel().isPressed()) {
                return TOGGLE_BUTTON_SELECTED_PRESSED;
            } else {
                return TOGGLE_BUTTON_SELECTED;
            }
        } else {
            if (!active) {
                return BUTTON_INACTIVE;
            } else if (button.getModel().isPressed()) {
                System.out.println("BINGO!!");
                return BUTTON_PRESSED;
            } else {
                return BUTTON_NORMAL;
            }
        }
    }

}
