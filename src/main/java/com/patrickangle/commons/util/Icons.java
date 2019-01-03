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
package com.patrickangle.commons.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author patrickangle
 */
public class Icons {

    public static Icon coloredTagIcon(final Color innerColor, final Color outerColor, final int size) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics graphics, int x, int y) {
                Graphics2D g = (Graphics2D) graphics.create();
                GraphicsHelpers.enableAntialiasing(g);

                g.setColor(outerColor);
                g.fillOval(x, y, size, size);

                g.setColor(innerColor);
                g.fillOval(x + 1, y + 1, size - 2, size - 2);

                g.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }

        };
    }
}
