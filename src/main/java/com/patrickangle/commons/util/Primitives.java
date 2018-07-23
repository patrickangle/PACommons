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

/**
 *
 * @author patrickangle
 */
public class Primitives {
    /**
     * Attempt to convert the provided object to the provided type, returning
     * the object unchanged if it could not be converted. Primitive types as
     * well as any type that supports being constructed from a single string
     * should be able to be converted to with this method, so long as the
     * provided object produces an accurate {@code toString()} value.
     * @param object
     * @param wantedType
     * @return 
     */
    public static Object convertObjectToType(Object object, Class wantedType) {
        if (wantedType == null) {
            return object;
        } else if (object == null) {
            try {
                return wantedType.getConstructor(String.class).newInstance();
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                // Last-ditch effort to convert type was unsucessful without a constructor accepting a no arguments. Return the original object instead.
                return null;
            }
        } else if (wantedType.isInstance(object)) {
            return object;
        } else {
            String stringValue = object == null ? "" : object.toString();
            if (wantedType == String.class) {
                return stringValue;
            } else if (wantedType == Character.class) {
                return stringValue.charAt(0);
            }
            
            try {
                return wantedType.getConstructor(String.class).newInstance(stringValue);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                // Last-ditch effort to convert type was unsucessful without a constructor accepting a single string argument. Return the original object instead.
                return object;
            }
        }
    }
}
