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

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 *
 * @author patrickangle
 */
public class ObservableListListenerAdapter<E> implements ObservableListListener<E>{

    @Override
    public void elementsAdded(ObservableList<E> list, int startIndex, int length, List<E> newElements) {
        listChanged();
    }

    @Override
    public void elementsRemoved(ObservableList<E> list, int startIndex, int length, List<E> oldElements) {
        listChanged();
    }

    @Override
    public void elementReplaced(ObservableList<E> list, int index, E oldElement, E newElement) {
        listChanged();
    }

    @Override
    public void elementPropertyChanged(ObservableList<E> list, int index, E element, PropertyChangeEvent propertyChangeEvent) {
        listChanged();
    }
    
    public void listChanged() {
        
    }
    
}
