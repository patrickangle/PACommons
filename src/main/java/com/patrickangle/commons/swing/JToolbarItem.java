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
package com.patrickangle.commons.swing;

import com.patrickangle.commons.laf.modern.ui.MacToolbarButtonUI;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author patrickangle
 */
public class JToolbarItem extends JPanel {
    private AbstractButton button;
    private JLabel label;
    
    public JToolbarItem(Action action) {
        this(action, false);
    }
    
    public JToolbarItem(Action action, boolean toggleable) {
        super();
        
        this.setOpaque(false);
        
        this.setLayout(new GridBagLayout());
        String actionName = (String) action.getValue(Action.NAME);
        action.putValue(Action.NAME, "");
        
        this.button = toggleable ? new JToggleButton(action) : new JButton(action);
        this.button.setUI(new MacToolbarButtonUI());
        this.button.setFocusable(false);
        this.label = new JLabel(actionName);
        this.label.setFocusable(false);
        
        this.add(this.button, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(this.label, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }
    
    public JToolbarItem(Icon icon, String label, ActionListener actionListener) {
        this(icon, label,actionListener, false);
    }
    
    public JToolbarItem(Icon icon, String label, ActionListener actionListener, boolean toggleable) {
        this(new AbstractAction(label, icon) {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        }, toggleable);
    }

    public AbstractButton getButton() {
        return button;
    }
}
