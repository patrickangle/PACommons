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

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.PanelUI;

/**
 *
 * @author patrickangle
 */
public interface TearawayDialogUI {

    /**
     * Install this UI on the provided JTearawayDialog. This is an opportunity
     * to set properties outside the scope of other methods in this interface,
     * as well as to create components that may be returned by other methods.
     *
     * This method will always be called before any other method in the
     * TearawayDialogUI subclass.
     *
     * @param dialog
     */
    public void installUI(JTearawayDialog dialog);

    /**
     * Uninstall this UI from the provided JTearawayDialog. This should undo any
     * actions that installUI performed, including detaching any previously
     * attached listeners.
     *
     * @param dialog
     */
    public void uninstallUI(JTearawayDialog dialog);

    /**
     * 
     * @param dialog
     * @return 
     */
    public JComponent createHeaderComponent(JTearawayDialog dialog);
    
    /**
     * Create a border by which the contents of the JTearawayDialog will be
     * offset given the current attachment state as well as the direction of the
     * attachment.
     *
     * @param dialog
     * @param attached
     * @param attachmentDirection
     * @return
     */
    public Border getDialogBorder(JTearawayDialog dialog, boolean attached, int attachmentDirection);

    public Insets getBodyInsets(JTearawayDialog dialog);
    
    /**
     * Paint the background of the JTearawayDialog, including the attachment
     * indicator if necessary. The attachedTo point will always be a point along
     * the edge of the bounds, or null if the JTearawayDialog is not currently
     * attached.
     *
     * Because a JTearawayDialog is undecorated, as well as opaque, you must
     * explicitly draw a background for the dialog. Pixels not drawn to will
     * remain transparent, showing content below.
     *
     * @param g
     * @param dialog
     * @param bounds
     * @param attachedTo
     */
    public void paintBackground(Graphics g, JTearawayDialog dialog, Rectangle bounds, Point attachedTo);

    /**
     * Paint the border of the JTearawayDialog, including the attachment
     * indicator if necessary. The attachedTo point will always be a point along
     * the edge of the bounds, or null if the JTearawayDialog is not currently
     * attached.
     *
     * @param g
     * @param dialog
     * @param bounds
     * @param attachedTo
     */
    public void paintBorder(Graphics g, JTearawayDialog dialog, Rectangle bounds, Point attachedTo);
    
    default public void installContentPaneUI(JPanel c) {
        c.setUI((PanelUI)UIManager.getUI(c));
//        return (PanelUI)UIManager.getUI(c);
    }
}
