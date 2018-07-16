
package com.patrickangle.commons.util;

import com.patrickangle.commons.types.Display;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

/**
 *
 * @author Patrick Angle
 */
public class Displays {
    public static Display[] availableDisplays() {
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();

        ArrayList<Display> availableDisplays = new ArrayList<>();
        
        for (int i = 0; i < devices.length; i++) {
            availableDisplays.add(new Display(i));
        }
        
        return availableDisplays.toArray(new Display[0]);
    }
    
    public static DisplayMode displayMode(int displayNumber) throws ArrayIndexOutOfBoundsException {
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        return devices[displayNumber].getDisplayMode();
    }
}
