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

import com.patrickangle.commons.laf.modern.ModernShapedComponentUI;
import com.patrickangle.commons.laf.modern.ModernUIComponentPainting;
import com.patrickangle.commons.laf.modern.ModernUIUtilities;
import com.patrickangle.commons.util.Colors;
import com.patrickangle.commons.util.GraphicsHelpers;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 *
 * @author patrickangle
 */
public class ModernComboBoxUI extends BasicComboBoxUI implements ModernShapedComponentUI {

    private final JComboBox comboBox;

    private static final int CORNER_DIAMETER = 8;
    private static final int GENERAL_PADDING = 4;
    private static final int LEADING_PADDING = 8;

    private static final Insets BUTTON_INSETS = new Insets(GENERAL_PADDING, GENERAL_PADDING, GENERAL_PADDING, GENERAL_PADDING);

    private final MouseAdapter hoverListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            ModernComboBoxUI.this.comboBoxRollover = true;
            comboBox.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ModernComboBoxUI.this.comboBoxRollover = false;
            comboBox.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            comboBox.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            comboBox.repaint();
        }

    };

    private boolean comboBoxRollover = false;
    private boolean editorRollover = false;
    private boolean buttonRollover = false;

    private boolean comboBoxPressed = false;
    private boolean editorPressed = false;
    private boolean buttonPressed = false;

    public ModernComboBoxUI(JComboBox c) {
        this.comboBox = c;
    }

    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static ComponentUI createUI(JComponent c) {
        return new ModernComboBoxUI((JComboBox) c);
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
        c.setMinimumSize(getMinimumSize(c));
        c.setSize(getPreferredSize(c));
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        comboBox.addMouseListener(hoverListener);
    }

    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        comboBox.removeMouseListener(hoverListener);
    }

    @Override
    protected JButton createArrowButton() {
        JButton button = new JButton() {
            @Override
            public void paint(Graphics g) {
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, 0);
            }

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(0, 0);
            }
        };
        button.setBorder(new EmptyBorder(0, 0, 0, 0));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonRollover = true;
                comboBox.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonRollover = false;
                comboBox.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                buttonPressed = true;
                comboBox.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                buttonPressed = false;
                comboBox.repaint();
            }
        });

        return button;
    }

    @Override
    public Shape getShape(JComponent c) {

        final Insets buttonBorderInsets = c.getBorder().getBorderInsets(c);

        Shape buttonRect = new RoundRectangle2D.Double(
                buttonBorderInsets.left - BUTTON_INSETS.left,
                buttonBorderInsets.top - BUTTON_INSETS.top,
                c.getWidth() - buttonBorderInsets.left - buttonBorderInsets.right + BUTTON_INSETS.left + BUTTON_INSETS.right,
                c.getHeight() - buttonBorderInsets.top - buttonBorderInsets.bottom + BUTTON_INSETS.top + BUTTON_INSETS.bottom,
                CORNER_DIAMETER,
                CORNER_DIAMETER);

        if (comboBox.isEditable()) {
            Shape addonRect3 = new Rectangle2D.Double(
                    buttonRect.getBounds().x,
                    buttonRect.getBounds().y,
                    buttonRect.getBounds().width / 2,
                    buttonRect.getBounds().height);

            Area compositeShape3 = new Area(buttonRect);
            compositeShape3.add(new Area(addonRect3));

            buttonRect = compositeShape3;
        }

        return buttonRect;
    }

    protected Shape getArrowAreaShape(JComponent c) {
        final Insets buttonBorderInsets = c.getBorder().getBorderInsets(c);

        int height = c.getHeight() - buttonBorderInsets.top - buttonBorderInsets.bottom + BUTTON_INSETS.top + BUTTON_INSETS.bottom;
        int totalWidth = c.getWidth() - buttonBorderInsets.left - buttonBorderInsets.right + BUTTON_INSETS.left + BUTTON_INSETS.right;

        Shape buttonRect = new RoundRectangle2D.Double(
                buttonBorderInsets.left - BUTTON_INSETS.left + totalWidth - height + 2, // +2 on x to offset from edge to prevent jagged edge
                buttonBorderInsets.top - BUTTON_INSETS.top,
                height - 2, // Offsets the +2 on the x.
                height,
                CORNER_DIAMETER,
                CORNER_DIAMETER);

        Shape addonRect3 = new Rectangle2D.Double(
                buttonRect.getBounds().x,
                buttonRect.getBounds().y,
                buttonRect.getBounds().width / 2,
                buttonRect.getBounds().height);

        Area compositeShape3 = new Area(buttonRect);
        compositeShape3.add(new Area(addonRect3));

        buttonRect = compositeShape3;

        return buttonRect;
    }

    @Override
    public void paint(Graphics graphics, JComponent component) {
        final JComboBox comboBox = (JComboBox) component;

        final Graphics2D g = (Graphics2D) graphics.create();
        GraphicsHelpers.enableAntialiasing(g);
        GraphicsHelpers.enableStrokeNormalization(g);

        ModernUIComponentPainting.paintComponentShadowOrFocus(g, component, getShape(component));
        paintComponentBackgroundFill(g, comboBox, getShape(component));
        paintComponentBorderHighlight(g, comboBox, getShape(component));

        paintComponentArrowAreaBackgroundFill(g, comboBox, getArrowAreaShape(component));
        paintComponentArrowAreaBorderHighlight(g, comboBox, getArrowAreaShape(component));

//        ModernUIComponentPainting.paintComponentDownArrow(g, editor);
        paintComponentDownArrow(g, component, getArrowAreaShape(component));

        g.dispose();

        paintCurrentValue(graphics, rectangleForCurrentValue(), hasFocus);
    }

    public static void paintComponentDownArrow(Graphics2D g, Component component, Shape bounds) {
        g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        final int xU = bounds.getBounds().width / 5;
        final int yU = bounds.getBounds().width / 5;
        final Path2D.Double path = new Path2D.Double();
        path.moveTo(xU + 1, yU + 2);
        path.lineTo(3 * xU + 1, yU + 2);
        path.lineTo(2 * xU + 1, 3 * yU);
        path.lineTo(xU + 1, yU + 2);
        path.closePath();

        if (component.isEnabled()) {
            // Enabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));
        } else {
            // Disabled
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
        }

        Graphics2D protectedGraphics = (Graphics2D) g.create();

        protectedGraphics.translate(bounds.getBounds().x + 2, bounds.getBounds().y + 4);
        protectedGraphics.fill(path);

        protectedGraphics.dispose();
    }

    @Override
    protected Rectangle rectangleForCurrentValue() {
        final Insets buttonBorderInsets = comboBox.getBorder().getBorderInsets(comboBox);
        int height = comboBox.getHeight() - buttonBorderInsets.top - buttonBorderInsets.bottom + BUTTON_INSETS.top + BUTTON_INSETS.bottom;
        int totalWidth = comboBox.getWidth() - buttonBorderInsets.left - buttonBorderInsets.right + BUTTON_INSETS.left + BUTTON_INSETS.right;

        return new Rectangle(buttonBorderInsets.left - BUTTON_INSETS.left + 2, buttonBorderInsets.top - BUTTON_INSETS.top + 2, totalWidth - height - 2, height - 4);
    }

    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        ListCellRenderer renderer = comboBox.getRenderer();
        Component c;

        c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
        c.setBackground(Colors.transparentColor());
        c.setForeground(UIManager.getColor(ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY));

        c.setFont(comboBox.getFont());

        currentValuePane.paintComponent(g, c, comboBox, bounds.x, bounds.y, bounds.width, bounds.height, c instanceof JPanel);
    }

    private void paintComponentBackgroundFill(Graphics2D g, JComboBox comboBox, Shape buttonShape) {
        if (comboBox.isEnabled()) {
            if (comboBox.isEditable()) {
                g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
            } else {
                if (comboBox.isPopupVisible() || comboBoxPressed || buttonPressed || editorPressed) {
                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
                } else if (comboBoxRollover || buttonRollover || editorRollover) {
                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY));
                } else {
                    g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
                }
            }
        } else {
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
        }

        g.fill(buttonShape);
    }

    private void paintComponentArrowAreaBackgroundFill(Graphics2D g, JComboBox comboBox, Shape buttonShape) {
        if (comboBox.isEnabled()) {
            if (comboBox.isPopupVisible() || comboBoxPressed || buttonPressed || editorPressed) {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_DARK_COLOR_KEY));
            } else if (comboBoxRollover || buttonRollover || editorRollover) {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_LIGHT_COLOR_KEY));
            } else {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_MEDIUM_COLOR_KEY));
            }
        } else {
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
        }

        g.fill(buttonShape);
    }

    private void paintComponentBorderHighlight(Graphics2D g, JComboBox comboBox, Shape buttonShape) {
        g.setStroke(new BasicStroke(0.5f));

        if (comboBox.isEnabled()) {
            if (comboBox.isPopupVisible() || comboBoxPressed || buttonPressed || editorPressed) {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_MEDIUM_COLOR_KEY));
            } else if (comboBoxRollover || buttonRollover || editorRollover) {
                g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_LIGHT_COLOR_KEY));
            } else {
                g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY));
            }
        } else {
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
        }

        g.draw(buttonShape);
    }

    private void paintComponentArrowAreaBorderHighlight(Graphics2D g, JComboBox comboBox, Shape buttonShape) {
        g.setStroke(new BasicStroke(0.5f));

        if (comboBox.isEnabled()) {
            if (comboBox.isPopupVisible() || comboBoxPressed || buttonPressed || editorPressed) {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_MEDIUM_COLOR_KEY));
            } else if (comboBoxRollover || buttonRollover || editorRollover) {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY));
            } else {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_LIGHT_COLOR_KEY));
            }
        } else {
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
        }

        g.draw(buttonShape);
    }

    protected ComboBoxEditor createEditor() {
        final ComboBoxEditor comboBoxEditor = super.createEditor();
        if (comboBoxEditor != null && comboBoxEditor.getEditorComponent() != null) {
            comboBoxEditor.getEditorComponent().addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    process(e);
                }

                private void process(KeyEvent e) {
                    final int code = e.getKeyCode();
                    if ((code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) && e.getModifiers() == 0) {
                        comboBox.dispatchEvent(e);
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    process(e);
                }
            });
            comboBoxEditor.getEditorComponent().addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    comboBox.revalidate();
                    comboBox.repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    comboBox.revalidate();
                    comboBox.repaint();
                }
            });
            comboBoxEditor.getEditorComponent().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    editorRollover = true;
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    editorRollover = false;
                }
            });
        }
        return comboBoxEditor;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(super.getPreferredSize(c).width, 28);
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        return new Dimension(super.getMinimumSize(c).width, 28);
    }
    
    
    
    public static ModernBasicBorder getDefaultBorder() {
        return new ModernBasicBorder(BUTTON_INSETS);
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ComboBoxUI", ModernComboBoxUI.class.getName());
        defaults.put("ComboBox.border", ModernComboBoxUI.getDefaultBorder());
    }

}
