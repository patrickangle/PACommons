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
package com.patrickangle.commons.laf.modern.colors;

import com.patrickangle.commons.util.AquaUtils;
import com.patrickangle.commons.util.Colors;
import java.awt.Color;

/**
 *
 * @author patrickangle
 */
public class MacModernColors extends AbstractModernColors {
    private static int lastUpdate = 0;
    private static Color accentColor;
    
    @Override
    public Color accentColor() {
        // There is a cost associated with this call, so we actually cache the result and update the color if it has been more than 5 seconds since the last update. Not perfect, but better than hundreds of times a second.
        if (accentColor == null || (lastUpdate + 5000) < System.currentTimeMillis()) {
            accentColor = Colors.withBrightness(AquaUtils.getAccentColor(), 0.7f);
        }
        
        return accentColor;
    }
}
