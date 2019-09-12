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

import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 *
 * @author Patrick Angle
 */
public interface ObservableList<E> extends List<E> {
    public static final String ADD_OBSERVABLE_LIST_LISTENER_METHOD_SIGNATURE = "addObservableListListener";
    public static final String REMOVE_OBSERVABLE_LIST_LISTENER_METHOD_SIGNATURE = "removeObservableListListener";
    
    public void addObservableListListener(ObservableListListener<E> listener);
    public void removeObservableListListener(ObservableListListener<E> listener);
    
    public static <E> boolean addObservableListListener(List<E> list, ObservableListListener<E> observableListListener) {
        if (list == null) {
            return false;
        }
        
        if (list instanceof ObservableList) {
            ((ObservableList) list).addObservableListListener(observableListListener);
            return true;
        } else {
            try {
                list.getClass().getMethod(ADD_OBSERVABLE_LIST_LISTENER_METHOD_SIGNATURE, ObservableListListener.class).invoke(list, observableListListener);
                return true;
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                return false;
            }
        }
    }
    
    public static <E> boolean removeObservableListListener(List<E> list, ObservableListListener<E> observableListListener) {
        if (list == null) {
            return false;
        }
        
        if (list instanceof ObservableList) {
            ((ObservableList) list).removeObservableListListener(observableListListener);
            return true;
        } else {
            try {
                list.getClass().getMethod(REMOVE_OBSERVABLE_LIST_LISTENER_METHOD_SIGNATURE, ObservableListListener.class).invoke(list, observableListListener);
                return true;
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                return false;
            }
        }
    }
}
