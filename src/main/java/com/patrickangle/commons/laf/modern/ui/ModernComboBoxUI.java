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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 *
 * @author patrickangle
 */
public class ModernComboBoxUI extends BasicComboBoxUI implements ModernShapedComponentUI {

    private final JComboBox comboBox;

    private static final int CORNER_DIAMETER = 8;
    private static final int GENERAL_PADDING = 4;
    private static final int LEADING_PADDING = 12;

    private static final Insets BUTTON_INSETS = new Insets(GENERAL_PADDING, GENERAL_PADDING + LEADING_PADDING, GENERAL_PADDING, GENERAL_PADDING);

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

    }

    @Override
    protected JButton createArrowButton() {
        JButton button = new JButton("⌃");
        button.putClientProperty(ModernButtonUI.JBUTTON_SEGMENT_POSITION_KEY, ModernButtonUI.JBUTTON_SEGMENT_POSITION_LAST_VALUE);
        button.setUI(new ModernButtonUI() {            
            @Override
            public Shape getShape(JComponent c) {
                Shape buttonRect = new RoundRectangle2D.Double(
                        0,
                        0,
                        c.getWidth() + GENERAL_PADDING,
                        c.getHeight() + (GENERAL_PADDING * 2),
                        CORNER_DIAMETER,
                        CORNER_DIAMETER);
                Shape squareLeftSide = new Rectangle2D.Double(
                        0,
                        0,
                        (CORNER_DIAMETER / 2) + 1,
                        c.getHeight() + (GENERAL_PADDING * 2)
                );
                Area buttonArea = new Area(buttonRect);
                Area squareLeftSideArea = new Area(squareLeftSide);
                buttonArea.add(squareLeftSideArea);
                
                return buttonArea;
            }

            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(comboBox.getWidth() + GENERAL_PADDING, comboBox.getHeight() + (GENERAL_PADDING * 2));
            }
            
            
        });
        button.setBorder(new EmptyBorder(0, 0, 0, 0));

        return button; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Shape getShape(JComponent c) {
//        final Insets buttonBorderInsets = c.getBorder().getBorderInsets(c);

        Shape buttonRect = new RoundRectangle2D.Double(
                0,
                0,
                c.getWidth(),//  + BUTTON_INSETS.left + BUTTON_INSETS.right,
                c.getHeight(),//  + BUTTON_INSETS.top + BUTTON_INSETS.bottom,
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
    protected Insets getInsets() {
        return BUTTON_INSETS;
    }
    
    

    public static ModernBasicBorder getDefaultBorder() {
        return new ModernBasicBorder(new Insets(0, 0, 0, 0));
    }

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ComboBoxUI", ModernComboBoxUI.class.getName());
        defaults.put("ComboBox.border", ModernComboBoxUI.getDefaultBorder());
    }

}
