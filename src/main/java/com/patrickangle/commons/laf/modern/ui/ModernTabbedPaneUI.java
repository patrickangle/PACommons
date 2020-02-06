package com.patrickangle.commons.laf.modern.ui;

//import com.bulenkov.iconloader.util.JBInsets;
//import com.bulenkov.iconloader.util.JBUI;
//import com.bulenkov.iconloader.util.SystemInfo;
import com.patrickangle.commons.laf.modern.ModernUIUtilities;
import com.patrickangle.commons.laf.modern.ui.util.GradientTexturePaint;
import com.patrickangle.commons.laf.modern.ui.util.NoisePaint;
import com.patrickangle.commons.util.AquaUtils;
import com.patrickangle.commons.util.Colors;
import com.patrickangle.commons.util.GraphicsHelpers;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

/**
 *
 * @author patrickangle
 */
public class ModernTabbedPaneUI extends BasicTabbedPaneUI {

    private static final int CORNER_DIAMETER = 8;
    private static final int TAB_SPACING = 2;
    private static final int LEADING_AND_TRAILING_PADDING = 12;

    private static final Insets TAB_INSETS = new Insets(0, TAB_SPACING / 2, 0, TAB_SPACING / 2);

    private enum TabStyle {
        underline
    }

    private TabStyle tabStyle;
    private PropertyChangeListener panePropertyListener;
    private MouseListener paneMouseListener;
    private MouseMotionListener paneMouseMotionListener;

    private int hoverTab = -1;
    private int pressTab = -1;

    private static final float OFFSET = 1;
    private static final int FONT_SIZE_OFFSET = (int) UIManager.getDefaults().getOrDefault("TabbedPane.fontSizeOffset", Integer.valueOf(-1));

//    public static final Color CONTENT_AREA_COLOR = new Color(0x323232); //JBColor.namedColor("TabbedPane.contentAreaColor", 0xbfbfbf)
//    public static final Color ENABLED_SELECTED_COLOR = new Color(0x4A88C7); //JBColor.namedColor("TabbedPane.underlineColor", 0x4083C9);
//    public static final Color DISABLED_SELECTED_COLOR = new Color(0x7a7a7a); //JBColor.namedColor("TabbedPane.disabledUnderlineColor", Gray.xAB);
//    public static final Color DISABLED_TEXT_COLOR = new Color(0x777777); //JBColor.namedColor("TabbedPane.disabledForeground", Gray.x99);
//    public static final Color HOVER_COLOR = new Color(0x2e3133); //JBColor.namedColor("TabbedPane.hoverColor", Gray.xD9);
//    public static final Color FOCUS_COLOR = new Color(0x3d4b5c); //JBColor.namedColor("TabbedPane.focusColor", 0xDAE4ED);
//    public static final int TAB_HEIGHT = 32; //new JBValue.UIInteger("TabbedPane.tabHeight", 32);
//    public static final int SELECTION_HEIGHT = 3; //new JBValue.UIInteger("TabbedPane.tabSelectionHeight", 3);
//    TabbedPane.disabledForeground=777777
//TabbedPane.tabAreaInsets=0,0,0,0
//TabbedPane.tabInsets=0,12,0,12
//TabbedPane.selectedTabPadInsets=0,0,0,0
//TabbedPane.labelShift=0
//TabbedPane.tabsOverlapBorder=true
//TabbedPane.tabHeight=32
//TabbedPane.tabSelectionHeight=3
//TabbedPane.underlineColor=4A88C7
//TabbedPane.disabledUnderlineColor=7a7a7a
//TabbedPane.hoverColor=2e3133
//TabbedPane.focusColor=3d4b5c
//TabbedPane.selectedLabelShift=0
//TabbedPane.contentAreaColor=323232
    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new ModernTabbedPaneUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        modifyFontSize();

