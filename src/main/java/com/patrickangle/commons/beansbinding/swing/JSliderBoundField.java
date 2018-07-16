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
package com.patrickangle.commons.beansbinding.swing;

import com.patrickangle.commons.beansbinding.interfaces.AbstractBoundField;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import static com.patrickangle.commons.beansbinding.swing.bindings.JSliderBinding.SYNTHETIC_FIELD_VALUE;
import static com.patrickangle.commons.beansbinding.swing.bindings.JSliderBinding.SYNTHETIC_FIELD_VALUE_IGNORE_ADJUSTING;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.util.Classes;
import java.beans.PropertyChangeListener;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Patrick Angle
 */
public class JSliderBoundField extends AbstractBoundField<JSlider> {
    public static final String SYNTHETIC_FIELD_VALUE = "value$";
    public static final String SYNTHETIC_FIELD_VALUE_IGNORE_ADJUSTING = "value$ignoreAdjusting";
    
    protected JSlider containingObject;
    protected BindableField<JSlider> bindableField;
    
    protected PropertyChangeListener propertyChangeListener;
    
    protected ChangeListener changeListener;
    protected int cachedValue;
    protected int cachedValueIgnoreAdjusting;
    
    public JSliderBoundField(JSlider containingObject, BindableField<JSlider> bindableField) {
        this.containingObject = containingObject;
        this.bindableField = bindableField;
        
        this.commonInit();
    }
    
    public JSliderBoundField(JSlider containingObject, String fieldName) {
        this.containingObject = containingObject;
        this.bindableField = BindableFields.forClassWithName(Classes.classFor(containingObject), fieldName);
        
        this.commonInit();
    }
    
    private void commonInit() {
        if (propertyChangeListener == null) {
            propertyChangeListener = (propertyChangeEvent) -> {
                this.propertyChangeSupport.firePropertyChange(propertyChangeEvent);
            };
        }
        
        PropertyChangeObservable.addPropertyChangeListener(containingObject, propertyChangeListener);
        
        if (changeListener == null) {
            changeListener = (changeEvent) -> {
                valueChanged();
                if (!containingObject.getValueIsAdjusting()) {
                    valueChangedIgnoreAdjusting();
                }
            };
        }
        
        this.containingObject.addChangeListener(changeListener);
    }
    
    public JSlider getContainingObject() {
        return this.containingObject;
    }
    
    public BindableField<JSlider> getBindableField() {
        return this.bindableField;
    }

    @Override
    public Class getFieldClass() {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_VALUE:
            case SYNTHETIC_FIELD_VALUE_IGNORE_ADJUSTING:
            default:
                return super.getFieldClass();
        }
    }

    @Override
    public Object getValue() {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_VALUE:
            case SYNTHETIC_FIELD_VALUE_IGNORE_ADJUSTING:
            default:
                return super.getValue();
        }
    }

    @Override
    public void setValue(Object value) {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_VALUE:
            case SYNTHETIC_FIELD_VALUE_IGNORE_ADJUSTING:
            default:
                super.setValue(value);
        }
    }
    
    protected void valueChanged() {
        int oldValue = cachedValue;
        cachedValue = containingObject.getValue();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_VALUE, oldValue, cachedValue);
    }
    
    protected void valueChangedIgnoreAdjusting() {
        int oldValue = cachedValueIgnoreAdjusting;
        cachedValueIgnoreAdjusting = containingObject.getValue();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_VALUE_IGNORE_ADJUSTING, oldValue, cachedValueIgnoreAdjusting);
    }
}
