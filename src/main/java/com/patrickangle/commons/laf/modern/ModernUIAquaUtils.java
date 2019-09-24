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

import com.patrickangle.commons.util.AquaUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.plaf.UIResource;

/**
 *
 * @author patrickangle
 */
public class ModernUIAquaUtils {
    
    
    private static boolean isWindowTextured(final Component c) {
        if (!(c instanceof JComponent)) {
            return false;
        }
        final JRootPane pane = ((JComponent) c).getRootPane();
        if (pane == null) {
            return false;
        }
        Object prop = pane.getClientProperty(
                AquaUtils.WINDOW_BRUSH_METAL_LOOK);
        if (prop != null) {
            return Boolean.parseBoolean(prop.toString());
        }
        prop = pane.getClientProperty(AquaUtils.WINDOW_STYLE);
        return prop != null && "textured".equals(prop);
    }

    private static Color resetAlpha(final Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
    }

    public static void fillRect(final Graphics g, final Component c) {
        fillRect(g, c, c.getBackground(), 0, 0, c.getWidth(), c.getHeight());
    }

    public static void fillRect(final Graphics g, final Component c, final Color color, final int x, final int y, final int w, final int h) {
        if (!(g instanceof Graphics2D)) {
            return;
        }
        final Graphics2D cg = (Graphics2D) g.create();
        try {
            if (color instanceof UIResource && isWindowTextured(c) && color.equals(SystemColor.window)) {
                
                cg.setComposite(AlphaComposite.Src);
                cg.setColor(resetAlpha(color));
            } else {
                cg.setColor(color);
            }
            cg.fillRect(x, y, w, h);
        } finally {
            cg.dispose();
        }
    }
}
