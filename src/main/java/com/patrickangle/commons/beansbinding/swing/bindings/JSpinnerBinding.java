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
package com.patrickangle.commons.beansbinding.swing.bindings;

import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.event.ChangeListener;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import javax.swing.JSpinner;

/**
 *
 * @author patrickangle
 * @deprecated Use proper explicit BoundField to construct a binding exposing additional synthetic values.
 */
public class JSpinnerBinding<E> extends BasicBinding<E, JSpinner> {
    public static final String SYNTHETIC_FIELD_VALUE = "value$";
    
    private PropertyChangeSupport propertyChangeSupport;
    
    protected ChangeListener changeListener;
    
    protected Object cachedValue;
    
    public JSpinnerBinding(BoundField<E> backBoundField, BoundField<JSpinner> frontBoundField, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        super(backBoundField, frontBoundField, updateStrategy, converter);
        this.propertyChangeSupport = new PropertyChangeSupport(this);

    }
    
    public JSpinnerBinding(BoundField<E> backBoundField, BoundField<JSpinner> frontBoundField, Binding.UpdateStrategy updateStrategy) {
        super(backBoundField, frontBoundField, updateStrategy);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public JSpinnerBinding(E backContainingObject, String backObjectFieldName, JSpinner frontContainingObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        super(backContainingObject, backObjectFieldName, frontContainingObject, frontObjectFieldName, updateStrategy, converter);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    public JSpinnerBinding(E backObject, String backObjectFieldName, JSpinner frontObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy) {
        super(backObject, backObjectFieldName, frontObject, frontObjectFieldName, updateStrategy);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    @Override
    protected void bind() {
        super.bind();
        
        // Binding for synthetic updates.
        if (changeListener == null) {
            changeListener = (changeEvent) -> {
                valueChanged();
            };
        }
        frontBoundField.getContainingObject().addChangeListener(changeListener);
        
        this.propertyChangeSupport.addPropertyChangeListener(frontObjectListener);
    }
    
    @Override
    protected void unbind() {
        super.unbind();
        
        frontBoundField.getContainingObject().removeChangeListener(changeListener);
        
        this.propertyChangeSupport.removePropertyChangeListener(frontObjectListener);
    }
    
    protected void valueChanged() {
        Object oldValue = cachedValue;
        cachedValue = frontBoundField.getContainingObject().getValue();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_VALUE, oldValue, cachedValue);
    }
}
