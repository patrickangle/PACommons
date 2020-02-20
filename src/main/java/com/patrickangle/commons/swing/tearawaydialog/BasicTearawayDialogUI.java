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

import com.patrickangle.commons.swing.util.ComponentMover;
import com.patrickangle.commons.swing.util.ComponentResizer;
import com.patrickangle.commons.swing.util.WindowMouseInputAdapter;
import com.patrickangle.commons.util.OperatingSystems;
import com.patrickangle.commons.util.Windows;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Window.Type;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author patrickangle
 */
public class BasicTearawayDialogUI implements TearawayDialogUI {
    private static final Color TITLE_AND_CLOSE_BUTTON_FOCUSED_COLOR = new Color(0xffffff);
    private static final Color TITLE_AND_CLOSE_BUTTON_UNFOCUSED_COLOR = new Color(0xcccccc);
    private static final Color CLOSE_BUTTON_PRESSED_COLOR = new Color(0xaaaaaa);

    private final Color BORDER_TOP_COLOR = new Color(255, 255, 255, 59);
    private final Color BORDER_BOTTOM_COLOR = new Color(255, 255, 255, 25);
    private final Color BACKGROUND_FOCUSED_COLOR = new Color(30, 30, 30, 250);
    private final Color BACKGROUND_UNFOCUSED_COLOR = new Color(30, 30, 30, 170);

    private static final int ROUNDED_RECT_DIAMETER = 16;
    private static final int ATTACHMENT_TRIANGLE_HEIGHT = 12;
    
    private static final Insets BODY_INSETS = new Insets(2, 5, 5, 5);

    private JButton closeButton;

    private JLabel titleLabel;

    private WindowFocusListener focusListener;
    private PropertyChangeListener titlePropertyChangeListener;
    private PropertyChangeListener attachedPropertyChangeListener;

