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

import ca.weblite.objc.Client;
import ca.weblite.objc.Proxy;
import ca.weblite.objc.RuntimeUtils;
import com.sun.jna.Pointer;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.PanelUI;
import javax.swing.plaf.RootPaneUI;

/**
 *
 * @author patrickangle
 */
public class AquaUtils {

    /**
     * An Appearance manages how AppKit renders your app's UI elements.
     * Specifically, appearance objects determine which colors and images AppKit
     * uses when drawing windows, views, and controls. Although you can use an
     * appearance object to determine how to draw custom views and controls, a
     * better approach is to choose colors and images that adapt automatically
     * to the current appearance. For example, define a color asset whose actual
     * color value changes for light and dark appearances.
     *
     * The user chooses the default appearance for the system, but you can
     * override that appearance for all or part of your app. Apps inherit the
     * default system appearance, windows inherit their app's appearance, and
     * views inherit the appearance of their nearest ancestor (either a
     * superview or window). To force a window or view to adopt an appearance,
     * assign a specific appearance object to its appearance property.
     *
     * When AppKit draws a control, it automatically sets the current appearance
     * on the current thread to the control’s appearance. The current appearance
     * influences the drawing path and return values you get when you access
     * system fonts and colors. The current appearance also affects the
     * appearance of text and images, such as the text and template images in a
     * toolbar.
     */
    public enum Appearance {
        /**
         * The standard light system appearance.
         */
        Aqua("NSAppearanceNameAqua", false),
        /**
         * The standard dark system appearance.
         */
        DarkAqua("NSAppearanceNameDarkAqua", true),
        /**
         * The light vibrant appearance, available only in visual effect views.
         */
        VibrantLight("NSAppearanceNameVibrantLight", false),
        /**
         * A dark vibrant appearance, available only in visual effect views.
         */
        VibrantDark("NSAppearanceNameVibrantDark", true),
        /**
         * A high-contrast version of the standard light system appearance.
         */
        AccessibilityHighContrastAqua("NSAppearanceNameAccessibilityHighContrastAqua", false),
        /**
         * A high-contrast version of the standard dark system appearance.
         */
        AccessibilityHighContrastDarkAqua("NSAppearanceNameAccessibilityHighContrastDarkAqua", true),
        /**
         * A high-contrast version of the light vibrant appearance.
         */
        AccessibilityHighContrastVibrantLight("NSAppearanceNameAccessibilityHighContrastVibrantLight", false),
        /**
         * A high-contrast version of the dark vibrant appearance.
         */
        AccessibilityHighContrastVibrantDark("NSAppearanceNameAccessibilityHighContrastVibrantDark", true),
        /**
         * An unknown appearance, possibly the result of querying appearance on
         * a platform other than macOS.
         */
        Unknown("NSAppearanceNameAqua", false);

        private final String nsAppearanceName;
        private final boolean dark;

        Appearance(String nsAppearanceName, boolean dark) {
            this.nsAppearanceName = nsAppearanceName;
            this.dark = dark;
        }

        public String getNsAppearanceName() {
            return nsAppearanceName;
        }

        public boolean isDark() {
            return dark;
        }

        public static Appearance forNsAppearanceName(String nsAppearanceName) {
            switch (nsAppearanceName) {
                case "NSAppearanceNameAqua":
                    return Aqua;
                case "NSAppearanceNameDarkAqua":
                    return DarkAqua;
                case "NSAppearanceNameVibrantLight":
                    return VibrantLight;
                case "NSAppearanceNameVibrantDark":
                    return VibrantDark;
                case "NSAppearanceNameAccessibilityHighContrastAqua":
                    return AccessibilityHighContrastAqua;
                case "NSAppearanceNameAccessibilityHighContrastDarkAqua":
                    return AccessibilityHighContrastDarkAqua;
                case "NSAppearanceNameAccessibilityHighContrastVibrantLight":
                    return AccessibilityHighContrastVibrantLight;
                case "NSAppearanceNameAccessibilityHighContrastVibrantDark":
                    return AccessibilityHighContrastVibrantDark;
                default:
                    return Unknown;
            }
        }
    }

