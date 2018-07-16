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
        } else {
            throw new RuntimeException("BindableField requires a get method for field '" + fieldName + "' with the signature '" + getterSignature + "' or, for booleans, '" + isSignature +"'");
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
                throw new RuntimeException("Getter could not be invoked", ex);
            }
        } else {
            throw new RuntimeException("BindableField.getValue requires that a getter exist on the underlying class to access the field");
        }
    }
    
    public void setValue(C object, Object value) {
        if (this.setter != null) {
            try {
                this.setter.invoke(object, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException("Setter could not be invoked", ex);
            }
        } else {
            throw new RuntimeException("BindableField.setValue requires that a setter exist on the underlying class to mutate the field");
        }
    }
    
    private static String methodSignatureForFieldName(String verb, String fieldName) {
        return verb + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
    
    @Override
    public String toString() {
        return containingClass.getName() + "." + fieldName + " [ " + ((getter != null) ? getter.getName() + "() -> " + getter.getReturnType().getName() + "" : "-") + " | " + ((setter != null) ? setter.getName() + "(" + Arrays.asList(setter.getParameterTypes()).stream().map((t) -> {
            return t.getName();
        }).collect(Collectors.toList()).toString() + ")" : "-") + " ]";
    }
    
//    private static List<BindableField> bindableFieldsForClass(Class containingClass) {
//        
//    }
    
//    public static List<String> bindableFieldNamesForClass(Class c) {
//        List<String> returnList = new ArrayList<>();
//        if (c != null) {
//            for (Method method : c.getMethods()) {
//                // A getter must start with get, take no parameters, be public, and not return void.
//                if (method.getName().startsWith("get")
//                        && method.getParameterCount() == 0
//                        && Modifier.isPublic(method.getModifiers())
//                        && !method.getReturnType().equals(Void.TYPE)) {
//                    returnList.add(method.getName().substring(3));
//                }
//            }
//        }
//        
//        return returnList;
//    }
}
