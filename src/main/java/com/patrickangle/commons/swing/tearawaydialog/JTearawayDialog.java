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
package com.patrickangle.commons.swing.tearawaydialog;

import com.patrickangle.commons.logging.Logging;
import com.patrickangle.commons.swing.util.WindowDragAdapter;
import com.patrickangle.commons.swing.util.WindowMouseInputAdapter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.BeanProperty;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * A JTearawayDialog is a special dialog that is initially visually attached to
 * some other component in its parent, but can be torn off to become an
 * independent dialog that does not disappear when focus is lost.
 *
 * This is similar to a Popover on macOS, but works across platforms. A custom
 * UI may be declared as part of a Look and Feel, and that UI will be used to
 * render this component.
 *
 * @author patrickangle
 */
public class JTearawayDialog extends JWindow {

    private static final String uiClassID = "TearawayDialogUI";

    protected final Component attachedTo;
    protected final String identifier;

    private JPanel contentPanel;

    private JPanel bodyPanel;

    private TearawayDialogUI ui;

    private String title;
    private boolean resizeable;

    private WindowDragAdapter windowDragAdapter;
    private WindowFocusListener windowFocusAdapter;

    boolean attached = true;

    @FunctionalInterface
    public static interface Creator {

        public JTearawayDialog create(Window parent, Component attachedTo, String identifier);
    }

    /**
     * Handle the presentation of a JTearawayDialog.Every JTearawayDialog has a
     * unique identifier which is used to determine if subsequent calls refer to
     * the same dialog. A dialog is considered the same if its parent and
     * identifier match between calls. The attachedTo component can change
     * between calls.
     *
     * If a dialog with the same parent and identifier are already visible, or
     * displayable, on screen then the existing dialog is brought into focus. If
     * it does not already exist, or has been disposed of previously, then a new
     * JTearawayDialog is created using the default Creator, which provides an
     * empty JTearawayDialog.
     *
     * If the dialog already exists, is currently attached, and the attachedTo
     * component is the same, the dialog is closed. This mimics the behavior of
     * macOS and Popovers, where pressing a button a second time hides the
     * Popover.
     *
     * If the dialog already exists, is currently attached, but the attachedTo
     * component is different, the dialog is detached from its current component
     * and brought to the front.
     *
     * If the dialog already exists, but is not currently attached, the dialog
     * is brought to the front.
     *
     * If the dialog does not exist, the default Creator is used to create and
     * show a new dialog. If the attachedTo component is not null then the
     * dialog will be attached to the component.
     *
     * @param parent
     * @param attachedTo
     * @param identifier
     * @return
     */
    public static JTearawayDialog showOrFocusDialog(Window parent, Component attachedTo, String identifier) {
        return JTearawayDialog.showOrFocusDialog(parent, attachedTo, identifier, (p, a, i) -> {
            return new JTearawayDialog(p, a, i);
        });
    }

    /**
     * Handle the presentation of a JTearawayDialog.Every JTearawayDialog has a
     * unique identifier which is used to determine if subsequent calls refer to
     * the same dialog. A dialog is considered the same if its parent and
     * identifier match between calls. The attachedTo component can change
     * between calls.
     *
     * If a dialog with the same parent and identifier are already visible, or
     * displayable, on screen then the existing dialog is brought into focus. If
     * it does not already exist, or has been disposed of previously, then a new
     * JTearawayDialog is created using the provided Creator, where you can
     * override the type of JTearawayDialog that is created to utilize a
     * subclass. Subclasses of JTearawayDialog should provide a Creator for this
     * purpose.
     *
     * If the dialog already exists, is currently attached, and the attachedTo
     * component is the same, the dialog is closed. This mimics the behavior of
     * macOS and Popovers, where pressing a button a second time hides the
     * Popover.
     *
     * If the dialog already exists, is currently attached, but the attachedTo
     * component is different, the dialog is detached from its current component
     * and brought to the front.
     *
     * If the dialog already exists, but is not currently attached, the dialog
     * is brought to the front.
     *
     * If the dialog does not exist, the provided Creator is used to create and
     * show a new dialog. If the attachedTo component is not null then the
     * dialog will be attached to the component.
     *
     * @param parent
     * @param attachedTo
     * @param identifier
     * @param creator
     * @return
     */
    public static JTearawayDialog showOrFocusDialog(Window parent, Component attachedTo, String identifier, Creator creator) {
        for (Window w : Window.getWindows()) {
            if (w instanceof JTearawayDialog && w.isDisplayable()) {
                if (((JTearawayDialog) w).getIdentifier().equals(identifier) && ((JTearawayDialog) w).getParent() == parent) {
                    if (((JTearawayDialog) w).isAttached()) {
                        if (((JTearawayDialog) w).getAttachedTo() == attachedTo) {
                            // If the dialog is currently attached, and the component is the same, close the dialog.
                            w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
                        } else {
                            // If the dialog is currently attached, but to a different component, break the dialog off and bring it into focus.
                            ((JTearawayDialog) w).setAttached(false);
                            w.toFront();
                        }
                    } else {
                        // Otherwise, bring the dialog into focus.
                        w.toFront();
                    }

                    return (JTearawayDialog) w;
                }
            }
        }

        JTearawayDialog d = creator.create(parent, attachedTo, identifier);
        d.setVisible(true);
        return d;
    }

