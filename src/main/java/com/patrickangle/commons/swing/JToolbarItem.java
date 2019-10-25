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

import com.patrickangle.commons.laf.modern.ui.ToolbarButtonUI;
import com.patrickangle.commons.laf.modern.ui.util.ToolbarConstants;
import com.patrickangle.commons.util.Colors;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author patrickangle
 */
public class JToolbarItem extends JPanel {
    private static final Color LABEL_COLOR_ACTIVE = Colors.grey(0.7f);
    private static final Color LABEL_COLOR_INACTIVE = Colors.grey(0.43f);
    private static final float LABEL_FONT_SIZE = 10f;
    
    private AbstractButton button;
    private JLabel label;
    
    public JToolbarItem(Action action) {
        this(action, ToolbarConstants.ButtonUIStyle.Default);
    }
    
    public JToolbarItem(Action action, boolean toggleable) {
        this(action, false, ToolbarConstants.ButtonUIStyle.Default);
    }
    
    public JToolbarItem(Icon icon, String label, ActionListener actionListener) {
        this(icon, label, actionListener, ToolbarConstants.ButtonUIStyle.Default);
    }
    
    public JToolbarItem(Icon icon, String label, ActionListener actionListener, boolean toggleable) {
        this(icon, label, actionListener, toggleable, ToolbarConstants.ButtonUIStyle.Default);
    }
    
    public JToolbarItem(Action action, ToolbarConstants.ButtonUIStyle style) {
        this(action, false, style);
    }
    
    public JToolbarItem(Action action, boolean toggleable, ToolbarConstants.ButtonUIStyle style) {
        super();
        
        this.setOpaque(false);
        
        this.setLayout(new GridBagLayout());
        String actionName = (String) action.getValue(Action.NAME);
        action.putValue(Action.NAME, "");
        
        this.button = toggleable ? new JToggleButton(action) : new JButton(action);
        this.button.putClientProperty(ToolbarConstants.ButtonUIStyle.KEY, style);
        this.button.setUI(new ToolbarButtonUI());
        this.button.setFocusable(false);
        this.label = new JLabel(actionName);
        this.label.setFocusable(false);
        this.label.setForeground(LABEL_COLOR_ACTIVE);
        this.label.setFont(this.label.getFont().deriveFont(LABEL_FONT_SIZE));
        
        this.add(this.button, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, (style == ToolbarConstants.ButtonUIStyle.SegmentedFirst || style == ToolbarConstants.ButtonUIStyle.SegmentedMiddle || style == ToolbarConstants.ButtonUIStyle.SegmentedLast || style == ToolbarConstants.ButtonUIStyle.Dropdown) ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(this.label, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 0, 0, 0), 0, 0));
        
        this.setMaximumSize(this.getLayout().minimumLayoutSize(this));
        this.setSize(this.getLayout().minimumLayoutSize(this));
        this.setBorder(new EmptyBorder(2, 6, 2, 6));
    }
    
    public JToolbarItem(Icon icon, String label, ActionListener actionListener, ToolbarConstants.ButtonUIStyle style) {
        this(icon, label, actionListener, false, style);
    }
    
    public JToolbarItem(Icon icon, String label, ActionListener actionListener, boolean toggleable, ToolbarConstants.ButtonUIStyle style) {
        this(new AbstractAction(label, icon) {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        }, toggleable, style);
    }

    public AbstractButton getButton() {
        return button;
    }
    
    public void setWidth(int width) {
        this.setMaximumSize(new Dimension(width, this.getMaximumSize().height));
        this.setSize(new Dimension(width, this.getMinimumSize().height));
        this.setMinimumSize(new Dimension(width, this.getMinimumSize().height));
        this.setPreferredSize(new Dimension(width, this.getPreferredSize().height));
    }
}
