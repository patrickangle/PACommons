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

import com.patrickangle.commons.laf.modern.ui.ModernButtonUI;
import com.patrickangle.commons.laf.modern.ui.ModernComboBoxUI;
import com.patrickangle.commons.util.Colors;
import com.patrickangle.commons.util.GraphicsHelpers;
import com.patrickangle.commons.util.Images;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.IconUIResource;

/**
 *
 * @author patrickangle
 */
public class ModernUIUtilities {

    public static final String ACCENT_HIGHLIGHT_COLOR_KEY = "ModernLAF.AccentHighlightColor";
    public static final String ACCENT_LIGHT_COLOR_KEY = "ModernLAF.AccentLightColor";
    public static final String ACCENT_MEDIUM_COLOR_KEY = "ModernLAF.AccentMediumColor";
    public static final String ACCENT_DARK_COLOR_KEY = "ModernLAF.AccentDarkColor";

    public static final String PRIMARY_LIGHT_COLOR_KEY = "ModernLAF.PrimaryLightColor";
    public static final String PRIMARY_MEDIUM_LIGHT_COLOR_KEY = "ModernLAF.PrimaryMediumLightColor";
    public static final String PRIMARY_MEDIUM_COLOR_KEY = "ModernLAF.PrimaryMediumColor";
    public static final String PRIMARY_MEDIUM_DARK_COLOR_KEY = "ModernLAF.PrimaryMediumDarkColor";
    public static final String PRIMARY_DARK_COLOR_KEY = "ModernLAF.PrimaryDarkColor";
    public static final String PRIMARY_ULTRA_DARK_COLOR_KEY = "ModernLAF.rimaryUltraDarkColor";
    public static final String BACKGROUND_COLOR_KEY = "ModernLAF.BackgroundColor";
    
    public static final String WORKSPACE_BACKGROUND_COLOR_KEY = "ModernLAF.WorkspaceBackgroundColor";

    public static final String SHADOW_COLOR_KEY = "ModernLAF.ShadowColor";

    public static void installDefaults(UIDefaults defaults) {
        defaults.put(ACCENT_HIGHLIGHT_COLOR_KEY, new Color(0x0090e8));
        defaults.put(ACCENT_LIGHT_COLOR_KEY, new Color(0x146bb9));
        defaults.put(ACCENT_MEDIUM_COLOR_KEY, new Color(0x1e4e89));
        defaults.put(ACCENT_DARK_COLOR_KEY, new Color(0x1d3c6c));

        defaults.put(PRIMARY_LIGHT_COLOR_KEY, new Color(0xd3e1eb));
        defaults.put(PRIMARY_MEDIUM_LIGHT_COLOR_KEY, new Color(0x9faebb));
        defaults.put(PRIMARY_MEDIUM_COLOR_KEY, new Color(0x67778a));
        defaults.put(PRIMARY_MEDIUM_DARK_COLOR_KEY, new Color(0x47566d));
        defaults.put(PRIMARY_DARK_COLOR_KEY, new Color(0x29323f));
        defaults.put(PRIMARY_ULTRA_DARK_COLOR_KEY, new Color(0x232a35));
        defaults.put(BACKGROUND_COLOR_KEY, new Color(0x212833));
        
        defaults.put(WORKSPACE_BACKGROUND_COLOR_KEY, new Color(0x151a21));

        defaults.put(SHADOW_COLOR_KEY, Color.BLACK);

//        defaults.put("Panel.background", defaults.getColor(BACKGROUND_COLOR_KEY));
//        defaults.put("window", defaults.getColor(BACKGROUND_COLOR_KEY));
//        defaults.put("control", defaults.getColor(BACKGROUND_COLOR_KEY));

        Font systemFont = new Font(Font.DIALOG, Font.PLAIN, 14);

        defaults.put("defaultFont", systemFont);

        ModernButtonUI.installIntoDefaults(defaults);
//        ModernComboBoxUI.installIntoDefaults(defaults);

//        defaults.put("OptionPane.informationIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneInfo.png"))));
//        defaults.put("OptionPane.questionIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneQuestion.png"))));
//        defaults.put("OptionPane.warningIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneWarning.png"))));
//        defaults.put("OptionPane.errorIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneError.png"))));

//        defaults.put("ButtonUI", ModernButtonUI.class.getName());
//        defaults.put("Button.border", new ModernButtonPainter(new Insets(2, 8, 2, 8)));
    }

    public static void paintButtonBackgroundFill(Graphics2D g, AbstractButton button, Shape buttonShape) {
        final ButtonModel buttonModel = button.getModel();

        if (button.isEnabled()) {
            // Button is enabled
            if (buttonModel.isPressed()) {
                // Button is in its selected, depressed state
                g.setColor(UIManager.getColor(PRIMARY_DARK_COLOR_KEY));
            } else {
                if ((button instanceof JButton && ((JButton) button).isDefaultButton())) {
                    // Button is the default button
                    g.setColor(UIManager.getColor(ACCENT_DARK_COLOR_KEY));
                } else {
                    // Button is a normal button
                    g.setColor(UIManager.getColor(PRIMARY_MEDIUM_DARK_COLOR_KEY));
                }
            }
        } else {
            // Button is disabled
            g.setColor(UIManager.getColor(PRIMARY_ULTRA_DARK_COLOR_KEY));
        }

        g.fill(buttonShape);
    }

    public static void paintButtonBorderHighlight(Graphics2D g, AbstractButton button, Shape buttonShape) {
        boolean defaultOrPressedButton = button.isSelected() || (button instanceof JButton && ((JButton) button).isDefaultButton());

        g.setStroke(new BasicStroke(0.25f));

        if (button.hasFocus()) {
            g.setColor(UIManager.getColor(ACCENT_HIGHLIGHT_COLOR_KEY));
            g.draw(buttonShape);
        } else {
            if (defaultOrPressedButton) {
                g.setColor(UIManager.getColor(ACCENT_MEDIUM_COLOR_KEY));
                g.draw(buttonShape);
            } else {
                g.setColor(UIManager.getColor(PRIMARY_MEDIUM_COLOR_KEY));
                g.draw(buttonShape);
            }
        }
    }

    public static void paintButtonShadowOrGlow(Graphics2D g, AbstractButton button, Shape buttonShape) {
        if (button.hasFocus()) {
            g.setColor(Colors.transparentColor(UIManager.getColor(ACCENT_HIGHLIGHT_COLOR_KEY), 0.5f));
            GraphicsHelpers.drawBorderShadow(g, buttonShape, 3);
        } else {
            g.setColor(Colors.transparentColor(UIManager.getColor(SHADOW_COLOR_KEY), 0.25f));
            GraphicsHelpers.drawBorderShadow(g, buttonShape, 3);
        }
    }
}