        Object rStyle = UIManager.get("TabbedPane.tabFillStyle");
        tabStyle = rStyle != null ? TabStyle.valueOf(rStyle.toString()) : TabStyle.underline;
        contentBorderInsets = tabPane.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT ? new Insets(1, 0, 0, 0) : new Insets(0, 0, 0, 0);
    }

    private void modifyFontSize() {
//        if (SystemInfo.isMac || SystemInfo.isLinux) {
//            Font font = UIManager.getFont("TabbedPane.font");
//            tabPane.setFont(tabPane.getFont().deriveFont((float) font.getSize() + FONT_SIZE_OFFSET));
//        }
    }

    @Override
    protected void installListeners() {
        super.installListeners();

        panePropertyListener = evt -> {
            String propName = evt.getPropertyName();
            if ("JTabbedPane.hasFullBorder".equals(propName) || "tabLayoutPolicy".equals(propName)) {
                boolean fullBorder = tabPane.getClientProperty("JTabbedPane.hasFullBorder") == Boolean.TRUE;
                contentBorderInsets = (tabPane.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT)
                        ? fullBorder ? new Insets(1, 1, 1, 1) : new Insets(1, 0, 0, 0)//JBUI.insetsTop(1)
                        : fullBorder ? new Insets(0, 1, 1, 1) : new Insets(0, 0, 0, 0);
                tabPane.revalidate();
                tabPane.repaint();
            } else if ("enabled".equals(propName)) {
                for (int ti = 0; ti < tabPane.getTabCount(); ti++) {
                    Component tc = tabPane.getTabComponentAt(ti);
                    if (tc != null) {
                        tc.setEnabled(evt.getNewValue() == Boolean.TRUE);
                    }
                }
                tabPane.repaint();
            }
        };

        tabPane.addPropertyChangeListener(panePropertyListener);

        paneMouseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hoverTab = tabForCoordinate(tabPane, e.getX(), e.getY());
                tabPane.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverTab = -1;
                tabPane.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressTab = tabForCoordinate(tabPane, e.getX(), e.getY());
                tabPane.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressTab = -1;
                tabPane.repaint();
            }

        };

        tabPane.addMouseListener(paneMouseListener);

        paneMouseMotionListener = new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hoverTab = tabForCoordinate(tabPane, e.getX(), e.getY());
                tabPane.repaint();
            }
        };
        tabPane.addMouseMotionListener(paneMouseMotionListener);
    }

    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        if (panePropertyListener != null) {
            tabPane.removePropertyChangeListener(panePropertyListener);
        }

        if (paneMouseListener != null) {
            tabPane.removeMouseListener(paneMouseListener);
        }

        if (paneMouseMotionListener != null) {
            tabPane.removeMouseMotionListener(paneMouseMotionListener);
        }
    }

    @Override
    protected Insets getContentBorderInsets(int tabPlacement) {
        Insets i = new Insets(contentBorderInsets.top, contentBorderInsets.left, contentBorderInsets.bottom, contentBorderInsets.right);//JBInsets.create(contentBorderInsets);
        rotateInsets(contentBorderInsets, i, tabPlacement);
        return i;
    }

//    @Override
//    protected Insets getTabAreaInsets(int tabPlacement) {
//        return TAB_INSETS;
//    }
    @Override
    protected Insets getSelectedTabPadInsets(int tabPlacement) {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    protected Insets getTabAreaInsets(int tabPlacement) {
        return TAB_INSETS;
    }

    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        Rectangle bounds = g.getClipBounds();
        g.setColor(Color.BLACK);
        g.fillRect(bounds.x, bounds.y + bounds.height - (int) OFFSET, bounds.x + bounds.width, (int) OFFSET);

        // 
        // Temporary code to paint texture.
        // 
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new GradientTexturePaint(bounds.x, bounds.y, Colors.transparentColor(Color.BLACK, 0f), bounds.x, bounds.y + bounds.height, Colors.transparentColor(Color.BLACK, 0.2f)));
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.y + bounds.height);

        g2.setPaint(new NoisePaint(Colors.grey(0.2f), 0.01f, 0.01f));
        g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.y + bounds.height);

        //
        // Draw bottom line.
        //
        g.drawLine(0, bounds.height - 1, bounds.width, bounds.height - 1);
