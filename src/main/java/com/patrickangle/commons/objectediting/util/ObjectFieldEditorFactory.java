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
import com.patrickangle.commons.beansbinding.BoundFields;
import com.patrickangle.commons.beansbinding.FlipBooleanBindingConverter;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.AbstractButtonBoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.JSpinnerBoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.JTextComponentBoundField;
import com.patrickangle.commons.beansbinding.swing.models.ObservableComboBoxModel;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.objectediting.annotations.ObjectEditingProperty;
import com.patrickangle.commons.objectediting.editors.Point2dObjectEditor;
import com.patrickangle.commons.objectediting.editors.list.ListObjectEditor;
import com.patrickangle.commons.objectediting.interfaces.CustomObjectEditingComponent;
import com.patrickangle.commons.objectediting.util.list2deditor.ObjectEditingList2dTableEditorDialog;
import com.patrickangle.commons.util.Annotations;
import com.patrickangle.commons.util.Classes;
import com.patrickangle.commons.util.Lists;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.undo.UndoManager;

/**
 *
 * @author patrickangle
 */
public class ObjectFieldEditorFactory {

    public static final class ComponentReturn {

        public JComponent component;
        public boolean selfLabeled;
        public boolean multiLineEditor;

        public ComponentReturn(JComponent component, boolean selfLabeled) {
            this.component = component;
            this.selfLabeled = selfLabeled;
        }

        public ComponentReturn(JComponent component, boolean selfLabeled, boolean multiLineEditor) {
            this.component = component;
            this.selfLabeled = selfLabeled;
            this.multiLineEditor = multiLineEditor;
        }

        public JComponent getComponent() {
            return component;
        }

        public boolean isSelfLabeled() {
            return selfLabeled;
        }

        public boolean isMultiLineEditor() {
            return multiLineEditor;
        }
    }