    /**
     * Constants for user interactions that change the appearance of a view or
     * control. Instead of defining separate colors for user interactions with a
     * view, use this method to retrieve the appropriate color for use with
     * those interactions. This method blends the current color with an
     * appropriate modifier and returns the results. For example, specifying
     * NSColorSystemEffectPressed for the systemEffect parameter yields the
     * color to use when you want your view to appear as if it had been pressed.
     * This method takes the current appearance into account, returning an
     * appropriately modified color for both light and dark appearances.
     */
    public enum ColorSystemEffect {
        None("NSColorSystemEffectNone"),
        Pressed("NSColorSystemEffectPressed"),
        DeepPressed("NSColorSystemEffectDeepPressed"),
        Rollover("NSColorSystemEffectRollover"),
        Disabled("NSColorSystemEffectDisabled");

        private final String nsColorSystemEffectName;

        ColorSystemEffect(String nsColorSystemEffectName) {
            this.nsColorSystemEffectName = nsColorSystemEffectName;
        }

        public String getNsColorSystemEffectName() {
            return nsColorSystemEffectName;
        }

        public static ColorSystemEffect forNsColorSystemEffectName(String nsColorSystemEffectName) {
            switch (nsColorSystemEffectName) {
                case "NSColorSystemEffectNone":
                    return None;
                case "NSColorSystemEffectPressed":
                    return Pressed;
                case "NSColorSystemEffectDeepPressed":
                    return DeepPressed;
                case "NSColorSystemEffectRollover":
                    return Rollover;
                case "NSColorSystemEffectDisabled":
                    return Disabled;
                default:
                    return None;
            }
        }
    }

    public enum VisualEffectMaterial {
        Titlebar(0),
        Selection(0),
        Menu(0),
        Popover(0),
        Sidebar(0),
        HeaderView(0),
        Sheet(0),
        WindowBackground(0),
        HUDWindow(0),
        FullScreenUI(0),
        ToolTip(0),
        ContentBackground(0),
        UnderWindowBackground(0),
        UnderPageBackground(0);

        VisualEffectMaterial(int nsVisualEffectMaterialValue) {

        }
    }

//    NSVisualEffectMaterialTitlebar
//The material for a window’s titlebar.
//NSVisualEffectMaterialSelection
//The material used to indicate a selection.
//NSVisualEffectMaterialMenu
//The material for menus.
//NSVisualEffectMaterialPopover
//The material for the background of popover windows.
//NSVisualEffectMaterialSidebar
//The material for the background of window sidebars.
//NSVisualEffectMaterialHeaderView
//The material for in-line header or footer views.
//NSVisualEffectMaterialSheet
//The material for the background of sheet windows.
//NSVisualEffectMaterialWindowBackground
//The material for the background of opaque windows.
//NSVisualEffectMaterialHUDWindow
//The material for the background of heads-up display (HUD) windows.
//NSVisualEffectMaterialFullScreenUI
//The material for the background of a full-screen modal interface.
//NSVisualEffectMaterialToolTip
//The material for the background of a tool tip.
//NSVisualEffectMaterialContentBackground
//The material for the background of opaque content.
//NSVisualEffectMaterialUnderWindowBackground
//The material for under a window's background.
//NSVisualEffectMaterialUnderPageBackground
//The material for the area behind the pages of a document.
    /**
     * This client property determines if the window should use the Brushed
     * Metal texture. This client property overrides the corresponding system
     * property, which determines its default value.<br/>
     * <br/>
     * <b>WARNING:</b> This property must be set before the heavyweight peer for
     * the Window is created. Once addNotify() has been called on the component,
     * causing creation of the heavyweight peer, changing this property has no
     * effect.<br/>
     * <br/>
     * Type: <code>Boolean</code><br/>
     * Allowed Values: <code>Boolean.TRUE</code>,
     * <code>Boolean.FALSE</code><br/>
     * Default Value: <code>null</code><br/>
     * Applies to: <code>JRootPane</code><br/>
     */
    public static final String WINDOW_BRUSH_METAL_LOOK = "apple.awt.brushMetalLook";

