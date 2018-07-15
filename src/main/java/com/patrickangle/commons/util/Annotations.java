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
package com.patrickangle.commons.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author patrickangle
 */
public class Annotations {
    public static <T extends Annotation> T valueFromAnnotationOnField(Field field, Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }
    
    public static <T extends Annotation> T valueFromAnnotationOnMethod(Method method, Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }

    public static List<Method> classMethodsWith(final Class<?> type, final Class<?> annotation) {
        final List<Method> methods = new ArrayList<>();
        Class<?> klass = type;
        while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
            // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
            final List<Method> allMethods = new ArrayList<>(Arrays.asList(klass.getDeclaredMethods()));
            for (final Method method : allMethods) {
                if (method.isAnnotationPresent((Class<? extends java.lang.annotation.Annotation>) annotation)) {
                    methods.add(method);
                }
            }
            // move to the upper class in the hierarchy in search for more methods
            klass = klass.getSuperclass();
        }
        return methods;
    }
    
    public static List<Field> classFieldsWith(final Class<?> type, final Class<?> annotation) {
        final List<Field> fields = new ArrayList<>();
        Class<?> klass = type;
        while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
            // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
            final List<Field> allFields = new ArrayList<>(Arrays.asList(klass.getDeclaredFields()));
            for (final Field field : allFields) {
                if (field.isAnnotationPresent((Class<? extends java.lang.annotation.Annotation>) annotation)) {
                    fields.add(field);
                }
            }
            // move to the upper class in the hierarchy in search for more methods
            klass = klass.getSuperclass();
        }
        return fields;
    }
}
