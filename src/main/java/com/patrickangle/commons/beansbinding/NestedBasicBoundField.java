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
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.util.Classes;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A special type of bound field that supports binding to a nested field.
 *
 * @author Patrick Angle
 */
public class NestedBasicBoundField<C> extends AbstractBoundField<C> {

    protected C containingObject;
    protected BindableField<C> bindableField;

    protected String nestedFieldName;
    protected BoundField nestedBoundField;

    protected final PropertyChangeListener boundFieldPcl = (evt) -> {
        updateEvent(evt);
    };

    protected final PropertyChangeListener objectPcl = (evt) -> {
        rebuildNestedBoundField();
        updateEvent(evt);
    };

    public NestedBasicBoundField(C containingObject, String fieldName) {
        String[] fieldNameParts = fieldName.split("\\.", 2);

        this.containingObject = containingObject;
        if (containingObject != null) {
            this.bindableField = BindableFields.forClassWithName(Classes.classFor(containingObject), fieldNameParts[0]);
        }
        this.nestedFieldName = fieldNameParts[1];
        if (containingObject != null) {
            commonInit();
        }
    }

    private void commonInit() {
        // Declare the source of events to be from the contained object, not ourselves.
        this.propertyChangeSupport = new PropertyChangeSupport(this.containingObject);
        rebuildNestedBoundField();
        PropertyChangeObservable.addPropertyChangeListener(containingObject, objectPcl);
    }

    protected void updateEvent(PropertyChangeEvent e) {
        if (e.getPropertyName().equals(nestedFieldName)) {
            this.propertyChangeSupport.firePropertyChange(getFieldName(), e.getOldValue(), getValue());
        }
    }

    protected void rebuildNestedBoundField() {
        PropertyChangeObservable.removePropertyChangeListener(this.nestedBoundField, boundFieldPcl);
        Object currentValue = super.getValue();
        if (currentValue != null) {
            this.nestedBoundField = BoundFields.boundField(currentValue, nestedFieldName);
            PropertyChangeObservable.addPropertyChangeListener(this.nestedBoundField, boundFieldPcl);
        } else {
            this.nestedBoundField = null;
        }
    }

    public C getContainingObject() {
        return this.containingObject;
    }

    @Override
    public String getFieldName() {
        return super.getFieldName() + "." + nestedFieldName;
    }

    public BindableField<C> getBindableField() {
        return this.bindableField;
    }

    @Override
    public Object getValue() {
        if (nestedBoundField != null) {
            return nestedBoundField.getValue();
        } else {
            return null;
        }

    }

    @Override
    public void setValue(Object value) {
        if (nestedBoundField != null) {
            nestedBoundField.setValue(value);
        }
    }

}