    /**
     * Setting this client property to Boolean.TRUE allows dragging of the
     * Window via mouse-down on any part of the window that is not a heavyweight
     * control. This client property overrides the system property.<br/>
     * <br/>
     * <b>WARNING:</b> This property must be set before the heavyweight peer for
     * the Window is created. Once addNotify() has been called on the component,
     * causing creation of the heavyweight peer, changing this property has no
     * effect.<br/>
     * <br/>
     * Type: <code>Boolean</code><br/>
     * Allowed Values: <code>Boolean.TRUE</code>,
     * <code>Boolean.FALSE</code><br/>
     * Default Value: <code>Boolean.FALSE</code><br/>
     * Applies to: <code>JRootPane</code><br/>
     */
    public static final String WINDOW_DRAGGABLE_BACKGROUND = "apple.awt.draggableWindowBackground";

    /**
     * This client property sets the opacity for the whole window and can be
     * changed throughout the lifetime of the window.<br/>
     * <br/>
     * <b>WARNING:</b> This property only applies to JRootPanes that have a
     * heavyweight peer.<br/>
     * <br/>
     * Type: <code>Float</code><br/>
     * Allowed Values: <code>0.0f</code> to <code>1.0f</code> inclusive<br/>
     * Default Value: <code>1.0f</code><br/>
     * Applies to: <code>JRootPane</code><br/>
     */
    public static final String WINDOW_ALPHA = "Window.alpha";

    /**
     * This client property determines if the window has a shadow.<br/>
     * <br/>
     * <b>WARNING:</b> This property must be set before the heavyweight peer for
     * the Window is created. Once addNotify() has been called on the component,
     * causing creation of the heavyweight peer, changing this property has no
     * effect.<br/>
     * <br/>
     * Type: <code>Boolean</code><br/>
     * Allowed Values: <code>Boolean.TRUE</code>,
     * <code>Boolean.FALSE</code><br/>
     * Default Value: <code>Boolean.FALSE</code><br/>
     * Applies to: <code>JRootPane</code><br/>
     */
    public static final String WINDOW_SHADOW = "Window.shadow";

    /**
     * This client property determines if the window has a Utility-style title
     * bar. In order to make this window style also float above all others you
     * must additionally call setAlwaysOnTop(true). Windows that have both the
     * "small" style and are set to always be on top will automatically hide
     * themselves when your application is no longer frontmost. This is similar
     * to how native applications behave.<br/>
     * <br/>
     * Additionally, there are several undocumented values permitted in the
     * latest versions of AquaLookAndFeel, including "textured", "unified", and
     * "hud".<br/>
     * <br/>
     * Instead of hard-coding string values for this key, you should always use
     * the provided <code>WINDOW_STYLE_VALUE_</code> prefixed constants provided
     * by this class.<br/>
     * <br/>
     * <b>WARNING:</b> This property must be set before the heavyweight peer for
     * the Window is created. Once addNotify() has been called on the component,
     * causing creation of the heavyweight peer, changing this property has no
     * effect.<br/>
     * <br/>
     * Type: <code>String</code><br/>
     * Allowed Values: <code>WINDOW_STYLE_VALUE_SMALL</code>,
     * <code>WINDOW_STYLE_VALUE_TEXTURED</code>,
     * <code>WINDOW_STYLE_VALUE_UNIFIED</code>,
     * <code>WINDOW_STYLE_VALUE_HUD</code><br/>
     * Default Value: <code>null</code><br/>
     * Applies to: <code>JRootPane</code><br/>
     */
    public static final String WINDOW_STYLE = "Window.style";

    /**
     * A value for the <code>WINDOW_STYLE</code> client property that decorates
     * the window with a Utility-style title bar. In order to make this window
     * style also float above all others you must additionally call
     * setAlwaysOnTop(true). Windows that have both the "small" style and are
     * set to always be on top will automatically hide themselves when your
     * application is no longer frontmost. This is similar to how native
     * applications behave.
     */
    public static final String WINDOW_STYLE_VALUE_SMALL = "small";

