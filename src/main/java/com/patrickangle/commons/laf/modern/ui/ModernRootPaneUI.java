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

import com.patrickangle.commons.laf.modern.ModernUIColors;
import com.patrickangle.commons.laf.modern.ui.util.GradientTexturePaint;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

/**
 *
 * @author patrickangle
 */
public class ModernRootPaneUI extends AbstractModernRootPaneUI {
    public static final String UID_CORNER_DIAMETER = "ModernRootPaneUI.cornerDiameter";
    public static final String UID_BACKGROUND_TOP = "ModernRootPaneUI.backgroundTop";
    public static final String UID_BACKGROUND_BOTTOM = "ModernRootPaneUI.backgroundBottom";
    
    
    // These properties, while originally for the Aqua LAF on macOS, are used
    // here to provide compatibility. They apply across all platforms.
    // https://developer.apple.com/library/archive/technotes/tn2007/tn2196.html

    /**
     * A client property key for enabling a unified window chrome across the
     * entire height of the window instead of a hard break at the title bar.
     * This property is the equivalent to the Aqua LAF's
     * apple.awt.brushMetalLook property.
     *
     * Expects either null, Boolean.TRUE, or Boolean.FALSE as the corresponding
     * value.
     */
    public static final String CPK_UNIFY_WINDOW_CHROME = "apple.awt.brushMetalLook";

    /**
     * A client property key for setting the file that the window is currently
     * affecting. This property is the equivalent to the Aqua LAF's
     * Window.documentFile property.
     *
     * Expects either null or a File as the corresponding value.
     */
    public static final String CPK_DOCUMENT_FILE = "Window.documentFile";

    /**
     * A client property key for setting that the current document that the
     * window contains has been modified, but not saved. This property is the
     * equivalent to the Aqua LAF's Window.documentModified property.
     *
     * Expects either null, Boolean.TRUE, or Boolean.FALSE as the corresponding
     * value.
     */
    public static final String CPK_DOCUMENT_MODIFIED = "Window.documentModified";

    public static ComponentUI createUI(JComponent c) {
        return new ModernRootPaneUI();
    }
    
    protected Shape getShape(Rectangle bounds) {
        Area a = new Area(new RoundRectangle2D.Double(
                bounds.x,
                bounds.y,
                bounds.width,
                bounds.height,
                UIManager.getInt(UID_CORNER_DIAMETER),
                UIManager.getInt(UID_CORNER_DIAMETER)
        ));
        
        a.add(new Area(new Rectangle(
                bounds.x,
                bounds.y + bounds.height - (UIManager.getInt(UID_CORNER_DIAMETER) / 2),
                bounds.width,
                (UIManager.getInt(UID_CORNER_DIAMETER) / 2)
        )));
        
        return a;
    }

    public void paintBackground(Graphics g, JRootPane rootPane, Rectangle bounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        if (rootPane.getClientProperty(CPK_UNIFY_WINDOW_CHROME) == Boolean.TRUE) {
            g2.setPaint(new GradientPaint(
                    0,
                    bounds.y,
                    UIManager.getColor(UID_BACKGROUND_TOP),
                    0,
                    bounds.y + 68,
                    UIManager.getColor(UID_BACKGROUND_BOTTOM)
            ));
        } else {
            g2.setPaint(new GradientPaint(
                    0,
                    bounds.y,
                    UIManager.getColor(UID_BACKGROUND_TOP),
                    0,
                    bounds.y + getTitlePane().getHeight(),
                    UIManager.getColor(UID_BACKGROUND_BOTTOM)
            ));
        }
        
        g2.fill(getShape(bounds));
        
    }

    public void paintBorder(Graphics g, JRootPane rootPane, Rectangle bounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(Color.GRAY);
        g2.draw(getShape(bounds));
        
        if (rootPane.getClientProperty(CPK_UNIFY_WINDOW_CHROME) != Boolean.TRUE) {
            g2.setColor(Color.BLACK);
            g2.drawLine(bounds.x, bounds.y + getTitlePane().getHeight(), bounds.x + bounds.width, bounds.y + getTitlePane().getHeight());
        }
        
//        g.setColor(Color.RED);
//        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public JComponent createHeaderComponent(JRootPane root) {
        System.out.println("Title created");
        return new ModernTitlePane(root);
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("RootPaneUI", ModernRootPaneUI.class.getName());
        defaults.put(UID_CORNER_DIAMETER, 8);
        defaults.put(UID_BACKGROUND_TOP, ModernUIColors.primaryDarkColor);
        defaults.put(UID_BACKGROUND_BOTTOM, ModernUIColors.backgroundColor);
    }
}
