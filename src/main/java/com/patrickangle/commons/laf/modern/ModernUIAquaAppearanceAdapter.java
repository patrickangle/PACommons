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

import com.patrickangle.commons.logging.Logging;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author patrickangle
 */
public class ModernUIAquaAppearanceAdapter {
    
    public static enum SystemColorScheme {
        Blue,
        Purple,
        Pink,
        Red,
        Orange,
        Yellow,
        Green,
        Graphite;
    }

    public static final Color mojaveLightBlueAccentColor = new Color(0, 122, 255);
    public static final Color mojaveDarkBlueAccentColor = new Color(0, 122, 255);
    public static final Color mojaveLightBlueHighContrastAccentColor = new Color(2, 64, 221);
    public static final Color mojaveDarkBlueHighContrastAccentColor = new Color(46, 147, 255);

    public static final Color mojaveLightPurpleAccentColor = new Color(149, 61, 150);
    public static final Color mojaveDarkPurpleAccentColor = new Color(165, 80, 167);
    public static final Color mojaveLightPurpleHighContrastAccentColor = new Color(149, 61, 150);
    public static final Color mojaveDarkPurpleHighContrastAccentColor = new Color(212, 81, 214);

    public static final Color mojaveLightPinkAccentColor = new Color(247, 79, 158);
    public static final Color mojaveDarkPinkAccentColor = new Color(247, 79, 158);
    public static final Color mojaveLightPinkHighContrastAccentColor = new Color(247, 79, 156);
    public static final Color mojaveDarkPinkHighContrastAccentColor = new Color(247, 79, 156);

    public static final Color mojaveLightRedAccentColor = new Color(224, 56, 61);
    public static final Color mojaveDarkRedAccentColor = new Color(255, 82, 87);
    public static final Color mojaveLightRedHighContrastAccentColor = new Color(224, 56, 61);
    public static final Color mojaveDarkRedHighContrastAccentColor = new Color(255, 82, 87);

    public static final Color mojaveLightOrangeAccentColor = new Color(247, 130, 26);
    public static final Color mojaveDarkOrangeAccentColor = new Color(247, 130, 26);
    public static final Color mojaveLightOrangeHighContrastAccentColor = new Color(253, 123, 26);
    public static final Color mojaveDarkOrangeHighContrastAccentColor = new Color(235, 123, 26);

    public static final Color mojaveLightYellowAccentColor = new Color(252, 184, 40);
    public static final Color mojaveDarkYellowAccentColor = new Color(252, 184, 40);
    public static final Color mojaveLightYellowHighContrastAccentColor = new Color(196, 143, 29);
    public static final Color mojaveDarkYellowHighContrastAccentColor = new Color(196, 143, 29);

    public static final Color mojaveLightGreenAccentColor = new Color(98, 186, 70);
    public static final Color mojaveDarkGreenAccentColor = new Color(98, 186, 70);
    public static final Color mojaveLightGreenHighContrastAccentColor = new Color(88, 168, 64);
    public static final Color mojaveDarkGreenHighContrastAccentColor = new Color(88, 168, 64);

    public static final Color mojaveLightGraphiteAccentColor = new Color(152, 152, 152);
    public static final Color mojaveDarkGraphiteAccentColor = new Color(140, 140, 140);
    public static final Color mojaveLightGraphiteHighContrastAccentColor = new Color(107, 107, 107);
    public static final Color mojaveDarkGraphiteHighContrastAccentColor = new Color(140, 140, 140);

    protected static Color systemAccentColor() {
        try {
            Class appearanceManagerClass = Class.forName("org.violetlib.aqua.AppearanceManager");
            Method getCurrentAppearanceMethod = appearanceManagerClass.getMethod("getCurrentAppearance");
            Object currentAppearance = getCurrentAppearanceMethod.invoke(null);
            Method getColorsMethod = currentAppearance.getClass().getMethod("getColors");

            Object colors = getColorsMethod.invoke(currentAppearance);

            if (colors instanceof Map) {
                if (((Map) colors).get("controlAccent") instanceof Color) {
                    return (Color) ((Map) colors).get("controlAccent");
                }
            }

            return null;
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            return null;
        }
    }
    
    public static SystemColorScheme systemColorScheme() {
        Color sac = systemAccentColor();
        
        if (sac == null) {
            return SystemColorScheme.Blue;
        }
        
        if (sac.equals(mojaveLightBlueAccentColor) ||
                sac.equals(mojaveDarkBlueAccentColor) ||
                sac.equals(mojaveLightBlueHighContrastAccentColor) ||
                sac.equals(mojaveDarkBlueHighContrastAccentColor)) {
            return SystemColorScheme.Blue;
        } else if (sac.equals(mojaveLightPurpleAccentColor) ||
                sac.equals(mojaveDarkPurpleAccentColor) ||
                sac.equals(mojaveLightPurpleHighContrastAccentColor) ||
                sac.equals(mojaveDarkPurpleHighContrastAccentColor)) {
            return SystemColorScheme.Purple;
        } else if (sac.equals(mojaveLightPinkAccentColor) ||
                sac.equals(mojaveDarkPinkAccentColor) ||
                sac.equals(mojaveLightPinkHighContrastAccentColor) ||
                sac.equals(mojaveDarkPinkHighContrastAccentColor)) {
            return SystemColorScheme.Pink;
        } else if (sac.equals(mojaveLightRedAccentColor) ||
                sac.equals(mojaveDarkRedAccentColor) ||
                sac.equals(mojaveLightRedHighContrastAccentColor) ||
                sac.equals(mojaveDarkRedHighContrastAccentColor)) {
            return SystemColorScheme.Red;
        } else if (sac.equals(mojaveLightOrangeAccentColor) ||
                sac.equals(mojaveDarkOrangeAccentColor) ||
                sac.equals(mojaveLightOrangeHighContrastAccentColor) ||
                sac.equals(mojaveDarkOrangeHighContrastAccentColor)) {
            return SystemColorScheme.Orange;
        } else if (sac.equals(mojaveLightYellowAccentColor) ||
                sac.equals(mojaveDarkYellowAccentColor) ||
                sac.equals(mojaveLightYellowHighContrastAccentColor) ||
                sac.equals(mojaveDarkYellowHighContrastAccentColor)) {
            return SystemColorScheme.Yellow;
        } else if (sac.equals(mojaveLightGreenAccentColor) ||
                sac.equals(mojaveDarkGreenAccentColor) ||
                sac.equals(mojaveLightGreenHighContrastAccentColor) ||
                sac.equals(mojaveDarkGreenHighContrastAccentColor)) {
            return SystemColorScheme.Green;
        } else if (sac.equals(mojaveLightGraphiteAccentColor) ||
                sac.equals(mojaveDarkGraphiteAccentColor) ||
                sac.equals(mojaveLightGraphiteHighContrastAccentColor) ||
                sac.equals(mojaveDarkGraphiteHighContrastAccentColor)) {
            return SystemColorScheme.Graphite;
        } else {
            return SystemColorScheme.Blue;
        }
    }

}
