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
package com.patrickangle.commons.observable.interfaces;

import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Patrick Angle
 */
public interface PropertyChangeObservable {
    public static final String ADD_PROPERTY_CHANGE_LISTENER_METHOD_SIGNATURE = "addPropertyChangeListener";
    public static final String REMOVE_PROPERTY_CHANGE_LISTENER_METHOD_SIGNATURE = "removePropertyChangeListener";
    
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);
    
    public static boolean addPropertyChangeListener(Object object, PropertyChangeListener propertyChangeListener) {
        if (object instanceof PropertyChangeObservable) {
            ((PropertyChangeObservable) object).addPropertyChangeListener(propertyChangeListener);
            return true;
        } else {
            try {
                object.getClass().getMethod(ADD_PROPERTY_CHANGE_LISTENER_METHOD_SIGNATURE, PropertyChangeListener.class).invoke(object, propertyChangeListener);
                return true;
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                return false;
            }
        }
    }
    
    public static boolean removePropertyChangeListener(Object object, PropertyChangeListener propertyChangeListener) {
        if (object instanceof PropertyChangeObservable) {
            ((PropertyChangeObservable) object).removePropertyChangeListener(propertyChangeListener);
            return true;
        } else {
            try {
                object.getClass().getMethod(REMOVE_PROPERTY_CHANGE_LISTENER_METHOD_SIGNATURE, PropertyChangeListener.class).invoke(object, propertyChangeListener);
                return true;
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                return false;
            }
        }
    }
}
