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
package com.patrickangle.commons.objectediting.editors;

import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.JSpinnerBoundField;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.objectediting.annotations.ObjectEditingProperty;
import com.patrickangle.commons.objectediting.util.ObjectFieldEditorFactory;
import com.patrickangle.commons.util.Annotations;
import com.patrickangle.commons.util.Colors;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.geom.Point2D;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 *
 * @author patrickangle
 */
public class Point2dObjectEditor {

    public static ObjectFieldEditorFactory.ComponentReturn createBoundComponentForPoint2D(BoundField<Point2D> objectField, BindingGroup bindingGroup) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(objectField.getBindableField()), ObjectEditingProperty.class);

        JPanel pointEditor = new JPanel(new GridBagLayout());
        pointEditor.setBackground(Colors.transparentColor());
        pointEditor.setOpaque(true);
        
        GridBagConstraints gridBagConstraints;
        
        JSpinner spinnerX = new JSpinner(new SpinnerNumberModel(
                        (int) configInfo.numberMinimumValue(),
                        (int) configInfo.numberMinimumValue(),
                        (int) configInfo.numberMaximumValue(),
                        (int) configInfo.numberStepValue()
                ));
        
        JSpinner.NumberEditor numberEditorX = new JSpinner.NumberEditor(spinnerX);
        numberEditorX.getFormat().setGroupingUsed(false);
//        numberEditorX.getFormat().setDecimalSeparatorAlwaysShown(customObjectEditingSpinnerShowDecimalPoint());
        spinnerX.setEditor(numberEditorX);

        
        if (spinnerX.getEditor() instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor)spinnerX.getEditor()).getTextField().setHorizontalAlignment(SwingConstants.RIGHT);
        }
//        objectField.getValue()
        Binding spinnerXBinding = new BasicBinding(objectField.getValue(), "x", spinnerX, JSpinnerBoundField.SYNTHETIC_FIELD_VALUE, Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(spinnerXBinding);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pointEditor.add(spinnerX, gridBagConstraints);
        
        JLabel commaLabel = new JLabel(",");
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pointEditor.add(commaLabel, gridBagConstraints);
        
        JSpinner spinnerY = new JSpinner(new SpinnerNumberModel(
                        (int) configInfo.numberMinimumValue(),
                        (int) configInfo.numberMinimumValue(),
                        (int) configInfo.numberMaximumValue(),
                        (int) configInfo.numberStepValue()
                ));
        
        JSpinner.NumberEditor numberEditorY = new JSpinner.NumberEditor(spinnerY);
        numberEditorY.getFormat().setGroupingUsed(false);
//        numberEditorY.getFormat().setDecimalSeparatorAlwaysShown(customObjectEditingSpinnerShowDecimalPoint());
        spinnerY.setEditor(numberEditorY);
        
        if (spinnerY.getEditor() instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor)spinnerY.getEditor()).getTextField().setHorizontalAlignment(SwingConstants.LEFT);
        }
        
        Binding spinnerYBinding = new BasicBinding(objectField.getValue(), "y", spinnerY, JSpinnerBoundField.SYNTHETIC_FIELD_VALUE, Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(spinnerYBinding);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pointEditor.add(spinnerY, gridBagConstraints);
        
        return new ObjectFieldEditorFactory.ComponentReturn(pointEditor, false);
    }
}
