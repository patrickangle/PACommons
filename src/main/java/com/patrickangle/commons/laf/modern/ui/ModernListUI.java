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

import static com.patrickangle.commons.laf.modern.ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.PRIMARY_ULTRA_DARK_COLOR_KEY;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;

/**
 *
 * @author patrickangle
 */
public class ModernListUI extends BasicListUI {
    public static ComponentUI createUI(final JComponent x) {
        return new ModernListUI();
    }
    
    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ListUI", ModernListUI.class.getName());

        
        defaults.put("List.background", defaults.getColor(PRIMARY_ULTRA_DARK_COLOR_KEY));
        defaults.put("List.foreground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("List.selectionBackground", defaults.getColor(ACCENT_HIGHLIGHT_COLOR_KEY));
        defaults.put("List.selectionForeground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
    }
}
