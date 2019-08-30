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

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author patrickangle
 */
public class MenuItems {
    public static final int SYSTEM_MODIFIER = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
    public static final int ALT_MOFIFIER = InputEvent.ALT_DOWN_MASK;
    public static final int SHIFT_MODIFIER = InputEvent.SHIFT_DOWN_MASK;
    
    public static final int SYSTEM_ALT_MODIFIER = SYSTEM_MODIFIER | ALT_MOFIFIER;
    public static final int SYSTEM_SHIFT_MODIFIER = SYSTEM_MODIFIER | SHIFT_MODIFIER;
    public static final int SYSTEM_ALT_SHIFT_MODIFIER = SYSTEM_MODIFIER | ALT_MOFIFIER | SHIFT_MODIFIER;
    
    // For macOS only; useful for toggling full screen.
    public static final int SYSTEM_CTRL_MODIFIER = SYSTEM_MODIFIER | InputEvent.CTRL_DOWN_MASK;
    
    public static JMenuItem item(String title, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }
    
    public static JMenuItem item(String title, int acceleratorKeyCode, int acceleratorModifier, ActionListener actionListener) {
        return MenuItems.item(title, KeyStroke.getKeyStroke(acceleratorKeyCode, acceleratorModifier), actionListener);
    }
    
    public static JMenuItem item(String title, int acceleratorKeyCode, int acceleratorModifier, ActionListener actionListener, String actionCommand) {
        return MenuItems.item(title, KeyStroke.getKeyStroke(acceleratorKeyCode, acceleratorModifier), actionListener, actionCommand);
    }
    
    public static JMenuItem item(String title, KeyStroke acceleratorKeystroke, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(acceleratorKeystroke);
        return menuItem;
    }
    
    public static JMenuItem item(String title, KeyStroke acceleratorKeystroke, ActionListener actionListener, String actionCommand) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(acceleratorKeystroke);
        menuItem.setActionCommand(actionCommand);
        return menuItem;
    }
}
