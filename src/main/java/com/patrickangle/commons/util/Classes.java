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

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patrickangle
 */
public class Classes {
    /**
     * Get the {@code Class} of the provided object, strongly typed to conform
     * to {@code Class<C>} when the provided object is of type {@code C}.
     * @param <C> The type of the provided object and the resulting
     * {@code Class}.
     * @param object The object from which to get the class.
     * @return The strongly typed {@code Class<C>} for the provided object.
     */
    public static <C extends Object> Class<C> classFor(C object) {
        return (Class<C>) object.getClass();
    }
    
    public static Class primitaveClassFor(Class clazz) {
        if (clazz == Short.class) {
            return Short.TYPE;
        } else if (clazz == Byte.class) {
            return Byte.TYPE;
        } else if (clazz == Integer.class) {
            return Integer.TYPE;
        } else if (clazz == Long.class) {
            return Long.TYPE;
        } else if (clazz == Float.class) {
            return Float.TYPE;
        } else if (clazz == Double.class) {
            return Double.TYPE;
        } else if (clazz == Character.class) {
            return Character.TYPE;
        } else if (clazz == Boolean.class) {
            return Boolean.TYPE;
        } else {
            return clazz;
        }
    }
    
    public static Class primitaveClassFor(Object object) {
        return primitaveClassFor(object.getClass());
    }
    
    public static <E extends Object> E newInstance(Class<E> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