    /**
     * A value for the <code>WINDOW_STYLE</code> client property that is
     * equivalent to setting the <code>WINDOW_BRUSH_METAL_LOOK</code> client
     * property to <code>Boolean.TRUE</code>.
     */
    public static final String WINDOW_STYLE_VALUE_TEXTURED = "textured";

    /**
     * A value for the <code>WINDOW_STYLE</code> client property that is
     * equivalent to setting the <code>WINDOW_BRUSH_METAL_LOOK</code> client
     * property to <code>Boolean.TRUE</code>.
     */
    public static final String WINDOW_STYLE_VALUE_UNIFIED = "unified";

    /**
     * A value for the <code>WINDOW_STYLE</code> client property that styles
     * decorates the window as a HUD-style window. By itself, this value only
     * effects the title bar of the window, but when the the
     * <code>WINDOW_BRUSH_METAL_LOOK</code> client property is set to
     * <code>Boolean.TRUE</code> the entire window is rendered in HUD style
     * (assuming the Content Pane and children of the Window are opaque).
     */
    public static final String WINDOW_STYLE_VALUE_HUD = "hud";

    /**
     * TODO: Document this field. NOTE: This property does not work properly.
     */
    public static final String WINDOW_DOC_MODAL_SHEET = "apple.awt.documentModalSheet";

    /**
     * When this client property is set to <code>Boolean.TRUE</code>, the
     * window’s contentView consumes the full size of the window. Although you
     * can combine this constant with other window style masks, it is respected
     * only for windows with a title bar. Note that using this mask opts in to
     * layer-backing.<br/>
     * <br/>
     * <b>WARNING:</b> This property must be set before the heavyweight peer for
     * the Window is created. Once addNotify() has been called on the component,
     * causing creation of the heavyweight peer, changing this property has no
     * effect.<br/>
     * <br/>
     * Type: <code>Boolean</code><br/>
     * Allowed Values: <code>Boolean.TRUE</code>,
     * <code>Boolean.FALSE</code><br/>
     * Default Value: <code>null</code><br/>
     * Applies to: <code>JRootPane</code><br/>
     */
    public static final String WINDOW_FULL_CONTENT = "apple.awt.fullWindowContent";

    /**
     * A client property that indicates whether the title bar draws its
     * background. When the value of this property is <code>Boolean.TRUE</code>,
     * the title bar does not draw its background, which allows all content
     * underneath it to show through.<br/>
     * <br/>
     * <b>WARNING:</b> This property must be set before the heavyweight peer for
     * the Window is created. Once addNotify() has been called on the component,
     * causing creation of the heavyweight peer, changing this property has no
     * effect.<br/>
     * <br/>
     * Type: <code>Boolean</code><br/>
     * Allowed Values: <code>Boolean.TRUE</code>,
     * <code>Boolean.FALSE</code><br/>
     * Default Value: <code>null</code><br/>
     * Applies to: <code>JRootPane</code><br/>
     */
    public static final String WINDOW_TRANSPARENT_TITLE_BAR = "apple.awt.transparentTitleBar";

    /**
     * Changing the value of this client property causes the window's shadow to
     * be recomputed based on the current window shape. The values used are
     * inconsequential, as long as the new value is different from the current
     * value. This property uses equals() to compare the equality of the two
     * objects involved.<br/>
     * <br/>
     * <b>WARNING:</b> This property only applies to JRootPanes that have a
     * heavyweight peer.<br/>
     * <br/>
     * <b>IMPORTANT:</b> This property requires the <code>Window.shadow</code>
     * property to be explicitly set to <code>Boolean.TRUE</code><br/>
     * <br/>
     * Type: <code>Object</code><br/>
     * Allowed Values: Any<br/>
     * Default Value: <code>null</code><br/>
     * Applies to: <code>JRootPane</code><br/>
     */
    public static final String WINDOW_SHADOW_REVALIDATE_NOW = "apple.awt.windowShadow.revalidateNow";

