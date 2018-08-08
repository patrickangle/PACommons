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
package com.patrickangle.commons.objectediting.util.listeditor;

import com.patrickangle.commons.beansbinding.swing.models.ObservableListModel;
import com.patrickangle.commons.observable.collections.ObservableList;
import com.patrickangle.commons.observable.collections.ObservableListListener;
import com.patrickangle.commons.observable.support.TableModelSupport;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Patrick Angle
 */
public class ObjectEditingListTableModel<E> extends ObservableListModel<E> implements TableModel {
    protected TableModelSupport tableModelSupport = new TableModelSupport(this);
    protected ObservableListListener observableListListener;
    
    protected Class<E> itemClass;

    public ObjectEditingListTableModel(ObservableList<E> items, Class<E> itemClass) {
        super(items);
        this.itemClass = itemClass;
        commonInit();
    }

    public ObjectEditingListTableModel(Class<E> itemClass) {
        super(itemClass);
        this.itemClass = itemClass;
        commonInit();
    }
    
    private void commonInit() {
        this.observableListListener = new ObservableListListener() {
            @Override
            public void elementsAdded(ObservableList list, int startIndex, int length, List newElements) {
                tableModelSupport.fireInserted(startIndex, startIndex+ length, TableModelEvent.ALL_COLUMNS);
            }

            @Override
            public void elementsRemoved(ObservableList list, int startIndex, int length, List oldElements) {
                tableModelSupport.fireDeleted(startIndex, startIndex + oldElements.size(), TableModelEvent.ALL_COLUMNS);
            }

            @Override
            public void elementReplaced(ObservableList list, int index, Object oldElement, Object newElement) {
                tableModelSupport.fireUpdated(index, index, TableModelEvent.ALL_COLUMNS);
            }

            @Override
            public void elementPropertyChanged(ObservableList list, int index, Object element, PropertyChangeEvent proeprtyChangeEvent) {
                //
            }
        };
        
        this.items.addObservableListListener(observableListListener);
    }
    
    @Override
    public void setItems(List<E> items) {
        this.items.removeObservableListListener(observableListListener);
        this.tableModelSupport.fireDeleted(0, this.items.size(), TableModelEvent.ALL_COLUMNS);
        
        super.setItems(items);
        
        this.items.addObservableListListener(observableListListener);
        this.tableModelSupport.fireInserted(0, this.items.size(), TableModelEvent.ALL_COLUMNS);
    }
    
    @Override
    public int getRowCount() {
        return this.items.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int i) {
        return "";
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return this.itemClass;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return true;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        return this.items.get(i);
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {
        if (o != null) {
            this.items.set(i, (E) o);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
        this.tableModelSupport.addListDataListener(tl);
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
        this.tableModelSupport.removeListDataListener(tl);
    }
    
}
