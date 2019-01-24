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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Patrick Angle
 */
public class BeanBindableField<C> implements BindableField<C> {
    private Class<C> containingClass;
    private String fieldName;
    private Class fieldClass;
    
    private Method getter;
    private Method setter;
    
    public BeanBindableField(Class<C> containingClass, String fieldName) {
        this.containingClass = containingClass;
        this.fieldName = fieldName;

        String getterSignature = methodSignatureForFieldName("get", this.fieldName);
        String isSignature = methodSignatureForFieldName("is", this.fieldName);
        String setterSignature = methodSignatureForFieldName("set", this.fieldName);

        try {
            this.getter = this.containingClass.getMethod(getterSignature);
            if (!Modifier.isPublic(this.getter.getModifiers())) {
                this.getter = null;
            }
        } catch (NoSuchMethodException | SecurityException ex) {
        }

        if (this.getter == null) {
            try {
                this.getter = this.containingClass.getMethod(isSignature);
                if (!Modifier.isPublic(this.getter.getModifiers())) {
                    this.getter = null;
                }
            } catch (NoSuchMethodException | SecurityException ex) {
            }
        }

        if (this.getter != null) {
            this.fieldClass = this.getter.getReturnType();
            try {
                this.setter = this.containingClass.getMethod(setterSignature, this.fieldClass);
                if (!Modifier.isPublic(this.setter.getModifiers())) {
                    this.setter = null;
                }
            } catch (NoSuchMethodException | SecurityException ex) {
            }
        }
    }

    public Class<C> getContainingClass() {
        return containingClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class getFieldClass() {
        return fieldClass;
    }
    
    public boolean isReadable() {
        return this.getter != null;
    }
    
    public boolean isWriteable() {
        return this.setter != null;
    }
    
    public Object getValue(C object) {
        if (this.getter != null) {
            try {
                return this.getter.invoke(object);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException(this.toString() + " has a getter, but it could not be invoked.", ex);
            }
        } else {
            throw new RuntimeException(this.toString() + " must have a getter to invoke the getValue() method.");
        }
    }
    
    public void setValue(C object, Object value) {
        if (this.setter != null) {
            try {
                this.setter.invoke(object, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException(this.toString() + " has a setter, but it could not be invoked with the value '" + value + "'.", ex);
            }
        } else {
            throw new RuntimeException(this.toString() + " must have a setter to invoke the setValue() method.");
        }
    }
    
    private static String methodSignatureForFieldName(String verb, String fieldName) {
        return verb + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(containingClass.getSimpleName());
        sb.append(".");
        sb.append(fieldName);
        sb.append("<");
        sb.append(fieldClass.getSimpleName());
        sb.append(">[");
        sb.append(getter != null ? "R" : "-");
        sb.append(setter != null ? "W" : "-");
        sb.append("]");
        
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.containingClass);
        hash = 13 * hash + Objects.hashCode(this.fieldName);
        hash = 13 * hash + Objects.hashCode(this.fieldClass);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BeanBindableField<?> other = (BeanBindableField<?>) obj;
        if (!Objects.equals(this.fieldName, other.fieldName)) {
            return false;
        }
        if (!Objects.equals(this.containingClass, other.containingClass)) {
            return false;
        }
        if (!Objects.equals(this.fieldClass, other.fieldClass)) {
            return false;
        }
        return true;
    }
}
