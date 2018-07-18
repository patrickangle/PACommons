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

import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.observable.collections.ObservableList;
import com.patrickangle.commons.observable.collections.ObservableListListener;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservableBase;
import com.patrickangle.commons.observable.support.TableModelSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.TableModelListener;

/**
 *
 * @author Patrick Angle
 */
public class ObservableTableModelColumnMapper<E> {
    protected TableModelSupport tableModelSupport;
    
    protected ObservableList<E> items;
    protected ObservableListListener backingListListener;
    
    protected List<ColumnDefinition<E>> columns;
    
    protected Map<E, List<BoundField<E>>> boundFieldMap;
    
    public ObservableTableModelColumnMapper(ObservableList<E> items, List<ColumnDefinition<E>> columns) {
        this.items = items;
        this.columns = columns;
        this.boundFieldMap = new HashMap<>();
    }
    
    protected void rebuildBoundFieldMap() {
        
    }
    
    protected void tearDownBoundFieldMap() {
        // Current no implementation here, but eventually BoundField objects may
        // support unbinding their listeners, so this method is present to implement
        // that functionality.
    }
            
    public void addTableModelListener(TableModelListener tableModelListener) {
        this.tableModelSupport.addListDataListener(tableModelListener);
    }

    public void removeTableModelListener(TableModelListener tableModelListener) {
        this.tableModelSupport.removeListDataListener(tableModelListener);
    }
            
    public static class ColumnDefinition<E> {
        protected String name;
        protected BindableField<E> binding;
        protected boolean mutable;
        
        public ColumnDefinition(String name, BindableField<E> binding, boolean mutable) {
            this.name = name;
            this.binding = binding;
            this.mutable = mutable;
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
