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
package com.patrickangle.commons.beansbinding.util;

import com.patrickangle.commons.beansbinding.BeanBindableField;
import com.patrickangle.commons.beansbinding.SyntheticBindableField;
import com.patrickangle.commons.beansbinding.interfaces.SyntheticFieldProvider;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.util.Fields;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Patrick Angle
 */
public class BindableFields {
    public static <C extends Object> BindableField<C> forClassWithName(Class<C> containingClass, String fieldName) {
        if (fieldName.contains("$") && !fieldName.startsWith("$")) {
            return new SyntheticBindableField<>((Class) containingClass, fieldName);
        } else {
            return new BeanBindableField<>(containingClass, fieldName);
        }
    }
    
    public static <C extends Object> List<BindableField<C>> forClass(Class<C> containingClass) {
        List<BindableField<C>> returnList = new ArrayList<>();
        
        for (Method method : containingClass.getMethods()) {
            // A getter must start with get, take no parameters, be public, and not return void.
            if ((method.getName().startsWith("get") || method.getName().startsWith("is"))
                    && method.getParameterCount() == 0
                    && Modifier.isPublic(method.getModifiers())
                    && !method.getReturnType().equals(Void.TYPE)) {
                // Reformat name to be in the proper case
                String fieldName = method.getName().substring((method.getName().startsWith("get") ? 3 : 2));
                fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);

                // Try and create a BeanBindableField, but if that fails no further action is needed.
                try {
                    returnList.add(new BeanBindableField<>(containingClass, fieldName));
                } catch (Exception e) {}
            }
        }
        
        if (SyntheticBindableField.class.isAssignableFrom(containingClass)) {
            try {
                List<String> availableSyntheticFieldNames = (List<String>) containingClass.getMethod("syntheticFieldNames").invoke(null);
                
                availableSyntheticFieldNames.forEach((syntheticFieldName) -> {
                    try {
                        returnList.add((BindableField<C>) new SyntheticBindableField<>((Class<? extends SyntheticFieldProvider>) containingClass, syntheticFieldName));
                    } catch (Exception e) {}
                });
            } catch (Exception e) {}
        }
        
        return returnList;
    }
    
 
    
    public static <C extends Object> List<BindableField<C>> forClassIsAssignable(Class<C> containingClass, Class isAssignableToClass) {
        return BindableFields.forClass(containingClass).stream().filter((t) -> {
            return t.getFieldClass().equals(isAssignableToClass);
        }).collect(Collectors.toList());
    }
    
    public static Field reflectionFieldForBindableField(BindableField bindableField) {
        return Fields.fieldForNameInClass(bindableField.getContainingClass(), bindableField.getFieldName());
    }
}
