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
package com.patrickangle.commons.laf.modern.ui;

import com.patrickangle.commons.laf.modern.ui.util.MaximizeWindowAction;
import com.patrickangle.commons.laf.modern.ui.util.MinimizeWindowAction;
import com.patrickangle.commons.swing.tearawaydialog.BasicTearawayDialogCloseButtonImage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author patrickangle
 */
public class ModernTitlePane extends JComponent {
    

    private final JRootPane rootPane;
    private final Window window;

    public ModernTitlePane(JRootPane rootPane) {
        this.rootPane = rootPane;
        this.window = SwingUtilities.getWindowAncestor(rootPane);

        initComponents();
    }

    protected void initComponents() {
        this.setLayout(new BorderLayout());
        this.add(createMacTrafficLightControls(), BorderLayout.WEST);
        this.setMinimumSize(new Dimension(-1, 32));

        JLabel titleLabel = new JLabel();
        this.add(titleLabel, BorderLayout.CENTER);
        
        window.addPropertyChangeListener("title", (propertyChangeEvent) -> {
            if (window instanceof Frame) {
                this.add(new JLabel(((Frame) window).getTitle()), BorderLayout.CENTER);
            } else if (window instanceof Dialog) {
                this.add(new JLabel(((Dialog) window).getTitle()), BorderLayout.CENTER);
            }
        });
        
        if (window instanceof Frame) {
            titleLabel.setText(((Frame) window).getTitle());
        } else if (window instanceof Dialog) {
            titleLabel.setText(((Dialog) window).getTitle());
        }
    }

    protected JComponent createMacTrafficLightControls() {
        JPanel controls = new JPanel(new GridLayout(1, 3, 8, 0));
        controls.setOpaque(false);
        controls.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JButton close = new JButton(new CloseWindowAction(window));
        close.setOpaque(false);
        close.setFocusable(false);
        close.setBorder(BorderFactory.createEmptyBorder());
        close.setUI(new BasicButtonUI());
        close.setBorderPainted(false);
        close.setContentAreaFilled(false);
        close.setIcon(new BasicTearawayDialogCloseButtonImage(Color.DARK_GRAY));
        close.setRolloverIcon(new BasicTearawayDialogCloseButtonImage(Color.RED));
        close.setRolloverSelectedIcon(new BasicTearawayDialogCloseButtonImage(Color.RED.brighter().brighter()));
        close.setSelectedIcon(new BasicTearawayDialogCloseButtonImage(Color.RED.brighter().brighter()));
        close.setDisabledIcon(new BasicTearawayDialogCloseButtonImage(Color.DARK_GRAY.darker().darker()));
        controls.add(close);

        JButton minimize = new JButton(new MinimizeWindowAction(window));
        minimize.setOpaque(false);
        minimize.setFocusable(false);
        minimize.setBorder(BorderFactory.createEmptyBorder());
        minimize.setUI(new BasicButtonUI());
        minimize.setBorderPainted(false);
        minimize.setContentAreaFilled(false);
        minimize.setIcon(new BasicTearawayDialogCloseButtonImage(Color.DARK_GRAY));
        minimize.setRolloverIcon(new BasicTearawayDialogCloseButtonImage(Color.YELLOW));
        minimize.setRolloverSelectedIcon(new BasicTearawayDialogCloseButtonImage(Color.YELLOW.brighter().brighter()));
        minimize.setSelectedIcon(new BasicTearawayDialogCloseButtonImage(Color.YELLOW.brighter().brighter()));
        minimize.setDisabledIcon(new BasicTearawayDialogCloseButtonImage(Color.DARK_GRAY.darker().darker()));
        controls.add(minimize);

        JButton maximize = new JButton(new MaximizeWindowAction(window));
        maximize.setOpaque(false);
        maximize.setFocusable(false);
        maximize.setBorder(BorderFactory.createEmptyBorder());
        maximize.setUI(new BasicButtonUI());
        maximize.setBorderPainted(false);
        maximize.setContentAreaFilled(false);
        maximize.setIcon(new BasicTearawayDialogCloseButtonImage(Color.DARK_GRAY));
        maximize.setRolloverIcon(new BasicTearawayDialogCloseButtonImage(Color.GREEN));
        maximize.setRolloverSelectedIcon(new BasicTearawayDialogCloseButtonImage(Color.GREEN.brighter().brighter()));
        maximize.setSelectedIcon(new BasicTearawayDialogCloseButtonImage(Color.GREEN.brighter().brighter()));
        maximize.setDisabledIcon(new BasicTearawayDialogCloseButtonImage(Color.DARK_GRAY.darker().darker()));
        controls.add(maximize);

        return controls;
    }

    protected void createCrossPlatformControls() {
        
    }

    public static class CloseWindowAction extends AbstractAction {

        private final Window window;

        public CloseWindowAction(Window window) {
            super();
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (this.window != null) {
                this.window.dispatchEvent(new WindowEvent(this.window, WindowEvent.WINDOW_CLOSING));
            }
        }

    }
}
