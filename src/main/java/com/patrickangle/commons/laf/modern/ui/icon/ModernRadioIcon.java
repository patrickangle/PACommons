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
package com.patrickangle.commons.laf.modern.ui.icon;

import com.patrickangle.commons.laf.modern.ModernUIComponentPainting;
import com.patrickangle.commons.util.GraphicsHelpers;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.swing.AbstractButton;
import javax.swing.Icon;

/**
 *
 * @author patrickangle
 */
public class ModernRadioIcon implements Icon {
        private static final int RADIO_ICON_SIZE = 14;
        private static final int GENERAL_PADDING = 4;
        
        private static final Shape ICON_SHAPE = new Ellipse2D.Double(0, 0, RADIO_ICON_SIZE, RADIO_ICON_SIZE);
        
        @Override
        public void paintIcon(Component c, Graphics graphics, int x, int y) {
            // A check icon is only ever used inside the CheckBoxUI, and therefor will always inheiret from AbstractButton.
            AbstractButton button = (AbstractButton) c;
            
            Graphics2D g = (Graphics2D) graphics.create();
            GraphicsHelpers.enableAntialiasing(g);
            GraphicsHelpers.enableStrokeNormalization(g);
            g.translate(x + GENERAL_PADDING, y + GENERAL_PADDING);
            
            ModernUIComponentPainting.paintComponentShadowOrFocus(g, c, ICON_SHAPE);
            ModernUIComponentPainting.paintComponentBackgroundFill(g, button, ICON_SHAPE);
            ModernUIComponentPainting.paintComponentBorderHighlight(g, button, ICON_SHAPE);
            
            if (button.getModel().isSelected()) {
                ModernUIComponentPainting.paintComponentRadioMark(g, c, ICON_SHAPE);
            }
        }

        @Override
        public int getIconHeight() {
            return RADIO_ICON_SIZE + (GENERAL_PADDING * 2);
        }

        @Override
        public int getIconWidth() {
            return RADIO_ICON_SIZE + (GENERAL_PADDING * 2);
        }
    }
