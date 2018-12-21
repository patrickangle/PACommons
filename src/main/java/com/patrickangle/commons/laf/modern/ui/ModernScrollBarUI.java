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
import java.awt.Adjustable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import static java.awt.SystemColor.scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author patrickangle
 */
public class ModernScrollBarUI extends BasicScrollBarUI {
//    public static final String JBUTTON_SEGMENT_POSITION_KEY = "JButton.segmentPosition";
//    public static final String JBUTTON_SEGMENT_POSITION_ONLY_VALUE = "only";
//    public static final String JBUTTON_SEGMENT_POSITION_FIRST_VALUE = "first";
//    public static final String JBUTTON_SEGMENT_POSITION_MIDDLE_VALUE = "middle";
//    public static final String JBUTTON_SEGMENT_POSITION_LAST_VALUE = "last";
//        
//    private static final int CORNER_DIAMETER = 8;
//    private static final int GENERAL_PADDING = 4;
//    private static final int LEADING_AND_TRAILING_PADDING = 12;
//    
//    private static final Insets BUTTON_INSETS = new Insets(GENERAL_PADDING, GENERAL_PADDING + LEADING_AND_TRAILING_PADDING, GENERAL_PADDING, GENERAL_PADDING + LEADING_AND_TRAILING_PADDING);

    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static ComponentUI createUI(JComponent c) {
        return new ModernScrollBarUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
        c.setFocusable(false);
    }

//    public static DoubleColor getGradientLightColor() {
//        return new DoubleColor(Gray._251, Gray._95);
//    }
//
//    public static DoubleColor getGradientDarkColor() {
//        return new DoubleColor(Gray._215, Gray._80);
//    }
//
//    private static DoubleColor getGradientThumbBorderColor() {
//        return new DoubleColor(Gray._201, Gray._85);
//    }
//
//    public static DoubleColor getTrackBackground() {
//        return new DoubleColor(Gray._245, UIUtil.getListBackground());
//    }
//
//    public static DoubleColor getTrackBorderColor() {
//        return new DoubleColor(Gray._230, UIUtil.getListBackground());
//    }

//    private static final BasicStroke BORDER_STROKE = new BasicStroke();

//    private static int getAnimationColorShift() {
//        return UIUtil.isUnderDarcula() ? 20 : 40;
//    }

//    private final AdjustmentListener myAdjustmentListener;
//    private final MouseMotionAdapter myMouseMotionListener;
//    private final MouseAdapter myMouseListener;

//    private Animator myAnimator;

//    private int myAnimationColorShift = 0;
//    private boolean myMouseIsOverThumb = false;
//    public static final int DELAY_FRAMES = 4;
//    public static final int FRAMES_COUNT = 10 + DELAY_FRAMES;

//    protected DarculaScrollBarUI() {
//        myAdjustmentListener = new AdjustmentListener() {
//            @Override
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                resetAnimator();
//            }
//        };
//
//        myMouseMotionListener = new MouseMotionAdapter() {
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                boolean inside = isOverThumb(e.getPoint());
//                if (inside != myMouseIsOverThumb) {
//                    myMouseIsOverThumb = inside;
//                    resetAnimator();
//                }
//            }
//        };
//
//        myMouseListener = new MouseAdapter() {
//            @Override
//            public void mouseExited(MouseEvent e) {
//                if (myMouseIsOverThumb) {
//                    myMouseIsOverThumb = false;
//                    resetAnimator();
//                }
//            }
//        };
//    }

    @Override
    public void layoutContainer(Container scrollbarContainer) {
        try {
            super.layoutContainer(scrollbarContainer);
        } catch (NullPointerException ignore) {
            //installUI is not performed yet or uninstallUI has set almost every field to null. Just ignore it //IDEA-89674
        }
    }

    @Override
    protected ModelListener createModelListener() {
        return new ModelListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (scrollbar != null) {
                    super.stateChanged(e);
                }
            }
        };
    }

    public int getDecrementButtonHeight() {
        return decrButton.getHeight();
    }

    public int getIncrementButtonHeight() {
        return incrButton.getHeight();
    }

//    private void resetAnimator() {
//        myAnimator.reset();
//        if (scrollbar != null && scrollbar.getValueIsAdjusting() || myMouseIsOverThumb) {
//            myAnimator.suspend();
//            myAnimationColorShift = getAnimationColorShift();
//        } else {
//            myAnimator.resume();
//        }
//    }

