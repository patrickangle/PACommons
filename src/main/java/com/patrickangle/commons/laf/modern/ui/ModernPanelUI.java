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

import static com.patrickangle.commons.laf.modern.ModernUIUtilities.BACKGROUND_COLOR_KEY;
import com.patrickangle.commons.laf.modern.ModernUIAquaUtils;
import com.patrickangle.commons.util.OperatingSystems;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 *
 * @author patrickangle
 */
public class ModernPanelUI extends BasicPanelUI {

    @Override
    public void installUI(JComponent c) {
        super.installUI(c); //To change body of generated methods, choose Tools | Templates.
        c.setOpaque(false);
    }

    public static ComponentUI createUI(final JComponent x) {
        return new ModernPanelUI();
    }

    @Override
    public final void update(final Graphics g, final JComponent c) {
        if (c.isOpaque() && OperatingSystems.current() == OperatingSystems.Macintosh) {
            ModernUIAquaUtils.fillRect(g, c);
        }
        paint(g, c);
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("PanelUI", ModernPanelUI.class.getName());
        defaults.put("Panel.background", defaults.getColor(BACKGROUND_COLOR_KEY));
    }
}
