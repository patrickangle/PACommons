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

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author patrickangle
 */
public class PlatformMouse {
    /**
     * Detect an event that is a Right Click, including macOS Ctrl+Click
     * @param e
     * @return 
     */
    public static boolean isRightClick(MouseEvent e) {
        return (e.getButton() == MouseEvent.BUTTON3
            || ((OperatingSystems.current() == OperatingSystems.Macintosh
            && (e.getModifiers() & InputEvent.BUTTON1_MASK) != 0
            && (e.getModifiers() & InputEvent.CTRL_MASK) != 0))) || SwingUtilities.isRightMouseButton(e);
    }
}
