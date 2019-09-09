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

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 *
 * @author patrickangle
 */
public class ModernSplitPaneUI extends BasicSplitPaneUI {

    @Override
    public void installUI(JComponent c) {
        super.installUI(c); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new BasicSplitPaneDivider(this) {
            @Override
            public void paint(Graphics g) {
                // Don't.
            }
        };
    }

    
    
    
    public static ComponentUI createUI(final JComponent x) {
        return new ModernSplitPaneUI();
    }
    
    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("SplitPaneUI", ModernSplitPaneUI.class.getName());
    }
}
