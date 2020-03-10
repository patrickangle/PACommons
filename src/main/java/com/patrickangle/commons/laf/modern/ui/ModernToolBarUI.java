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

import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.MINIMUM_HEIGHT;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.MINIMUM_LEADING_TRAILING_AREA;
import static com.patrickangle.commons.laf.modern.ui.ModernButtonUI.TOOLBAR_BUTTON_AREA_HEIGHT;
import com.patrickangle.commons.laf.modern.ui.util.GradientTexturePaint;
import com.patrickangle.commons.laf.modern.ui.util.NoisePaint;
import com.patrickangle.commons.util.AquaUtils;
import com.patrickangle.commons.util.Colors;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;

/**
 *
 * @author patrickangle
 */
public class ModernToolBarUI extends BasicToolBarUI {

    private static final int MINIMUM_HEIGHT = 36;
    
    private WindowAdapter windowFocusListener;
    private HierarchyListener buttonHierarchyListener;
    protected Window currentWindow;

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new ModernToolBarUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
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
                updateFocusListener(c);
            }
        };
    }

    @Override
    protected void installListeners() {
        super.installListeners(); //To change body of generated methods, choose Tools | Templates.
        toolBar.removeMouseListener(dockingListener);
        toolBar.removeMouseMotionListener(dockingListener);
        toolBar.addHierarchyListener(buttonHierarchyListener);
    }

    @Override
    protected void uninstallListeners() {
        super.uninstallListeners(); //To change body of generated methods, choose Tools | Templates.
    }

    protected void updateFocusListener(JComponent b) {
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

    private GradientTexturePaint cachedPaint = null;
    private int cachedHeight = 0;

    @Override
    public void paint(Graphics g, JComponent c) {
        if (!AquaUtils.isMac()) {
            g.setColor(UIManager.getColor("ToolBar.background"));
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
    }

    @Override
    protected void setBorderToRollover(Component c) {
        if (c instanceof AbstractButton) {
            ((AbstractButton) c).putClientProperty(ModernButtonUI.Style.Key, ModernButtonUI.Style.Toolbar);
        }
    }

    @Override
    protected void setBorderToNonRollover(Component c) {
        if (c instanceof AbstractButton) {
            ((AbstractButton) c).putClientProperty(ModernButtonUI.Style.Key, ModernButtonUI.Style.Toolbar);
        }
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        Dimension superDims = super.getMinimumSize(c);
        return new Dimension(superDims != null ? superDims.width : 0, Math.max(MINIMUM_HEIGHT, superDims != null ? superDims.height : 0));
    }

//    @Override
//    protected Border getNonRolloverBorder(AbstractButton b) {
//        return b.getBorder();
//    }
//
//    @Override
//    protected Border getRolloverBorder(AbstractButton b) {
//        return b.getBorder();
//    }
//
//    @Override
//    protected void installNormalBorders(JComponent c) {
//    }
//
//    @Override
//    protected void installNonRolloverBorders(JComponent c) {
//    }
//
//    @Override
//    protected void installRolloverBorders(JComponent c) {
//    }
//
//    @Override
//    protected void setBorderToNonRollover(Component c) {
//    }
//
//    @Override
//    protected void setBorderToNormal(Component c) {
//    }
    @Override
    public void update(Graphics g, JComponent c) {
        super.update(g, c); //To change body of generated methods, choose Tools | Templates.
        g.setColor(Color.BLACK);
        g.drawLine(0, c.getHeight() - 1, c.getWidth(), c.getHeight() - 1);
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ToolBarUI", ModernToolBarUI.class.getName());
        defaults.put("ToolBar.Border", new EmptyBorder(0, 0, 0, 0));
    }
}
