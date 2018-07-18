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
package com.patrickangle.commons.beansbinding.swing.boundfields;

import com.patrickangle.commons.beansbinding.interfaces.AbstractBoundField;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.util.Classes;
import java.beans.PropertyChangeListener;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Patrick Angle
 */
public class JSpinnerBoundField extends AbstractBoundField<JSpinner> {
    public static final String SYNTHETIC_FIELD_VALUE = "value$";
    
    protected JSpinner containingObject;
    protected BindableField<JSpinner> bindableField;
    
    protected PropertyChangeListener propertyChangeListener;
    
    protected ChangeListener changeListener;
    protected Object cachedValue;
    
    public JSpinnerBoundField(JSpinner containingObject, BindableField<JSpinner> bindableField) {
        this.containingObject = containingObject;
        this.bindableField = bindableField;
        
        this.commonInit();
    }
    
    public JSpinnerBoundField(JSpinner containingObject, String fieldName) {
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
        
        // Binding for synthetic updates.
        if (changeListener == null) {
            changeListener = (changeEvent) -> {
                valueChanged();
            };
        }
        
        this.containingObject.addChangeListener(changeListener);
    }
    
    public JSpinner getContainingObject() {
        return this.containingObject;
    }
    
    public BindableField<JSpinner> getBindableField() {
        return this.bindableField;
    }

    @Override
    public Class getFieldClass() {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_VALUE:
            default:
                return super.getFieldClass();
        }
    }

    @Override
    public Object getValue() {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_VALUE:
            default:
                return super.getValue();
        }
    }

    @Override
    public void setValue(Object value) {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_VALUE:
            default:
                super.setValue(value);
        }
    }
    
    protected void valueChanged() {
        Object oldValue = cachedValue;
        cachedValue = this.getContainingObject().getValue();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_VALUE, oldValue, cachedValue);
    }
}
