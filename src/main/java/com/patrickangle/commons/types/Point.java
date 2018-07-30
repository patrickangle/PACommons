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
public abstract class Point<E extends Number> extends PropertyChangeObservableBase implements CustomObjectEditingComponent, PropertyChangeObservable, JsonableObject {    
    @JsonProperty protected E x;
    @JsonProperty protected E y;
    
    public E getX() {
        return this.x;
    }
    
    public void setX(E x) {
        E oldX = this.x;
        this.x = x;
        this.propertyChangeSupport.firePropertyChange("x", oldX, this.x);
    }
    
    public E getY() {
        return this.y;
    }
    
    public void setY(E y) {
        E oldY = this.y;
        this.y = y;
        this.propertyChangeSupport.firePropertyChange("y", oldY, this.y);
    }
    
    public abstract void setPoint(E x, E y);
    
    public abstract Point<E> pointOffset(E x, E y);
    public abstract Point<E> pointOffset(Point<E> offset);
    public void test() {
        
    }
    
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
        
        Binding spinnerXBinding = new BasicBinding(this, "x", spinnerX, JSpinnerBoundField.SYNTHETIC_FIELD_VALUE, Binding.UpdateStrategy.READ_WRITE);
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
        
        JSpinner spinnerY = new JSpinner(customObjectEditingSpinnerNumberModel());
        
        JSpinner.NumberEditor numberEditorY = new JSpinner.NumberEditor(spinnerY);
        numberEditorY.getFormat().setGroupingUsed(false);
        numberEditorY.getFormat().setDecimalSeparatorAlwaysShown(customObjectEditingSpinnerShowDecimalPoint());
        spinnerY.setEditor(numberEditorY);
        
        if (spinnerY.getEditor() instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor)spinnerY.getEditor()).getTextField().setHorizontalAlignment(SwingConstants.LEFT);
        }
        
        Binding spinnerYBinding = new BasicBinding(this, "y", spinnerY, JSpinnerBoundField.SYNTHETIC_FIELD_VALUE, Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(spinnerYBinding);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pointEditor.add(spinnerY, gridBagConstraints);
        
        return new ObjectFieldEditorFactory.ComponentReturn(pointEditor, false);
    }
    
    public static class IntegerPoint extends Point<Integer> {
        public IntegerPoint() {
            this(0, 0);
        }
        
        public IntegerPoint(String value) {
            String[] parts = value.split(",");
            if (parts.length == 2) {
                this.x = Integer.parseInt(parts[0]);
                this.y = Integer.parseInt(parts[1]);
            } else {
                throw new IllegalArgumentException("Point.IntegerPoint can only be constructed with two values seperated by a comma.");
            }
        }
        
        public IntegerPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public void setPoint(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Point.IntegerPoint pointOffset(Integer x, Integer y) {
            return new Point.IntegerPoint(this.x + x, this.y + y);
        }
        
        @Override
        public Point.IntegerPoint pointOffset(Point<Integer> offset) {
            return new Point.IntegerPoint(this.x + offset.x, this.y + offset.y);
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
    
    public static class FloatPoint extends Point<Float> {
        public FloatPoint() {
            this(0f, 0f);
        }
        
        public FloatPoint(String value) {
            String[] parts = value.split(",");
            if (parts.length == 2) {
                this.x = Float.parseFloat(parts[0]);
                this.y = Float.parseFloat(parts[1]);
            } else {
                throw new IllegalArgumentException("Point.IntegerPoint can only be constructed with two values seperated by a comma.");
            }
        }
        
        public FloatPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public void setPoint(Float x, Float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Point.FloatPoint pointOffset(Float x, Float y) {
            return new Point.FloatPoint(this.x + x, this.y + y);
        }
        
        @Override
        public Point.FloatPoint pointOffset(Point<Float> offset) {
            return new Point.FloatPoint(this.x + offset.x, this.y + offset.y);
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
    
    public static class DoublePoint extends Point<Double> {
        public DoublePoint() {
            this(0f, 0f);
        }
        
        public DoublePoint(String value) {
            String[] parts = value.split(",");
            if (parts.length == 2) {
                this.x = Double.parseDouble(parts[0]);
                this.y = Double.parseDouble(parts[1]);
            } else {
                throw new IllegalArgumentException("Point.IntegerPoint can only be constructed with two values seperated by a comma.");
            }
        }
        
        public DoublePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public void setPoint(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Point.DoublePoint pointOffset(Double x, Double y) {
            return new Point.DoublePoint(this.x + x, this.y + y);
        }
        
        @Override
        public Point.DoublePoint pointOffset(Point<Double> offset) {
            return new Point.DoublePoint(this.x + offset.x, this.y + offset.y);
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
