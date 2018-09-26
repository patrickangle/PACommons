/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.RootPaneContainer;

/**
 *
 * @author Patrick Angle
 */
public class Windows {
    
    public static void takeFullscreen(Window window, int screen) {
        GraphicsDevice[] graphicsDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        if (screen > -1 && screen < graphicsDevices.length) {
            graphicsDevices[screen].setFullScreenWindow(window);
        } else if (graphicsDevices.length > 0) {
            graphicsDevices[0].setFullScreenWindow(window);
        } else {
            throw new RuntimeException("No screens are available in the current environment.");
        }
    }
    
    public static void moveToScreen(Window window, int screen) {
        GraphicsDevice[] graphicsDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        if (screen > -1 && screen < graphicsDevices.length) {
            window.setLocation(graphicsDevices[screen].getDefaultConfiguration().getBounds().getLocation());
        } else if (graphicsDevices.length > 0) {
            window.setLocation(graphicsDevices[0].getDefaultConfiguration().getBounds().getLocation());
        } else {
            throw new RuntimeException("No screens are available in the current environment.");
        }
    }
    
    public static void center(Window window) {
        window.setLocationRelativeTo(null);
    }
    
    public static void setIcons(Window window, String... iconClasspaths) {
        List<Image> images = new ArrayList<>();
        Toolkit kit = Toolkit.getDefaultToolkit();
        
        for (int i = 0; i < iconClasspaths.length; i++) {
            URL url = ClassLoader.getSystemResource(iconClasspaths[i]);
            images.add(kit.createImage(url));
        }
        
        window.setIconImages(images);
    }
    
    public static void setFileProxy(Window window, File file) {
        if (window instanceof RootPaneContainer) {
            ((RootPaneContainer) window).getRootPane().putClientProperty( "Window.documentFile", file);
        }
    }
}
