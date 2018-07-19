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

import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.objectediting.interfaces.CustomObjectEditingComponent;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Patrick Angle
 */
public class ObjectEditingListCellRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    protected Object object;
    
    JComponent component;
    
    @Override
    public Object getCellEditorValue() {
        return null;
    }

//    @Override
//    public boolean isCellEditable(EventObject eo) {
//        return true;
//    }
//
//    @Override
//    public boolean shouldSelectCell(EventObject eo) {
//        return true;
//    }
    
    

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.object = object;
        if (CustomObjectEditingComponent.class.isInstance(value)) {
            BindingGroup bindingGroup = new BindingGroup();
            
            component = ((CustomObjectEditingComponent) value).customObjectEditingComponent(bindingGroup).getComponent();
            
            bindingGroup.bind();
            return component;
        } else {
            return new JLabel("Unsupported...");
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object value, boolean isSelected, int row, int column) {
        this.object = object;
        if (CustomObjectEditingComponent.class.isInstance(value)) {
            BindingGroup bindingGroup = new BindingGroup();
            
            component = ((CustomObjectEditingComponent) value).customObjectEditingComponent(bindingGroup).getComponent();
            
            bindingGroup.bind();
            return component;
        } else {
            return new JLabel("Unsupported...");
        }
    }
    
}
