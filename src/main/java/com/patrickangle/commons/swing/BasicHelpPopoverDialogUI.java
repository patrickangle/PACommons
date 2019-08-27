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

import com.patrickangle.commons.swing.tearawaydialog.BasicTearawayDialogUI;
import com.patrickangle.commons.swing.tearawaydialog.JTearawayDialog;
import com.patrickangle.commons.util.Colors;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author patrickangle
 */
public class BasicHelpPopoverDialogUI extends BasicTearawayDialogUI {
    private final JComponent headerComponent;
    
    public BasicHelpPopoverDialogUI() {
        headerComponent = new JPanel();
        headerComponent.setOpaque(false);
        headerComponent.setMaximumSize(new Dimension(-1, 1));
    }

    @Override
    public JComponent createHeaderComponent(JTearawayDialog dialog) {
        return null;
    }

    @Override
    protected Color getFocusedBackgroundColor() {
        return UIManager.getColor("ToolTip.background");
    }

    @Override
    protected Color getUnfocusedBackgroundColor() {
        return UIManager.getColor("ToolTip.background");
    }

    @Override
    protected Color getFocusedTitleLabelColor() {
        return Color.black;
    }

    @Override
    protected Color getUnfocusedTitleLabelColor() {
        return Color.black;
    }

    @Override
    protected Color getFocusedBorderTopColor() {
        return Colors.transparentColor();
    }

    @Override
    protected Color getFocusedBorderBottomColor() {
        return Colors.transparentColor();
    }

    @Override
    protected Color getUnfocusedBorderTopColor() {
        return Colors.transparentColor();
    }

    @Override
    protected Color getUnfocusedBorderBottomColor() {
        return Colors.transparentColor();
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
