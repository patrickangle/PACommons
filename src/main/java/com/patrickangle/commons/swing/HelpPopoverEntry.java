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

import com.patrickangle.commons.util.OperatingSystems;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 *
 * @author patrickangle
 */
public class HelpPopoverEntry {

    public static final String HELP_TIP_CLIENT_PROPERTY_KEY = "HelpTipEntry.Entry";
    private static final String HELP_TIP_DIALOG_CLIENT_PROPERTY_KEY = "HelpTipDialogKey";

    private final WeakReference<JComponent> component;

    private final String title;
    private final String description;
    private final Object additionalHelp;

    private final MouseListener mouseListener;
    private final KeyListener keyListener;

    public static void assign(final JComponent component, final String title, final String description, final Object additionalHelp) {
        component.putClientProperty(HELP_TIP_CLIENT_PROPERTY_KEY, new HelpPopoverEntry(component, title, description, additionalHelp));
    }

    /**
     * Create a new HelpTipEntry for the given component, which will be assigned
     * to the appropriate client property on a component.
     *
     * @param component
     * @param title
     * @param description
     * @param additionalHelp
     */
    protected HelpPopoverEntry(final JComponent component, final String title, final String description, final Object additionalHelp) {
        this.component = new WeakReference(component);

        this.title = title;
        this.description = description;
        this.additionalHelp = additionalHelp;

        this.mouseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
//                Component c = e.getComponent();
//                if (c != null && c instanceof JComponent) {
//                    JComponent component = (JComponent) c;
//                    Object possibleEntry = component.getClientProperty(HelpTipEntry.HELP_TIP_CLIENT_PROPERTY_KEY);
//                    if (possibleEntry != null && possibleEntry instanceof HelpTipEntry) {
//                        HelpTipEntry entry = (HelpTipEntry) possibleEntry;
                        JHelpPopover dialog = new JHelpPopover(SwingUtilities.getWindowAncestor(component), component, component.toString());
                        dialog.setTitle(getTitle());
                        dialog.getBodyPanel().setLayout(new BorderLayout());

                        JLabel titleLabel = new JLabel(getTitle());
                        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, titleLabel.getFont().getSize() + 1));
                        titleLabel.setForeground(UIManager.getColor("ToolTip.foreground"));
                        dialog.getBodyPanel().add(titleLabel, BorderLayout.NORTH);

                        JLabel desc = new JLabel();
                        // Dynamically force multi-line rendering when we know we will exceed 360px.
                        if (BasicGraphicsUtils.getStringWidth(desc, desc.getFontMetrics(desc.getFont()), getDescription()) > 360) {
                            desc.setText("<html><body width='360'><span>" + getDescription());
                        } else {
                            desc.setText("<html><body><span>" + getDescription());
                        }
                        desc.setForeground(UIManager.getColor("ToolTip.foreground"));
                        dialog.getBodyPanel().add(desc, BorderLayout.CENTER);

                        if (getAdditionalHelp() != null) {
                            JLabel moreLabel = new JLabel("Press " + (OperatingSystems.current() == OperatingSystems.Macintosh ? "⌘" : "⌃") + "/ for more info.");
                            moreLabel.setFont(moreLabel.getFont().deriveFont(Font.BOLD));
                            moreLabel.setForeground(UIManager.getColor("ToolTip.foreground"));
                            moreLabel.setBorder(new EmptyBorder(3, 0, 0, 0));
                            dialog.getBodyPanel().add(moreLabel, BorderLayout.SOUTH);
                        }

                        component.putClientProperty(HELP_TIP_DIALOG_CLIENT_PROPERTY_KEY, dialog);

//                        dialog.addWindowListener(new WindowAdapter() {
//                            @Override
//                            public void windowClosed(WindowEvent e) {
//                                System.out.println("nulling out");
//                                dialog.setVisible(false);
//                                dialog.dispose();
//                                component.putClientProperty(HELP_TIP_DIALOG_CLIENT_PROPERTY_KEY, null);
////                                dialog.removeKeyListener(keyListener);
//                            }
//                        });

//                        dialog.addKeyListener(keyListener);
                        dialog.setVisible(true);
//                        dialog.requestFocus();
//                    }
//                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
//                Component c = e.getComponent();
//                if (c != null && c instanceof JComponent) {
//                    JComponent component = (JComponent) c;
                    Object window = component.getClientProperty(HELP_TIP_DIALOG_CLIENT_PROPERTY_KEY);
                    if (window != null && window instanceof Window) {
                        
                        ((Window) window).dispatchEvent(new WindowEvent((Window) window, WindowEvent.WINDOW_CLOSING));
                        
                    }
//                }
            }
        };

        this.keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Event!");
                if (e.getExtendedKeyCode() == KeyEvent.VK_SLASH && ((OperatingSystems.current() == OperatingSystems.Macintosh) ? e.isMetaDown() : e.isControlDown())) {
                    System.out.println("Magic!");
                    Component c = e.getComponent();
                    if (c instanceof JComponent) {
                        JComponent component = (JComponent) c;
                        Object possibleEntry = component.getClientProperty(HelpPopoverEntry.HELP_TIP_CLIENT_PROPERTY_KEY);
                        if (possibleEntry != null && possibleEntry instanceof HelpPopoverEntry) {
                            HelpPopoverEntry entry = (HelpPopoverEntry) possibleEntry;
                            if (entry.getAdditionalHelp() != null) {
                                JOptionPane.showMessageDialog(component, entry.getAdditionalHelp(), "More information coming soon?", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }

            }
        };

        Window window = SwingUtilities.getWindowAncestor(component);
        if (window != null) {
        window.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println("Focus lost, time to move on...");
                Object window = component.getClientProperty(HELP_TIP_DIALOG_CLIENT_PROPERTY_KEY);
                System.out.println(window);
                if (window != null && window instanceof Window) {
//                    ((Window) window).dispatchEvent(new WindowEvent((Window) window, WindowEvent.WINDOW_CLOSING));
                    ((Window) window).dispose();
                }
            }
        });
        }
    }

    public JComponent getComponent() {
        return component.get();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Object getAdditionalHelp() {
        return additionalHelp;
    }

    public void activate() {
        JComponent c = component.get();
        if (c != null) {
            c.addMouseListener(mouseListener);
//            c.addKeyListener(keyListener);
        }
    }

    public void deactivate() {
        JComponent c = component.get();
        if (c != null) {
            c.removeMouseListener(mouseListener);
//            c.removeKeyListener(keyListener);
        }
    }

}
