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

import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.beansbinding.BoundFields;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.objectediting.annotations.ObjectEditingProperty;
import com.patrickangle.commons.objectediting.util.listeditor.ObjectEditingListCellRenderer;
import com.patrickangle.commons.observable.collections.ObservableCollections;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.util.Annotations;
import com.patrickangle.commons.util.legacy.ListUtils;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author Patrick Angle
 */
public class ObjectEditingList2dTableEditorDialog extends JDialog {
    protected Class itemClass;
    protected List<List<Object>> backingList;
    protected ObjectEditingList2dTableModel tableModel;
    
    public ObjectEditingList2dTableEditorDialog(Dialog parentDialog, boolean modal, BoundField boundField, BindingGroup bindingGroup) {
        super(parentDialog, modal);
        commonInit(boundField, bindingGroup);
        
    }
    
//    public ObjectEditingList2dTableEditorDialog(Frame parentFrame, boolean modal, BoundField boundField, BindingGroup bindingGroup) {
//        super(parentFrame, modal);
//        commonInit(boundField, bindingGroup);
//    }
    
    private void commonInit(BoundField boundField, BindingGroup bindingGroup) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(boundField.getBindableField()), ObjectEditingProperty.class);
        this.itemClass = configInfo.listNewItemClass();

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setType(Type.UTILITY);
        this.setResizable(true);
        this.setTitle("Wayne Romanowski Celebratory LED Cabinet Layout Editor Window");
        
        Binding backingListBinding = new BasicBinding(boundField, BoundFields.boundField(this, "backingList"), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(backingListBinding);
        backingListBinding.setBound(true);
        
        this.tableModel = new ObjectEditingList2dTableModel(backingList);
        
        
        
        JTable table = new JTable(tableModel);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setRowHeight(64);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setDefaultEditor(Object.class, new ObjectEditingListCellRenderer());
        table.setDefaultRenderer(Object.class, new ObjectEditingListCellRenderer());
        table.setGridColor(Color.GRAY);
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        RowHeaderUtils.addNumberColumn(table, 1, false);
        
        Dimension currentArrayDimensions = ListUtils.dimensionsOfArray2dList(backingList);
        JPanel pointEditor = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints;
        
        JLabel xLabel = new JLabel("Columns:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        pointEditor.add(xLabel, gridBagConstraints);
        
        JSpinner spinnerX = new JSpinner(new SpinnerNumberModel(currentArrayDimensions.width, 1, 256, 1));
        spinnerX.addChangeListener((ChangeEvent ce) -> {
            ListUtils.changeColumnsInArray2dList(backingList, (int)spinnerX.getValue());
            tableModel.fireTableDataChanged();
            tableModel.fireTableStructureChanged();
            this.firePropertyChange("backingList", null, this.backingList);
        });
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.weightx = 1.0;
        pointEditor.add(spinnerX, gridBagConstraints);

        JLabel labelY = new JLabel("Rows:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pointEditor.add(labelY, gridBagConstraints);
        
        JSpinner spinnerY = new JSpinner(new SpinnerNumberModel(currentArrayDimensions.height, 1, 256, 1));
        spinnerY.addChangeListener((ChangeEvent ce) -> {
            ListUtils.changeRowsInArray2dList(backingList, (int)spinnerY.getValue());
            tableModel.fireTableDataChanged();
            tableModel.fireTableStructureChanged();
            this.firePropertyChange("backingList", null, this.backingList);
        });
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.weightx = 1.0;
        pointEditor.add(spinnerY, gridBagConstraints);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pointEditor, scrollPane);
        
        this.getContentPane().add(splitPane);
        
        pack();
    }

    public List<List<Object>> getBackingList() {
        return backingList;
    }

    public void setBackingList(List<List<Object>> backingList) {
        this.backingList = backingList;
        if (this.tableModel != null) {
            this.tableModel.fireTableDataChanged();
            this.tableModel.fireTableStructureChanged();
        }
        
    }
    
    
}
