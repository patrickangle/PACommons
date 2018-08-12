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
package com.patrickangle.commons.objectediting.util;

import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.BasicBoundField;
import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.beansbinding.BoundFields;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.JSpinnerBoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.JTextComponentBoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.AbstractButtonBoundField;
import com.patrickangle.commons.beansbinding.swing.models.ObservableComboBoxModel;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.objectediting.annotations.ObjectEditingProperty;
import com.patrickangle.commons.objectediting.interfaces.CustomObjectEditingComponent;
import com.patrickangle.commons.objectediting.util.list2deditor.ObjectEditingList2dTableEditorDialog;
import com.patrickangle.commons.objectediting.util.listeditor.ObjectEditingListCellRenderer;
import com.patrickangle.commons.objectediting.util.listeditor.ObjectEditingListTableModel;
import com.patrickangle.commons.util.Annotations;
import com.patrickangle.commons.util.Classes;
import com.patrickangle.commons.util.legacy.ListUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author patrickangle
 */
public class ObjectFieldEditorFactory {
    public static final class ComponentReturn {
        public JComponent component;
        public boolean selfLabeled;
        public boolean multiLineEditor;

        public ComponentReturn(JComponent component, boolean selfLabeled) {
            this.component = component;
            this.selfLabeled = selfLabeled;
        }
        
        public ComponentReturn(JComponent component, boolean selfLabeled, boolean multiLineEditor) {
            this.component = component;
            this.selfLabeled = selfLabeled;
            this.multiLineEditor = multiLineEditor;
        }

        public JComponent getComponent() {
            return component;
        }

        public boolean isSelfLabeled() {
            return selfLabeled;
        }
        
        public boolean isMultiLineEditor() {
            return multiLineEditor;
        }
    }
    
