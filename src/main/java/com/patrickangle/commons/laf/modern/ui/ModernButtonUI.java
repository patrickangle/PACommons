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

import com.patrickangle.commons.awt.strokes.EdgeStroke;
import com.patrickangle.commons.laf.modern.ModernLookAndFeel;
import com.patrickangle.commons.laf.modern.ModernShapedComponentUI;
import com.patrickangle.commons.laf.modern.ModernUIUtilities;
import com.patrickangle.commons.laf.modern.borders.ModernComponentShadowFocusBorder;
import static com.patrickangle.commons.laf.modern.ui.ToolbarButtonUI.getTextColor;
import com.patrickangle.commons.laf.modern.ui.icon.TemplateImageIcon;
import com.patrickangle.commons.laf.modern.ui.util.ToolbarConstants;
import com.patrickangle.commons.laf.modern.util.GraphicsUtils;
import com.patrickangle.commons.laf.modern.util.PaintingUtils;
import com.patrickangle.commons.laf.modern.util.ShapeUtils;
import com.patrickangle.commons.laf.modern.util.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
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

    protected static final int TOOLBAR_BUTTON_AREA_HEIGHT = 24;

    private WindowAdapter windowFocusListener;

    private HierarchyListener buttonHierarchyListener;

    protected Window currentWindow;

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

    public static enum IconStyle {
        Normal, Template;

        public static final String Key = IconStyle.class.getName();

        public static IconStyle from(JComponent c) {
            Object style = c.getClientProperty(Key);
            if (style instanceof Style) {
                return (IconStyle) style;
            } else {
                return Normal;
            }
        }
    }

    public static enum DropdownStyle {
        None, Dropdown;

        public static final String Key = DropdownStyle.class.getName();

        public static DropdownStyle from(JComponent c) {
            Object style = c.getClientProperty(Key);
            if (style instanceof Style) {
                return (DropdownStyle) style;
            } else {
                return None;
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

        super.installUI(c);

        button.setRolloverEnabled(true);
        button.setOpaque(false);
    }

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
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

        Window w = javax.swing.SwingUtilities.getWindowAncestor(b);
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
    public void update(Graphics g, JComponent c) {
        if (Style.from(c) == Style.Toolbar) {
            System.out.println("Toolbar!");
            boolean segmented = Segment.from(c) != Segment.Only;
            c.setBorder(BorderFactory.createEmptyBorder(4, segmented ? 0 : 8, 4, segmented ? 0 : 8));
            ((AbstractButton) c).setVerticalAlignment(SwingConstants.CENTER);
            c.setFont(UIManager.getFont("Button.font").deriveFont(12f));
        }
        super.update(g, c);
    }

    @Override
    public void paint(Graphics graphics, JComponent c) {
        if (Style.from(c) == Style.Toolbar) {
            paintShapeToolbarStyle(graphics, (AbstractButton) c, getShape(c));
        } else if (((AbstractButton) c).isBorderPainted()) {
            paintShape(graphics, (AbstractButton) c, getShape(c));
        } else {
            paintShapeBorderless(graphics, (AbstractButton) c, getShape(c));
        }

        if (Style.from(c) == Style.Toolbar) {
            if (DropdownStyle.from(c) == DropdownStyle.Dropdown) {
                paintDropdownIcon(graphics, c, new Rectangle(button.getWidth() - 16, (button.getHeight() - 4) / 2, 8, 4));
                button.setHorizontalAlignment(SwingConstants.LEFT);
            } else {
                button.setHorizontalAlignment(SwingConstants.CENTER);
            }
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

    protected void paintShapeToolbarStyle(Graphics graphics, AbstractButton button, Shape shape) {
        Graphics2D g = GraphicsUtils.configureGraphics(graphics);

        // Paint fill
        g.translate(button.getBorder().getBorderInsets(button).left, button.getBorder().getBorderInsets(button).top);
        g.setPaint(ModernLookAndFeel.colors.componentToolbarPaint(button));
        g.fill(shape);

        // Paint the edge stroke, which in the standard color set is a top highlight.
        g.setPaint(ModernLookAndFeel.colors.componentToolbarRimPaint(button));
        g.setStroke(new EdgeStroke(0.5f, EdgeStroke.Align.Inside));
        g.draw(shape);

        g.dispose();
    }

    protected void paintShapeBorderless(Graphics graphics, AbstractButton button, Shape shape) {
        if (button.isEnabled()) {
            Graphics2D g = GraphicsUtils.configureGraphics(graphics);
            g.translate(button.getBorder().getBorderInsets(button).left, button.getBorder().getBorderInsets(button).top);

            if (button.getModel().isRollover()) {
                g.setPaint(ModernLookAndFeel.colors.componentRolloverPaint(button));
                g.fill(shape);
            } else if (button.getModel().isPressed()) {
                g.setPaint(ModernLookAndFeel.colors.componentPressedPaint(button));
                g.fill(shape);
            }

            g.dispose();
        }
    }

    public static Border getDefaultBorder() {
        return DEFAULT_BORDER;
    }

    @Override
    protected void paintIcon(Graphics graphics, JComponent component, Rectangle iconRect) {
        final AbstractButton button = (AbstractButton) component;

        Icon icon = getIcon(button);

        if (icon != null) {
            int x = iconRect.x + ((iconRect.width - icon.getIconWidth()) / 2);
            int y = iconRect.y + ((iconRect.height - icon.getIconHeight()) / 2);

            icon.paintIcon(component, graphics, x, y);
        }
    }

    protected static Icon getIcon(AbstractButton button) {
        Icon icon = button.getIcon();
        if (icon == null) {
            return null;
        }

        if (IconStyle.from(button) == IconStyle.Template) {
            if (SwingUtilities.buttonIsDefaultOrSelected(button)) {
                return new TemplateImageIcon(icon, ModernLookAndFeel.colors.componentSelectedPaint(button));
            } else if (button.isEnabled()) {
                return new TemplateImageIcon(icon, ModernLookAndFeel.colors.componentNormalTextPaint(button));
            } else {
                return new TemplateImageIcon(icon, ModernLookAndFeel.colors.componentDisabledTextPaint(button));
            }
        } else {
            return icon;
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
            case Toolbar:
                switch (Segment.from(c)) {
                    case First:
                        return ShapeUtils.roundedRectangle(0, 0, width, TOOLBAR_BUTTON_AREA_HEIGHT, 3, List.of(ShapeUtils.Corner.TopLeft, ShapeUtils.Corner.BottomLeft));
                    case Middle:
                        return ShapeUtils.roundedRectangle(0, 0, width, TOOLBAR_BUTTON_AREA_HEIGHT, 3, List.of());
                    case Last:
                        return ShapeUtils.roundedRectangle(0, 0, width, TOOLBAR_BUTTON_AREA_HEIGHT, 3, List.of(ShapeUtils.Corner.TopRight, ShapeUtils.Corner.BottomRight));
                    case Only:
                    default:
                        return ShapeUtils.roundedRectangle(0, 0, width, TOOLBAR_BUTTON_AREA_HEIGHT, 3);
                }
            case Normal:
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
            case Toolbar:
                return new Dimension(
                        super.getMinimumSize(c).width + (MINIMUM_LEADING_TRAILING_AREA * 2),
                        Math.max(TOOLBAR_BUTTON_AREA_HEIGHT, super.getMinimumSize(c).height) + c.getBorder().getBorderInsets(c).top + c.getBorder().getBorderInsets(c).bottom
                );
            case Normal:
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
            case Toolbar:
                return new Dimension(
                        super.getPreferredSize(c).width + (MINIMUM_LEADING_TRAILING_AREA * 2),
                        Math.max(TOOLBAR_BUTTON_AREA_HEIGHT, super.getPreferredSize(c).height) + c.getBorder().getBorderInsets(c).top + c.getBorder().getBorderInsets(c).bottom
                );
            case Normal:
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
