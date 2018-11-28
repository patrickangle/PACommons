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
package com.patrickangle.commons.observable.support;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Patrick Angle
 */
public class TableModelSupport {
    private final TableModel sourceBean;
    private final List<TableModelListener> tableModelListeners;
    
    public TableModelSupport(TableModel sourceBean) {
        this.sourceBean = sourceBean;
        this.tableModelListeners = new CopyOnWriteArrayList<>();
    }
    
    public void fireInserted(int firstRow, int lastRow, int column) {
        TableModelEvent event = new TableModelEvent(sourceBean, firstRow, lastRow, column, TableModelEvent.INSERT);
        
        tableModelListeners.forEach((tableModelListener) -> {
            tableModelListener.tableChanged(event);
        });
    }
    
    public void fireUpdated(int firstRow, int lastRow, int column) {
        TableModelEvent event = new TableModelEvent(sourceBean, firstRow, lastRow, column, TableModelEvent.UPDATE);
        
        tableModelListeners.forEach((tableModelListener) -> {
            tableModelListener.tableChanged(event);
        });
    }
    
    public void fireDeleted(int firstRow, int lastRow, int column) {
        TableModelEvent event = new TableModelEvent(sourceBean, firstRow, lastRow, column, TableModelEvent.DELETE);

        tableModelListeners.forEach((tableModelListener) -> {
            tableModelListener.tableChanged(event);
        });
    }
    
    public void addListDataListener(TableModelListener listDataListener) {
        tableModelListeners.add(listDataListener);
    }
    
    public void removeListDataListener(TableModelListener listDataListener) {
        tableModelListeners.remove(listDataListener);
    }
}