    public static ComponentReturn createEditorForObject(Object containingObject, BindableField bindableField, BindingGroup bindingGroup) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(bindableField), ObjectEditingProperty.class);
        
        BoundField objectField = BoundFields.boundField(containingObject, bindableField);
        Class fieldClass = Classes.primitaveClassFor(bindableField.getFieldClass());
        
        if (!configInfo.mutable()) {
            return createBoundComponentForNonMutableObject(objectField, bindingGroup);
        } else if (CustomObjectEditingComponent.class.isAssignableFrom(fieldClass)) {
            // If a custom editor is available for a class type, it will always be favored over all default editors.
            return ((CustomObjectEditingComponent) objectField.getValue()).customObjectEditingComponent(bindingGroup);
        } else if (Enum.class.isAssignableFrom(fieldClass)) {
            return createBoundComponentForEnum(objectField, bindingGroup);
        } else if (fieldClass == Boolean.TYPE) {
            return createBoundComponentForBoolean(objectField, bindingGroup);
        } else if (fieldClass == Integer.TYPE) {
            return createBoundComponentForFixedPointNumber(objectField, bindingGroup);
        } else if (fieldClass == Float.TYPE || fieldClass == Double.TYPE) {
            return createBoundComponentForFloatingPointNumber(objectField, bindingGroup);
        } else if (Color.class.isAssignableFrom(objectField.getFieldClass())) {
            return createBoundComponentForColor(objectField, bindingGroup);
        } else if (List.class.isAssignableFrom(objectField.getFieldClass())) {
            int depth = ListUtils.depthOfMultiDimensionList(objectField.getValue());
            switch(depth) {
                case 0:
                    // Zero is returned if the list is empty, so it really should be treated as a depth of one.
                case 1:
                    return createBoundComponentForList(objectField, bindingGroup);
                case 2:
                    return createBoundComponentFor2dList(objectField, bindingGroup);
                default:
                    return new ComponentReturn(new JLabel("List too deep."), false);
            }
        } else {
            if (configInfo.stringMultilineEditor()) {
                return createBoundComponentForMultilineString(objectField, bindingGroup);
            } else {
                return createBoundComponentForString(objectField, bindingGroup);
            }
        }
    }
    
    private static ComponentReturn createBoundComponentForNonMutableObject(BoundField objectField, BindingGroup bindingGroup) {
        JLabel labelComponent = new JLabel("");
        
        Binding binding = new BasicBinding(objectField, BoundFields.boundField(labelComponent, "text"), Binding.UpdateStrategy.READ_ONLY);
        bindingGroup.add(binding);
        
        return new ComponentReturn(labelComponent, false);
    }
    
    private static ComponentReturn createBoundComponentForBoolean(BoundField objectField, BindingGroup bindingGroup) {
        JCheckBox checkBox = new JCheckBox(ObjectEditingBindings.nameForBindableField(objectField.getBindableField()));
        
        BasicBinding binding = new BasicBinding(objectField, BoundFields.boundField(checkBox, AbstractButtonBoundField.SYNTHETIC_FIELD_SELECTED), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(binding);
        
        return new ComponentReturn(checkBox, true);
    }
    
    private static ComponentReturn createBoundComponentForFixedPointNumber(BoundField objectField, BindingGroup bindingGroup) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(objectField.getBindableField()), ObjectEditingProperty.class);
        
        
        switch (configInfo.numberEditor()) {
            case TEXT_CONTROL:
            case SLIDER_CONTROL:
            case SPINNER_CONTROL:
            default:
                JSpinner spinner = new JSpinner(new SpinnerNumberModel(
                        (int)configInfo.numberMinimumValue(),
                        (int)configInfo.numberMinimumValue(),
                        (int)configInfo.numberMaximumValue(),
                        (int)configInfo.numberStepValue()
                ));
                JSpinner.NumberEditor spinnerNumberEditor = new JSpinner.NumberEditor(spinner);
                spinnerNumberEditor.getFormat().setGroupingUsed(false);
                spinner.setEditor(spinnerNumberEditor);
                
                BasicBinding binding = new BasicBinding(objectField, BoundFields.boundField(spinner, JSpinnerBoundField.SYNTHETIC_FIELD_VALUE), Binding.UpdateStrategy.READ_WRITE);
                bindingGroup.add(binding);
                
                return new ComponentReturn(spinner, false);
        }
    }
    
    private static ComponentReturn createBoundComponentForFloatingPointNumber(BoundField objectField, BindingGroup bindingGroup) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(objectField.getBindableField()), ObjectEditingProperty.class);
        
        
        switch (configInfo.numberEditor()) {
            case TEXT_CONTROL:
                return createBoundComponentForString(objectField, bindingGroup);
            case SLIDER_CONTROL:
            case SPINNER_CONTROL:
            default:
                JSpinner spinner = new JSpinner(new SpinnerNumberModel(
                        (double)configInfo.numberMinimumValue(),
                        (double)configInfo.numberMinimumValue(),
                        (double)configInfo.numberMaximumValue(),
                        (double)configInfo.numberStepValue()
                ));
                JSpinner.NumberEditor spinnerNumberEditor = new JSpinner.NumberEditor(spinner);
                spinnerNumberEditor.getFormat().setGroupingUsed(false);
                spinnerNumberEditor.getFormat().setDecimalSeparatorAlwaysShown(true);
                spinner.setEditor(spinnerNumberEditor);
                
                BasicBinding binding = new BasicBinding(objectField, BoundFields.boundField(spinner, JSpinnerBoundField.SYNTHETIC_FIELD_VALUE), Binding.UpdateStrategy.READ_WRITE);
                bindingGroup.add(binding);
                
                return new ComponentReturn(spinner, false);
        }
    }
    
    private static ComponentReturn createBoundComponentForString(BoundField objectField, BindingGroup bindingGroup) {
        JTextField textField = new JTextField();
        
        BasicBinding binding = new BasicBinding(objectField, BoundFields.boundField(textField, JTextComponentBoundField.SYNTHETIC_FIELD_TEXT), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(binding);

        return new ComponentReturn(textField, false);
    }
    
    private static ComponentReturn createBoundComponentForMultilineString(BoundField objectField, BindingGroup bindingGroup) {
        JTextArea textField = new JTextArea();
        textField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        textField.setTabSize(4);
        BasicBinding binding = new BasicBinding(objectField, BoundFields.boundField(textField, JTextComponentBoundField.SYNTHETIC_FIELD_TEXT), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(binding);
        
        JScrollPane scrollPane = new JScrollPane(textField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 100);
            }
            
        };


        return new ComponentReturn(scrollPane, false, true);
    }
    
    private static ComponentReturn createBoundComponentForColor(BoundField objectField, BindingGroup bindingGroup) {
        JButton colorButton = new JButton();
        
        colorButton.addActionListener((event) -> {
            Color newColor = JColorChooser.showDialog(null, ObjectEditingBindings.nameForBindableField(objectField.getBindableField()), colorButton.getBackground());
            
            if (newColor != null) {
                colorButton.setBackground(newColor);
            }
        });
        
        BasicBinding binding = new BasicBinding(objectField, new BasicBoundField(colorButton, "background"), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(binding);
        
        return new ComponentReturn(colorButton, false);
    }
    
    public static ComponentReturn createBoundComponentForList(BoundField<List> objectField, BindingGroup bindingGroup) {
        ObjectEditingProperty configInfo = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(objectField.getBindableField()), ObjectEditingProperty.class);

        JPanel listEditor = new JPanel(new BorderLayout());
        
        JTable table = new JTable(new ObjectEditingListTableModel(objectField.getFieldClass()));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setTableHeader(null);
        
        if (CustomObjectEditingComponent.class.isAssignableFrom(configInfo.listNewItemClass())) {
            table.getColumnModel().getColumn(0).setCellEditor(new ObjectEditingListCellRenderer());
            table.getColumnModel().getColumn(0).setCellRenderer(new ObjectEditingListCellRenderer());
            
            table.setDefaultEditor(Object.class, new ObjectEditingListCellRenderer());
            table.setDefaultRenderer(Object.class, new ObjectEditingListCellRenderer());
        } else {
            table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JTextField()));
            table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));
        }
        
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
            private static final int MIN_HEIGHT = 125;
            @Override
            public Dimension getPreferredSize() {
                Dimension newPreferredSize = super.getPreferredSize();
                newPreferredSize.height = MIN_HEIGHT;
                return newPreferredSize;
            }

            @Override
            public Dimension getMinimumSize() {
                Dimension newMinimumSize = super.getMinimumSize();
                newMinimumSize.height = MIN_HEIGHT;
                return newMinimumSize;
            }
            
            
        };
        
        
        listEditor.add(scrollPane, BorderLayout.CENTER);
        
        table.setRowHeight(24);
        table.setRowMargin(6);
        
        Binding listBinding = new BasicBinding(objectField, BoundFields.boundField(table.getModel(), "items"), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(listBinding);
        
        if (configInfo.listNewItemClass() != Object.class) {
            JToolBar toolbar = new JToolBar();
            toolbar.setFloatable(false);
            toolbar.add(Box.createHorizontalGlue());
            
            JButton addButton = new JButton("+");
            addButton.addActionListener((actionEvent) -> {
                ((ObjectEditingListTableModel) table.getModel()).getItems().add(Classes.newInstance(configInfo.listNewItemClass()));
            });
            toolbar.add(addButton);
            
            JButton removeButton = new JButton("−");
            removeButton.addActionListener((actionEvent) -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    ((ObjectEditingListTableModel) table.getModel()).getItems().remove(row);
                }
            });
            toolbar.add(removeButton);

            JButton moveUpButton= new JButton("▲");
            moveUpButton.addActionListener((actionEvent) -> {
                int row = table.getSelectedRow();
                if (row > 0) {
                    Object o = ((ObjectEditingListTableModel) table.getModel()).getItems().remove(row);
                    ((ObjectEditingListTableModel) table.getModel()).getItems().add(row - 1, o);
                }
            });
            toolbar.add(moveUpButton);
            
            JButton moveDownButton= new JButton("▼");
            moveDownButton.addActionListener((actionEvent) -> {
                int row = table.getSelectedRow();
                if (row < table.getRowCount() - 1) {
                    Object o = ((ObjectEditingListTableModel) table.getModel()).getItems().remove(row);
                    ((ObjectEditingListTableModel) table.getModel()).getItems().add(row + 1, o);
                }
            });
            toolbar.add(moveDownButton);

            listEditor.add(toolbar, BorderLayout.PAGE_END);
        }
        
        return new ComponentReturn(listEditor, false, true);
    }
    
    public static ComponentReturn createBoundComponentFor2dList(BoundField<List<List>> boundField, BindingGroup bindingGroup) {
        JButton editButton = new JButton("Open Editor");
        editButton.addActionListener((actionEvent) -> {
            BindingGroup editorBindingGroup = new BindingGroup();
            new ObjectEditingList2dTableEditorDialog(null, true, boundField, editorBindingGroup).setVisible(true);
            editorBindingGroup.bind();
        });
        return new ComponentReturn(editButton, false);
    }
    
    public static ComponentReturn createBoundComponentForEnum(BoundField boundField, BindingGroup bindingGroup) {
        JComboBox comboBox = new JComboBox(new ObservableComboBoxModel(boundField.getFieldClass(), Arrays.asList(boundField.getFieldClass().getEnumConstants())));
        
        Binding selectionBinding = new BasicBinding(boundField, BoundFields.boundField(comboBox.getModel(), "selectedItem"), Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(selectionBinding);
        
        return new ComponentReturn(comboBox, false);
    }
}
