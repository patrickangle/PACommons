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
package com.patrickangle.commons.objectediting.editors.list;

import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.objectediting.interfaces.CustomObjectEditingComponent;
import com.patrickangle.commons.util.Colors;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Patrick Angle
 */
public class ListObjectEditorCellRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    
    public static final Border selectedBorder = new MatteBorder(3, 9, 3, 3, UIManager.getColor("TextField.selectionBackground"));
    public static final Border unselectedBorder = new MatteBorder(3, 9, 3, 3, UIManager.getColor("Viewport.background"));

    private final Map<Object, JComponent> cachedEditors = new HashMap<>();

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    public ListObjectEditorCellRenderer() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (!cachedEditors.containsKey(value)) {
            // No editor is cached, we need to create one.
            JComponent newComponent;

            if (value instanceof CustomObjectEditingComponent) {
                BindingGroup bindingGroup = new BindingGroup();

                newComponent = ((CustomObjectEditingComponent) value).customObjectEditingComponent(null, bindingGroup, null).getComponent();

                bindingGroup.bind();

            } else {
                newComponent = new JLabel("Editing of this property is not supported.");
            }
            newComponent.setBackground(Colors.transparentColor());
            newComponent.setOpaque(false);
            JPanel containerPanel = new JPanel(new BorderLayout());
            containerPanel.add(newComponent, BorderLayout.CENTER);
            cachedEditors.put(value, containerPanel);
        }

        JComponent returnComponent = cachedEditors.get(value);
        

        returnComponent.setBorder(unselectedBorder);
        

//        returnComponent.addPropertyChangeListener((PropertyChangeEvent evt) -> {
//            if (evt.getPropertyName().equals("preferredSize")) {
//                jtable.setRowHeight(row, returnComponent.getPreferredSize().height + DEFAULT_ROW_MARGIN);
//            }
//        });

        jtable.setRowHeight(row, returnComponent.getComponent(0).getPreferredSize().height + 3);

        return returnComponent;
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object value, boolean isSelected, int row, int column) {
        // We always know that our renderer returns a JComponent, and more specifically a JPanel.
        JComponent c = (JComponent) getTableCellRendererComponent(jtable, value, isSelected, false, row, column);
        c.setBorder(selectedBorder);
        return c;
    }
    
    public void invalidateCache() {
        cachedEditors.clear();
    }
}
