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
package com.patrickangle.commons.beansbinding.interfaces;

import com.patrickangle.commons.observable.interfaces.PropertyChangeObservableBase;
import com.patrickangle.commons.util.Primitives;

/**
 *
 * @author Patrick Angle
 */
public abstract class AbstractBoundField<C> extends PropertyChangeObservableBase implements BoundField {
    @Override
    public Class<C> getContainingClass() {
        return (Class<C>) this.getContainingObject().getClass();
    }
    
    @Override
    public String getFieldName() {
        return this.getBindableField().getFieldName();
    }
    
    @Override
    public Class getFieldClass() {
        return this.getBindableField().getFieldClass();
    }
    
    @Override
    public boolean isReadable() {
        return this.getBindableField().isReadable();
    }
    
    @Override
    public boolean isWriteable() {
        return this.getBindableField().isWriteable();
    }
    
    @Override
    public Object getValue() {
        return this.getBindableField().getValue(this.getContainingObject());
    }
    
    @Override
    public void setValue(Object value) {
        this.getBindableField().setValue(this.getContainingObject(), Primitives.convertObjectToType(value, getFieldClass()));
    }

    @Override
    public String toString() {
        return this.getContainingObject() + " {" + this.getBindableField() + "}";
    }
    
    
    
}
