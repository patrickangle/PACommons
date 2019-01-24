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

import static com.patrickangle.commons.laf.modern.ModernUIUtilities.ACCENT_LIGHT_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.BACKGROUND_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY;
import com.patrickangle.commons.util.Images;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.basic.BasicOptionPaneUI;

/**
 *
 * @author patrickangle
 */
public class ModernOptionPaneUI extends BasicOptionPaneUI {
    private static final int kOKCancelButtonWidth = 79;
    private static final int kButtonHeight = 23;

    private static final int kDialogSmallPadding = 4;
    private static final int kDialogLargePadding = 23;

    /**
     * Creates a new BasicOptionPaneUI instance.
     */
    public static ComponentUI createUI(final JComponent x) {
        return new ModernOptionPaneUI();
    }

    /**
     * Creates and returns a Container containin the buttons. The buttons
     * are created by calling {@code getButtons}.
     */
    protected Container createButtonArea() {
        final Container bottom = super.createButtonArea();
        // Now replace the Layout
        bottom.setLayout(new ModernButtonAreaLayout(true, kDialogSmallPadding));
        return bottom;
    }

    /**
     * Messaged from installComponents to create a Container containing the
     * body of the message.
     * The icon and body should be aligned on their top edges
     */
    @Override
    protected Container createMessageArea() {
        final JPanel top = new JPanel();
        top.setBorder(UIManager.getBorder("OptionPane.messageAreaBorder"));
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

        /* Fill the body. */
        final Container body = new JPanel();

        final Icon sideIcon = getIcon();

        if (sideIcon != null) {
            final JLabel iconLabel = new JLabel(sideIcon);
            iconLabel.setVerticalAlignment(SwingConstants.TOP);

            final JPanel iconPanel = new JPanel();
            iconPanel.add(iconLabel);
            top.add(iconPanel);
            top.add(Box.createHorizontalStrut(kDialogLargePadding));
        }

        body.setLayout(new GridBagLayout());
        final GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = cons.gridy = 0;
        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.gridheight = 1;
        cons.anchor = GridBagConstraints.WEST;
        cons.insets = new Insets(0, 0, 3, 0);

        addMessageComponents(body, cons, getMessage(), getMaxCharactersPerLineCount(), false);
        top.add(body);

        return top;
    }

    @Override
    protected int getMaxCharactersPerLineCount() {
        return 100;
    }
//    
    

    /**
     * AquaButtonAreaLayout lays out all
     *   components according to the HI Guidelines:
     * The most important button is always on the far right
     * The group of buttons is on the right for left-to-right,
     *         left for right-to-left
     * The widths of each component will be set to the largest preferred size width.
     *
     *
     * This inner class is marked &quot;public&quot; due to a compiler bug.
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of BasicOptionPaneUI.
     *
     * BasicOptionPaneUI expects that its buttons are layed out with
     * a subclass of ButtonAreaLayout
     */
    protected static class ModernButtonAreaLayout extends ButtonAreaLayout {
        public ModernButtonAreaLayout(final boolean syncAllWidths, final int padding) {
            super(true, padding);
        }

        public void layoutContainer(final Container container) {
            final Component[] children = container.getComponents();
            if (children == null || 0 >= children.length) return;

            final int numChildren = children.length;
            final int yLocation = container.getInsets().top;

            // Always syncAllWidths - and heights!
            final Dimension maxSize = new Dimension(kOKCancelButtonWidth, kButtonHeight);
            for (int i = 0; i < numChildren; i++) {
                final Dimension sizes = children[i].getPreferredSize();
                maxSize.width = Math.max(maxSize.width, sizes.width);
                maxSize.height = Math.max(maxSize.height, sizes.height);
            }

            // ignore getCentersChildren, because we don't
            int xLocation = container.getSize().width - (maxSize.width * numChildren + (numChildren - 1) * padding);
            final int xOffset = maxSize.width + padding;

            // most important button (button zero) on far right
            for (int i = numChildren - 1; i >= 0; i--) {
                children[i].setBounds(xLocation, yLocation, maxSize.width, maxSize.height);
                xLocation += xOffset;
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container c) {
            if (c != null) {
                Component[] children = c.getComponents();
                if (children != null && children.length > 0) {
                    int numChildren = children.length;
                    Insets cInsets = c.getInsets();
                    int extraHeight = cInsets.top + cInsets.bottom;
                    int extraWidth = cInsets.left + cInsets.right;
                    int okCancelButtonWidth = extraWidth
                            + (kOKCancelButtonWidth * numChildren)
                            + (numChildren - 1) * padding;
                    int okbuttonHeight = extraHeight + kButtonHeight;
                    Dimension minSize = super.minimumLayoutSize(c);
                    return new Dimension(Math.max(minSize.width,
                            okCancelButtonWidth),
                            Math.max(minSize.height, okbuttonHeight));
                }
            }
            return new Dimension(0, 0);
        }
    }
    
    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("OptionPaneUI", ModernOptionPaneUI.class.getName());
        
        defaults.put("OptionPane.messageForeground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("OptionPane.background", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("OptionPane.foreground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        
        defaults.put("OptionPane.informationIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneInfo.png"))));
        defaults.put("OptionPane.questionIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneQuestion.png"))));
        defaults.put("OptionPane.warningIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneWarning.png"))));
        defaults.put("OptionPane.errorIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneError.png"))));
    }
}
