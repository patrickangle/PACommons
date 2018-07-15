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
package com.patrickangle.commons.objectediting.util;

import com.patrickangle.commons.beansbinding.BeanBindableField;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.objectediting.annotations.ObjectEditingProperty;
import com.patrickangle.commons.util.Annotations;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author patrickangle
 */
public class ObjectEditingBindings {
    /**
     * A constant defining the field to search for to name groups in an
     * ObjectEditingPanel.
     */
    private static final String OBJECT_EDITING_PROPERTY_GROUP_FIELD_NAME = "USER_EDITABLE_PROPERTY_GROUP";
    
    /**
     * Get a list of BindableFields representing the properties of the provided
     * object that have been annotated as {@code @ObjectEditingProperty}s.
     * @param editingObject
     * @return 
     */
    public static List<BindableField> bindableFieldsForObject(Object editingObject) {
        List<String> fieldNames = Annotations.classFieldsWith(editingObject.getClass(), ObjectEditingProperty.class).stream().map(field ->  field.getName()).collect(Collectors.toList());
        
        List<BindableField> bindableFields = new ArrayList<>();
        
        fieldNames.forEach((fieldName) -> {
            bindableFields.add(new BeanBindableField(editingObject.getClass(), fieldName));
        });
        
        return bindableFields;
    }
    
    /**
     * Get the group name for a {@code BindableField} based on it's containing
     * classes' name, or  if defined as a {@code static String}, its
     * {@code USER_EDITABLE_PROPERTY_GROUP} field.
     * @param bindableField
     * @return 
     */
    public static String getGroupNameForBindableField(BindableField bindableField) {
        Field field = BindableFields.reflectionFieldForBindableField(bindableField);
        return getGroupNameForClass(field.getDeclaringClass());
        
    }
    
    /**
     * Get the group name for a {@code Class} based on it's name, or if defined
     * as a {@code static String}, its {@code USER_EDITABLE_PROPERTY_GROUP}
     * field.
     * @param bindableField
     * @return 
     */
    public static String getGroupNameForClass(Class c) {
        for (Field declaredField : c.getDeclaredFields()) {
            if (declaredField.getName().equals(OBJECT_EDITING_PROPERTY_GROUP_FIELD_NAME)
                    && Modifier.isStatic(declaredField.getModifiers())
                    && String.class.isAssignableFrom(declaredField.getType())) {
                try {
                    return (String) declaredField.get(null);
                } catch (IllegalAccessException | IllegalArgumentException ex) {}
            }
        }
        
        return c.getSimpleName();
    }
    
    public static String nameForBindableField(BindableField bindableField) {
        String nameFromAnnotation = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(bindableField), ObjectEditingProperty.class).name();
        if (!nameFromAnnotation.equals("")) {
            return nameFromAnnotation;
        } else {
            return bindableField.getFieldName();
        }
    }
}