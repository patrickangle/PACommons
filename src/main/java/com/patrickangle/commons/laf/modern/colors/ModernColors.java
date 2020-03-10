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

import com.patrickangle.commons.laf.modern.util.SwingUtilities;
import java.awt.Color;
import java.awt.Paint;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;

/**
 *
 * @author patrickangle
 */
public interface ModernColors {

    public Color componentShadowColor(JComponent c);

    public Paint componentFocusRingPaint(JComponent c);
    
    public Paint componentToolbarRimPaint(JComponent c);

    public Paint componentNormalPaint(JComponent c);

    public Paint componentSelectedPaint(JComponent c);

    public Paint componentRolloverPaint(JComponent c);

    public Paint componentRolloverSelectedPaint(JComponent c);

    public Paint componentPressedPaint(JComponent c);

    public Paint componentPressedSelectedPaint(JComponent c);

    public Paint componentDisabledPaint(JComponent c);

    public Paint componentNormalTextPaint(JComponent c);

    public Paint componentSelectedTextPaint(JComponent c);

    public Paint componentDisabledTextPaint(JComponent c);

    public Color textAreaNormalTextColor();

    public Color textAreaDisabledTextColor();

    public Paint textAreaNormalBackgroundPaint(JComponent c);

    public Paint textAreaDisabledBackgroundPaint(JComponent c);

    public Paint textAreaNormalBaselinePaint(JComponent c);

    public Paint textAreaDisabledBaselinePaint(JComponent c);

    public Paint textAreaNormalBorderPaint(JComponent c);

    public Paint textAreaDisabledBorderPaint(JComponent c);

    public Color textAreaSelectionHighlightColor();

    public Paint windowBackgroundPaint(JComponent c);

    public Paint panelBackgroundPaint(JComponent c);

    public Paint workspaceBackgroundPaint(JComponent c);

    public default Paint componentPaint(JComponent c) {
        if (c instanceof AbstractButton) {
            return componentPaint((AbstractButton) c);
        }

        if (c.isEnabled()) {
            // Enabled
            return componentNormalPaint(c);
        } else {
            // Disabled
            return componentDisabledPaint(c);
        }
    }

    public default Paint componentPaint(AbstractButton button) {
        final ButtonModel buttonModel = button.getModel();

        if (button.isEnabled()) {
            // Enabled
            if (buttonModel.isPressed()) {
                // Enabled + Pressed
                if (SwingUtilities.buttonIsDefaultOrSelected(button)) {
                    // Enabled + Pressed + Default or Selected
                    return componentPressedSelectedPaint(button);
                } else {
                    // Enabled + Pressed + Normal
                    return componentPressedPaint(button);
                }
            } else if (buttonModel.isRollover()) {
                // Enabled + Hover
                if (SwingUtilities.buttonIsDefaultOrSelected(button)) {
                    // Enabled + Hover + Default or Selected
                    return componentRolloverSelectedPaint(button);
                } else {
                    // Enabled + Hover + Normal
                    return componentRolloverPaint(button);
                }
            } else {
                // Enabled + Normal
                if (SwingUtilities.buttonIsDefaultOrSelected(button)) {
                    // Enabled + Normal + Default or Selected
                    return componentSelectedPaint(button);
                } else {
                    // Enabled + Normal + Normal
                    return componentNormalPaint(button);
                }
            }
        } else {
            // Disabled
            return componentDisabledPaint(button);
        }
    }

    public default Paint componentToolbarPaint(JComponent c) {
        if (c instanceof AbstractButton) {
            return componentToolbarPaint((AbstractButton) c);
        }

        if (c.isEnabled()) {
            // Enabled
            return componentNormalPaint(c);
        } else {
            // Disabled
            return componentDisabledPaint(c);
        }
    }

    /**
     * Get the color to paint the component's background in it's current state.
     * For toolbars, we don't use any of the selected state, as that is handled
     * at the icon level
     *
     * @param button
     * @return
     */
    public default Paint componentToolbarPaint(AbstractButton button) {
        final ButtonModel buttonModel = button.getModel();

        if (button.isEnabled()) {
            // Enabled
            if (buttonModel.isPressed()) {
                // Enabled + Pressed
                return componentRolloverPaint(button);
            } else {
                // Enabled + Normal
                return componentNormalPaint(button);
            }
        } else {
            // Disabled
            return componentDisabledPaint(button);
        }
    }
}