//        if (tabPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT) {
//            Rectangle bounds = g.getClipBounds();
//            g.setColor(CONTENT_AREA_COLOR);
//            g.setColor(Color.red);
//
//            if (tabPlacement == LEFT || tabPlacement == RIGHT) {
//                g.fillRect(bounds.x + bounds.width - (int) OFFSET, bounds.y, (int) OFFSET, bounds.y + bounds.height);
//            } else {
//                g.fillRect(bounds.x, bounds.y + bounds.height - (int) OFFSET, bounds.x + bounds.width, (int) OFFSET);
//            }
//        }
        super.paintTabArea(g, tabPlacement, selectedIndex);
    }

    protected Shape shapeForTabInBounds(int tab, Rectangle bounds) {
        Shape tabShape = new RoundRectangle2D.Double(
                bounds.x + TAB_INSETS.left,
                bounds.y + TAB_INSETS.top,
                bounds.width - TAB_INSETS.left - TAB_INSETS.right,
                bounds.height - TAB_INSETS.top - TAB_INSETS.bottom,
                CORNER_DIAMETER,
                CORNER_DIAMETER);

        // Now get rid of the bottom rounded corners.
        Shape bottomCorners = new Rectangle2D.Double(
                tabShape.getBounds().x,
                tabShape.getBounds().y + (tabShape.getBounds().height / 2),
                tabShape.getBounds().width,
                tabShape.getBounds().height / 2
        );

        Area compositeShape = new Area(tabShape);
        compositeShape.add(new Area(bottomCorners));

        return compositeShape;
    }

    @Override
    protected void paintTabBackground(Graphics graphics, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
//        final ButtonModel buttonModel = button.getModel();
        Graphics2D g = (Graphics2D) graphics.create();
        GraphicsHelpers.enableAntialiasing(g);

//        if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
//            // Enabled
//            if (pressTab == tabIndex) {
//                // Enabled + Pressed
//                if (isSelected) {
//                    // Enabled + Pressed + Default or Selected
//                    g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_DARK_COLOR_KEY));
//                } else if (tabPane.isFocusOwner()) {
//                    // Enabled + Pressed + In Focus
//                    g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_DARK_COLOR_KEY));
//                } else {
//                    // Enabled + Pressed + Normal
//                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
//                }
//            } else if (hoverTab == tabIndex) {
//                // Enabled + Hover
//                if (isSelected) {
//                    // Enabled + Hover + Default or Selected
//                    g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_LIGHT_COLOR_KEY));
//                } else if (tabPane.isFocusOwner()) {
//                    // Enabled + Hover + In Focus
//                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY));
//                } else {
//                    // Enabled + Hover + Normal
//                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY));
//                }
//            } else {
//                // Enabled + Normal
//                if (isSelected) {
//                    // Enabled + Normal + Default or Selected
//                    g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_MEDIUM_COLOR_KEY));
//                } else if (tabPane.isFocusOwner()) {
//                    // Enabled + Normal + In Focus
//                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
//                } else {
//                    // Enabled + Normal + Normal
//                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
//                }
//            }
//        } else {
//            // Disabled
//            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
//        }
//
//        g.fill(shapeForTabInBounds(tabIndex, new Rectangle(x, y, w, h)));
//        
        if (isSelected) {
            g.setColor(Colors.transparentColor(Color.WHITE, 0.3f));
            g.fill(shapeForTabInBounds(tabIndex, new Rectangle(x, y, w, h)));
        }

        g.dispose();
//        g.fillRect(x, y, w, h);
    }

    @Override
    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex,
            String title, Rectangle textRect, boolean isSelected) {

        View v = getTextViewForTab(tabIndex);
        if (v != null || tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
            super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
        } else { // tab disabled
            int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);

            g.setFont(font);
//            g.setColor(DISABLED_TEXT_COLOR);
            BasicGraphicsUtils.drawStringUnderlineCharAt(tabPane, (Graphics2D) g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
        }
    }

    protected void layoutLabel(int tabPlacement,
            FontMetrics metrics, int tabIndex,
            String title, Icon icon,
            Rectangle tabRect, Rectangle iconRect,
            Rectangle textRect, boolean isSelected) {
        textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

        View v = getTextViewForTab(tabIndex);
        if (v != null) {
            tabPane.putClientProperty("html", v);
        }

        SwingUtilities.layoutCompoundLabel(tabPane,
                metrics, title, icon,
                SwingUtilities.CENTER,
                SwingUtilities.CENTER,
                SwingUtilities.TRAILING,
                SwingUtilities.CENTER,
                tabRect,
                iconRect,
                textRect,
                textIconGap);

        tabPane.putClientProperty("html", null);

        int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
        int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
        iconRect.x += xNudge;
        iconRect.y += yNudge;
        textRect.x += xNudge;
        textRect.y += yNudge;
    }

    @Override
    protected void paintTabBorder(Graphics graphics, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
//        Graphics2D g = (Graphics2D) graphics.create();
//        GraphicsHelpers.enableAntialiasing(g);
//
//        g.setStroke(new BasicStroke(0.5f));
//
//        if (tabPane.isEnabledAt(tabIndex)) {
//            // Enabled
//            if (isSelected) {
//                // Enabled + Default or Selected
//                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_MEDIUM_COLOR_KEY));
//            } else if (pressTab == tabIndex) {
//                // Enabled + In Focus
//                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY));
//            } else {
//                // Enabled + Normal
//                g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY));
//            }
//        } else {
//            // Disabled
//            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
//        }
//        
//        if (isSelected) {
//            g.setColor(Colors.transparentColor(Color.WHITE, 0.3f));
//            g.draw(shapeForTabInBounds(tabIndex, new Rectangle(x, y, w, h)));
//        }
//
////        g.draw(shapeForTabInBounds(tabIndex, new Rectangle(x, y, w, h)));
//        g.dispose();

//        if (isSelected && tabStyle == TabStyle.underline) {
//            g.setColor(tabPane.isEnabled() ? ENABLED_SELECTED_COLOR : DISABLED_SELECTED_COLOR);
//
//            int offset;
//            boolean wrap = tabPane.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT;
//            switch (tabPlacement) {
//                case LEFT:
//                    offset = (int) (SELECTION_HEIGHT - (wrap ? OFFSET : 0));
//                    g.fillRect(x + w - offset, y, SELECTION_HEIGHT, h);
//                    break;
//                case RIGHT:
//                    offset = wrap ? (int) OFFSET : 0;
//                    g.fillRect(x - offset, y, SELECTION_HEIGHT, h);
//                    break;
//                case BOTTOM:
//                    offset = wrap ? (int) OFFSET : 0;
//                    g.fillRect(x, y - offset, w, SELECTION_HEIGHT);
//                    break;
//                case TOP:
//                default:
//                    offset = SELECTION_HEIGHT - (wrap ? (int) OFFSET : 0);
//                    g.fillRect(x, y + h - offset, w, SELECTION_HEIGHT);
//                    break;
//            }
//        }
    }

    @Override
    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