    /**
     * Creates a new JTearawayDialog. The dialog will be a child of the provided
     * parent, or be an orphaned window if the parent is null.
     *
     * The attachedTo component is used to visually attached this dialog to an
     * existing component in the parent Window. If the attachedTo component is
     * null, the dialog is left detached, and will not be able to attach to any
     * component.
     *
     * The identifier is used to prevent created duplicate dialogues, but that
     * behavior is enforced by the static showOrFocusDialog(…) methods, not the
     * constructor.
     *
     * @param parent
     * @param attachedTo
     * @param identifier
     */
    public JTearawayDialog(Window parent, Component attachedTo, String identifier) {
        super(parent);

        this.attachedTo = attachedTo;
        this.identifier = identifier;

        initComponents();
    }

    /**
     * Perform the initialization of the components in every JTearawayDialog
     * based on the look and feel.
     */
    private void initComponents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }

        });
        
        setType(Type.POPUP);

        // Must be set before we continue. Changing this isn't really supported yet.
        this.setUI(getLookAndFeelDeclaredUI());

        this.setTitle(getIdentifier());

        this.contentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                JTearawayDialog.this.getUI().paintBackground(g, JTearawayDialog.this, new Rectangle(0, 0, JTearawayDialog.this.getWidth(), JTearawayDialog.this.getHeight()), attached ? new Point() : null);
            }

            @Override
            protected void paintBorder(Graphics g) {
                JTearawayDialog.this.getUI().paintBorder(g, JTearawayDialog.this, new Rectangle(0, 0, JTearawayDialog.this.getWidth(), JTearawayDialog.this.getHeight()), attached ? new Point() : null);
            }
        };
        this.contentPanel.setOpaque(false);
        this.contentPanel.setBorder(getUI().getDialogBorder(this, attached, SwingConstants.NORTH));
        this.setContentPane(contentPanel);

        new WindowMouseInputAdapter(this, () -> {
            setAttached(false);
        });

        // If this dialog is current attached and the user clicks away, close the dialog. Also repaint the window if focus changes.
        this.windowFocusAdapter = new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                e.getWindow().repaint();
                if (JTearawayDialog.this.isAttached()) {
                    JTearawayDialog.this.dispatchEvent(new WindowEvent(JTearawayDialog.this, WindowEvent.WINDOW_CLOSING));
                }
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                e.getWindow().repaint();
            }

        };
        this.addWindowFocusListener(windowFocusAdapter);

        JComponent headerComponent = getUI().createHeaderComponent(this);
        if (headerComponent != null) {
            this.contentPanel.add(headerComponent, BorderLayout.NORTH);
        }

        // Create and add the main body panel to the dialog.
        this.bodyPanel = new JPanel();
        this.bodyPanel.setOpaque(false);
        this.bodyPanel.setBorder(new EmptyBorder(getUI().getBodyInsets(this)));
        this.contentPanel.add(this.bodyPanel, BorderLayout.CENTER);

        setAttached(attachedTo != null);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            this.pack();
        }
        super.setVisible(b); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void validate() {
        super.validate(); //To change body of generated methods, choose Tools | Templates.
        
        if (attached) {
            int x = (attachedTo.getLocationOnScreen().x + (attachedTo.getWidth() / 2)) - (this.getWidth() / 2);
            int y = (attachedTo.getLocationOnScreen().y + attachedTo.getHeight() + 2);
            setLocation(x, y);
        }
    }

    @Override
    public void dispose() {
        // TODO: Remove when upstream bug is fixed.
        // Delay super.dispose() to work around macOS 10.14.5 bug where not waiting 1 millisecond after a modal dialog closes to dispose of this frame causes the native frame to not be disposed.
        SwingUtilities.invokeLater(() -> {
            Timer disposeDelay = new Timer(1, (evt) -> {
                JTearawayDialog.super.dispose();
            });
            disposeDelay.setRepeats(false);
            disposeDelay.start();
        });
    }
    
    

    /**
     * Get the current attachment state of the dialog.
     *
     * @return
     */
    public boolean isAttached() {
        return attached;
    }

    /**
     * Set the current attachment state of the dialog.
     *
     * If you set a dialog to be attached, but null was provided as the
     * attachedTo component, then the dialog ignores the request to attach.
     *
     * @param attached
     */
    public void setAttached(boolean attached) {
        boolean oldAttached = this.attached;
        // The provided state is only honored if there is a component to which this dialog was initally attached;
        this.firePropertyChange("attached", this.attached, this.attached = attached && (attachedTo != null));

        if (oldAttached != this.attached) {
            this.contentPanel.setBorder(getUI().getDialogBorder(this, attached, SwingConstants.NORTH));
        }

        revalidate();
    }

    /**
     * Get the component to which this dialog is, or at one point was, attached.
     * See isAttached() to determine if the dialog is currently attached to this
     * component.
     *
     * @return
     */
    public Component getAttachedTo() {
        return attachedTo;
    }

    /**
     * Get the identifier used to uniquely mark this dialog.
     *
     * @return
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Get the panel that contains the controls for this dialog. This is the
     * panel that should have all the controls specific to this dialog.
     *
     * @return
     */
    public JPanel getBodyPanel() {
        return bodyPanel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String oldTitle = this.title;
        this.title = title;
        this.firePropertyChange("title", oldTitle, this.title);
    }

    public boolean isResizeable() {
        return resizeable;
    }

    public void setResizeable(boolean resizeable) {
        boolean oldResizeable = this.resizeable;
        this.resizeable = resizeable;
        this.firePropertyChange("resizeable", oldResizeable, resizeable);
    }

    /**
     * Get the String used to identify the TearawayDialogUI in the UIDefaults
     * map.
     *
     * @return
     */
    @BeanProperty(bound = false, expert = true, description = "UIClassID")
    public String getUIClassID() {
        return uiClassID;
    }

    /**
     * Sets the UI to the given TearawayDialogUI instance. Currently, calling
     * this method after the component has been initialized will result in
     * inconsistent results. Only the UI defined by the current look and feel,
     * or the fallback BasicTearawayDialogUI can be used at this time.
     *
     * @param ui
     */
    public void setUI(TearawayDialogUI ui) {
        if (this.ui != null) {
            this.ui.uninstallUI(this);
        }
        this.ui = ui;
        this.ui.installUI(this);
    }

    /**
     * Get the current TearawayDialogUI being used by this dialog.
     *
     * @return
     */
    public TearawayDialogUI getUI() {
        return ui;
    }

    /**
     * Get the TearawayDialogUI declared by the current look and feel, or the
     * BasicTearawayDialogUI if that is unavailable.
     *
     * @return
     */
    protected TearawayDialogUI getLookAndFeelDeclaredUI() {
        String className = UIManager.getString(getUIClassID());
        if (className != null) {
            try {
                Class c = Class.forName(className);
                Object o = c.getConstructor().newInstance();
                if (o instanceof TearawayDialogUI) {
                    return (TearawayDialogUI) o;
                }
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logging.error(this, "Declared `" + getUIClassID() + "` of `" + className + "` was not valid. Using BasicTearawayDialogUI.");
            }
        }

        return getDefaultUI();
    }

    protected TearawayDialogUI getDefaultUI() {
        return new BasicTearawayDialogUI();
    }

}