//    public static BasicScrollBarUI createNormal() {
//        return new DarculaScrollBarUI();
//        cre
//    }

//    @Override
//    public void installUI(JComponent c) {
//        super.installUI(c);
//        scrollbar.setFocusable(false);
//    }

    @Override
    protected void installDefaults() {
        final int incGap = UIManager.getInt("ScrollBar.incrementButtonGap");
        final int decGap = UIManager.getInt("ScrollBar.decrementButtonGap");
        try {
            UIManager.put("ScrollBar.incrementButtonGap", 0);
            UIManager.put("ScrollBar.decrementButtonGap", 0);
            super.installDefaults();
        } finally {
            UIManager.put("ScrollBar.incrementButtonGap", incGap);
            UIManager.put("ScrollBar.decrementButtonGap", decGap);
        }
    }

    @Override
    protected void installListeners() {
//        if (myAnimator == null || myAnimator.isDisposed()) {
//            myAnimator = createAnimator();
//        }

        super.installListeners();
//        scrollbar.addAdjustmentListener(myAdjustmentListener);
//        scrollbar.addMouseListener(myMouseListener);
//        scrollbar.addMouseMotionListener(myMouseMotionListener);
    }

//    private Animator createAnimator() {
//        return new Animator("Adjustment fadeout", FRAMES_COUNT, FRAMES_COUNT * 50, false) {
//            @Override
//            public void paintNow(int frame, int totalFrames, int cycle) {
//                myAnimationColorShift = getAnimationColorShift();
//                if (frame > DELAY_FRAMES) {
//                    myAnimationColorShift *= 1 - ((double) (frame - DELAY_FRAMES)) / ((double) (totalFrames - DELAY_FRAMES));
//                }
//
//                if (scrollbar != null) {
//                    scrollbar.repaint(((DarculaScrollBarUI) scrollbar.getUI()).getThumbBounds());
//                }
//            }
//        };
//    }

//    private boolean isOverThumb(Point p) {
//        final Rectangle bounds = getThumbBounds();
//        return bounds != null && bounds.contains(p);
//    }

    @Override
    public Rectangle getThumbBounds() {
        return super.getThumbBounds();
    }

    @Override
    protected void uninstallListeners() {
        if (scrollTimer != null) {
            // it is already called otherwise
            super.uninstallListeners();
        }
//        scrollbar.removeAdjustmentListener(myAdjustmentListener);
//        myAnimator.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle bounds) {
        g.setColor(UIManager.getColor(ModernUIUtilities.WORKSPACE_BACKGROUND_COLOR_KEY));
//        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

//        g.setColor(getTrackBorderColor());
//        if (isVertical()) {
//            g.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height);
//        } else {
//            g.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y);
//        }
    }
    
    

    @Override
    protected Dimension getMinimumThumbSize() {
        final int thickness = getThickness();
        return isVertical() ? new Dimension(thickness, thickness * 2) : new Dimension(thickness * 2, thickness);
    }

    protected int getThickness() {
        return 13;
    }

    @Override
    public Dimension getMaximumSize(JComponent c) {
        int thickness = getThickness();
        return new Dimension(thickness, thickness);
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        return getMaximumSize(c);
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return getMaximumSize(c);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        g.translate(thumbBounds.x, thumbBounds.y);
        paintMaxiThumb((Graphics2D) g, thumbBounds);
        g.translate(-thumbBounds.x, -thumbBounds.y);
    }

    private void paintMaxiThumb(Graphics2D g, Rectangle thumbBounds) {
        final boolean vertical = isVertical();
        int hGap = vertical ? 2 : 1;
        int vGap = vertical ? 1 : 2;

        int w = adjustThumbWidth(thumbBounds.width - hGap * 2);
        int h = thumbBounds.height - vGap * 2;

        // leave one pixel between thumb and right or bottom edge
        if (vertical) {
            h -= 1;
        } else {
            w -= 1;
        }

//        final Paint paint;
//        final Color start = adjustColor(getGradientLightColor());
//        final Color end = adjustColor(getGradientDarkColor());
//
//        if (vertical) {
//            paint = new GradientPaint(1, 0, start, w + 1, 0, end);
//        } else {
//            paint = new GradientPaint(0, 1, start, 0, h + 1, end);
//        }

//        g.setPaint(paint);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Colors.transparentColor(UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY), (float) (isThumbRollover() ? 1.0f : 0.5f)));
        
        int cornerDiameter = 0;
        
        if (vertical) {
            cornerDiameter = w;
        } else {
            cornerDiameter = h;
        }
        
        g.fillRoundRect(hGap + 1, vGap + 1, w - 1, h - 1, cornerDiameter, cornerDiameter);

//        final Stroke stroke = g.getStroke();
//        g.setStroke(BORDER_STROKE);
//        g.setColor(getGradientThumbBorderColor());
//        g.drawRoundRect(hGap, vGap, w, h, 3, 3);
//        g.setStroke(stroke);
    }

    @Override
    public boolean getSupportsAbsolutePositioning() {
        return true;
    }

    protected int adjustThumbWidth(int width) {
        return width;
    }

