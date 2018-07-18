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
package com.patrickangle.commons.beansbinding;

import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.interfaces.AbstractBoundField;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.util.Classes;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Patrick Angle
 */
public class BasicBoundField<C> extends AbstractBoundField<C> {
    protected C containingObject;
    protected BindableField<C> bindableField;
    
    public BasicBoundField(C containingObject, BindableField<C> bindableField) {
        this.containingObject = containingObject;
        this.bindableField = bindableField;
        
        commonInit();
    }
    
    public BasicBoundField(C containingObject, String fieldName) {
        this.containingObject = containingObject;
        this.bindableField = BindableFields.forClassWithName(Classes.classFor(containingObject), fieldName);
        
        commonInit();
    }
    
    private void commonInit() {
        // Declare the source of events to be from the contained object, not ourselves.
        this.propertyChangeSupport = new PropertyChangeSupport(this.containingObject);
        
        PropertyChangeObservable.addPropertyChangeListener(containingObject, (PropertyChangeEvent propertyChangeEvent) -> {
            BasicBoundField.this.propertyChangeSupport.firePropertyChange(propertyChangeEvent);
        });
    }
    
    public C getContainingObject() {
        return this.containingObject;
    }
    
    public BindableField<C> getBindableField() {
        return this.bindableField;
    }
}
