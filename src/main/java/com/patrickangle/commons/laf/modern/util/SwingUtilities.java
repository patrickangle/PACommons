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
package com.patrickangle.commons.laf.modern.util;

import com.patrickangle.commons.laf.modern.ui.ModernSpinnerUI;
import java.awt.Component;
import java.awt.Window;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;

/**
 *
 * @author patrickangle
 */
public class SwingUtilities {

    /**
     * Returns true if the provided button is the default button or is otherwise
     * currently selected.
     *
     * @param button
     * @return
     */
    public static boolean buttonIsDefaultOrSelected(AbstractButton button) {
        return (button instanceof JButton && ((JButton) button).isDefaultButton()) || (button instanceof JToggleButton && button.isSelected());
    }
    
    public static boolean spinnerIsFocusedOrSelected(JSpinner spinner) {
        final JComponent editor = SwingUtilities.findComponentOfType(spinner, JFormattedTextField.class);
        final JComponent nextButton = SwingUtilities.findComponentWithName(spinner, ModernSpinnerUI.COMPONENT_NAME_NEXT_BUTTON);
        final JComponent previousButton = SwingUtilities.findComponentWithName(spinner, ModernSpinnerUI.COMPONENT_NAME_PREVIOUS_BUTTON);

        boolean editorFocused = editor != null && editor.isFocusOwner();
        boolean nextFocused = nextButton != null && nextButton.isFocusOwner();
        boolean nextSelected = nextButton != null && nextButton instanceof AbstractButton && SwingUtilities.buttonIsDefaultOrSelected((AbstractButton) nextButton);
        boolean previousFocused = previousButton != null && previousButton.isFocusOwner();
        boolean previousSelected = previousButton != null && previousButton instanceof AbstractButton && SwingUtilities.buttonIsDefaultOrSelected((AbstractButton) previousButton);
        
        return spinner.isEnabled() && spinner.isVisible() && (editorFocused || nextFocused || nextSelected || previousFocused || previousSelected);
    }

    /**
     * Returns true if the provided component is currently enabled, and the
     * window that contains the component is currently the focused window. If
     * the component does not belong to a window this returns true if the
     * component is enabled, forgoing the focus check.
     *
     * @param c
     * @return
     */
    public static boolean componentIsEnabledAndWindowInFocus(Component c) {
        return c.isEnabled() && (windowAncestor(c) == null || windowAncestor(c).isFocused());
    }

    /**
     * Returns the first Window ancestor of c, or null if c is not contained
     * inside a Window.
     *
     * @param c Component to get Window ancestor of.
     * @return the first Window ancestor of c, or null if c is not contained
     * inside a Window.
     */
    public static Window windowAncestor(Component c) {
        return javax.swing.SwingUtilities.getWindowAncestor(c);
    }

    public static <T extends JComponent> T findComponentOfType(JComponent parent, Class<T> cls) {
        if (parent == null || cls.isAssignableFrom(parent.getClass())) {
            @SuppressWarnings({"unchecked"})
            final T t = (T) parent;
            return t;
        }
        for (Component component : parent.getComponents()) {
            if (component instanceof JComponent) {
                T comp = findComponentOfType((JComponent) component, cls);
                if (comp != null) {
                    return comp;
                }
            }
        }
        return null;
    }
    
    public static JComponent findComponentWithName(JComponent parent, String name) {
        if (parent == null || (parent.getName() != null && parent.getName().equals(name))) {
            @SuppressWarnings({"unchecked"})
            final JComponent t = (JComponent) parent;
            return t;
        }
        for (Component component : parent.getComponents()) {
            if (component instanceof JComponent) {
                JComponent comp = findComponentWithName((JComponent) component, name);
                if (comp != null) {
                    return comp;
                }
            }
        }
        return null;
    }


    public static <T> T getParentOfType(Class<? extends T> cls, Component c) {
        Component eachParent = c;
        while (eachParent != null) {
            if (cls.isAssignableFrom(eachParent.getClass())) {
                @SuppressWarnings({"unchecked"})
                final T t = (T) eachParent;
                return t;
            }

            eachParent = eachParent.getParent();
        }

        return null;
    }
}
