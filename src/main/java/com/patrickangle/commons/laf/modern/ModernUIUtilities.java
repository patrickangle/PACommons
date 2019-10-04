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

import com.patrickangle.commons.laf.modern.keybindings.GTKKeyBindings;
import com.patrickangle.commons.laf.modern.keybindings.MacKeyBindings;
import com.patrickangle.commons.laf.modern.keybindings.WindowsKeyBindings;
import com.patrickangle.commons.laf.modern.ui.MacTearawayDialogUI;
import com.patrickangle.commons.laf.modern.ui.ModernButtonUI;
import com.patrickangle.commons.laf.modern.ui.ModernCheckBoxUI;
import com.patrickangle.commons.laf.modern.ui.ModernComboBoxUI;
import com.patrickangle.commons.laf.modern.ui.ModernListUI;
import com.patrickangle.commons.laf.modern.ui.ModernOptionPaneUI;
import com.patrickangle.commons.laf.modern.ui.ModernPanelUI;
import com.patrickangle.commons.laf.modern.ui.ModernRadioButtonUI;
import com.patrickangle.commons.laf.modern.ui.ModernScrollBarUI;
import com.patrickangle.commons.laf.modern.ui.ModernSpinnerUI;
import com.patrickangle.commons.laf.modern.ui.ModernSplitPaneUI;
import com.patrickangle.commons.laf.modern.ui.ModernTabbedPaneUI;
import com.patrickangle.commons.laf.modern.ui.ModernTextFieldUI;
import com.patrickangle.commons.laf.modern.ui.ModernToolBarUI;
import com.patrickangle.commons.util.Images;
import com.patrickangle.commons.util.ObjCRuntime;
import com.patrickangle.commons.util.OperatingSystems;
import com.sun.jna.Pointer;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Field;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
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
    
    

    public static void installLafDefaults(UIDefaults defaults) {
        installInputMapDefaults(defaults);

        defaults.put(ACCENT_HIGHLIGHT_COLOR_KEY, ModernUIColors.accentHighlightColor);
        defaults.put(ACCENT_LIGHT_COLOR_KEY, ModernUIColors.accentLightColor);
        defaults.put(ACCENT_MEDIUM_COLOR_KEY, ModernUIColors.accentMediumColor);
        defaults.put(ACCENT_DARK_COLOR_KEY, ModernUIColors.accentDarkColor);

        defaults.put(PRIMARY_LIGHT_COLOR_KEY, ModernUIColors.primaryLightColor);
        defaults.put(PRIMARY_MEDIUM_LIGHT_COLOR_KEY, ModernUIColors.primaryMediumLightColor);
        defaults.put(PRIMARY_MEDIUM_COLOR_KEY, ModernUIColors.primaryMediumColor);
        defaults.put(PRIMARY_MEDIUM_DARK_COLOR_KEY, ModernUIColors.primaryMediumDarkColor);
        defaults.put(PRIMARY_DARK_COLOR_KEY, ModernUIColors.primaryDarkColor);
        defaults.put(PRIMARY_ULTRA_DARK_COLOR_KEY, ModernUIColors.primaryUltraDarkColor);
        defaults.put(BACKGROUND_COLOR_KEY, ModernUIColors.backgroundColor);

        defaults.put(WORKSPACE_BACKGROUND_COLOR_KEY, ModernUIColors.workspaceBackgroundColor);

        defaults.put(SHADOW_COLOR_KEY, Color.BLACK);

        defaults.put("ToolTip.background", ModernUIColors.tooltipColor);
        defaults.put("ToolTip.foreground", Color.BLACK);

        defaults.put("text", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("textText", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("infoText", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("Label.foreground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("OptionPane.messageForeground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("link.forground", defaults.getColor(ACCENT_LIGHT_COLOR_KEY));

        defaults.put("Viewport.background", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("ScrollPane.background", defaults.getColor(WORKSPACE_BACKGROUND_COLOR_KEY));

        defaults.put("window", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("control", defaults.getColor(BACKGROUND_COLOR_KEY));

        Font systemFont = new Font(Font.DIALOG, Font.PLAIN, 14);

        defaults.put("defaultFont", systemFont);

        ModernPanelUI.installIntoDefaults(defaults);
        ModernButtonUI.installIntoDefaults(defaults);
        ModernTextFieldUI.installIntoDefaults(defaults);
        ModernComboBoxUI.installIntoDefaults(defaults);
        ModernListUI.installIntoDefaults(defaults);
        ModernScrollBarUI.installIntoDefaults(defaults);
        ModernOptionPaneUI.installIntoDefaults(defaults);
        ModernCheckBoxUI.installIntoDefaults(defaults);
        ModernRadioButtonUI.installIntoDefaults(defaults);
        ModernSpinnerUI.installIntoDefaults(defaults);
        ModernTabbedPaneUI.installIntoDefaults(defaults);
        ModernToolBarUI.installIntoDefaults(defaults);
//        ModernRootPaneUI.installIntoDefaults(defaults);
        ModernSplitPaneUI.installIntoDefaults(defaults);

        defaults.put("OptionPane.informationIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneInfo.png"))));
        defaults.put("OptionPane.questionIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneQuestion.png"))));
        defaults.put("OptionPane.warningIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneWarning.png"))));
        defaults.put("OptionPane.errorIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneError.png"))));
        defaults.put("Viewport.background", defaults.getColor(WORKSPACE_BACKGROUND_COLOR_KEY));

        defaults.put("ScrollPane.background", defaults.get(ModernUIUtilities.WORKSPACE_BACKGROUND_COLOR_KEY));
        defaults.put("Table.selectionBackground", defaults.get(ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY));
        defaults.put("Table.background", defaults.get(ModernUIUtilities.WORKSPACE_BACKGROUND_COLOR_KEY));
        defaults.put("Table.forground", defaults.get(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));

        defaults.put("TextArea.foreground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));

    }

    /**
     * Installs defaults, as well as overrides some additional defaults, to be
     * compatible with use on top of the Darcula LAF for the missing UIs not
     * currently implemented here. This method will be deprecated when all UIs
     * are implemented.
     *
     * @param defaults
     */
    public static void installCompatibleDefaults(UIDefaults defaults) {
        if (OperatingSystems.current() == OperatingSystems.Macintosh) {
            // Force dark mode all day every day.
//            Pointer nsAppClass = ObjCRuntime.objc_lookUpClass("NSApplication");
//            Pointer sharedApplication = ObjCRuntime.class_getProperty(nsAppClass, "sharedApplication");
//            Pointer nsAppAppearanceProperty = ObjCRuntime.class_getProperty(nsAppClass, "appearance");
//            ObjCRuntime.objc_msgSend(nsAppClass, nsAppClass, arguments)
        }
        
        installInputMapDefaults(defaults);

        defaults.put(ACCENT_HIGHLIGHT_COLOR_KEY, ModernUIColors.accentHighlightColor);
        defaults.put(ACCENT_LIGHT_COLOR_KEY, ModernUIColors.accentLightColor);
        defaults.put(ACCENT_MEDIUM_COLOR_KEY, ModernUIColors.accentMediumColor);
        defaults.put(ACCENT_DARK_COLOR_KEY, ModernUIColors.accentDarkColor);

        defaults.put(PRIMARY_LIGHT_COLOR_KEY, ModernUIColors.primaryLightColor);
        defaults.put(PRIMARY_MEDIUM_LIGHT_COLOR_KEY, ModernUIColors.primaryMediumLightColor);
        defaults.put(PRIMARY_MEDIUM_COLOR_KEY, ModernUIColors.primaryMediumColor);
        defaults.put(PRIMARY_MEDIUM_DARK_COLOR_KEY, ModernUIColors.primaryMediumDarkColor);
        defaults.put(PRIMARY_DARK_COLOR_KEY, ModernUIColors.primaryDarkColor);
        defaults.put(PRIMARY_ULTRA_DARK_COLOR_KEY, ModernUIColors.primaryUltraDarkColor);
        defaults.put(BACKGROUND_COLOR_KEY, ModernUIColors.backgroundColor);

        defaults.put(WORKSPACE_BACKGROUND_COLOR_KEY, ModernUIColors.workspaceBackgroundColor);

        defaults.put(SHADOW_COLOR_KEY, Color.BLACK);

        defaults.put("ToolTip.background", ModernUIColors.tooltipColor);
        defaults.put("ToolTip.foreground", Color.BLACK);

        defaults.put("text", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("textText", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("infoText", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("Label.foreground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("link.forground", defaults.getColor(ACCENT_LIGHT_COLOR_KEY));

        defaults.put("Panel.background", defaults.getColor(BACKGROUND_COLOR_KEY));

        defaults.put("window", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("windowBorder", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("windowText", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));

        defaults.put("menu", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("Menu.background", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("MenuBar.background", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("Menu.foreground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("MenuItem.background", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("CheckBoxMenuItem.background", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("RadioButtonMenuItem.background", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("PopupMenu.background", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("Seperator.background", defaults.getColor(BACKGROUND_COLOR_KEY));

        defaults.put("TextField.background", defaults.getColor(PRIMARY_DARK_COLOR_KEY));

        defaults.put("Viewport.background", defaults.getColor(WORKSPACE_BACKGROUND_COLOR_KEY));
        defaults.put("ScrollPane.background", defaults.get(ModernUIUtilities.WORKSPACE_BACKGROUND_COLOR_KEY));

        defaults.put("TabbedPane.background", defaults.getColor(BACKGROUND_COLOR_KEY));

        defaults.put("ScrollPane.background", defaults.get(ModernUIUtilities.WORKSPACE_BACKGROUND_COLOR_KEY));
        defaults.put("Table.selectionBackground", defaults.get(ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY));
        defaults.put("Table.background", defaults.get(ModernUIUtilities.WORKSPACE_BACKGROUND_COLOR_KEY));
        defaults.put("Table.forground", defaults.get(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("ToolBar.background", defaults.get(ModernUIUtilities.BACKGROUND_COLOR_KEY));

        Font systemFont = new Font(Font.DIALOG, Font.PLAIN, 14);

        defaults.put("defaultFont", systemFont);

//        ModernButtonUI.installIntoDefaults(defaults);
//        ModernTextFieldUI.installIntoDefaults(defaults);
//        ModernComboBoxUI.installIntoDefaults(defaults);
//        ModernListUI.installIntoDefaults(defaults);
//        ModernScrollBarUI.installIntoDefaults(defaults);
//        ModernOptionPaneUI.installIntoDefaults(defaults);
//
//        ModernCheckBoxUI.installIntoDefaults(defaults);
//        ModernRadioButtonUI.installIntoDefaults(defaults);
//
//        ModernSpinnerUI.installIntoDefaults(defaults);
//        ModernTabbedPaneUI.installIntoDefaults(defaults);
//        ModernPanelUI.installIntoDefaults(defaults);
        ModernButtonUI.installIntoDefaults(defaults);
        ModernTextFieldUI.installIntoDefaults(defaults);
        ModernComboBoxUI.installIntoDefaults(defaults);
        ModernListUI.installIntoDefaults(defaults);
        ModernScrollBarUI.installIntoDefaults(defaults);
        ModernOptionPaneUI.installIntoDefaults(defaults);
        ModernCheckBoxUI.installIntoDefaults(defaults);
        ModernRadioButtonUI.installIntoDefaults(defaults);
        ModernSpinnerUI.installIntoDefaults(defaults);
        ModernTabbedPaneUI.installIntoDefaults(defaults);
        ModernToolBarUI.installIntoDefaults(defaults);
//        ModernRootPaneUI.installIntoDefaults(defaults);
        ModernSplitPaneUI.installIntoDefaults(defaults);

        if (OperatingSystems.current() == OperatingSystems.Macintosh) {
//            defaults.put("RootPaneUI", "com.apple.laf.AquaRootPaneUI");
            defaults.put("TearawayDialogUI", MacTearawayDialogUI.class.getName());
        }

    }

    /**
     * Install input map defaults for all components based on the current
     * Operating System. Windows and Macintosh have specific input map sets, and
     * all other operating systems use the GTK input map.
     *
     * @param defaults
     */
    private static void installInputMapDefaults(UIDefaults defaults) {
        switch (OperatingSystems.current()) {
            case Windows:
                WindowsKeyBindings.installKeybindings(defaults);
                break;
            case Macintosh:
                MacKeyBindings.installKeybindings(defaults);
                break;
            default:
                GTKKeyBindings.installKeybindings(defaults);
                break;
        }
    }

    public static Object clientPropertyOrDefault(JComponent c, String key, Object def) {
        Object returnObj = c.getClientProperty(key);
        if (returnObj != null) {
            return returnObj;
        } else {
            return def;
        }
    }

    public static double getDisplayScale() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice device = env.getDefaultScreenDevice();
        try {
            Field field = device.getClass().getDeclaredField("scale");
            if (field != null) {
                field.setAccessible(true);
                Object scale = field.get(device);
                if (scale instanceof Number && ((Number) scale).doubleValue() > 0) {
                    return ((Number) scale).doubleValue();
                } else {
                    System.out.println("Scale is: " + scale);
                }
            }
        } catch (Exception ignore) {
        }
        return 1.0;
    }

    public static double getDisplayScaleInverse() {
        return 1.0 / getDisplayScale();
    }

    public static AffineTransform getDisplayScaleTransform() {
        return AffineTransform.getScaleInstance(getDisplayScale(), getDisplayScale());
    }

    public static AffineTransform getDisplayScaleInverseTransform() {
        return AffineTransform.getScaleInstance(getDisplayScaleInverse(), getDisplayScaleInverse());
    }
}
