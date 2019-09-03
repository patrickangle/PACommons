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
package com.patrickangle.commons.laf.modern.ui.util;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;

/**
 *
 * @author patrickangle
 */
public class MinimizeWindowAction extends AbstractAction {

    private final Window window;

    public MinimizeWindowAction(Window window) {
        super();
        this.window = window;
        
        if (!(window instanceof Frame)) {
            this.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (window instanceof Frame) {
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_ICONIFIED));
        }
    }
}
