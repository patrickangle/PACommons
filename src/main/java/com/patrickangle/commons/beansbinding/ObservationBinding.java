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
package com.patrickangle.commons.beansbinding;

import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import java.beans.PropertyChangeListener;

/**
 *
 * @author patrickangle
 */
public class ObservationBinding<C> implements Binding {
    protected PropertyChangeListener propertyChangeListener;
    
    protected BoundField<C> boundField;
        
    protected boolean bound = false;
    
    protected PropertyChangeListener objectListener;
        
    public ObservationBinding(BoundField<C> boundField, PropertyChangeListener propertyChangeListener) {
        this.boundField = boundField;
        this.propertyChangeListener = propertyChangeListener;
    }

    public ObservationBinding(C containingObject, String backObjectFieldName, PropertyChangeListener propertyChangeListener) {
        this(BoundFields.boundField(containingObject, backObjectFieldName), propertyChangeListener);
    }

    @Override
    public boolean isBound() {
        return this.bound;
    }

    @Override
    public void setBound(boolean bound) {
        if (bound && !this.bound) {
            bind();
            this.bound = true;
        } else if (!bound && this.bound) {
            unbind();
            this.bound = false;
        }
    }

    protected void bind() {
        objectListener = (propertyChangeEvent) -> {
            if (propertyChangeEvent.getPropertyName().equals(this.boundField.getFieldName())) {
                propertyChangeListener.propertyChange(propertyChangeEvent);
            }
        };
        
        PropertyChangeObservable.addPropertyChangeListener(boundField, objectListener);
    }
    
    protected void unbind() {
        PropertyChangeObservable.removePropertyChangeListener(boundField.getContainingObject(), objectListener);
    }
    
}