    /**
     * This client property adds the document dirty mark in the close button of
     * the window. This is used to indicate that the document window has unsaved
     * content, and attempts to close the window will request that the user save
     * the document or discard changes.<br/>
     * <br/>
     * Type: <code>Boolean</code><br/>
     * Allowed Values: <code>Boolean.TRUE</code>,
     * <code>Boolean.FALSE</code><br/>
     * Default Value: <code>Boolean.FALSE</code><br/>
     * Applies to: <code>JRootPane</code>, <code>JInternalFrame</code><br/>
     */
    public static final String WINDOW_DOCUMENT_MODIFIED = "Window.documentModified";

    /**
     * This client property adds a document proxy icon to the title bar of the
     * window. This icon is the effective representation of the document that
     * can be dragged and dropped into the Finder or the Dock. Command-clicking
     * on the title will present the full path to the document in a popup. The
     * proxy icon in the title bar will have the icon of document as presented
     * by LaunchServices in the Finder or Open and Save dialog boxes. This
     * property can be changed at anytime throughout the lifetime of the window.
     * Using this property from an untrusted applet or WebStart application will
     * result in a security exception as these types of applications do not have
     * access to the file system.<br/>
     * <br/>
     * <b>WARNING:</b> This property only applies to JRootPanes that have a
     * heavyweight peer.<br/>
     * <br/>
     * Type: <code>java.io.File</code><br/>
     * Allowed Values: Any<br/>
     * Default Value: <code>null</code><br/>
     * Applies to: <code>JRootPane</code><br/>
     */
    public static final String WINDOW_DOCUMENT_FILE = "Window.documentFile";

    /**
     * TODO: Document this field.
     */
    public static final String WINDOW_CLOSEABLE = "Window.closeable";

    /**
     * TODO: Document this field.
     */
    public static final String WINDOW_MINIMIZABLE = "Window.minimizable";

    /**
     * TODO: Document this field.
     */
    public static final String WINDOW_ZOOMABLE = "Window.zoomable";

    /**
     * TODO: Document this field.
     */
    public static final String WINDOW_FULLSCREENABLE = "apple.awt.fullscreenable";

    /**
     * TODO: Document this field.
     */
    public static final String WINDOW_HIDES_ON_DEACTIVATE = "Window.hidesOnDeactivate";

    // These properties are defined by CPlatformWindow, but should be considered private. They are redefined here for completeness.
    /**
     * TODO: Document this field.
     */
    private static final String WINDOW_FADE_DELEGATE = "apple.awt._windowFadeDelegate";

    /**
     * TODO: Document this field.
     */
    private static final String WINDOW_FADE_IN = "apple.awt._windowFadeIn";

    /**
     * TODO: Document this field.
     */
    private static final String WINDOW_FADE_OUT = "apple.awt._windowFadeOut";
    
    public static final String APPLICATION_USE_SCREEN_MENU_BAR = "apple.laf.useScreenMenuBar";

    /**
     * The magic color on macOS that allows a component to paint through to the
     * native window background, which is generally either a gradient texture or
     * a translucent effect depending on the configuration of the JRootPane of
     * the Window containing the component.
     *
     * On platforms other than macOS, this color will be the SystemColor.window
     * color, wrapped as a ColorUIResource.
     */
    public static final Color MAGIC_ERASER_COLOR = new ColorUIResource(SystemColor.window);

    /**
     * The system font, San Francisco, in its standard weight, at a size of 1
     * point. You should use .deriveFont(…) to construct a font of the desired
     * size and style.
     *
     * On platforms other than macOS (or on older versions of macOS), this font
     * will be the font returned by the name Font.Dialog.
     */
    public static final Font SYSTEM_FONT = createSystemFont();

    /**
     * The system monospaced font, San Francisco Mono, in its standard weight,
     * at a size of 1 point. You should use .deriveFont(…) to construct a font
     * of the desired size and style.
     *
     * On platforms other than macOS (or on older versions of macOS), this font
     * will be the font returned by the name Font.MONOSPACED.
     */
    public static final Font SYSTEM_MONO_FONT = createSystemMonospaceFont();

