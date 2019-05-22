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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
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
        super.installListeners();
    }

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
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle bounds) {
        g.setColor(UIManager.getColor(ModernUIUtilities.WORKSPACE_BACKGROUND_COLOR_KEY));
    }
    
    

    @Override
    protected Dimension getMinimumThumbSize() {
        final int thickness = getThickness();
        return isVertical() ? new Dimension(thickness, thickness * 2) : new Dimension(thickness * 2, thickness);
    }

    protected int getThickness() {
        return 9;
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

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Colors.transparentColor(UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY), (float) (isThumbRollover() ? 1.0f : 0.5f)));
        
        int cornerDiameter = 0;
        
        if (vertical) {
            cornerDiameter = w;
        } else {
            cornerDiameter = h;
        }
        
        g.fillRoundRect(hGap + 1, vGap + 1, w - 1, h - 1, cornerDiameter, cornerDiameter);
    }

    @Override
    public boolean getSupportsAbsolutePositioning() {
        return true;
    }

    protected int adjustThumbWidth(int width) {
        return width;
    }

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


    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ScrollBarUI", ModernScrollBarUI.class.getName());
    }
}
