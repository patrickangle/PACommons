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

import com.patrickangle.commons.laf.modern.ModernLookAndFeel;
import com.patrickangle.commons.laf.modern.ModernShapedComponentUI;
import com.patrickangle.commons.laf.modern.ModernUIUtilities;
import com.patrickangle.commons.laf.modern.borders.ModernComponentShadowFocusBorder;
import com.patrickangle.commons.laf.modern.icons.ModernCompactDoubleButtonArrowIcon;
import com.patrickangle.commons.laf.modern.util.GraphicsUtils;
import com.patrickangle.commons.laf.modern.util.ShapeUtils;
import com.patrickangle.commons.util.Colors;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 *
 * @author patrickangle
 */
public class ModernComboBoxUI extends BasicComboBoxUI implements ModernShapedComponentUI {

    protected static final Border DEFAULT_BORDER = new ModernComponentShadowFocusBorder();
    protected static final Border EMPTY_BORDER = new EmptyBorder(0, 0, 0, 0);

    private final JComboBox comboBox;

//    private static final int CORNER_DIAMETER = 8;
//    private static final int GENERAL_PADDING = 4;
//    private static final int LEADING_PADDING = 8;
//    private static final Insets BUTTON_INSETS = new Insets(GENERAL_PADDING, GENERAL_PADDING, GENERAL_PADDING, GENERAL_PADDING);
    private final FocusAdapter repaintFocusAdapter = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            comboBox.repaint();
        }

        @Override
        public void focusLost(FocusEvent e) {
            comboBox.repaint();
        }
    };

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
            public Dimension getPreferredSize() {
                return new Dimension(16,32);
            }
            
        };
        button.putClientProperty(ModernButtonUI.Style.Key, ModernButtonUI.Style.Attached);
        button.setBorder(EMPTY_BORDER);
        button.setIcon(new ModernCompactDoubleButtonArrowIcon());

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
        float width = c.getWidth() - c.getBorder().getBorderInsets(c).left - c.getBorder().getBorderInsets(c).right;
        float height = c.getHeight() - c.getBorder().getBorderInsets(c).top - c.getBorder().getBorderInsets(c).bottom;

        if (comboBox.isEditable()) {
            return ShapeUtils.roundedRectangle(0, 0, width, height, 3, List.of(ShapeUtils.Corner.TopRight, ShapeUtils.Corner.BottomRight));
        } else {
            return ShapeUtils.roundedRectangle(0, 0, width, height, 3);
        }
    }

    @Override
    public void paint(Graphics graphics, JComponent component) {
        final JComboBox comboBox = (JComboBox) component;
        final 

        Graphics2D g = GraphicsUtils.configureGraphics(graphics);

        if (!comboBox.isEditable()) {
            g.translate(comboBox.getBorder().getBorderInsets(comboBox).left, comboBox.getBorder().getBorderInsets(comboBox).top);
            g.setPaint(ModernLookAndFeel.colors.componentPaint(comboBox));
            g.fill(getShape(comboBox));
        } else {
            g.setStroke(new BasicStroke(0.5f));
            g.translate(component.getBorder().getBorderInsets(component).left, component.getBorder().getBorderInsets(component).top);
            if (component.isEnabled()) {
                g.setPaint(ModernLookAndFeel.colors.textAreaNormalBackgroundPaint(component));
            } else {
                g.setPaint(ModernLookAndFeel.colors.textAreaDisabledBackgroundPaint(component));
            }

            g.fill(rectangleForCurrentValue());

            if (component.isEnabled()) {
                g.setPaint(ModernLookAndFeel.colors.textAreaNormalBorderPaint(component));
            } else {
                g.setPaint(ModernLookAndFeel.colors.textAreaDisabledBorderPaint(component));
            }

            g.draw(rectangleForCurrentValue());

            if (component.isEnabled()) {
                g.setPaint(ModernLookAndFeel.colors.textAreaNormalBaselinePaint(component));
            } else {
                g.setPaint(ModernLookAndFeel.colors.textAreaDisabledBaselinePaint(component));
            }
            g.clip(rectangleForCurrentValue());
            g.drawLine(rectangleForCurrentValue().getBounds().x, rectangleForCurrentValue().getBounds().height, rectangleForCurrentValue().getBounds().width, rectangleForCurrentValue().getBounds().height);
        }

        g.dispose();

        paintCurrentValue(graphics, rectangleForCurrentValue(), hasFocus);
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

    public static Border getDefaultBorder() {
        return DEFAULT_BORDER;
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ComboBoxUI", ModernComboBoxUI.class.getName());
        defaults.put("ComboBox.border", ModernComboBoxUI.getDefaultBorder());
    }

}