    public static ComponentReturn createEditorForObject(Object containingObject, BindableField bindableField, BindingGroup bindingGroup, UndoManager undoManager) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(bindableField), ObjectEditingProperty.class);

        BoundField objectField = BoundFields.boundField(containingObject, bindableField);
        Class fieldClass = Classes.primitaveClassFor(bindableField.getFieldClass());
        
        // If undo is disabled for this editor, pass null as the UndoManager.
        UndoManager realizedUndoManager = configInfo.prohibitsUndo() ? null : undoManager;
        
        ComponentReturn componentReturn;

        if (!configInfo.mutable()) {
            componentReturn = createBoundComponentForNonMutableObject(objectField, bindingGroup);
        } else if (CustomObjectEditingComponent.class.isAssignableFrom(fieldClass)) {
            // If a custom editor is available for a class type, it will always be favored over all default editors.
            componentReturn = ((CustomObjectEditingComponent) objectField.getValue()).customObjectEditingComponent(bindingGroup, realizedUndoManager);
        } else if (Enum.class.isAssignableFrom(fieldClass)) {
            componentReturn = createBoundComponentForEnum(objectField, bindingGroup);
        } else if (fieldClass == Boolean.TYPE) {
            componentReturn = createBoundComponentForBoolean(objectField, bindingGroup);
        } else if (fieldClass == Integer.TYPE) {
            componentReturn = createBoundComponentForFixedPointNumber(objectField, bindingGroup);
        } else if (fieldClass == Float.TYPE || fieldClass == Double.TYPE) {
            componentReturn = createBoundComponentForFloatingPointNumber(objectField, bindingGroup, realizedUndoManager);
        } else if (Color.class.isAssignableFrom(objectField.getFieldClass())) {
            componentReturn = createBoundComponentForColor(objectField, bindingGroup);
        } else if (Point2D.class.isAssignableFrom(objectField.getFieldClass())) {
            componentReturn = Point2dObjectEditor.createBoundComponentForPoint2D(objectField, bindingGroup);
        } else if (List.class.isAssignableFrom(objectField.getFieldClass())) {
            int depth = Lists.depthOfMultiDimensionalList((List) objectField.getValue());
            switch (depth) {
                case 0:
                // Zero is returned if the list is empty, so it really should be treated as a depth of one.
                case 1:
                    componentReturn = ListObjectEditor.createBoundComponentForList(objectField, bindingGroup);
                    break;
                case 2:
                    componentReturn = createBoundComponentFor2dList(objectField, bindingGroup);
                    break;
                default:
                    componentReturn = new ComponentReturn(new JLabel("List too deep at " + depth + "."), false);
                    break;
            }
        } else {
            componentReturn = createBoundComponentForString(objectField, bindingGroup, realizedUndoManager);
        }
        
        
        if (!configInfo.trackBooleanPropertyNamedForEnabled().equals("")) {
            Binding enabledBinding = new BasicBinding(containingObject, configInfo.trackBooleanPropertyNamedForEnabled(), componentReturn.getComponent(), "enabled", Binding.UpdateStrategy.READ_ONLY);
            bindingGroup.add(enabledBinding);
        } else if (!configInfo.trackBooleanPropertyNamedForDisabled().equals("")) {
            System.out.println(configInfo.trackBooleanPropertyNamedForDisabled());
            Binding disabledBinding = new BasicBinding(containingObject, configInfo.trackBooleanPropertyNamedForDisabled(), componentReturn.getComponent(), "enabled", Binding.UpdateStrategy.READ_ONLY, new FlipBooleanBindingConverter());
            bindingGroup.add(disabledBinding);
        }
        
        return componentReturn;
    }

    private static ComponentReturn createBoundComponentForNonMutableObject(BoundField objectField, BindingGroup bindingGroup) {
        JTextField labelComponent = new JTextField("");
        labelComponent.setEditable(false);

        Binding binding = new BasicBinding(objectField, BoundFields.boundField(labelComponent, "text"), Binding.UpdateStrategy.READ_ONLY);
        bindingGroup.add(binding);

        return new ComponentReturn(labelComponent, false);
    }

    private static ComponentReturn createBoundComponentForBoolean(BoundField objectField, BindingGroup bindingGroup) {
        JCheckBox checkBox = new JCheckBox(ObjectEditingBindings.nameForBindableField(objectField.getBindableField()));

        BasicBinding binding = new BasicBinding(objectField, BoundFields.boundField(checkBox, AbstractButtonBoundField.SYNTHETIC_FIELD_SELECTED), Binding.UpdateStrategy.READ_WRITE);
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
                        (int) configInfo.numberMinimumValue(),
                        (int) configInfo.numberMinimumValue(),
                        (int) configInfo.numberMaximumValue(),
                        (int) configInfo.numberStepValue()
                ));
                JSpinner.NumberEditor spinnerNumberEditor = new JSpinner.NumberEditor(spinner);
                spinnerNumberEditor.getFormat().setGroupingUsed(false);
                spinner.setEditor(spinnerNumberEditor);

                BasicBinding binding = new BasicBinding(objectField, BoundFields.boundField(spinner, JSpinnerBoundField.SYNTHETIC_FIELD_VALUE), Binding.UpdateStrategy.READ_WRITE);
                bindingGroup.add(binding);

                return new ComponentReturn(spinner, false);
        }
    }

    private static ComponentReturn createBoundComponentForFloatingPointNumber(BoundField objectField, BindingGroup bindingGroup, UndoManager undoManager) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(objectField.getBindableField()), ObjectEditingProperty.class);

        switch (configInfo.numberEditor()) {
            case TEXT_CONTROL:
                return createBoundComponentForString(objectField, bindingGroup, undoManager);
            case SLIDER_CONTROL:
            case SPINNER_CONTROL:
            default:
                JSpinner spinner = new JSpinner(new SpinnerNumberModel(
                        (double) configInfo.numberMinimumValue(),
                        (double) configInfo.numberMinimumValue(),
                        (double) configInfo.numberMaximumValue(),
                        (double) configInfo.numberStepValue()
                ));
                JSpinner.NumberEditor spinnerNumberEditor = new JSpinner.NumberEditor(spinner);
                spinnerNumberEditor.getFormat().setGroupingUsed(false);
                spinnerNumberEditor.getFormat().setDecimalSeparatorAlwaysShown(true);
                spinner.setEditor(spinnerNumberEditor);

                BasicBinding binding = new BasicBinding(objectField, BoundFields.boundField(spinner, JSpinnerBoundField.SYNTHETIC_FIELD_VALUE), Binding.UpdateStrategy.READ_WRITE);
                bindingGroup.add(binding);

                return new ComponentReturn(spinner, false);
        }
    }

    private static ComponentReturn createBoundComponentForString(BoundField objectField, BindingGroup bindingGroup, UndoManager undoManager) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(objectField.getBindableField()), ObjectEditingProperty.class);
        
        JTextField textField = new JTextField();

        String setOn = JTextComponentBoundField.SYNTHETIC_FIELD_TEXT;
        switch (configInfo.setOn()) {
            case ANY_CHANGE:
                setOn = JTextComponentBoundField.SYNTHETIC_FIELD_TEXT;
                break;
            case FOCUS_LOST:
                setOn = JTextComponentBoundField.SYNTHETIC_FIELD_TEXT_ON_FOCUS_LOST;
                break;
            case ACTION_OR_FOCUS_LOST:
                setOn = JTextComponentBoundField.SYNTHETIC_FIELD_TEXT_ON_ACTION_OR_FOCUS_LOST;
                break;
        }
        
        textField.getDocument().addUndoableEditListener(undoManager);
        
        BasicBinding binding = new BasicBinding(objectField, BoundFields.boundField(textField, setOn), Binding.UpdateStrategy.READ_WRITE);
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

    public static ComponentReturn createBoundComponentFor2dList(BoundField<List<List>> boundField, BindingGroup bindingGroup) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(boundField.getBindableField()), ObjectEditingProperty.class);
        JButton editButton = new JButton("Open Editor");
        editButton.addActionListener((actionEvent) -> {
            BindingGroup editorBindingGroup = new BindingGroup();
            ObjectEditingList2dTableEditorDialog editor = new ObjectEditingList2dTableEditorDialog(null, true, boundField, editorBindingGroup);
            editor.setTitle(configInfo.name() + " Editor");
            editor.setVisible(true);
            editorBindingGroup.bind();
        });
        return new ComponentReturn(editButton, false);
    }

    public static ComponentReturn createBoundComponentForEnum(BoundField boundField, BindingGroup bindingGroup) {
        JComboBox comboBox = new JComboBox(new ObservableComboBoxModel(boundField.getFieldClass(), Arrays.asList(boundField.getFieldClass().getEnumConstants())));

        Binding selectionBinding = new BasicBinding(boundField, BoundFields.boundField(comboBox.getModel(), "selectedItem"), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(selectionBinding);

        return new ComponentReturn(comboBox, false);
    }
}
