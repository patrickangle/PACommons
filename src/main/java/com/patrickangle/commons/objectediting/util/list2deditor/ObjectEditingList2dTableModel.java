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
package com.patrickangle.commons.objectediting.util.list2deditor;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Patrick Angle
 */
public class ObjectEditingList2dTableModel<T> extends AbstractTableModel {
    protected List<List<T>> list2d;
    
    public ObjectEditingList2dTableModel(List<List<T>> list2d) {
        this.list2d = list2d;
    }

    public List<List<T>> getList2d() {
        return list2d;
    }

    public void setList2d(List<List<T>> list2d) {
        this.list2d = list2d;
    }

    @Override
    public int getRowCount() {
        if (list2d.size() > 0) {
            return list2d.get(0).size();
        } else {
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return list2d.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return list2d.get(column).get(row);
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return true;
    }
}
