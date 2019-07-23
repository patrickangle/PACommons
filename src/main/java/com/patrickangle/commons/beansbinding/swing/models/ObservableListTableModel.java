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
package com.patrickangle.commons.beansbinding.swing.models;

import com.patrickangle.commons.beansbinding.BoundFields;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.logging.Logging;
import com.patrickangle.commons.observable.collections.ObservableList;
import com.patrickangle.commons.observable.collections.ObservableListListener;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.observable.support.TableModelSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Patrick Angle
 */
public class ObservableListTableModel<E> extends ObservableListModel<E> implements TableModel {
    protected TableModelSupport tableModelSupport = new TableModelSupport(this);
    
    protected List<ColumnDefinition<E>> columns;
    
    protected Map<E, List<BoundField<E>>> boundFieldMap;
    protected PropertyChangeListener boundFieldPropertyListener;
    
    protected ObservableListListener observableListListener;

    public ObservableListTableModel(ObservableList<E> items, List<ColumnDefinition<E>> columns) {
        super(items);
        this.columns = columns;
        commonInit();
    }
    
    public ObservableListTableModel(Class<E> itemClass, List<ColumnDefinition<E>> columns) {
        super(itemClass);
        this.columns = columns;
        commonInit();
    }
    
    public void fireUpdated() {
        tableModelSupport.fireUpdated(0, items.size(), TableModelEvent.ALL_COLUMNS);
    }
    
    private void commonInit() {
        
        this.boundFieldMap = new HashMap<>();
        
        this.observableListListener = new ObservableListListener() {
            @Override
            public void elementsAdded(ObservableList list, int startIndex, int length, List newElements) {
                for (int i = startIndex; i < startIndex + length; i++) {
                    bindColumnsFor(ObservableListTableModel.this.items.get(i));
                }
                
                tableModelSupport.fireInserted(startIndex, startIndex+ length, TableModelEvent.ALL_COLUMNS);
                propertyChangeSupport.firePropertyChange("items", null, items);
            }

            @Override
            public void elementsRemoved(ObservableList list, int startIndex, int length, List oldElements) {
                oldElements.forEach((item) -> {
                    unbindColumnsFor((E) item);
                });
                
                tableModelSupport.fireDeleted(startIndex, startIndex + oldElements.size(), TableModelEvent.ALL_COLUMNS);
                propertyChangeSupport.firePropertyChange("items", null, items);
            }

            @Override
            public void elementReplaced(ObservableList list, int index, Object oldElement, Object newElement) {
                unbindColumnsFor((E)oldElement);
                bindColumnsFor(ObservableListTableModel.this.items.get(index));
                tableModelSupport.fireUpdated(index, index, TableModelEvent.ALL_COLUMNS);
                propertyChangeSupport.firePropertyChange("items", null, items);
            }

            @Override
            public void elementPropertyChanged(ObservableList list, int index, Object element, PropertyChangeEvent proeprtyChangeEvent) {
                propertyChangeSupport.firePropertyChange("items", null, items);
            }
        };
        
        this.items.addObservableListListener(observableListListener);
        
        this.boundFieldPropertyListener = (propertyChangeEvent) -> {
            int row = ObservableListTableModel.this.items.indexOf(propertyChangeEvent.getSource());
            int column = -1;
            
            for (ColumnDefinition<E> columnDefinition : this.columns) {
                if (columnDefinition.getBinding().getFieldName().equals(propertyChangeEvent.getPropertyName())) {
                    column = this.columns.indexOf(columnDefinition);
                    break;
                }
            }
            
            tableModelSupport.fireUpdated(row, row, column);
        };
        
        bindAllColumns();
    }

    @Override
    public void setItems(List<E> items) {
        unbindAllColumns();
        this.items.removeObservableListListener(observableListListener);
        this.tableModelSupport.fireDeleted(0, this.items.size(), TableModelEvent.ALL_COLUMNS);
        
        super.setItems(items);
        
        bindAllColumns();
        this.items.addObservableListListener(observableListListener);
        this.tableModelSupport.fireInserted(0, this.items.size(), TableModelEvent.ALL_COLUMNS);
    }
    
    protected void unbindAllColumns() {
        boundFieldMap.values().forEach((boundFieldList) -> {
            boundFieldList.forEach((boundField) -> {
                PropertyChangeObservable.removePropertyChangeListener(boundField, boundFieldPropertyListener);
            });
        });
        
        boundFieldMap.clear();
    }
    
    protected void bindAllColumns() {
        for (E item : this.items) {
            bindColumnsFor(item);
        }
    }

    protected void unbindColumnsFor(E item) {
        boundFieldMap.get(item).forEach((boundField) -> {
            PropertyChangeObservable.removePropertyChangeListener(boundField, boundFieldPropertyListener);
        });
    }
    
    protected void bindColumnsFor(E item) {
        List<BoundField<E>> boundFields = new ArrayList<>();
        for (ColumnDefinition<E> columnDefinition : this.columns) {
            BoundField boundField = BoundFields.boundField(item, columnDefinition.getBinding());
            PropertyChangeObservable.addPropertyChangeListener(boundField, boundFieldPropertyListener);
            boundFields.add(boundField);
        }
        boundFieldMap.put(item, boundFields);
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="TableModel Implementation">   
    @Override
    public int getRowCount() {
        return this.getSize();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getBinding().getFieldClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // In this specific type of table model, all rows are treated the same, so we only need to use the columnIndex here.
        return columns.get(columnIndex).getBinding().isWriteable() && columns.get(columnIndex).isMutable();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).getBinding().getValue(getElementAt(rowIndex));
    }

    @Override
    public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
        Logging.warning(this, "Setting value at " + rowIndex + ":" + columnIndex + ". Value is: " + newValue);
        printCondensedStackTrace();
        columns.get(columnIndex).getBinding().setValue(getElementAt(rowIndex), newValue);
    }
    
    public static void printCondensedStackTrace() {
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        StringBuilder print = new StringBuilder("Tracing Thread `").append(Thread.currentThread().getName()).append("`: ");
        for (int i = traces.length - 1; i >= 2; i--) {
            if (i != traces.length - 1) {
                print.append("»");
            }
            String[] classNameParts = traces[i].getClassName().split("\\.");
            print.append(classNameParts[classNameParts.length - 1]).append(".").append(traces[i].getMethodName()).append(":").append(traces[i].getLineNumber());
        }
        Logging.trace(ObservableListTableModel.class, print);
    }

    @Override
    public void addTableModelListener(TableModelListener tableModelListener) {
        this.tableModelSupport.addListDataListener(tableModelListener);
    }

    @Override
    public void removeTableModelListener(TableModelListener tableModelListener) {
        this.tableModelSupport.removeListDataListener(tableModelListener);
    }// </editor-fold>
    
    
    public static class ColumnDefinition<E> {
        protected String name;
        protected BindableField<E> binding;
        protected boolean mutable;
        
        public ColumnDefinition(String name, BindableField<E> binding, boolean mutable) {
            this.name = name;
            this.binding = binding;
            this.mutable = mutable;
        }
        
        public ColumnDefinition(String name, BindableField<E> binding) {
            this(name, binding, binding.isWriteable());
        }
        
        public ColumnDefinition(BindableField<E> binding) {
            this(binding.getFieldName(), binding);
        }

        public String getName() {
            return name;
        }

        public BindableField<E> getBinding() {
            return binding;
        }

        public boolean isMutable() {
            return mutable;
        }
        
        
    }
}
