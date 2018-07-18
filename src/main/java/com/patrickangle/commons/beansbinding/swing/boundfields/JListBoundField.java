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
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.util.Classes;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Patrick Angle
 */
public class JListBoundField extends AbstractBoundField<JList> {
    public static final String SYNTHETIC_FIELD_SELECTED_VALUE = "selectedValue$";
    public static final String SYNTHETIC_FIELD_SELECTED_VALUE_IGNORE_ADJUSTING = "selectedValue$ignoreAdjusting";
    public static final String SYNTHETIC_FIELD_SELECTED_VALUES_LIST = "selectedValuesList$";
    public static final String SYNTHETIC_FIELD_SELECTED_VALUES_LIST_IGNORE_ADJUSTING = "selectedValuesList$ignoreAdjusting";
    
    protected JList containingObject;
    protected BindableField<JList> bindableField;
    
    protected PropertyChangeListener propertyChangeListener;
    
    protected ListSelectionListener listSelectionListener;
    
    protected Object cachedSelectedValue;
    protected List<Object> cachedSelectedValuesList;
    protected Object cachedSelectedValueIgnoreAdjusting;
    protected List<Object> cachedSelectedValuesListIgnoreAdjusting;
    
    public JListBoundField(JList containingObject, BindableField<JList> bindableField) {
        this.containingObject = containingObject;
        this.bindableField = bindableField;
        
        this.commonInit();
    }
    
    public JListBoundField(JList containingObject, String fieldName) {
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
        
        this.containingObject.addListSelectionListener(listSelectionListener);
    }
    
    public JList getContainingObject() {
        return this.containingObject;
    }
    
    public BindableField<JList> getBindableField() {
        return this.bindableField;
    }

    @Override
    public Class getFieldClass() {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_SELECTED_VALUE:
            case SYNTHETIC_FIELD_SELECTED_VALUE_IGNORE_ADJUSTING:
            case SYNTHETIC_FIELD_SELECTED_VALUES_LIST:
            case SYNTHETIC_FIELD_SELECTED_VALUES_LIST_IGNORE_ADJUSTING:
            default:
                return super.getFieldClass();
        }
    }

    @Override
    public Object getValue() {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_SELECTED_VALUE:
            case SYNTHETIC_FIELD_SELECTED_VALUE_IGNORE_ADJUSTING:
            case SYNTHETIC_FIELD_SELECTED_VALUES_LIST:
            case SYNTHETIC_FIELD_SELECTED_VALUES_LIST_IGNORE_ADJUSTING:
            default:
                return super.getValue();
        }
    }

    @Override
    public void setValue(Object value) {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_SELECTED_VALUE:
            case SYNTHETIC_FIELD_SELECTED_VALUE_IGNORE_ADJUSTING:
                containingObject.setSelectedValue(value, true);
                break;
            case SYNTHETIC_FIELD_SELECTED_VALUES_LIST:
            case SYNTHETIC_FIELD_SELECTED_VALUES_LIST_IGNORE_ADJUSTING:
                break; // Unsupported set.
            default:
                super.setValue(value);
        }
    }
    
    protected void selectionChanged() {
        Object oldSelectedValue = cachedSelectedValue;
        List<Object> oldSelectedValuesList = cachedSelectedValuesList;
        
        cachedSelectedValue = containingObject.getSelectedValue();
        cachedSelectedValuesList = containingObject.getSelectedValuesList();
        
        this.propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_VALUE, oldSelectedValue, cachedSelectedValue);
        this.propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_VALUES_LIST, oldSelectedValuesList, cachedSelectedValuesList);
    }
    
    protected void selectionChangedIgnoreAdjusting() {
        Object oldSelectedValueIgnoreAdjusting = cachedSelectedValueIgnoreAdjusting;
        List<Object> oldSelectedValuesListIgnoreAdjusting = cachedSelectedValuesListIgnoreAdjusting;
        
        cachedSelectedValueIgnoreAdjusting = containingObject.getSelectedValue();
        cachedSelectedValuesListIgnoreAdjusting = containingObject.getSelectedValuesList();
        
        this.propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_VALUE_IGNORE_ADJUSTING, oldSelectedValueIgnoreAdjusting, cachedSelectedValueIgnoreAdjusting);
        this.propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_VALUES_LIST_IGNORE_ADJUSTING, oldSelectedValuesListIgnoreAdjusting, cachedSelectedValuesListIgnoreAdjusting);
    }
}
