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
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.PRIMARY_LIGHT_COLOR_KEY;
import static com.patrickangle.commons.laf.modern.ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY;
import com.patrickangle.commons.util.Colors;
import com.patrickangle.commons.util.GraphicsHelpers;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import static javax.swing.SwingConstants.NORTH;
import static javax.swing.SwingConstants.SOUTH;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

/**
 *
 * @author patrickangle
 */
public class ModernSpinnerUI extends BasicSpinnerUI implements ModernShapedComponentUI {

    private final JSpinner spinner;

    private static final int CORNER_DIAMETER = 8;
    private static final int GENERAL_PADDING = 4;
    private static final int LEADING_PADDING = 8;

    private static final Insets BUTTON_INSETS = new Insets(GENERAL_PADDING, GENERAL_PADDING, GENERAL_PADDING, GENERAL_PADDING);

    private final MouseAdapter hoverListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            ModernSpinnerUI.this.comboBoxRollover = true;
            spinner.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ModernSpinnerUI.this.comboBoxRollover = false;
            spinner.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            spinner.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            spinner.repaint();
        }

    };

    private boolean comboBoxRollover = false;
    private boolean editorRollover = false;

    private boolean buttonNorthRollover = false;
    private boolean buttonSouthRollover = false;

    private boolean comboBoxPressed = false;
    private boolean editorPressed = false;

    private boolean buttonNorthPressed = false;
    private boolean buttonSouthPressed = false;

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new ModernSpinnerUI((JSpinner) c);
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
//        c.setMinimumSize(getMinimumSize(c));
//        c.setSize(getPreferredSize(c));
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        spinner.addMouseListener(hoverListener);
    }

    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        spinner.removeMouseListener(hoverListener);
    }

    public ModernSpinnerUI(JSpinner s) {
        this.spinner = s;
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

    protected Shape getArrowAreaShape(JComponent c, int side, boolean zeroOrigin) {
        final Insets buttonBorderInsets = c.getBorder().getBorderInsets(c);

        int height = c.getHeight() - buttonBorderInsets.top - buttonBorderInsets.bottom + BUTTON_INSETS.top + BUTTON_INSETS.bottom;
        int totalWidth = c.getWidth() - buttonBorderInsets.left - buttonBorderInsets.right + BUTTON_INSETS.left + BUTTON_INSETS.right;

        Shape buttonRect = new RoundRectangle2D.Double(
                zeroOrigin ? 0 : buttonBorderInsets.left - BUTTON_INSETS.left + totalWidth - height + 2, // +2 on x to offset from edge to prevent jagged edge
                zeroOrigin ? 0 : buttonBorderInsets.top - BUTTON_INSETS.top,
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

        switch (side) {
            case SwingConstants.NORTH:
                Shape southMask = new Rectangle2D.Double(
                        buttonRect.getBounds().x,
                        buttonRect.getBounds().y + (buttonRect.getBounds().height / 2),
                        buttonRect.getBounds().width,
                        buttonRect.getBounds().height / 2
                );
                Area southMasked = new Area(buttonRect);
                southMasked.subtract(new Area(southMask));
                buttonRect = southMasked;
                break;
            case SwingConstants.SOUTH:
                Shape northMask = new Rectangle2D.Double(
                        buttonRect.getBounds().x,
                        buttonRect.getBounds().y,
                        buttonRect.getBounds().width,
                        buttonRect.getBounds().height / 2
                );
                Area northMasked = new Area(buttonRect);
                northMasked.subtract(new Area(northMask));
                if (zeroOrigin) {
                    northMasked.transform(AffineTransform.getTranslateInstance(0, -buttonRect.getBounds().height / 2));
                }
                buttonRect = northMasked;
                break;
        }

        return buttonRect;
    }

    public void paint(Graphics graphics, JComponent component) {
//        final JSpinner comboBox = (JSpinner) component;

        final Graphics2D g = (Graphics2D) graphics.create();
        GraphicsHelpers.enableAntialiasing(g);
        GraphicsHelpers.enableStrokeNormalization(g);

        ModernUIComponentPainting.paintComponentShadowOrFocus(g, component, getShape(component));
        paintComponentBackgroundFill(g, spinner, getShape(component));
        paintComponentBorderHighlight(g, spinner, getShape(component));

        paintComponentArrowAreaBackgroundFill(g, spinner, getArrowAreaShape(component, NORTH, false), NORTH);
        paintComponentArrowAreaBorderHighlight(g, spinner, getArrowAreaShape(component, NORTH, false), NORTH);
        paintComponentDownArrow(g, component, getArrowAreaShape(component, NORTH, false), NORTH);

        paintComponentArrowAreaBackgroundFill(g, spinner, getArrowAreaShape(component, SOUTH, false), SOUTH);
        paintComponentArrowAreaBorderHighlight(g, spinner, getArrowAreaShape(component, SOUTH, false), SOUTH);
        paintComponentDownArrow(g, component, getArrowAreaShape(component, SOUTH, false), SOUTH);

        g.dispose();
    }

    public static void paintComponentDownArrow(Graphics2D g, Component component, Shape bounds, int direction) {
        g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        final int xU = bounds.getBounds().height / 3;
        final int yU = bounds.getBounds().height / 3;
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

        if (direction == NORTH) {
            path.transform(AffineTransform.getRotateInstance(Math.PI));
            path.transform(AffineTransform.getTranslateInstance(path.getBounds().width + 8, path.getBounds().height + 10));
        }

        protectedGraphics.translate(bounds.getBounds().x + 2, bounds.getBounds().y - 2);

        protectedGraphics.fill(path);

        protectedGraphics.dispose();
    }

    protected Rectangle rectangleForCurrentValue() {
        final Insets buttonBorderInsets = spinner.getBorder().getBorderInsets(spinner);
        int height = spinner.getHeight() - buttonBorderInsets.top - buttonBorderInsets.bottom + BUTTON_INSETS.top + BUTTON_INSETS.bottom;
        int totalWidth = spinner.getWidth() - buttonBorderInsets.left - buttonBorderInsets.right + BUTTON_INSETS.left + BUTTON_INSETS.right;

        return new Rectangle(buttonBorderInsets.left - BUTTON_INSETS.left + 2, buttonBorderInsets.top - BUTTON_INSETS.top + 2, totalWidth - height - 2, height - 4);
    }

    private void paintComponentBackgroundFill(Graphics2D g, JSpinner comboBox, Shape buttonShape) {
        if (comboBox.isEnabled()) {
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
        } else {
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
        }

        g.fill(buttonShape);
    }

    private void paintComponentArrowAreaBackgroundFill(Graphics2D g, JSpinner comboBox, Shape buttonShape, int side) {
        if (comboBox.isEnabled()) {
            if (comboBoxPressed || (side == NORTH) ? buttonNorthPressed : buttonSouthPressed || editorPressed) {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_DARK_COLOR_KEY));
            } else if (comboBoxRollover || (side == NORTH) ? buttonNorthRollover : buttonSouthRollover || editorRollover) {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_LIGHT_COLOR_KEY));
            } else {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_MEDIUM_COLOR_KEY));
            }
        } else {
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
        }

        g.fill(buttonShape);
    }

    private void paintComponentBorderHighlight(Graphics2D g, JSpinner comboBox, Shape buttonShape) {
        g.setStroke(new BasicStroke(0.5f));

        if (comboBox.isEnabled()) {
            if (comboBoxPressed || buttonNorthPressed || buttonSouthPressed || editorPressed) {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_MEDIUM_COLOR_KEY));
            } else if (comboBoxRollover || buttonNorthRollover || buttonSouthRollover || editorRollover) {
                g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_LIGHT_COLOR_KEY));
            } else {
                g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_COLOR_KEY));
            }
        } else {
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_DARK_COLOR_KEY));
        }

        g.draw(buttonShape);
    }

    private void paintComponentArrowAreaBorderHighlight(Graphics2D g, JSpinner comboBox, Shape buttonShape, int side) {
        g.setStroke(new BasicStroke(0.5f));

        if (comboBox.isEnabled()) {
            if (comboBoxPressed || (side == NORTH) ? buttonNorthPressed : buttonSouthPressed || editorPressed) {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_MEDIUM_COLOR_KEY));
            } else if (comboBoxRollover || (side == NORTH) ? buttonNorthRollover : buttonSouthRollover || editorRollover) {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_HIGHLIGHT_COLOR_KEY));
            } else {
                g.setColor(UIManager.getColor(ModernUIUtilities.ACCENT_LIGHT_COLOR_KEY));
            }
        } else {
            g.setColor(UIManager.getColor(ModernUIUtilities.PRIMARY_MEDIUM_DARK_COLOR_KEY));
        }

        g.draw(buttonShape);
    }

    @Override
    protected JComponent createEditor() {
        final JComponent comboBoxEditor = super.createEditor();

        comboBoxEditor.setOpaque(false);
        comboBoxEditor.setBackground(Colors.transparentColor());

        if (comboBoxEditor != null) {
            comboBoxEditor.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    process(e);
                }

                private void process(KeyEvent e) {
                    final int code = e.getKeyCode();
                    if ((code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) && e.getModifiers() == 0) {
                        spinner.dispatchEvent(e);
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    process(e);
                }
            });
            comboBoxEditor.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    spinner.revalidate();
                    spinner.repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    spinner.revalidate();
                    spinner.repaint();
                }
            });
            comboBoxEditor.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    editorRollover = true;
                    spinner.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    editorRollover = false;
                    spinner.repaint();
                }
            });
        }
        return comboBoxEditor;
    }

    private JButton previousButton = null;
    private JButton nextButton = null;

    @Override
    protected Component createPreviousButton() {
        if (previousButton == null) {
            previousButton = createArrowButton(SwingConstants.SOUTH);
            previousButton.setName("Spinner.previousButton");
            installPreviousButtonListeners(previousButton);
        }
        return previousButton;
    }

    @Override
    protected Component createNextButton() {
        if (nextButton == null) {
            nextButton = createArrowButton(SwingConstants.NORTH);
            nextButton.setName("Spinner.nextButton");
            installNextButtonListeners(nextButton);
        }
        return nextButton;
    }

    protected JButton createArrowButton(int side) {
        JButton button = new JButton() {
            @Override
            public void paint(Graphics g) {
            }

            @Override
            public Dimension getMinimumSize() {
                return new Dimension(14, 32);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(14, 32);
            }

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(14, 32);
            }
        };
        button.setBorder(new EmptyBorder(0, 0, 0, 0));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                switch (side) {
                    case NORTH:
                        buttonNorthRollover = true;
                        buttonSouthRollover = false;
                        break;
                    case SOUTH:
                        buttonSouthRollover = true;
                        buttonNorthRollover = false;
                        break;
                }
                spinner.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                switch (side) {
                    case NORTH:
                        buttonNorthRollover = false;
                        break;
                    case SOUTH:
                        buttonSouthRollover = false;
                        break;
                }
                spinner.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                switch (side) {
                    case NORTH:
                        buttonNorthPressed = true;
                        buttonSouthPressed = false;
                        break;
                    case SOUTH:
                        buttonSouthPressed = true;
                        buttonNorthPressed = false;
                        break;
                }
                spinner.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                switch (side) {
                    case NORTH:
                        buttonNorthPressed = false;
                        break;
                    case SOUTH:
                        buttonSouthPressed = false;
                        break;
                }
                spinner.repaint();
            }
        });
        button.setUI(new ModernButtonUI() {
            @Override
            public Shape getShape(JComponent c) {
                return getArrowAreaShape(spinner, side, true);

            }
        });
        button.putClientProperty(ModernButtonUI.JBUTTON_PAINTS_SHADOW_KEY, false);

        return button;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        try {
            return new Dimension(super.getPreferredSize(c).width, 28);
        } catch (Exception e) {
            return new Dimension(0, 28);
        }
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {

        try {
            return new Dimension(super.getMinimumSize(c).width, 28);
        } catch (Exception e) {
            return new Dimension(0, 28);
        }
    }

    @Override
    protected LayoutManager createLayout() {
        return new LayoutManagerDelegate(super.createLayout()) {
            @Override
            public void layoutContainer(Container parent) {
                super.layoutContainer(parent);
                final JComponent editor = spinner.getEditor();
                if (editor != null) {
                    editor.setBounds(rectangleForCurrentValue());
                }
                if (previousButton != null) {
                    previousButton.setBounds(getArrowAreaShape(spinner, SwingConstants.SOUTH, false).getBounds());
                }
                if (nextButton != null) {
                    nextButton.setBounds(getArrowAreaShape(spinner, SwingConstants.NORTH, false).getBounds());
                }
            }
        };
    }

    static class LayoutManagerDelegate implements LayoutManager {

        protected final LayoutManager myDelegate;

        LayoutManagerDelegate(LayoutManager delegate) {
            myDelegate = delegate;
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
            myDelegate.addLayoutComponent(name, comp);
        }

        @Override
        public void removeLayoutComponent(Component comp) {
            myDelegate.removeLayoutComponent(comp);
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return myDelegate.preferredLayoutSize(parent);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return myDelegate.minimumLayoutSize(parent);
        }

        @Override
        public void layoutContainer(Container parent) {
            myDelegate.layoutContainer(parent);
        }
    }

    public static Border getDefaultBorder() {
        return new ModernBasicBorder(new Insets(0, 0, 0, 0));
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("SpinnerUI", ModernSpinnerUI.class.getName());
        defaults.put("Spinner.border", ModernTextFieldUI.getDefaultBorder());

        defaults.put("Spinner.foreground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("Spinner.inactiveForeground", defaults.getColor(PRIMARY_MEDIUM_DARK_COLOR_KEY));
        defaults.put("Spinner.selectionBackground", defaults.getColor(ACCENT_HIGHLIGHT_COLOR_KEY));
        defaults.put("Spinner.selectionForeground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
    }
}