    /**
     * The system rounded font, San Francisco Rounded, in its standard weight,
     * at a size of 1 point. You should use .deriveFont(…) to construct a font
     * of the desired size and style.
     *
     * On platforms other than macOS (or on older versions of macOS), this font
     * will be the font returned by the name Font.Dialog.
     */
    public static final Font SYSTEM_ROUNDED_FONT = createSystemRoundedFont();

    /**
     * The system serif font, New York, in its standard weight, at a size of 1
     * point. You should use .deriveFont(…) to construct a font of the desired
     * size and style.
     *
     * On platforms other than macOS (or on older versions of macOS), this font
     * will be the font returned by the name Font.SERIF.
     */
    public static final Font SYSTEM_SERIF_FONT = createSystemSerifFont();

    /**
     * Installs the AquaRootPaneUI onto the provided JRootPane if the class if
     * available. This is useful if you wish to take advantage of Aqua-specific
     * features while using another Look and Feel.
     *
     * The JRootPane's background color is also set to the MAGIC_ERASER_COLOR if
     * AquaRootPaneUI can be installed.
     *
     * @param rootPane
     */
    public static void installAquaRootPaneUiIfAvailable(JRootPane rootPane) {
        try {
            Class aquaRootPaneUiClass = AquaUtils.class.getClassLoader().loadClass("com.apple.laf.AquaRootPaneUI");
            rootPane.getRootPane().setUI((RootPaneUI) aquaRootPaneUiClass.getConstructor().newInstance());
            rootPane.setBackground(MAGIC_ERASER_COLOR);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        }
    }

    public static void installAquaPaneUiIfAvailable(JPanel panel) {
        try {
            Class aquaPanelUiClass = AquaUtils.class.getClassLoader().loadClass("com.apple.laf.AquaPanelUI");
            panel.setUI((PanelUI) aquaPanelUiClass.getConstructor().newInstance());
            panel.setBackground(MAGIC_ERASER_COLOR);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        }
    }

    /**
     * Set the application's appearance to the desired appearance. If null in
     * provided as the appearance, the appearance is set to the system's
     * default. This has no effect on platforms other than macOS.
     *
     * @param appearance
     */
    public static void setApplicationAppearance(Appearance appearance) {
        if (isMac()) {
            Client c = Client.getInstance();
            c.sendProxy("NSApplication", "sharedApplication").send("setAppearance:", c.send("NSAppearance", "appearanceNamed:", appearance == null ? null : RuntimeUtils.str(appearance.getNsAppearanceName())));
        }
    }

    /**
     * Get the application's current appearance. This may not match the
     * previously set appearance, depending on the accessibility settings
     * configured by the user. If null was provided to to
     * setApplicationAppearance, this returns the system-provided appearance the
     * user has configured.
     *
     * On platforms that are not macOS, this returns Appearance.Unknown.
     *
     * @return
     */
    public static Appearance getApplicationAppearance() {
        if (isMac()) {
            Client c = Client.getInstance();
            Proxy appearanceObject = c.sendProxy("NSApplication", "sharedApplication").sendProxy("appearance");
            Pointer appearanceNameNsString;
            if (appearanceObject != null) {
                appearanceNameNsString = appearanceObject.getProxy("name").getPeer();
            } else {
                // If the appearance was null, defer to the effective appearance to determine the current appearance
                appearanceNameNsString = c.sendProxy("NSApplication", "sharedApplication").sendProxy("effectiveAppearance").getProxy("name").getPeer();
            }

            String appearanceName = RuntimeUtils.str(appearanceNameNsString);
            return Appearance.forNsAppearanceName(appearanceName);
        } else {
            return Appearance.Unknown;
        }
    }

