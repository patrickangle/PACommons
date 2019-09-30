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

import java.awt.SystemColor;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.PanelUI;
import javax.swing.plaf.RootPaneUI;

/**
 *
 * @author patrickangle
 */
public class AquaUtils {

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

    public static final String WINDOW_CLOSEABLE = "Window.closeable";
    public static final String WINDOW_MINIMIZABLE = "Window.minimizable";
    public static final String WINDOW_ZOOMABLE = "Window.zoomable";
    public static final String WINDOW_HIDES_ON_DEACTIVATE = "Window.hidesOnDeactivate";

    public static final String WINDOW_DOC_MODAL_SHEET = "apple.awt.documentModalSheet";
    public static final String WINDOW_FADE_DELEGATE = "apple.awt._windowFadeDelegate";
    public static final String WINDOW_FADE_IN = "apple.awt._windowFadeIn";
    public static final String WINDOW_FADE_OUT = "apple.awt._windowFadeOut";
    public static final String WINDOW_FULLSCREENABLE = "apple.awt.fullscreenable";
    public static final String WINDOW_FULL_CONTENT = "apple.awt.fullWindowContent";
    public static final String WINDOW_TRANSPARENT_TITLE_BAR = "apple.awt.transparentTitleBar";
    
    public static void installAquaRootPaneUiIfAvailable(JRootPane rootPane) {
        try {
            Class aquaRootPaneUiClass = AquaUtils.class.getClassLoader().loadClass("com.apple.laf.AquaRootPaneUI");
            rootPane.getRootPane().setUI((RootPaneUI) aquaRootPaneUiClass.getConstructor().newInstance());
            rootPane.setBackground(new ColorUIResource(SystemColor.window));
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        }
    }
    
    public static void installAquaPaneUiIfAvailable(JPanel panel) {
        try {
            Class aquaPanelUiClass = AquaUtils.class.getClassLoader().loadClass("com.apple.laf.AquaPanelUI");
            panel.setUI((PanelUI) aquaPanelUiClass.getConstructor().newInstance());
            panel.setBackground(new ColorUIResource(SystemColor.window));
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        }
    }
    
    public static boolean isWindowNativeFullScreen(Window window) {
        // TODO: Access this reflectively.
//        final Object peer = AWTAccessor.getComponentAccessor().getPeer(window);
//        if (!(peer instanceof LWWindowPeer)) return false;
//        Object platformWindow = ((LWWindowPeer) peer).getPlatformWindow();
//        if (!(platformWindow instanceof CPlatformWindow)) return false;
//        return ((CPlatformWindow)platformWindow).isFullScreenMode();
    return false;
    }
    
    public static void setWindowNativeFullScreen(Window window, boolean fullscreen) {
        // TODO: Access this reflectively.
//        final Object peer = AWTAccessor.getComponentAccessor().getPeer(window);
//        if (!(peer instanceof LWWindowPeer)) return;
//        Object platformWindow = ((LWWindowPeer) peer).getPlatformWindow();
//        if (!(platformWindow instanceof CPlatformWindow)) return;
//        if (fullscreen) {
//            ((CPlatformWindow) platformWindow).enterFullScreenMode();
//        } else {
//            ((CPlatformWindow) platformWindow).exitFullScreenMode();
//        }
    }
}
