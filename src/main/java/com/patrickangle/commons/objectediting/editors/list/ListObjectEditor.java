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

import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.beansbinding.BoundFields;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.objectediting.annotations.ObjectEditingProperty;
import com.patrickangle.commons.objectediting.interfaces.CustomObjectEditingComponent;
import com.patrickangle.commons.objectediting.util.ObjectFieldEditorFactory;
import com.patrickangle.commons.util.Annotations;
import com.patrickangle.commons.util.Classes;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 *
 * @author patrickangle
 */
public class ListObjectEditor {
    public static final int LIST_OBJECT_EDITOR_COMPONENT_HEIGHT = 150;
    
    public static ObjectFieldEditorFactory.ComponentReturn createBoundComponentForList(BoundField<List> objectField, BindingGroup bindingGroup) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(objectField.getBindableField()), ObjectEditingProperty.class);

        JPanel listEditor = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getMinimumSize() {
                return new Dimension(super.getMinimumSize().width, LIST_OBJECT_EDITOR_COMPONENT_HEIGHT);
            }
            
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, LIST_OBJECT_EDITOR_COMPONENT_HEIGHT);
            }

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(super.getMaximumSize().width, LIST_OBJECT_EDITOR_COMPONENT_HEIGHT);
            }
            
            
        };

        JTable table = new JTable(new ListObjectEditorTableModel(objectField.getFieldClass()));
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setTableHeader(null);

        ListObjectEditorCellRenderer cellRenderer = new ListObjectEditorCellRenderer();
        if (CustomObjectEditingComponent.class.isAssignableFrom(configInfo.listNewItemClass())) {
            table.getColumnModel().getColumn(0).setCellEditor(cellRenderer);
            table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

            table.setDefaultEditor(Object.class, cellRenderer);
            table.setDefaultRenderer(Object.class, cellRenderer);
        } else {
            table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JTextField()));
            table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));
        }

        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
            @Override
            public Dimension getPreferredSize() {
                Dimension newPreferredSize = super.getPreferredSize();
                newPreferredSize.height = LIST_OBJECT_EDITOR_COMPONENT_HEIGHT;
                return newPreferredSize;
            }

            @Override
            public Dimension getMinimumSize() {
                Dimension newMinimumSize = super.getMinimumSize();
                newMinimumSize.height = LIST_OBJECT_EDITOR_COMPONENT_HEIGHT;
                return newMinimumSize;
            }

        };

        listEditor.add(scrollPane, BorderLayout.CENTER);

        table.setRowHeight(24);
        table.setRowMargin(ListObjectEditorCellRenderer.DEFAULT_ROW_MARGIN);

        Binding listBinding = new BasicBinding(objectField, BoundFields.boundField(table.getModel(), "items"), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(listBinding);

        if (configInfo.listNewItemClass() != Object.class) {
            JToolBar toolbar = new JToolBar();
            toolbar.setFloatable(false);
            toolbar.add(Box.createHorizontalGlue());

            JButton addButton = new JButton("+");
            addButton.addActionListener((actionEvent) -> {
                ((ListObjectEditorTableModel) table.getModel()).getItems().add(Classes.newInstance(configInfo.listNewItemClass()));
                cellRenderer.invalidateCache();
                table.invalidate();
            });
            addButton.putClientProperty("JButton.buttonType", "textured");
            toolbar.add(addButton);

            JButton removeButton = new JButton("−");
            removeButton.addActionListener((actionEvent) -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    ((ListObjectEditorTableModel) table.getModel()).getItems().remove(row);
                    cellRenderer.invalidateCache();
                    table.invalidate();
                }
            });
            removeButton.putClientProperty("JButton.buttonType", "textured");
            toolbar.add(removeButton);

            JButton moveUpButton = new JButton("▲");
            moveUpButton.addActionListener((actionEvent) -> {
                int row = table.getSelectedRow();
                if (row > 0) {
                    Object o = ((ListObjectEditorTableModel) table.getModel()).getItems().remove(row);
                    ((ListObjectEditorTableModel) table.getModel()).getItems().add(row - 1, o);
                    cellRenderer.invalidateCache();
                    table.invalidate();
                }
            });
            moveUpButton.putClientProperty("JButton.buttonType", "textured");
            toolbar.add(moveUpButton);

            JButton moveDownButton = new JButton("▼");
            moveDownButton.addActionListener((actionEvent) -> {
                int row = table.getSelectedRow();
                if (row < table.getRowCount() - 1) {
                    Object o = ((ListObjectEditorTableModel) table.getModel()).getItems().remove(row);
                    ((ListObjectEditorTableModel) table.getModel()).getItems().add(row + 1, o);
                    cellRenderer.invalidateCache();
                    table.invalidate();
                }
            });
            moveDownButton.putClientProperty("JButton.buttonType", "textured");
            toolbar.add(moveDownButton);

            listEditor.add(toolbar, BorderLayout.PAGE_END);
        }

        return new ObjectFieldEditorFactory.ComponentReturn(listEditor, false, true);
    }
}
