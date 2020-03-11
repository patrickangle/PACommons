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
import com.patrickangle.commons.laf.modern.borders.ModernComponentShadowFocusBorder;
import com.patrickangle.commons.laf.modern.icons.ModernCompactButtonArrowIcon;
import com.patrickangle.commons.laf.modern.util.ShapeUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

/**
 *
 * @author patrickangle
 */
public class ModernSpinnerUI extends BasicSpinnerUI implements ModernShapedComponentUI {
    public static final String COMPONENT_NAME_NEXT_BUTTON = "Spinner.nextButton";
    public static final String COMPONENT_NAME_PREVIOUS_BUTTON = "Spinner.previousButton";

    protected static final Border DEFAULT_BORDER = new ModernComponentShadowFocusBorder();
    protected static final Border EMPTY_BORDER = new EmptyBorder(0, 0, 0, 0);

    private final JSpinner spinner;
    private final ModernSpinnerScrollAdapter scrollAdapter;


    private final FocusAdapter repaintFocusAdapter = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            spinner.repaint();
        }

        @Override
        public void focusLost(FocusEvent e) {
            spinner.repaint();
        }
    };


    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new ModernSpinnerUI((JSpinner) c);
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        spinner.addMouseWheelListener(scrollAdapter);
    }

    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        spinner.removeMouseWheelListener(scrollAdapter);
    }

    public ModernSpinnerUI(JSpinner s) {
        this.spinner = s;
        this.scrollAdapter = new ModernSpinnerScrollAdapter(s);
    }

    @Override
    public Shape getShape(JComponent c) {
        float width = c.getWidth() - c.getBorder().getBorderInsets(c).left - c.getBorder().getBorderInsets(c).right;
        float height = c.getHeight() - c.getBorder().getBorderInsets(c).top - c.getBorder().getBorderInsets(c).bottom;

        return ShapeUtils.roundedRectangle(0, 0, width, height, 3, List.of(ShapeUtils.Corner.TopRight, ShapeUtils.Corner.BottomRight));
    }

    @Override
    protected void replaceEditor(JComponent oldEditor, JComponent newEditor) {
        super.replaceEditor(oldEditor, newEditor);
        if (oldEditor != null && oldEditor.getComponents().length > 0) {
            oldEditor.getComponents()[0].removeFocusListener(repaintFocusAdapter);
        }
        if (newEditor != null && newEditor.getComponents().length > 0) {
            newEditor.getComponents()[0].addFocusListener(repaintFocusAdapter);
            ((JComponent) newEditor.getComponents()[0]).setBorder(EMPTY_BORDER);
        }
    }

    @Override
    protected JComponent createEditor() {
        final JComponent editor = super.createEditor();
        editor.getComponents()[0].addFocusListener(repaintFocusAdapter);
        ((JComponent) editor.getComponents()[0]).setBorder(EMPTY_BORDER);
        return editor;
    }

    private JButton previousButton = null;
    private JButton nextButton = null;

    @Override
    protected Component createPreviousButton() {
        if (previousButton == null) {
            previousButton = createArrowButton(ModernButtonUI.Segment.Last);
            previousButton.setIcon(new ModernCompactButtonArrowIcon(false));
            previousButton.setName(COMPONENT_NAME_PREVIOUS_BUTTON);
            installPreviousButtonListeners(previousButton);
        }
        return previousButton;
    }

    @Override
    protected Component createNextButton() {
        if (nextButton == null) {
            nextButton = createArrowButton(ModernButtonUI.Segment.First);
            nextButton.setIcon(new ModernCompactButtonArrowIcon(true));
            nextButton.setName(COMPONENT_NAME_NEXT_BUTTON);
            installNextButtonListeners(nextButton);
        }
        return nextButton;
    }

    protected JButton createArrowButton(ModernButtonUI.Segment segment) {
        JButton button = new JButton() {
            // Without this, the buttons end up without a size. This size is not exactly correct, but seems to be ignored in the end anyways.
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(16, 8);
            }
        };
        button.setBorder(new EmptyBorder(0, 0, 0, 0));

        button.putClientProperty(ModernButtonUI.Style.Key, ModernButtonUI.Style.Attached);
        button.putClientProperty(ModernButtonUI.Segment.Key, segment);

        return button;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        try {
            return new Dimension(super.getPreferredSize(c).width, 28);
        } catch (Exception e) {
            return new Dimension(128, 28);
        }
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {

        try {
            return new Dimension(super.getMinimumSize(c).width, 28);
        } catch (Exception e) {
            return new Dimension(64, 28);
        }
    }

    public static Border getDefaultBorder() {
        return DEFAULT_BORDER;
    }
    
    

    public static void installIntoDefaults(UIDefaults defaults) {
        defaults.put("SpinnerUI", ModernSpinnerUI.class.getName());
        defaults.put("Spinner.border", getDefaultBorder());
        defaults.put("Spinner.editorBorderPainted", Boolean.FALSE);
        defaults.put("Spinner.arrowButtonInsets", new Insets(3, 0, 3, 3));
    }
}
