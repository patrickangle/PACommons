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

import com.patrickangle.commons.laf.modern.util.GraphicsUtils;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 *
 * @author patrickangle
 */
public class ModernLabelUI extends BasicLabelUI {
    public static ComponentUI createUI(JComponent component) {
        return new ModernLabelUI();
    }

    @Override
    public void paint(Graphics graphics, JComponent c) {
        Graphics2D g = GraphicsUtils.configureGraphics(graphics);
        super.paint(g, c);
        g.dispose();
    }
    
    

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("LabelUI", ModernLabelUI.class.getName());
    }
}
