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

import com.patrickangle.commons.beansbinding.interfaces.AbstractBinding;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import java.beans.PropertyChangeListener;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;

/**
 *
 * @author patrickangle
 */
public class BasicBinding<B, F> extends AbstractBinding<B, F> {

    protected PropertyChangeListener backObjectListener;
    protected PropertyChangeListener frontObjectListener;
    protected boolean locked = false;

    public BasicBinding(BoundField<B> backBoundField, BoundField<F> frontBoundField, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        super(backBoundField, frontBoundField, updateStrategy, converter);
    }

    public BasicBinding(BoundField<B> backBoundField, BoundField<F> frontBoundField, Binding.UpdateStrategy updateStrategy) {
        super(backBoundField, frontBoundField, updateStrategy);
    }

    public BasicBinding(B backContainingObject, String backObjectFieldName, F frontContainingObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        super(backContainingObject, backObjectFieldName, frontContainingObject, frontObjectFieldName, updateStrategy, converter);
    }

    public BasicBinding(B backObject, String backObjectFieldName, F frontObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy) {
        super(backObject, backObjectFieldName, frontObject, frontObjectFieldName, updateStrategy);
    }

    @Override
    protected void bind() {
        if (updateStrategy.isForwardOnce()) {
            this.updateForward(backBoundField.getValue());
        } else {
            this.updateBackward(frontBoundField.getValue());
        }

        backObjectListener = (propertyChangeEvent) -> {
            if (!this.locked && propertyChangeEvent.getPropertyName().equals(this.backBoundField.getFieldName())) {
                if (this.updateStrategy.isForward()) {
                    this.updateForward(propertyChangeEvent.getNewValue());
                }
            }
        };

        frontObjectListener = (propertyChangeEvent) -> {
            if (!this.locked && propertyChangeEvent.getPropertyName().equals(this.frontBoundField.getFieldName())) {
                if (this.updateStrategy.isBackward()) {
                    this.updateBackward(propertyChangeEvent.getNewValue());
                }
            }
        };

        PropertyChangeObservable.addPropertyChangeListener(backBoundField, backObjectListener);
        PropertyChangeObservable.addPropertyChangeListener(frontBoundField, frontObjectListener);
    }

    @Override
    protected void unbind() {
        PropertyChangeObservable.removePropertyChangeListener(backBoundField.getContainingObject(), backObjectListener);
        PropertyChangeObservable.removePropertyChangeListener(frontBoundField.getContainingObject(), frontObjectListener);
    }

    protected void updateForward(Object newValue) {
        this.locked = true;

        this.frontBoundField.setValue(converter.convertForward(newValue));

        this.locked = false;
    }

    protected void updateBackward(Object newValue) {
        this.locked = true;

        this.backBoundField.setValue(converter.convertBackward(newValue));

        this.locked = false;
    }

    public static <B extends Object, F extends Object> void bind(BoundField<B> backBoundField, BoundField<F> frontBoundField, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        new BasicBinding<B, F>(backBoundField, frontBoundField, updateStrategy, converter).bind();
    }

    public static <B extends Object, F extends Object> void bind(BoundField<B> backBoundField, BoundField<F> frontBoundField, Binding.UpdateStrategy updateStrategy) {
        new BasicBinding<B, F>(backBoundField, frontBoundField, updateStrategy).bind();
    }
    
    public static <B extends Object, F extends Object> void bind(B backContainingObject, String backObjectFieldName, F frontContainingObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        new BasicBinding<B, F>(backContainingObject, backObjectFieldName, frontContainingObject, frontObjectFieldName, updateStrategy, converter).bind();
    }
    
    public static <B extends Object, F extends Object> void bind(B backObject, String backObjectFieldName, F frontObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy) {
        new BasicBinding<B, F>(backObject, backObjectFieldName, frontObject, frontObjectFieldName, updateStrategy).bind();
    }
}