//    protected Color adjustColor(Color c) {
//        if (myAnimationColorShift == 0) {
//            return c;
//        }
//        final int sign = UIUtil.isUnderDarcula() ? -1 : 1;
//        return Gray.get(Math.max(0, Math.min(255, c.getRed() - sign * myAnimationColorShift)));
//    }

    private boolean isVertical() {
        return scrollbar.getOrientation() == Adjustable.VERTICAL;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new EmptyButton();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new EmptyButton();
    }

    private static class EmptyButton extends JButton {

        private EmptyButton() {
            setFocusable(false);
            setRequestFocusEnabled(false);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(0, 0);
        }

        @Override
        public Dimension getPreferredSize() {
            return getMaximumSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return getMaximumSize();
        }
    }

//    @Override
//    public void paint(Graphics graphics, JComponent component) {
//        final Insets buttonBorderInsets  = component.getBorder().getBorderInsets(component);
//        
//        Shape buttonRect = new RoundRectangle2D.Double(
//                buttonBorderInsets.left - BUTTON_INSETS.left,
//                buttonBorderInsets.top - BUTTON_INSETS.top,
//                component.getWidth() - buttonBorderInsets.left - buttonBorderInsets.right + BUTTON_INSETS.left + BUTTON_INSETS.right,
//                component.getHeight() - buttonBorderInsets.top - buttonBorderInsets.bottom + BUTTON_INSETS.top + BUTTON_INSETS.bottom,
//                CORNER_DIAMETER,
//                CORNER_DIAMETER);
//        
//        String segmentValue = (String) component.getClientProperty(JBUTTON_SEGMENT_POSITION_KEY);
//        if (segmentValue != null && !segmentValue.equals(JBUTTON_SEGMENT_POSITION_ONLY_VALUE)) {
//            switch (segmentValue) {
//                case JBUTTON_SEGMENT_POSITION_FIRST_VALUE:
//                    Shape addonRect = new Rectangle2D.Double(
//                            buttonRect.getBounds().x + (buttonRect.getBounds().width / 2),
//                            buttonRect.getBounds().y,
//                            (buttonRect.getBounds().width / 2) + buttonBorderInsets.right,
//                            buttonRect.getBounds().height);
//                    
//                    Area compositeShape = new Area(buttonRect);
//                    compositeShape.add(new Area(addonRect));
//                    
//                    buttonRect = compositeShape;
//                    
//                    break;
//                case JBUTTON_SEGMENT_POSITION_MIDDLE_VALUE:
//                    Shape addonRect2 = new Rectangle2D.Double(
//                            buttonRect.getBounds().x - buttonBorderInsets.left,
//                            buttonRect.getBounds().y,
//                            buttonRect.getBounds().width + buttonBorderInsets.left + buttonBorderInsets.right,
//                            buttonRect.getBounds().height);
//                    
//                    Area compositeShape2 = new Area(buttonRect);
//                    compositeShape2.add(new Area(addonRect2));
//                    
//                    buttonRect = compositeShape2;
//                    break;
//                case JBUTTON_SEGMENT_POSITION_LAST_VALUE:
//                    Shape addonRect3 = new Rectangle2D.Double(
//                            buttonRect.getBounds().x - buttonBorderInsets.left,
//                            buttonRect.getBounds().y,
//                            buttonRect.getBounds().width / 2,
//                            buttonRect.getBounds().height);
//                    
//                    Area compositeShape3 = new Area(buttonRect);
//                    compositeShape3.add(new Area(addonRect3));
//                    
//                    buttonRect = compositeShape3;
//                    break;
//            }
//        } else {
//            // Do nothing, the shape is already correct.
//        }
//        
//        paintShape(graphics, component, buttonRect);
//        super.paint(graphics, component);
//    }
//
//    @Override
//    protected void paintText(Graphics graphics, JComponent component, Rectangle textRect, String text) {
//        final AbstractButton button = (AbstractButton) component;
//        
//        final Graphics2D g = (Graphics2D) graphics.create();
//        
//        if (button.isEnabled()) {
//            // Button is enabled
//            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));
//        } else {
//            // Button is disabled
//            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
//        }
//        
//        int mnemonicIndex = button.getDisplayedMnemonicIndex();
//        FontMetrics metrics = component.getFontMetrics(component.getFont());
//        BasicGraphicsUtils.drawStringUnderlineCharAt(component, g, text, mnemonicIndex, textRect.x + getTextShiftOffset(), textRect.y + metrics.getAscent() + getTextShiftOffset());
//        
//        g.dispose();
//    }
//    
//    public void paintShape(Graphics graphics, JComponent component, Shape buttonRect) {
//        final AbstractButton button = (AbstractButton) component;
//        
//        final Graphics2D g = (Graphics2D) graphics.create();
//        GraphicsHelpers.enableAntialiasing(g);
//
//        paintButtonShadowOrGlow(g, button, buttonRect);
//        paintButtonBackgroundFill(g, button, buttonRect);
//        paintButtonBorderHighlight(g, button, buttonRect);
//                
//        g.dispose();
//    }
//    
//    public static ModernBasicBorder getDefaultBorder() {
//        return new ModernBasicBorder(BUTTON_INSETS);
//    }
//    
    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ScrollBarUI", ModernScrollBarUI.class.getName());
