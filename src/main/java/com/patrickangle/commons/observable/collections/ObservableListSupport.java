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
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Patrick Angle
 */
public class ObservableListSupport<E> {
    private ObservableList<E> list;
    
    private CopyOnWriteArrayList<ObservableListListener<E>> observableListListeners;
    
    public ObservableListSupport(ObservableList<E> list) {
        this.list = list;
        this.observableListListeners = new CopyOnWriteArrayList<>();
    }
    
    public void fireElementsAdded(int startIndex, int length, List<E> newElements) {
        observableListListeners.stream().forEach((t) -> {
            t.elementsAdded(list, startIndex, length, newElements);
        });
    }
    
    public void fireElementsRemoved(int startIndex, int length, List<E> oldElements) {
        observableListListeners.stream().forEach((t) -> {
            t.elementsRemoved(list, startIndex, length, oldElements);
        });
    }
    
    public void fireElementReplaced(int index, E oldElement, E newElement) {
        observableListListeners.stream().forEach((t) -> {
            t.elementReplaced(list, index, oldElement, newElement);
        });
    }
    
    public void fireElementPropertyChanged(int index, E element, PropertyChangeEvent proeprtyChangeEvent) {
        observableListListeners.stream().forEach((t) -> {
            t.elementPropertyChanged(list, index, element, proeprtyChangeEvent);
        });
    }
    
    public void addObservableListListener(ObservableListListener<E> listener) {
        observableListListeners.add(listener);        
    }
    
    public void removeObservableListListener(ObservableListListener<E> listener) {
        observableListListeners.remove(listener);
    }
}
