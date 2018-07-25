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
package com.patrickangle.commons.beansbinding.swing.models;

import com.patrickangle.commons.observable.collections.ObservableCollections;
import com.patrickangle.commons.observable.collections.ObservableList;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.observable.support.ListDataSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author patrickangle
 */
public class ObservableListModel<E> implements ListModel<E>, PropertyChangeObservable {
    protected List<E> specialItems;
    protected ObservableList<E> items;
    
    protected final PropertyChangeSupport propertyChangeSupport;
    protected final ListDataSupport listDataSupport;
    
    public ObservableListModel(ObservableList<E> items, List<E> specialItems) {
        this.items = items;
        
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.listDataSupport = new ListDataSupport(this);
        
        this.specialItems = specialItems;
        
        this.items.addObservableListListener(listDataSupport.getObservableListListener());
    }
    
    public ObservableListModel(Class<E> itemClass, List<E> specialItems) {
        this(ObservableCollections.concurrentObservableList(itemClass), specialItems);
    }
    
    public ObservableListModel(ObservableList<E> items) {
        this(items, new ArrayList<>());
    }
    
    public ObservableListModel(Class<E> itemClass) {
        this(itemClass, new ArrayList<>());
    }
    
    public void setItems(ObservableList<E> items) {
        ObservableList oldItems = this.items;
        
        this.items.removeObservableListListener(listDataSupport.getObservableListListener());
        this.items = (ObservableList<E>) ObservableCollections.concurrentObservableList(Object.class);
        this.listDataSupport.fireIntervalRemoved(0, oldItems.size());
        
        this.items = items;
        this.items.addObservableListListener(listDataSupport.getObservableListListener());
        
        this.listDataSupport.fireIntervalAdded(0, this.items.size());
        this.propertyChangeSupport.firePropertyChange("items", oldItems, this.items);
    }
    
    public ObservableList<E> getItems() {
        return items;
    }

    @Override
    public int getSize() {
        return specialItems.size() + items.size();
    }

    @Override
    public E getElementAt(int index) {
        if (index < specialItems.size()) {
            return specialItems.get(index);
        } else {
            return items.get(index - specialItems.size());
        }
        
    }

    @Override
    public void addListDataListener(ListDataListener listDataListener) {
        listDataSupport.addListDataListener(listDataListener);
    }

    @Override
    public void removeListDataListener(ListDataListener listDataListener) {
        listDataSupport.removeListDataListener(listDataListener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
       propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
    }
    
}