    /**
     * Get the current accent color configured by the user.
     *
     * On platforms that are not macOS, this returns SystemColor.activeCaption.
     *
     * @return
     */
    public static Color getAccentColor() {
        if (isMac()) {
            Client c = Client.getInstance();
            // sRGB is the standard color space in Java, so convert the color to that.
            Proxy colorObject = c.sendProxy("NSColor", "controlAccentColor").sendProxy("colorUsingColorSpace:", c.sendProxy("NSColorSpace", "sRGBColorSpace"));
            return new Color((float) colorObject.sendDouble("redComponent"), (float) colorObject.sendDouble("greenComponent"), (float) colorObject.sendDouble("blueComponent"), (float) colorObject.sendDouble("alphaComponent"));
        } else {
            // There is no direct equivilent for other platforms. Currently, we return SystemColor.activeCaption to return the window titlebar color.
            return SystemColor.activeCaption;
        }
    }

    /**
     * Get the current accent color configured by the user, augmented by the
     * provided ColorSystemEffect.
     *
     * On platforms that are not macOS, this returns SystemColor.activeCaption.
     *
     * @param colorSystemEffect
     * @return
     */
    public static Color getAccentColor(ColorSystemEffect colorSystemEffect) {
        if (isMac()) {
            if (colorSystemEffect == null) {
                colorSystemEffect = ColorSystemEffect.None;
            }
            Client c = Client.getInstance();
            // sRGB is the standard color space in Java, so convert the color to that.
            Proxy colorObject = c.sendProxy("NSColor", "controlAccentColor").sendProxy("colorWithSystemEffect:", colorSystemEffect.getNsColorSystemEffectName()).sendProxy("colorUsingColorSpace:", c.sendProxy("NSColorSpace", "sRGBColorSpace"));
            return new Color((float) colorObject.sendDouble("redComponent"), (float) colorObject.sendDouble("greenComponent"), (float) colorObject.sendDouble("blueComponent"), (float) colorObject.sendDouble("alphaComponent"));
        } else {
            // There is no direct equivilent for other platforms. Currently, we return SystemColor.activeCaption to return the window titlebar color.
            return SystemColor.activeCaption;
        }
    }
    
    public static void tryWindowVisualEffectStyle(Window window) {
//        if (isMac()) {
//            Client c = Client.getInstance();
//            Object peer = AWTAccessor.getComponentAccessor().getPeer(window);
//            System.out.println(peer);
//            if (peer instanceof LWWindowPeer) {
//                try {
//                    Class cPlatformWindowClass = AquaUtils.class.getClassLoader().loadClass("sun.lwawt.macosx.CPlatformWindow");
//                    System.out.println(Arrays.toString(cPlatformWindowClass.getMethods()));
//                    Method getContentViewMethod = cPlatformWindowClass.getMethod("getContentView");
//                    Object platformWindow = getContentViewMethod.invoke(((LWWindowPeer) peer).getPlatformWindow());
//                    
//                    Class cPlatformViewClass = AquaUtils.class.getClassLoader().loadClass("sun.lwawt.macosx.CPlatformView");
//                    Method getAWTViewMethod = cPlatformViewClass.getMethod("getAWTView");
//                    
////                    long pointer = (long) getAWTViewMethod.invoke(platformWindow);
////                    getAWTView returns a long
//                    
////                    long pointer = (long) getNativeViewPtr.invoke(null, ((LWWindowPeer) peer).getPlatformWindow());
////                    System.out.println("window pointer?: " + pointer);
//                    
////                    System.out.println(c.send(new Pointer(pointer), "description"));
//                    
//                    Pointer awtView = new Pointer((long) getAWTViewMethod.invoke(platformWindow));
//                    Proxy awtViewProxy = new Proxy(c, awtView);
//                    System.out.println("Pointer created");
//                    Object bounds = awtViewProxy.get("bounds");
//                    System.out.println(bounds);
//                    System.out.println(bounds.getClass().getName());
//                    Proxy visualEffectView = c.sendProxy("NSVisualEffectView", "initWithFrame:", bounds);
//                    System.out.println("Init done");
//                    visualEffectView.send("setAutoresizingMask:", 2 | 16); // NSViewWidthSizable|NSViewHeightSizable
//                    visualEffectView.send("setMaterial:", 1);
//                    visualEffectView.send("setBlendingMode:", c.send("NSVisualEffectView", "NSVisualEffectBlendingModeBehindWindow"));
//                    System.out.println("init done, time to crash?");
//                    c.send(awtView, "addSubview:positioned:relativeTo:", visualEffectView.getPeer(), -1, null);
//                    
////                    c.send(new Pointer(pointer), "addSubview:", c.sendProxy("NSTextField", "labelWithString:", RuntimeUtils.str("LOLOLOLOLOLOLOLOLOLOLOLOLOLOL")));
//                    
//                    
//                    // NSVisualEffectView *vibrant=[[vibrantClass alloc] initWithFrame:self.bounds];
////        [vibrant setAutoresizingMask:NSViewWidthSizable|NSViewHeightSizable];
////        [vibrant setBlendingMode:mode];
////        [self addSubview:vibrant positioned:NSWindowBelow relativeTo:nil];
//
//                } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//                    Logger.getLogger(AquaUtils.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
////            c.sendProxy(new Pointer(window.), Pointer.NULL, args)
//        }
    }
    
