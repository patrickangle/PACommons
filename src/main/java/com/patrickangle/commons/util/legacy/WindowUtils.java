/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util.legacy;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Patrick Angle
 */
@Deprecated(forRemoval = true)
public class WindowUtils {
    @Deprecated(forRemoval = true)
    public static void takeFrameFullscreenOnScreen(JFrame frame, int screen) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        if (screen > -1 && screen < gs.length) {
            gs[screen].setFullScreenWindow(frame);
        } else if (gs.length > 0) {
            gs[0].setFullScreenWindow(frame);
        } else {
            throw new RuntimeException("No Screens Found");
        }
    }
@Deprecated(forRemoval = true)
    public static void moveWindowToScreen(int screen, JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if (screen > -1 && screen < gd.length) {
            frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, frame.getY());
        } else if (gd.length > 0) {
            frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
        } else {
            throw new RuntimeException("Invalid Screen " + screen);
        }
    }
@Deprecated(forRemoval = true)
    public static final void setFrameIcon(JFrame frame, String iconClasspath) {
        URL url = ClassLoader.getSystemResource(iconClasspath);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        frame.setIconImage(img);
    }
    @Deprecated(forRemoval = true)
    public static final void setFrameIcons(JFrame frame, String... iconClasspath) {
        List<Image> images = new ArrayList<>();
        Toolkit kit = Toolkit.getDefaultToolkit();
        
        for (int i = 0; i < iconClasspath.length; i++) {
            URL url = ClassLoader.getSystemResource(iconClasspath[i]);
            images.add(kit.createImage(url));
        }
        
        frame.setIconImages(images);
    }
    @Deprecated(forRemoval = true)
    public static final void setAppIcon(String iconClasspath) {
        URL url = ClassLoader.getSystemResource(iconClasspath);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        if (Taskbar.isTaskbarSupported()) {
            if (Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_IMAGE)) {
                Taskbar.getTaskbar().setIconImage(img);
            }
        }
    }
}
