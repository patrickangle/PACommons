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

import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Patrick Angle
 */
public class ObservableCopyOnWriteArrayList<E> extends CopyOnWriteArrayList<E> implements ObservableList<E> {
    private ObservableListSupport<E> observableListSupport;
    private PropertyChangeListener elementPropertyChangeListener;
    
    public ObservableCopyOnWriteArrayList() {
        super();
        commonInit();
    }
    
    public ObservableCopyOnWriteArrayList(Collection<? extends E> c) {
        super(c);
        commonInit();
    }
    
    public ObservableCopyOnWriteArrayList(E[] toCopyIn) {
        super(toCopyIn);
        commonInit();
    }
    
    private final void commonInit() {
        observableListSupport = new ObservableListSupport<>(this);
        
        elementPropertyChangeListener = (propertyChangeEvent) -> {
            observableListSupport.fireElementPropertyChanged(this.indexOf(propertyChangeEvent.getSource()), (E) propertyChangeEvent.getSource(), propertyChangeEvent);
        };
        
        this.stream().forEach((t) -> {
            PropertyChangeObservable.addPropertyChangeListener(t, elementPropertyChangeListener);
        });
    }

    @Override
    public void addObservableListListener(ObservableListListener<E> listener) {
        observableListSupport.addObservableListListener(listener);
    }

    @Override
    public void removeObservableListListener(ObservableListListener<E> listener) {
        observableListSupport.removeObservableListListener(listener);
    }
    
    @Override
    public E set(int index, E element) {
        E oldElement = super.set(index, element);
        observableListSupport.fireElementReplaced(index, oldElement, element);
        
        PropertyChangeObservable.removePropertyChangeListener(oldElement, elementPropertyChangeListener);
        PropertyChangeObservable.addPropertyChangeListener(element, elementPropertyChangeListener);
        
        return oldElement;
    }
    
    @Override
    public boolean add(E element) {
        boolean success = super.add(element);
        if (success) {
            observableListSupport.fireElementsAdded(this.size() - 1, 1, Collections.singletonList(element));
            PropertyChangeObservable.addPropertyChangeListener(element, elementPropertyChangeListener);
        }
        return success;
    }
    
    @Override
    public void add(int index, E element) {
        super.add(index, element);
        observableListSupport.fireElementsAdded(index, 1, Collections.singletonList(element));
        
        PropertyChangeObservable.addPropertyChangeListener(element, elementPropertyChangeListener);
    }
    
    @Override
    public boolean remove(Object element) {
        int positionOfElement = this.indexOf(element);
        boolean success = super.remove(element);
        if (success) {
            // It should be safe to cast the element to the generic type here, as we know the element was in the list to begin with.
            observableListSupport.fireElementsRemoved(positionOfElement, 1, Collections.singletonList((E) element));
            PropertyChangeObservable.removePropertyChangeListener(element, elementPropertyChangeListener);
        }
        return success;
    }
    
    @Override
    public E remove(int index) {
        E oldElement = super.remove(index);
        observableListSupport.fireElementsRemoved(index, 1, Collections.singletonList(oldElement));
        
        PropertyChangeObservable.removePropertyChangeListener(oldElement, elementPropertyChangeListener);
        
        return oldElement;
    }
    
    @Override
    public boolean addAll(Collection<? extends E> c) {
        return this.addAll(this.size(), c);
    }
    
    public boolean addAll(int index, Collection<? extends E> c) {
        if (super.addAll(index, c)) {
            observableListSupport.fireElementsAdded(index, c.size(), new ArrayList<>(c));
            
            c.stream().forEach((t) -> {
                PropertyChangeObservable.addPropertyChangeListener(t, elementPropertyChangeListener);
            });
            
            return true;
        } else {
            return false;
        }
    }
    
    public void clear() {
        List<E> oldElements = new ArrayList<E>(this);
        super.clear();
        if (oldElements.size() != 0) {
            observableListSupport.fireElementsRemoved(0, oldElements.size(), oldElements);
        }
        
        oldElements.stream().forEach((t) -> {
            PropertyChangeObservable.removePropertyChangeListener(t, elementPropertyChangeListener);
        });
    }
}