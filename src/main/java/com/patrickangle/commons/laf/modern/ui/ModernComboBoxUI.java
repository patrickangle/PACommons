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
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 *
 * @author patrickangle
 */
public class ModernComboBoxUI extends BasicComboBoxUI {
    private final JComboBox comboBox;

    private static final int CORNER_DIAMETER = 8;
    private static final int GENERAL_PADDING = 4;
    private static final int LEADING_AND_TRAILING_PADDING = 12;

    private static final Insets BUTTON_INSETS = new Insets(GENERAL_PADDING, GENERAL_PADDING + LEADING_AND_TRAILING_PADDING, GENERAL_PADDING, GENERAL_PADDING + LEADING_AND_TRAILING_PADDING);

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
        button.setUI(new ModernButtonUI() {
            @Override
            public void paint(Graphics graphics, JComponent component) {
                final Insets buttonBorderInsets = component.getBorder().getBorderInsets(component);

                Shape buttonRect = new RoundRectangle2D.Double(
                        0,
                        0,
                        component.getWidth(),
                        component.getHeight(),
                        CORNER_DIAMETER,
                        CORNER_DIAMETER);
                Shape squareLeftSide = new Rectangle2D.Double(
                        0,
                        0,
                        (component.getWidth()) / 2,
                        component.getHeight()
                );
                Area buttonArea = new Area(buttonRect);
                Area squareLeftSideArea = new Area(squareLeftSide);
                buttonArea.add(squareLeftSideArea);

                paintShape(graphics, component, buttonArea);
            }
        });
        return button; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    public static ModernBasicBorder getDefaultBorder() {
        return new ModernBasicBorder(BUTTON_INSETS);
    }
    
    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("ComboBoxUI", ModernComboBoxUI.class.getName());
//        defaults.put("ComboBox.border", ModernComboBoxUI.getDefaultBorder());
    }

}
