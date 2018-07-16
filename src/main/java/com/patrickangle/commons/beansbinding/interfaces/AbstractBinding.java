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

import com.patrickangle.commons.beansbinding.BasicBindingConverter;
import com.patrickangle.commons.beansbinding.BasicBoundField;
import com.patrickangle.commons.beansbinding.BoundFields;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.util.Classes;

/**
 *
 * @author patrickangle
 */
public abstract class AbstractBinding<B, F> implements Binding {
    protected BoundField<B> backBoundField;
    protected BoundField<F> frontBoundField;
    
    protected Binding.UpdateStrategy updateStrategy;
    protected Binding.Converter converter;
    
    protected boolean bound = false;
        
    public AbstractBinding(BoundField<B> backBoundField, BoundField<F> frontBoundField, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        this.backBoundField = backBoundField;
        this.frontBoundField = frontBoundField;
        this.updateStrategy = updateStrategy;
        this.converter = converter;
    }
    
    public AbstractBinding(BoundField<B> backBoundField, BoundField<F> frontBoundField, Binding.UpdateStrategy updateStrategy) {
        this(backBoundField, frontBoundField, updateStrategy, new BasicBindingConverter());
    }

    public AbstractBinding(B backContainingObject, String backObjectFieldName, F frontContainingObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        this(BoundFields.boundField(backContainingObject, backObjectFieldName),
                BoundFields.boundField(frontContainingObject, frontObjectFieldName),
                updateStrategy,
                converter);
    }
    
    public AbstractBinding(B backObject, String backObjectFieldName, F frontObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy) {
        this(backObject, backObjectFieldName, frontObject, frontObjectFieldName, updateStrategy, new BasicBindingConverter());
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
    
    protected abstract void bind();
    
    protected abstract void unbind();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(backBoundField);
        sb.append(" -> ");
        sb.append(converter.getClass().getSimpleName());
        sb.append(" -> ");
        sb.append(frontBoundField);
        sb.append(" (");
        sb.append(updateStrategy);
        sb.append(")");
        
        return sb.toString();
    }
}
