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
import com.patrickangle.commons.laf.modern.ui.icon.ModernCheckIcon;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;

/**
 *
 * @author patrickangle
 */
public class ModernCheckBoxUI extends BasicCheckBoxUI {
    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static ComponentUI createUI(JComponent c) {
        return new ModernCheckBoxUI();
    }

    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        b.setRolloverEnabled(true);
        icon = new ModernCheckIcon();        
        b.setOpaque(false);
    }

    @Override
    protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
        ModernUIComponentPainting.paintComponentText(g, b, textRect, text, getTextShiftOffset());
    }
    
    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("CheckBoxUI", ModernCheckBoxUI.class.getName());
    }
}
