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
package com.patrickangle.commons.beansbinding.swing;

import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;

/**
 *
 * @author patrickangle
 */
public class JSliderBinding<E> extends BasicBinding<E, JSlider> {
    public static final String SYNTHETIC_FIELD_VALUE = "value$";
    public static final String SYNTHETIC_FIELD_VALUE_IGNORE_ADJUSTING = "value$ignoreAdjusting";
    
    private PropertyChangeSupport propertyChangeSupport;
    
    protected ChangeListener changeListener;
    protected PropertyChangeListener propertyChangeListener;
    
    protected int cachedValue;
    protected int cachedValueIgnoreAdjusting;
    
    public JSliderBinding(BoundField<E> backBoundField, BoundField<JSlider> frontBoundField, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        super(backBoundField, frontBoundField, updateStrategy, converter);
        this.propertyChangeSupport = new PropertyChangeSupport(this);

    }
    
    public JSliderBinding(BoundField<E> backBoundField, BoundField<JSlider> frontBoundField, Binding.UpdateStrategy updateStrategy) {
        super(backBoundField, frontBoundField, updateStrategy);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public JSliderBinding(E backContainingObject, String backObjectFieldName, JSlider frontContainingObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        super(backContainingObject, backObjectFieldName, frontContainingObject, frontObjectFieldName, updateStrategy, converter);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    public JSliderBinding(E backObject, String backObjectFieldName, JSlider frontObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy) {
        super(backObject, backObjectFieldName, frontObject, frontObjectFieldName, updateStrategy);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    @Override
    protected void bind() {
        super.bind();
        
        // Binding for synthetic updates.
        if (changeListener == null) {
            changeListener = (changeEvent) -> {
                //stateChanged
                valueChanged();
                if (!frontBoundField.getContainingObject().getValueIsAdjusting()) {
                    valueChangedIgnoreAdjusting();
                }
            };
        }
        frontBoundField.getContainingObject().addChangeListener(changeListener);
        
        if (propertyChangeListener == null) {
            propertyChangeListener = (propertyChangeEvent) -> {
                valueChanged();
            };
        }
        frontBoundField.getContainingObject().addPropertyChangeListener(propertyChangeListener);
        
        this.propertyChangeSupport.addPropertyChangeListener(frontObjectListener);
    }
    
    @Override
    protected void unbind() {
        super.unbind();
        
        frontBoundField.getContainingObject().removeChangeListener(changeListener);
        frontBoundField.getContainingObject().removePropertyChangeListener(propertyChangeListener);
        
        this.propertyChangeSupport.removePropertyChangeListener(frontObjectListener);
    }
    
    protected void valueChanged() {
        int oldValue = cachedValue;
        cachedValue = frontBoundField.getContainingObject().getValue();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_VALUE, oldValue, cachedValue);
    }
    
    protected void valueChangedIgnoreAdjusting() {
        int oldValue = cachedValueIgnoreAdjusting;
        cachedValueIgnoreAdjusting = frontBoundField.getContainingObject().getValue();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_VALUE_IGNORE_ADJUSTING, oldValue, cachedValueIgnoreAdjusting);
    }
}
