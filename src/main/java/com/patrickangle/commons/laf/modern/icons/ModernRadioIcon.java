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
package com.patrickangle.commons.laf.modern.icons;

import com.patrickangle.commons.laf.modern.ModernLookAndFeel;
import com.patrickangle.commons.laf.modern.ModernUIComponentPainting;
import com.patrickangle.commons.laf.modern.util.GraphicsUtils;
import com.patrickangle.commons.laf.modern.util.PaintingUtils;
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

    private static final int RADIO_ICON_SIZE = 16;
    private static final int RADIO_ICON_INSET_SIZE = PaintingUtils.FOCUSABLE_COMPONENT_INSET_SIZE;
    private static final Shape RADIO_ICON_SHAPE = new Ellipse2D.Float(0, 0, RADIO_ICON_SIZE, RADIO_ICON_SIZE);
    private static final Shape RADIO_ICON_SELECTION_SHAPE = new Ellipse2D.Float((RADIO_ICON_SIZE / 2) - (6 / 2), (RADIO_ICON_SIZE / 2) - (6 / 2), 6, 6);

    @Override
    public void paintIcon(Component c, Graphics graphics, int x, int y) {
        AbstractButton component = (AbstractButton) c;
        Graphics2D g = GraphicsUtils.configureGraphics(graphics);

        g.translate(RADIO_ICON_INSET_SIZE, RADIO_ICON_INSET_SIZE);

        g.setPaint(ModernLookAndFeel.colors.componentPaint(component));
        g.fill(RADIO_ICON_SHAPE);

        if (component.isFocusOwner()) {
            PaintingUtils.paintFocusRing(g, component, RADIO_ICON_SHAPE);
        } else {
            PaintingUtils.paintShadow(g, component, RADIO_ICON_SHAPE);
        }

        if (component.getModel().isSelected()) {
            g.setPaint(ModernLookAndFeel.colors.componentNormalTextPaint(component));
            g.fill(RADIO_ICON_SELECTION_SHAPE);
        }

        g.dispose();
    }

    @Override
    public int getIconWidth() {
        return RADIO_ICON_SIZE + (RADIO_ICON_INSET_SIZE * 2);
    }

    @Override
    public int getIconHeight() {
        return RADIO_ICON_SIZE + (RADIO_ICON_INSET_SIZE * 2);
    }
}