    public BasicTearawayDialogUI() {
        this.focusListener = new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                BasicTearawayDialogUI.this.titleLabel.setForeground(getFocusedTitleLabelColor());
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                BasicTearawayDialogUI.this.titleLabel.setForeground(getUnfocusedTitleLabelColor());
            }
        };

        this.titlePropertyChangeListener = (propertyChangeEvent) -> {
            this.titleLabel.setText((String) propertyChangeEvent.getNewValue());
        };

        this.attachedPropertyChangeListener = (propertyChangeEvent) -> {
            this.closeButton.setVisible(!((boolean) propertyChangeEvent.getNewValue()));
        };
    }

    @Override
    public void installUI(JTearawayDialog dialog) {
        this.closeButton = createCloseButton(dialog);
        this.titleLabel = createTitleLabel(dialog);
        
        dialog.setType(Type.UTILITY);
        dialog.setUndecorated(true);

        dialog.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
//        dialog.setUndecorated(true);
        dialog.getRootPane().setOpaque(false);
        Windows.makeNonOpaque(dialog);
        
        new WindowMouseInputAdapter(dialog, () -> {
            dialog.setAttached(false);
        });
//dialog.setAttached(false);
        
        ComponentMover cm = new ComponentMover(dialog, dialog.getContentPane());
        ComponentResizer cr = new ComponentResizer(new Insets(2, 5, 5, 5), dialog);
        cm.setDragInsets(new Insets(2, 5, 5, 5));
        
        dialog.addMouseListener(cm);
        dialog.addMouseListener(cr);

        dialog.addWindowFocusListener(focusListener);
        dialog.addPropertyChangeListener("title", titlePropertyChangeListener);
        dialog.addPropertyChangeListener("attached", attachedPropertyChangeListener);
    }

    @Override
    public void uninstallUI(JTearawayDialog dialog) {
        dialog.removeWindowFocusListener(focusListener);
        dialog.removePropertyChangeListener("title", titlePropertyChangeListener);
        dialog.removePropertyChangeListener("attached", attachedPropertyChangeListener);
    }

    /**
     * Get the corner diameter of the dialog, in pixels.
     *
     * @return
     */
    protected int getCornerDiameter() {
        return ROUNDED_RECT_DIAMETER;
    }

    /**
     * Get the height (or width) of the arrow showing the dialog's attachment to
     * another component.
     *
     * @return
     */
    protected int getAttachmentHeight() {
        return ATTACHMENT_TRIANGLE_HEIGHT;
    }

    /**
     * Get the focused title label color.
     *
     * @return
     */
    protected Color getFocusedTitleLabelColor() {
        return TITLE_AND_CLOSE_BUTTON_FOCUSED_COLOR;
    }

    /**
     * Get the unfocused title label color.
     *
     * @return
     */
    protected Color getUnfocusedTitleLabelColor() {
        return TITLE_AND_CLOSE_BUTTON_UNFOCUSED_COLOR;
    }

    /**
     * Get the rollover close button color.
     *
     * @return
     */
    protected Color getRolloverCloseButtonColor() {
        return TITLE_AND_CLOSE_BUTTON_FOCUSED_COLOR;
    }

    /**
     * Get the normal close button color.
     *
     * @return
     */
    protected Color getNormalCloseButtonColor() {
        return TITLE_AND_CLOSE_BUTTON_UNFOCUSED_COLOR;
    }

    /**
     * Get the pressed close button color.
     *
     * @return
     */
    protected Color getPressedCloseButtonColor() {
        return CLOSE_BUTTON_PRESSED_COLOR;
    }

    /**
     * Get the focused background color.
     *
     * @return
     */
    protected Color getFocusedBackgroundColor() {
        return BACKGROUND_FOCUSED_COLOR;
    }

    /**
     * Get the unfocused background color.
     *
     * @return
     */
    protected Color getUnfocusedBackgroundColor() {
        return BACKGROUND_UNFOCUSED_COLOR;
    }

    /**
     * Get the focused border top color.
     *
     * @return
     */
    protected Color getFocusedBorderTopColor() {
        return BORDER_TOP_COLOR;
    }

    /**
     * Get the focused border bottom color.
     *
     * @return
     */
    protected Color getFocusedBorderBottomColor() {
        return BORDER_BOTTOM_COLOR;
    }

    /**
     * Get the unfocused border top color.
     *
     * @return
     */
    protected Color getUnfocusedBorderTopColor() {
        return BORDER_TOP_COLOR.darker().darker();
    }

    /**
     * Get the unfocused border bottom color.
     *
     * @return
     */
    protected Color getUnfocusedBorderBottomColor() {
        return BORDER_BOTTOM_COLOR.darker().darker();
    }

    @Override
    public JComponent createHeaderComponent(JTearawayDialog dialog) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(-1, 20));

        Component spacer = Box.createHorizontalStrut(this.closeButton.getPreferredSize().width);

        panel.add(this.titleLabel, BorderLayout.CENTER);
        panel.add(this.closeButton, OperatingSystems.current() == OperatingSystems.Macintosh ? BorderLayout.WEST : BorderLayout.EAST);
        panel.add(spacer, OperatingSystems.current() == OperatingSystems.Macintosh ? BorderLayout.EAST : BorderLayout.WEST);

        return panel;
    }

    /**
     * Create the button that will close the dialog. This button is only visible
     * if the JTearawayDialog is currently detached. The showing and hiding is
     * handled by the JTearawayDialog, as is the assignment of the actual action
     * to the button. The JTearawayDialog is provided to allow you to bind
     * properties like focus to the style of the button.
     *
     * @param dialog
     * @return
     */
    protected JButton createCloseButton(final JTearawayDialog dialog) {
        JButton button = new JButton(new BasicTearawayDialogCloseButtonImage(getNormalCloseButtonColor()));
        button.setRolloverIcon(new BasicTearawayDialogCloseButtonImage(getRolloverCloseButtonColor()));
        button.setPressedIcon(new BasicTearawayDialogCloseButtonImage(getPressedCloseButtonColor()));

        // Padding based on current platform, since on macOS the button will be in the top-left corner instead of top-right.
        button.setBorder(OperatingSystems.current() == OperatingSystems.Macintosh
                ? BorderFactory.createEmptyBorder(0, 5, 0, 0)
                : BorderFactory.createEmptyBorder(0, 0, 0, 5));
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setUI(new BasicButtonUI());
        button.setOpaque(false);
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.addActionListener((actionEvent) -> {
            // Create a synthetic window event to simulate a real close button being pressed.
            dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
        });

        button.setVisible(!dialog.isAttached());

        return button;
    }

    /**
     * Create the label that will be used as the title for the JTearawayDialog.
     * The contents of the label will be populated and updated as needed by the
     * JTearawayDialog, so you do not need to provide any default text. The
     * JTearawayDialog is provided to allow you to bind properties like focus to
     * the style of the label.
     *
     * @param dialog
     * @return
     */
    protected JLabel createTitleLabel(final JTearawayDialog dialog) {
        JLabel label = new JLabel(dialog.getTitle(), SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 11.0f));

        if (dialog.isFocused()) {
            label.setForeground(getFocusedTitleLabelColor());
        } else {
            label.setForeground(getUnfocusedTitleLabelColor());
        }

        return label;
    }

    @Override
    public Border getDialogBorder(JTearawayDialog dialog, boolean attached, int attachmentDirection) {
        if (attached) {
            switch (attachmentDirection) {
                case SwingConstants.SOUTH:
                    return BorderFactory.createEmptyBorder(0, 0, getAttachmentHeight(), 0);
                case SwingConstants.EAST:
                    return BorderFactory.createEmptyBorder(0, 0, 0, getAttachmentHeight());
                case SwingConstants.WEST:
                    return BorderFactory.createEmptyBorder(0, getAttachmentHeight(), 0, 0);
                case SwingConstants.NORTH:
                default:
                    return BorderFactory.createEmptyBorder(getAttachmentHeight(), 0, 0, 0);
            }
        } else {
            return BorderFactory.createEmptyBorder();
        }
    }

    @Override
    public Insets getBodyInsets(JTearawayDialog dialog) {
        return BODY_INSETS;
    }
    
    

    protected Shape getDialogShape(JTearawayDialog dialog, Rectangle bounds, Point attachedTo) {
        Shape baseRectangle = new RoundRectangle2D.Double(
                bounds.x,
                bounds.y + (attachedTo != null ? getAttachmentHeight() : 0),
                bounds.width - 1,
                bounds.height - (attachedTo != null ? getAttachmentHeight() : 0) - 1,
                getCornerDiameter(),
                getCornerDiameter()
        );

        Area combinedArea = new Area(baseRectangle);

        if (attachedTo != null) {
            Shape arrow = new RoundRectangle2D.Double(
                    0,
                    0,
                    getAttachmentHeight() * 2,
                    getAttachmentHeight() * 2,
                    getCornerDiameter() / 3,
                    getCornerDiameter() / 3
            );

            Area arrowArea = new Area(arrow);
            arrowArea.transform(AffineTransform.getRotateInstance(Math.PI / 4));
            arrowArea.transform(AffineTransform.getTranslateInstance(bounds.width / 2, 0));
            

            combinedArea.add(arrowArea);
        }

        return combinedArea;
    }

    @Override
    public void paintBackground(Graphics g, JTearawayDialog dialog, Rectangle bounds, Point attachedTo) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the background.
        g2.setColor(dialog.isFocused() ? getFocusedBackgroundColor() : getUnfocusedBackgroundColor());
        g2.fill(getDialogShape(dialog, bounds, attachedTo));

        // Force the shadow to revalidate on macOS.
        dialog.getRootPane().putClientProperty("apple.awt.windowShadow.revalidateNow", new Object());

        g2.dispose();
    }

    @Override
    public void paintBorder(Graphics g, JTearawayDialog dialog, Rectangle bounds, Point attachedTo) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint the border, fading slightly towards the bottom.
        g2.setPaint(new GradientPaint(0, 0, dialog.isFocused() ? getFocusedBorderTopColor() : getUnfocusedBorderTopColor(), 0, bounds.height, dialog.isFocused() ? getFocusedBorderBottomColor() : getUnfocusedBorderBottomColor()));
        g2.draw(getDialogShape(dialog, bounds, attachedTo));
        
        g2.dispose();
    }
}