//        defaults.put("ToggleButtonUI", ModernScrollBarUI.class.getName());
//        defaults.put("Button.border", ModernScrollBarUI.getDefaultBorder());
    }
//    
//    public static void paintButtonBackgroundFill(Graphics2D g, AbstractButton button, Shape buttonShape) {
//        final ButtonModel buttonModel = button.getModel();
//
//        if (button.isEnabled()) {
//            // Button is enabled
//            if (buttonModel.isPressed()) {
//                // Button is in its selected, depressed state
//                g.setColor(UIManager.getColor(PRIMARY_DARK_COLOR_KEY));
//            } else {
//                if ((button instanceof JButton && ((JButton) button).isDefaultButton()) || (button instanceof JToggleButton && button.isSelected())) {
//                    // Button is the default button
//                    g.setColor(UIManager.getColor(ACCENT_DARK_COLOR_KEY));
//                } else {
//                    // Button is a normal button
//                    g.setColor(UIManager.getColor(PRIMARY_MEDIUM_DARK_COLOR_KEY));
//                }
//            }
//        } else {
//            // Button is disabled
//            g.setColor(UIManager.getColor(PRIMARY_ULTRA_DARK_COLOR_KEY));
//        }
//
//        g.fill(buttonShape);
//    }
//
//    public static void paintButtonBorderHighlight(Graphics2D g, AbstractButton button, Shape buttonShape) {
//        boolean defaultOrPressedButton = button.isSelected() || (button instanceof JButton && ((JButton) button).isDefaultButton());
//
//        g.setStroke(new BasicStroke(0.25f));
//
//        if (button.hasFocus()) {
//            g.setColor(UIManager.getColor(ACCENT_HIGHLIGHT_COLOR_KEY));
//            g.draw(buttonShape);
//        } else {
//            if (defaultOrPressedButton) {
//                g.setColor(UIManager.getColor(ACCENT_MEDIUM_COLOR_KEY));
//                g.draw(buttonShape);
//            } else {
//                g.setColor(UIManager.getColor(PRIMARY_MEDIUM_COLOR_KEY));
//                g.draw(buttonShape);
//            }
//        }
//    }
//
//    public static void paintButtonShadowOrGlow(Graphics2D g, AbstractButton button, Shape buttonShape) {
//        if (button.hasFocus() && !(button instanceof JToggleButton)) {
//            g.setColor(Colors.transparentColor(UIManager.getColor(ACCENT_HIGHLIGHT_COLOR_KEY), 0.5f));
//            GraphicsHelpers.drawBorderShadow(g, buttonShape, 3);
//        } else {
//            g.setColor(Colors.transparentColor(UIManager.getColor(SHADOW_COLOR_KEY), 0.25f));
//            GraphicsHelpers.drawBorderShadow(g, buttonShape, 3);
//        }
//    }
}