    public static void setShouldUseScreenMenuBar(boolean useMenuBar) {
        System.setProperty("apple.laf.useScreenMenuBar", Boolean.toString(useMenuBar));
        UIManager.getInstalledLookAndFeels(); // Needed to prevent "java.lang.UnsatisfiedLinkError: com.apple.laf.ScreenMenu.addMenuListeners"
    }

    public static boolean isWindowFullScreen(Window window) {
        return false;
    }

    public static void setWindowFullScreen(Window window, boolean fullscreen) {
//final Object peer = AWTAccessor.getComponentAccessor().getPeer(window);
////        if (!(peer instanceof LWWindowPeer)) return;
//        Object platformWindow = ((LWWindowPeer) peer).getPlatformWindow();
//        if (!(platformWindow instanceof CPlatformWindow)) return;
//        if (fullscreen) {
//            ((CPlatformWindow) platformWindow).enterFullScreenMode();
//        } else {
//            ((CPlatformWindow) platformWindow).exitFullScreenMode();
//        }
    }

    /**
     * Returns true if, and only if, the current operating system is macOS.
     *
     * @return
     */
    public static final boolean isMac() {
        return OperatingSystems.current() == OperatingSystems.Macintosh;
    }

    private static Font createSystemFont() {
        if (isMac()) {
            // Retrieve this from the file system does not work properly; we instead end up with a strange variant of the font when doing so.
            Font f = new Font(".SFNS-Regular", 0, 0);
            if (f != null) {
                return f;
            }
        }
        return new Font(Font.DIALOG, Font.PLAIN, 1);
    }

    private static Font createSystemMonospaceFont() {
        if (isMac()) {
            // Retrieving this from the file system does not work properly, as we can only see the Light variant of the font when doing so.
            Font f = new Font(".SFNSMono-Regular", 0, 0);
            if (f != null) {
                return f;
            }
        }
        return new Font(Font.MONOSPACED, Font.PLAIN, 1);
    }

    private static Font createSystemRoundedFont() {
        if (isMac()) {
            // This font is not currently available as a named font, so we explicitly load it from disk.
            try {
                return Font.createFont(Font.TRUETYPE_FONT, new File("/System/Library/Fonts/SFNSRounded.ttf")).deriveFont(Map.of(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR));
            } catch (FontFormatException | IOException ex) {
            }
        }
        return new Font(Font.DIALOG, Font.PLAIN, 1);
    }

    private static Font createSystemSerifFont() {
        if (isMac()) {
            // This font is not currently available as a named font, so we explicitly load it from disk.
            try {
                return Font.createFont(Font.TRUETYPE_FONT, new File("/System/Library/Fonts/NewYork.ttf")).deriveFont(Map.of(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR));
            } catch (FontFormatException | IOException ex) {
            }
        }
        return new Font(Font.SERIF, Font.PLAIN, 1);
    }
}
