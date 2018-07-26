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
package com.patrickangle.commons.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.swing.boundfields.JSpinnerBoundField;
import com.patrickangle.commons.json.JsonableObject;
import com.patrickangle.commons.objectediting.interfaces.CustomObjectEditingComponent;
import com.patrickangle.commons.objectediting.util.ObjectFieldEditorFactory;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservableBase;
import com.patrickangle.commons.util.Colors;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 *
 * @author patrickangle
 */
public abstract class Range<E extends Number> extends PropertyChangeObservableBase implements CustomObjectEditingComponent, PropertyChangeObservable, JsonableObject {    
    @JsonProperty protected E minimum;
    @JsonProperty protected E maximum;
    
    public E getMinimum() {
        return this.minimum;
    }
    
    public void setMinimum(E minimum) {
        E oldMinimum = this.minimum;
        this.minimum = minimum;
        this.propertyChangeSupport.firePropertyChange("minimum", oldMinimum, this.minimum);
    }
    
    public E getMaximum() {
        return this.maximum;
    }
    
    public void setMaximum(E maximum) {
        E oldMaximum = this.maximum;
        this.maximum = maximum;
        this.propertyChangeSupport.firePropertyChange("maximum", oldMaximum, this.maximum);
    }
    
    public abstract void setRange(E minimum, E maximum);
        
    protected abstract SpinnerNumberModel customObjectEditingSpinnerNumberModel();
    
    protected abstract boolean customObjectEditingSpinnerShowDecimalPoint();

    @Override
    public ObjectFieldEditorFactory.ComponentReturn customObjectEditingComponent(BindingGroup bindingGroup) {
        JPanel pointEditor = new JPanel(new GridBagLayout());
        pointEditor.setBackground(Colors.transparentColor());
        pointEditor.setOpaque(true);
        
        GridBagConstraints gridBagConstraints;
        
        JSpinner spinnerX = new JSpinner(customObjectEditingSpinnerNumberModel());
        
        JSpinner.NumberEditor numberEditorX = new JSpinner.NumberEditor(spinnerX);
        numberEditorX.getFormat().setGroupingUsed(false);
        numberEditorX.getFormat().setDecimalSeparatorAlwaysShown(customObjectEditingSpinnerShowDecimalPoint());
        spinnerX.setEditor(numberEditorX);

        
        if (spinnerX.getEditor() instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor)spinnerX.getEditor()).getTextField().setHorizontalAlignment(SwingConstants.RIGHT);
        }
        
        Binding spinnerXBinding = new BasicBinding(this, "minimum", spinnerX, JSpinnerBoundField.SYNTHETIC_FIELD_VALUE, Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(spinnerXBinding);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pointEditor.add(spinnerX, gridBagConstraints);
        
        JLabel commaLabel = new JLabel("..");
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pointEditor.add(commaLabel, gridBagConstraints);
        
        JSpinner spinnerY = new JSpinner(customObjectEditingSpinnerNumberModel());
        
        JSpinner.NumberEditor numberEditorY = new JSpinner.NumberEditor(spinnerY);
        numberEditorY.getFormat().setGroupingUsed(false);
        numberEditorY.getFormat().setDecimalSeparatorAlwaysShown(customObjectEditingSpinnerShowDecimalPoint());
        spinnerY.setEditor(numberEditorY);
        
        if (spinnerY.getEditor() instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor)spinnerY.getEditor()).getTextField().setHorizontalAlignment(SwingConstants.LEFT);
        }
        
        Binding spinnerYBinding = new BasicBinding(this, "maximum", spinnerY, JSpinnerBoundField.SYNTHETIC_FIELD_VALUE, Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(spinnerYBinding);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pointEditor.add(spinnerY, gridBagConstraints);
        
        return new ObjectFieldEditorFactory.ComponentReturn(pointEditor, false);
    }
    
    public static class IntegerRange extends Range<Integer> {
        public IntegerRange() {
            this(0, 0);
        }
        
        public IntegerRange(String value) {
            String[] parts = value.split(",");
            if (parts.length == 2) {
                this.minimum = Integer.parseInt(parts[0]);
                this.maximum = Integer.parseInt(parts[1]);
            } else {
                throw new IllegalArgumentException("Point.IntegerPoint can only be constructed with two values seperated by a comma.");
            }
        }
        
        public IntegerRange(int minimum, int maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }
        
        @Override
        public void setRange(Integer minimum, Integer maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }

        @Override
        protected SpinnerNumberModel customObjectEditingSpinnerNumberModel() {
            return new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        }

        @Override
        protected boolean customObjectEditingSpinnerShowDecimalPoint() {
            return false;
        }
    }
    
    public static class FloatRange extends Range<Float> {
        public FloatRange() {
            this(0f, 0f);
        }
        
        public FloatRange(String value) {
            String[] parts = value.split(",");
            if (parts.length == 2) {
                this.minimum = Float.parseFloat(parts[0]);
                this.maximum = Float.parseFloat(parts[1]);
            } else {
                throw new IllegalArgumentException("Point.IntegerPoint can only be constructed with two values seperated by a comma.");
            }
        }
        
        public FloatRange(float minimum, float maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }
        
        @Override
        public void setRange(Float minimum, Float maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }

        @Override
        protected SpinnerNumberModel customObjectEditingSpinnerNumberModel() {
            return new SpinnerNumberModel(0f, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        }
        
        @Override
        protected boolean customObjectEditingSpinnerShowDecimalPoint() {
            return true;
        }
    }
    
    public static class DoubleRange extends Range<Double> {
        public DoubleRange() {
            this(0f, 0f);
        }
        
        public DoubleRange(String value) {
            String[] parts = value.split(",");
            if (parts.length == 2) {
                this.minimum = Double.parseDouble(parts[0]);
                this.maximum = Double.parseDouble(parts[1]);
            } else {
                throw new IllegalArgumentException("Point.IntegerPoint can only be constructed with two values seperated by a comma.");
            }
        }
        
        public DoubleRange(double minimum, double maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }
        
        @Override
        public void setRange(Double minimum, Double maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
        }


        @Override
        protected SpinnerNumberModel customObjectEditingSpinnerNumberModel() {
            return new SpinnerNumberModel(0.0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        }
        
        @Override
        protected boolean customObjectEditingSpinnerShowDecimalPoint() {
            return true;
        }
    }
}
