/*
 * Patrick Angle Commons Library
 * Copyright 2018 Patrick Angle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrickangle.commons.objectediting.util;

import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.BasicBoundField;
import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.beansbinding.swing.JTextComponentBinding;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.objectediting.annotations.ObjectEditingProperty;
import com.patrickangle.commons.objectediting.interfaces.CustomObjectEditingComponent;
import com.patrickangle.commons.util.Annotations;
import com.patrickangle.commons.util.Classes;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author patrickangle
 */
public class ObjectFieldEditorFactory {
    public static final class ComponentReturn {
        public JComponent component;
        public boolean selfLabeled;

        public ComponentReturn(JComponent component, boolean selfLabeled) {
            this.component = component;
            this.selfLabeled = selfLabeled;
        }

        public JComponent getComponent() {
            return component;
        }

        public boolean isSelfLabeled() {
            return selfLabeled;
        }
    }
    
    public static ComponentReturn createEditorForObject(Object containingObject, BindableField bindableField, BindingGroup bindingGroup) {
        BoundField objectField = new BasicBoundField(containingObject, bindableField);
        Class fieldClass = Classes.primitaveClassFor(bindableField.getFieldClass());
        System.out.println(fieldClass.getName());
        if (CustomObjectEditingComponent.class.isAssignableFrom(objectField.getFieldClass())) {
            // If a custom editor is available for a class type, it will always be favored over all default editors.
            return ((CustomObjectEditingComponent) containingObject).getCustomObjectEditingComponent(bindingGroup);
        } else if (fieldClass == Boolean.TYPE) {
            return createBoundComponentForBoolean(objectField, bindingGroup);
        } else if (fieldClass == Integer.TYPE) {
            return createBoundComponentForFixedPointNumber(objectField, bindingGroup);
        } else if (fieldClass == Float.TYPE || fieldClass == Double.TYPE) {
            return createBoundComponentForFloatingPointNumber(objectField, bindingGroup);
        } else if (Color.class.isAssignableFrom(objectField.getFieldClass())) {
            return createBoundComponentForColor(objectField, bindingGroup);
        } else {
            return createBoundComponentForString(objectField, bindingGroup);
        }
    }
    
    private static ComponentReturn createBoundComponentForBoolean(BoundField objectField, BindingGroup bindingGroup) {
        JCheckBox checkBox = new JCheckBox(ObjectEditingBindings.nameForBindableField(objectField.getBindableField()));
        
        BasicBinding binding = new BasicBinding(objectField, new BasicBoundField(checkBox, "selected"), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(binding);
        
        return new ComponentReturn(checkBox, true);
    }
    
    private static ComponentReturn createBoundComponentForFixedPointNumber(BoundField objectField, BindingGroup bindingGroup) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(objectField.getBindableField()), ObjectEditingProperty.class);
        
        
        switch (configInfo.numberEditor()) {
            case TEXT_CONTROL:
            case SLIDER_CONTROL:
            case SPINNER_CONTROL:
            default:
                JSpinner spinner = new JSpinner(new SpinnerNumberModel(
                        (int)configInfo.numberMinimumValue(),
                        (int)configInfo.numberMinimumValue(),
                        (int)configInfo.numberMaximumValue(),
                        (int)configInfo.numberStepValue()
                ));
                JSpinner.NumberEditor spinnerNumberEditor = new JSpinner.NumberEditor(spinner);
                spinnerNumberEditor.getFormat().setGroupingUsed(false);
                spinner.setEditor(spinnerNumberEditor);
                
                BasicBinding binding = new BasicBinding(objectField, new BasicBoundField(spinner, "value"), Binding.UpdateStrategy.READ_WRITE);
                bindingGroup.add(binding);
                
                return new ComponentReturn(spinner, false);
        }
    }
    
    private static ComponentReturn createBoundComponentForFloatingPointNumber(BoundField objectField, BindingGroup bindingGroup) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(objectField.getBindableField()), ObjectEditingProperty.class);
        
        
        switch (configInfo.numberEditor()) {
            case TEXT_CONTROL:
                return createBoundComponentForString(objectField, bindingGroup);
            case SLIDER_CONTROL:
            case SPINNER_CONTROL:
            default:
                JSpinner spinner = new JSpinner(new SpinnerNumberModel(
                        (double)configInfo.numberMinimumValue(),
                        (double)configInfo.numberMinimumValue(),
                        (double)configInfo.numberMaximumValue(),
                        (double)configInfo.numberStepValue()
                ));
                JSpinner.NumberEditor spinnerNumberEditor = new JSpinner.NumberEditor(spinner);
                spinnerNumberEditor.getFormat().setGroupingUsed(false);
                spinnerNumberEditor.getFormat().setDecimalSeparatorAlwaysShown(true);
                spinner.setEditor(spinnerNumberEditor);
                
                BasicBinding binding = new BasicBinding(objectField, new BasicBoundField(spinner, "value"), Binding.UpdateStrategy.READ_WRITE);
                bindingGroup.add(binding);
                
                return new ComponentReturn(spinner, false);
        }
    }
    
    private static ComponentReturn createBoundComponentForString(BoundField objectField, BindingGroup bindingGroup) {
        JTextField textField = new JTextField();
        
        BasicBinding binding = new JTextComponentBinding(objectField, new BasicBoundField(textField, JTextComponentBinding.SYNTHETIC_FIELD_TEXT), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(binding);

        return new ComponentReturn(textField, false);
    }
    
    private static ComponentReturn createBoundComponentForColor(BoundField objectField, BindingGroup bindingGroup) {
        JButton colorButton = new JButton();
        
        colorButton.addActionListener((event) -> {
            Color newColor = JColorChooser.showDialog(null, ObjectEditingBindings.nameForBindableField(objectField.getBindableField()), colorButton.getBackground());
            
            if (newColor != null) {
                colorButton.setBackground(newColor);
            }
        });
        
        BasicBinding binding = new BasicBinding(objectField, new BasicBoundField(colorButton, "background"), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(binding);
        
        return new ComponentReturn(colorButton, false);
    }
}