//        int delta = SELECTION_HEIGHT;
//        if (tabPane.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT) {
//            delta -= OFFSET;
//        }
//
//        switch (tabPlacement) {
//            case RIGHT:
//            case LEFT:
//                return 0;
//
//            case BOTTOM:
//                return delta / 2;
//
//            case TOP:
//            default:
//                return -delta / 2;
//        }
    }

    @Override
    protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
//        int delta = SELECTION_HEIGHT;
//        if (tabPane.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT) {
//            delta -= OFFSET;
//        }
//
//        switch (tabPlacement) {
//            case TOP:
//            case BOTTOM:
//                return 0;
//
//            case LEFT:
//                return -delta / 2;
//
//            case RIGHT:
//            default:
//                return delta / 2;
//        }
    }

    public Insets getTabInsets() {
        return tabInsets;
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        Insets tabInsets = getTabInsets(tabPlacement, tabIndex);
        int width = tabInsets.left + tabInsets.right;
        Component tabComponent = tabPane.getTabComponentAt(tabIndex);
//        if (tabComponent != null) {
//            width += tabComponent.getPreferredSize().width;
//        } else {
        Icon icon = getIconForTab(tabIndex);
        int iconWidth = icon.getIconWidth();
//            if (icon != null) {
//                width += icon.getIconWidth() + textIconGap;
//            }
        View v = getTextViewForTab(tabIndex);
        int textWidth = 0;
        if (v != null) {
            // html
            textWidth = (int) v.getPreferredSpan(View.X_AXIS);
        } else {
            // plain text
            String title = tabPane.getTitleAt(tabIndex);

            textWidth = Math.round(BasicGraphicsUtils.getStringWidth(tabPane, metrics, title));
        }

        return width + Math.max(iconWidth, textWidth);
//        }
//        return width + 6;
    }

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        return getIconForTab(tabIndex) != null ? 54 : 24;
//        int height = 0;
//        Component c = tabPane.getTabComponentAt(tabIndex);
//        if (c != null) {
//            height = c.getPreferredSize().height;
//        } else {
//            View v = getTextViewForTab(tabIndex);
//            if (v != null) {
//                // html
//                height += (int) v.getPreferredSpan(View.Y_AXIS);
//            } else {
//                // plain text
//                height += fontHeight;
//            }
//            Icon icon = getIconForTab(tabIndex);
//
//            if (icon != null) {
//                height = Math.max(height, icon.getIconHeight());
//            }
//        }
//        Insets tabInsets = getTabInsets(tabPlacement, tabIndex);
//        height += tabInsets.top + tabInsets.bottom;
//
//        int minHeight = TAB_HEIGHT - (tabPane.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT ? (int) OFFSET : 0);
//        return Math.max(height, minHeight);
    }

//    protected Map<Icon, Icon> transformedIcons = new ConcurrentHashMap<>();
    @Override
    protected Icon getIconForTab(int tabIndex) {
        Icon providedIcon = super.getIconForTab(tabIndex); //To change body of generated methods, choose Tools | Templates.
//        if (providedIcon == null) {
//            return null;
//        }
//        return transformedIcons.computeIfAbsent(providedIcon, (icon) -> {
//            return modifiedIconForTab(icon);
//        });
        return providedIcon;

    }

//    protected Icon modifiedIconForTab(Icon original) {
//        BufferedImage bi = CompatibleImageUtil.compatibleBufferedImage(32, 32, BufferedImage.TRANSLUCENT);
//        
//        Graphics2D g = bi.createGraphics();
//        g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));
//        g.fillRect(0, 0, 32, 32);
//        g.setComposite(AlphaComposite.DstIn);
//        
//        original.paintIcon(tabPane, g, 0, 0);
//        return new ImageIcon(bi);
//    }
    @Override
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
    }

    @Override
    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
    }

    @Override
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
    }

    @Override
    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect,
            boolean isSelected) {
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("TabbedPaneUI", ModernTabbedPaneUI.class.getName());
        defaults.put("TabbedPane.font", AquaUtils.SYSTEM_FONT.deriveFont(11f));
        defaults.put("TabbedPane.foreground", defaults.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
    }
}
