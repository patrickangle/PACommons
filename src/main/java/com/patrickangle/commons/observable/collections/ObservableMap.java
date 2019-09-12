/*
 * Patrick Angle Commons Library
 * Copyright 2018 Patrick Angle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrickangle.commons.observable.collections;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 *
 * @author Patrick Angle
 */
public interface ObservableMap<K, V> extends Map<K, V> {
    public static final String ADD_OBSERVABLE_MAP_LISTENER_METHOD_SIGNATURE = "addObservableMapListener";
    public static final String REMOVE_OBSERVABLE_MAP_LISTENER_METHOD_SIGNATURE = "removeObservableMapListener";
    
    public void addObservableMapListener(ObservableMapListener<K, V> listener);
    public void removeObservableMapListener(ObservableMapListener<K, V> listener);
    
    public static <K, V> boolean addObservableListListener(Map<K, V> map, ObservableMapListener<K, V> observableMapListener) {
        if (map == null) {
            return false;
        }
        
        if (map instanceof ObservableMap) {
            ((ObservableMap) map).addObservableMapListener(observableMapListener);
            return true;
        } else {
            try {
                map.getClass().getMethod(ADD_OBSERVABLE_MAP_LISTENER_METHOD_SIGNATURE, ObservableMap.class).invoke(map, observableMapListener);
                return true;
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                return false;
            }
        }
    }
    
    public static <K, V> boolean removeObservableListListener(Map<K, V> map, ObservableMapListener<K, V> observableMapListener) {
        if (map == null) {
            return false;
        }
        
        if (map instanceof ObservableMap) {
            ((ObservableMap) map).removeObservableMapListener(observableMapListener);
            return true;
        } else {
            try {
                map.getClass().getMethod(REMOVE_OBSERVABLE_MAP_LISTENER_METHOD_SIGNATURE, ObservableMap.class).invoke(map, observableMapListener);
                return true;
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                return false;
            }
        }
    }
}
