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

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
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

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new ModernToolBarUI();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            g.setColor(UIManager.getColor("ToolBar.background"));
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
    }

    @Override
    protected Border getNonRolloverBorder(AbstractButton b) {
        return b.getBorder();
    }

    @Override
    protected Border getRolloverBorder(AbstractButton b) {
        return b.getBorder();
    }

    @Override
    protected void installNormalBorders(JComponent c) {
    }

    @Override
    protected void installNonRolloverBorders(JComponent c) {
    }

    @Override
    protected void installRolloverBorders(JComponent c) {
    }
    
    

    @Override
    protected void setBorderToNonRollover(Component c) {
    }

    @Override
    protected void setBorderToNormal(Component c) {
    }
    
    

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ToolBarUI", ModernToolBarUI.class.getName());
        defaults.put("ToolBar.Border", new EmptyBorder(0, 0, 0, 0));
    }
}
