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
package com.patrickangle.commons.beansbinding.swing.boundfields;

import com.patrickangle.commons.beansbinding.interfaces.AbstractBoundField;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.swing.models.ObservableListModel;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.util.Classes;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Patrick Angle
 */
public class JTableBoundField extends AbstractBoundField<JTable> {
    public static final String SYNTHETIC_FIELD_SELECTED_ELEMENT = "selectedElement$";
    public static final String SYNTHETIC_FIELD_SELECTED_ELEMENT_IGNORE_ADJUSTING = "selectedElement$ignoreAdjusting";
    public static final String SYNTHETIC_FIELD_SELECTED_ELEMENTS_LIST = "selectedElementsList$";
    public static final String SYNTHETIC_FIELD_SELECTED_ELEMENTS_LIST_IGNORE_ADJUSTING = "selectedElementsList$ignoreAdjusting";
    
    protected JTable containingObject;
    protected BindableField<JTable> bindableField;
    
    protected PropertyChangeListener propertyChangeListener;
    
    protected ListSelectionListener listSelectionListener;
    
    protected Object cachedSelectedElement;
    protected List<Object> cachedSelectedElementsList;
    protected Object cachedSelectedElementIgnoreAdjusting;
    protected List<Object> cachedSelectedElementsListIgnoreAdjusting;
    
    public JTableBoundField(JTable containingObject, BindableField<JTable> bindableField) {
        this.containingObject = containingObject;
        this.bindableField = bindableField;
        
        this.commonInit();
    }
    
    public JTableBoundField(JTable containingObject, String fieldName) {
        this.containingObject = containingObject;
        this.bindableField = BindableFields.forClassWithName(Classes.classFor(containingObject), fieldName);
        
        this.commonInit();
    }
    
    private void commonInit() {
        if (propertyChangeListener == null) {
            propertyChangeListener = (propertyChangeEvent) -> {
                this.propertyChangeSupport.firePropertyChange(propertyChangeEvent);
            };
        }
        
        PropertyChangeObservable.addPropertyChangeListener(containingObject, propertyChangeListener);
        
        // Binding for synthetic updates.
        if (listSelectionListener == null) {
            listSelectionListener = (event) -> {
                if (event.getValueIsAdjusting()) {
                    selectionChanged();
                } else {
                    selectionChanged();
                    selectionChangedIgnoreAdjusting();
                }
            };
        }
        
        this.containingObject.getSelectionModel().addListSelectionListener(listSelectionListener);
    }
    
    public JTable getContainingObject() {
        return this.containingObject;
    }
    
    public BindableField<JTable> getBindableField() {
        return this.bindableField;
    }

    @Override
    public Class getFieldClass() {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_SELECTED_ELEMENT:
            case SYNTHETIC_FIELD_SELECTED_ELEMENT_IGNORE_ADJUSTING:
                return Object.class;
            case SYNTHETIC_FIELD_SELECTED_ELEMENTS_LIST:
            case SYNTHETIC_FIELD_SELECTED_ELEMENTS_LIST_IGNORE_ADJUSTING:
                return List.class;
            default:
                return super.getFieldClass();
        }
    }

    @Override
    public Object getValue() {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_SELECTED_ELEMENT:
            case SYNTHETIC_FIELD_SELECTED_ELEMENT_IGNORE_ADJUSTING:
                return cachedSelectedElement;
            case SYNTHETIC_FIELD_SELECTED_ELEMENTS_LIST:
            case SYNTHETIC_FIELD_SELECTED_ELEMENTS_LIST_IGNORE_ADJUSTING:
                return cachedSelectedElementsList;
            default:
                return super.getValue();
        }
    }

    @Override
    public void setValue(Object value) {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_SELECTED_ELEMENT:
            case SYNTHETIC_FIELD_SELECTED_ELEMENT_IGNORE_ADJUSTING:
            case SYNTHETIC_FIELD_SELECTED_ELEMENTS_LIST:
            case SYNTHETIC_FIELD_SELECTED_ELEMENTS_LIST_IGNORE_ADJUSTING:
                break; // Unsupported set.
            default:
                super.setValue(value);
        }
    }
    
    protected void selectionChanged() {
        Object oldSelectedValue = cachedSelectedElement;
        List<Object> oldSelectedValuesList = cachedSelectedElementsList;
        
        cachedSelectedElement = (containingObject.getSelectedRow() == -1) ? null : ((ObservableListModel) containingObject.getModel()).getElementAt(containingObject.getSelectedRow());
        int[] objectIndexes = containingObject.getSelectedRows();
        List<Object> objects = new ArrayList<>(objectIndexes.length);
        for (int i : objectIndexes) {
            objects.add(((ObservableListModel) containingObject.getModel()).getElementAt(i));
        }
        cachedSelectedElementsList = objects;
        
        this.propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_ELEMENT, oldSelectedValue, cachedSelectedElement);
        this.propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_ELEMENTS_LIST, oldSelectedValuesList, cachedSelectedElementsList);
    }
    
    protected void selectionChangedIgnoreAdjusting() {
        Object oldSelectedValueIgnoreAdjusting = cachedSelectedElementIgnoreAdjusting;
        List<Object> oldSelectedValuesListIgnoreAdjusting = cachedSelectedElementsListIgnoreAdjusting;
        
        cachedSelectedElementIgnoreAdjusting = (containingObject.getSelectedRow() == -1) ? null : ((ObservableListModel) containingObject.getModel()).getElementAt(containingObject.getSelectedRow());
        int[] objectIndexes = containingObject.getSelectedRows();
        List<Object> objects = new ArrayList<>(objectIndexes.length);
        for (int i : objectIndexes) {
            objects.add(((ObservableListModel) containingObject.getModel()).getElementAt(i));
        }
        cachedSelectedElementsListIgnoreAdjusting = objects;
        
        this.propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_ELEMENT_IGNORE_ADJUSTING, oldSelectedValueIgnoreAdjusting, cachedSelectedElementIgnoreAdjusting);
        this.propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_ELEMENTS_LIST_IGNORE_ADJUSTING, oldSelectedValuesListIgnoreAdjusting, cachedSelectedElementsListIgnoreAdjusting);
    }
}
