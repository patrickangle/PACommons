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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 *
 * @author Patrick Angle
 */
public class ObservableArrayList<E> extends ArrayList<E> implements ObservableList<E> {

    private ObservableListSupport<E> observableListSupport;
    private PropertyChangeListener elementPropertyChangeListener;
    private ObservableListListener elementListListener;

    public ObservableArrayList() {
        super();
        commonInit();
    }

    public ObservableArrayList(Collection<? extends E> c) {
        super(c);
        commonInit();
    }

    public ObservableArrayList(int initialCapacity) {
        super(initialCapacity);
        commonInit();
    }

    private void commonInit() {
        observableListSupport = new ObservableListSupport<>(this);

        elementPropertyChangeListener = (propertyChangeEvent) -> {
            observableListSupport.fireElementPropertyChanged(this.indexOf(propertyChangeEvent.getSource()), (E) propertyChangeEvent.getSource(), propertyChangeEvent);
        };

        elementListListener = new ObservableListListenerAdapter() {
            @Override
            public void elementPropertyChanged(ObservableList list, int index, Object element, PropertyChangeEvent propertyChangeEvent) {
                observableListSupport.fireElementPropertyChanged(ObservableArrayList.this.indexOf(list), (E) list, propertyChangeEvent);
            }
        };

        this.stream().forEach((t) -> {
            PropertyChangeObservable.addPropertyChangeListener(t, elementPropertyChangeListener);
            if (t instanceof List) {
                ObservableList.addObservableListListener((List) t, elementListListener);
            }
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
        if (oldElement instanceof List) {
            ObservableList.removeObservableListListener((List) oldElement, elementListListener);
        }
        PropertyChangeObservable.addPropertyChangeListener(element, elementPropertyChangeListener);
        if (element instanceof List) {
            ObservableList.addObservableListListener((List) element, elementListListener);
        }

        return oldElement;
    }

    @Override
    public boolean add(E element) {
        boolean success = super.add(element);
        if (success) {
            observableListSupport.fireElementsAdded(this.size() - 1, 1, Collections.singletonList(element));
            PropertyChangeObservable.addPropertyChangeListener(element, elementPropertyChangeListener);
            if (element instanceof List) {
                ObservableList.addObservableListListener((List) element, elementListListener);
            }
        }
        return success;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        observableListSupport.fireElementsAdded(index, 1, Collections.singletonList(element));

        PropertyChangeObservable.addPropertyChangeListener(element, elementPropertyChangeListener);
        if (element instanceof List) {
            ObservableList.addObservableListListener((List) element, elementListListener);
        }
    }

    @Override
    public boolean remove(Object element) {
        int positionOfElement = this.indexOf(element);
        boolean success = super.remove(element);
        if (success) {
            // It should be safe to cast the element to the generic type here, as we know the element was in the list to begin with.
            observableListSupport.fireElementsRemoved(positionOfElement, 1, Collections.singletonList((E) element));
            PropertyChangeObservable.removePropertyChangeListener(element, elementPropertyChangeListener);
            if (element instanceof List) {
                ObservableList.removeObservableListListener((List) element, elementListListener);
            }
        }
        return success;
    }

    @Override
    public E remove(int index) {
        E oldElement = super.remove(index);
        observableListSupport.fireElementsRemoved(index, 1, Collections.singletonList(oldElement));

        PropertyChangeObservable.removePropertyChangeListener(oldElement, elementPropertyChangeListener);
        if (oldElement instanceof List) {
            ObservableList.removeObservableListListener((List) oldElement, elementListListener);
        }
        return oldElement;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return this.addAll(this.size(), c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (super.addAll(index, c)) {
            observableListSupport.fireElementsAdded(index, c.size(), new ArrayList<>(c));

            c.stream().forEach((t) -> {
                PropertyChangeObservable.addPropertyChangeListener(t, elementPropertyChangeListener);
                if (t instanceof List) {
                    ObservableList.addObservableListListener((List) t, elementListListener);
                }
            });

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        List<E> oldElements = new ArrayList<>(this);
        super.clear();
        if (!oldElements.isEmpty()) {
            observableListSupport.fireElementsRemoved(0, oldElements.size(), oldElements);
        }

        oldElements.stream().forEach((t) -> {
            PropertyChangeObservable.removePropertyChangeListener(t, elementPropertyChangeListener);
            if (t instanceof List) {
                ObservableList.removeObservableListListener((List) t, elementListListener);
            }
        });
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        System.err.println("[ObservableArrayList] removeAll(Collection<?> c) is not currently observable. This will be fixed in a future version.");
        return super.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        System.err.println("[ObservableArrayList] removeIf(Predicate<? super E> filter) is not currently observable. This will be fixed in a future version.");
        return super.removeIf(filter);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        System.err.println("[ObservableArrayList] removeRange(int fromIndex, int toIndex) is not currently observable. This will be fixed in a future version.");
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        System.err.println("[ObservableArrayList] replaceAll(UnaryOperator<E> operator) is not currently observable. This will be fixed in a future version.");
        super.replaceAll(operator);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        System.err.println("[ObservableArrayList] retainAll(Collection<?> c) is not currently observable. This will be fixed in a future version.");
        return super.retainAll(c);
    }
}
