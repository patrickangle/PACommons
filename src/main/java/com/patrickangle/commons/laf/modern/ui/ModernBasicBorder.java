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

import com.patrickangle.commons.laf.modern.ModernShapedComponentUI;
import com.patrickangle.commons.laf.modern.ModernUIComponentPainting;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.SHADOW_COLOR_KEY;
import com.patrickangle.commons.util.Colors;
import com.patrickangle.commons.util.GraphicsHelpers;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;

/**
 *
 * @author patrickangle
 */
public class ModernBasicBorder implements Border, UIResource {

    private static final int BLUR_SAFE_REGION = 4;
    private Insets additionalInsets;

    public ModernBasicBorder(Insets additionalInsets) {
        this.additionalInsets = additionalInsets;
    }

    @Override
    public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
        if (component instanceof JComponent && ((JComponent) component).getUI() instanceof ModernShapedComponentUI) {
            Shape shape = ((ModernShapedComponentUI) ((JComponent) component).getUI()).getShape((JComponent) component);
            
            
            
            final Graphics2D g = (Graphics2D) graphics.create();
            GraphicsHelpers.enableAntialiasing(g);
            g.translate(x, y);
            
//            ModernUIComponentPainting.paintComponentShadowOrFocus(g, component, shape);

//            if (component.hasFocus() && !(component instanceof JToggleButton)) {
//                g.setColor(Colors.transparentColor(UIManager.getColor(ACCENT_HIGHLIGHT_COLOR_KEY), 0.5f));
//                GraphicsHelpers.drawBorderShadow(g, shape, 3);
//            } else {
//                g.setColor(Colors.transparentColor(UIManager.getColor(SHADOW_COLOR_KEY), 0.25f));
//                GraphicsHelpers.drawBorderShadow(g, shape, 3);
//            }
            
            g.dispose();
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new InsetsUIResource(BLUR_SAFE_REGION + additionalInsets.top, BLUR_SAFE_REGION + additionalInsets.left, BLUR_SAFE_REGION + additionalInsets.bottom, BLUR_SAFE_REGION + additionalInsets.right);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
