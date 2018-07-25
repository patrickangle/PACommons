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

import com.patrickangle.commons.observable.collections.ObservableList;
import java.util.List;
import javax.swing.ComboBoxModel;

/**
 *
 * @author patrickangle
 */
public class ObservableComboBoxModel<E> extends ObservableListModel<E> implements ComboBoxModel<E>{
    private E selectedItem;
    
    public ObservableComboBoxModel(ObservableList<E> items, List<E> specialItems) {
        super(items, specialItems);
    }
    
    public ObservableComboBoxModel(Class<E> itemClass, List<E> specialItems) {
        super(itemClass, specialItems);
    }
    
    public ObservableComboBoxModel(ObservableList<E> items) {
        super(items);
    }
    
    public ObservableComboBoxModel(Class<E> itemClass) {
        super(itemClass);
    }
    
    @Override
    public void setSelectedItem(Object selectedItem) {
        E oldSelectedItem = this.selectedItem;
        this.selectedItem = (E) selectedItem;
        this.propertyChangeSupport.firePropertyChange("selectedItem", oldSelectedItem, this.selectedItem);
        this.listDataSupport.fireContentsChanged(-1, -1);
    }

    @Override
    public Object getSelectedItem() {
        return this.selectedItem;
    }    
}
