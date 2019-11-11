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

import com.patrickangle.commons.laf.modern.ui.util.ToolbarConstants;
import com.patrickangle.commons.laf.modern.ModernUIColors;
import com.patrickangle.commons.laf.modern.ModernUIUtilities;
import com.patrickangle.commons.laf.modern.ui.icon.TemplateImageIcon;
import com.patrickangle.commons.laf.modern.ui.util.BackgroundTextPair;
import com.patrickangle.commons.laf.modern.ui.util.LightDarkPair;
import com.patrickangle.commons.util.AquaUtils;
import java.awt.BasicStroke;
import java.awt.Color;
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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import sun.swing.SwingUtilities2;

/**
 *
 * @author patrickangle
 */
public class ToolbarButtonUI extends BasicButtonUI {

    private WindowAdapter windowFocusListener;

    private HierarchyListener buttonHierarchyListener;

    protected Window currentWindow;

    

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
        
        button.setVerticalAlignment(SwingConstants.CENTER);
        
        button.setFont(UIManager.getFont("Button.font").deriveFont(12f));
    }

    @Override
    protected void installListeners(AbstractButton b) {
        super.installListeners(b);
        b.addHierarchyListener(buttonHierarchyListener);
    }

    @Override
    protected void uninstallListeners(AbstractButton b) {
        super.uninstallListeners(b);
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

        g.setColor(getShadowColor(button, ModernUIUtilities.isDark())); // TODO: Support light mode.
        g.fill(getShadowArea(button, ModernUIUtilities.isDark())); // TODO: Support light mode.

        g.setColor(getBackgroundColor(button, ModernUIUtilities.isDark())); // TODO: Support light mode.
        g.fill(getBackgroundArea(button, ModernUIUtilities.isDark())); // TODO: Support light mode.

        if (ToolbarConstants.ButtonUIStyle.forComponent(button) == ToolbarConstants.ButtonUIStyle.Dropdown) {
            paintDropdownIcon(graphics, component, new Rectangle(button.getWidth() - 16, (button.getHeight() - 4) / 2, 8, 4));
            button.setHorizontalAlignment(SwingConstants.LEFT);
        } else {
            button.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        g.dispose();
        super.paint(graphics, component);
    }

    protected void paintText(Graphics graphics, JComponent component, Rectangle textRect, String text) {
        final AbstractButton button = (AbstractButton) component;

        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(getTextColor(button, ModernUIUtilities.isDark()));

        FontMetrics metrics = SwingUtilities2.getFontMetrics(button, g);
        SwingUtilities2.drawStringUnderlineCharAt(button, g, text, -1,
                textRect.x + getTextShiftOffset(),
                textRect.y + metrics.getAscent() + getTextShiftOffset());

        g.dispose();
    }

    @Override
    protected void paintIcon(Graphics graphics, JComponent component, Rectangle iconRect) {
        final AbstractButton button = (AbstractButton) component;

        Icon icon = getIcon(button, ModernUIUtilities.isDark());

        if (icon != null) {
            int x = iconRect.x + ((iconRect.width - icon.getIconWidth()) / 2);
            int y = iconRect.y + ((iconRect.height - icon.getIconHeight()) / 2);

            icon.paintIcon(component, graphics, x, y);
        }
    }
    
    protected void paintDropdownIcon(Graphics graphics, JComponent component, Rectangle iconRect) {
        final AbstractButton button = (AbstractButton) component;
        
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getTextColor(button, ModernUIUtilities.isDark()));
        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(iconRect.x, iconRect.y, iconRect.x + (iconRect.width / 2), iconRect.y + iconRect.height);
        g.drawLine(iconRect.x + iconRect.width, iconRect.y, iconRect.x + (iconRect.width / 2), iconRect.y + iconRect.height);
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

        if (ToolbarConstants.ButtonUIIconType.forComponent(button) == ToolbarConstants.ButtonUIIconType.Template) {
            return new TemplateImageIcon(icon, getTextColor(button, isDarkAppearance));
        } else {
            return icon;
        }

    }

    protected static Area getShadowArea(AbstractButton button, boolean isDarkAppearance) {
        // TODO: Support segmented buttons.
        Area area = new Area(new RoundRectangle2D.Double(
                0,
                0,
                button.getWidth(),
                button.getHeight(),
                ToolbarConstants.CORNER_DIAMETER,
                ToolbarConstants.CORNER_DIAMETER));

        return area;
    }

    protected static Area getBackgroundArea(AbstractButton button, boolean isDarkAppearance) {
        // TODO: Support segmented buttons.
        Area area = new Area(new RoundRectangle2D.Double(
                1.0,
                0.5,
                button.getWidth() - 1.0,
                button.getHeight() - 1.5,
                ToolbarConstants.CORNER_DIAMETER - 1.0,
                ToolbarConstants.CORNER_DIAMETER - 1.0));

        return area;
    }

    protected static Color getShadowColor(AbstractButton button, boolean isDarkAppearance) {
        Window owner = SwingUtilities.getWindowAncestor(button);
        boolean active = button.isEnabled() && (owner == null || owner.isActive());

        if (active) {
            if (isDarkAppearance) {
                return ToolbarConstants.BUTTON_BACKGROUND_SHADOW_NORMAL.getDark();
            } else {
                return ToolbarConstants.BUTTON_BACKGROUND_SHADOW_NORMAL.getLight();
            }
        } else {
            if (isDarkAppearance) {
                return ToolbarConstants.BUTTON_BACKGROUND_SHADOW_INACTIVE.getDark();
            } else {
                return ToolbarConstants.BUTTON_BACKGROUND_SHADOW_INACTIVE.getLight();
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
        Window owner = SwingUtilities.getWindowAncestor(button);
        boolean active = button.isEnabled() && (owner == null || owner.isActive());

        if (active && button instanceof JToggleButton && ((JToggleButton) button).getModel().isSelected() && button.getModel().getGroup() == null) {
            return isDarkAppearance ? ModernUIColors.accentHighlightColor.brighter() : ModernUIColors.accentHighlightColor;
        } else if (isDarkAppearance) {
            return getColors(button).getText().getDark();
        } else {
            return getColors(button).getText().getLight();
        }
    }

    protected static BackgroundTextPair<LightDarkPair<Color>> getColors(AbstractButton button) {
        // The button is active only if it is enabled and the window it is within is in focus.
        Window owner = SwingUtilities.getWindowAncestor(button);
        boolean active = button.isEnabled() && (owner == null || owner.isActive());

        if (button instanceof JToggleButton && ((JToggleButton) button).getModel().isSelected() && button.getModel().getGroup() != null) {
            // Special case for selected toggle buttons, as we handle their drawing specially.
            if (!active) {
                return ToolbarConstants.TOGGLE_BUTTON_SELECTED_INACTIVE;
            } else if (button.getModel().isPressed()) {
                return ToolbarConstants.TOGGLE_BUTTON_SELECTED_PRESSED;
            } else {
                return ToolbarConstants.TOGGLE_BUTTON_SELECTED;
            }
        } else {
            if (!active) {
                return ToolbarConstants.BUTTON_INACTIVE;
            } else if (button.getModel().isPressed()) {
                return ToolbarConstants.BUTTON_PRESSED;
            } else {
                return ToolbarConstants.BUTTON_NORMAL;
            }
        }
    }

}
