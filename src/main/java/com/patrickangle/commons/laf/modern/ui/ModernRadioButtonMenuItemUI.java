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

import com.patrickangle.commons.laf.modern.ModernUIComponentPainting;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY;
import com.patrickangle.commons.util.AquaUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import sun.swing.MenuItemLayoutHelper;

/**
 *
 * @author patrickangle
 */
public class ModernRadioButtonMenuItemUI extends ModernMenuItemUI {

    public static ComponentUI createUI(JComponent c) {
        return new ModernRadioButtonMenuItemUI();
    }

    protected String getPropertyPrefix() {
        return "RadioButtonMenuItem";
    }

    @Override
    protected void paintCheckIcon(Graphics g2, MenuItemLayoutHelper lh, MenuItemLayoutHelper.LayoutResult lr, Color holdc, Color foreground) {
        if (lh.getMenuItem().isSelected()) {
            Graphics2D g = (Graphics2D) g2.create();
            
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ModernUIComponentPainting.paintComponentRadioMark(g, menuItem, lr.getCheckRect());
            g.dispose();
        }
    }
    
    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("RadioButtonMenuItemUI", ModernRadioButtonMenuItemUI.class.getName());
        
        defaults.put("RadioButtonMenuItem.font", AquaUtils.SYSTEM_FONT.deriveFont(13f));
        
        defaults.put("RadioButtonMenuItem.foreground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("RadioButtonMenuItem.selectionBackground", defaults.getColor(ACCENT_HIGHLIGHT_COLOR_KEY));
        defaults.put("RadioButtonMenuItem.selectionForeground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
    }
}
