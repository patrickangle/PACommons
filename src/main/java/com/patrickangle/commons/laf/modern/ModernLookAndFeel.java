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
package com.patrickangle.commons.laf.modern;

import javax.swing.UIDefaults;
import javax.swing.plaf.basic.BasicLookAndFeel;

/**
 * 
 * @author patrickangle
 */
public final class ModernLookAndFeel extends BasicLookAndFeel {

    public static final String NAME = "Modern";

    public ModernLookAndFeel() {

    }


    @Override
    public UIDefaults getDefaults() {
            final UIDefaults defaults = super.getDefaults();
            ModernUIUtilities.installLafDefaults(defaults);

            return defaults;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getID() {
        return getName();
    }

    @Override
    public String getDescription() {
        return "Modern Look and Feel";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return true;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    @Override
    public boolean getSupportsWindowDecorations() {
        return true;
    }
}
