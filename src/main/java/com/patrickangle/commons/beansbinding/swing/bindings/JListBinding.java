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
package com.patrickangle.commons.beansbinding.swing.bindings;

import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.BasicBindingConverter;
import com.patrickangle.commons.beansbinding.BasicBoundField;
import com.patrickangle.commons.beansbinding.BoundFields;
import com.patrickangle.commons.beansbinding.SyntheticBindableField;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import java.beans.PropertyChangeSupport;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.util.Classes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author patrickangle
 * @deprecated Use proper explicit BoundField to construct a binding exposing additional synthetic values.
 */
public class JListBinding<E> extends BasicBinding<E, JList> {
    public static final String SYNTHETIC_FIELD_SELECTED_VALUE = "selectedValue$";
    public static final String SYNTHETIC_FIELD_SELECTED_VALUE_IGNORE_ADJUSTING = "selectedValue$ignoreAdjusting";
    public static final String SYNTHETIC_FIELD_SELECTED_VALUES_LIST = "selectedValuesList$";
    public static final String SYNTHETIC_FIELD_SELECTED_VALUES_LIST_IGNORE_ADJUSTING = "selectedValuesList$ignoreAdjusting";
    
    private PropertyChangeSupport propertyChangeSupport;
    
    protected ListSelectionListener listSelectionListener;
    
    protected Object cachedSelectedValue;
    protected List<Object> cachedSelectedValuesList;
    protected Object cachedSelectedValueIgnoreAdjusting;
    protected List<Object> cachedSelectedValuesListIgnoreAdjusting;

    public JListBinding(E backContainingObject, String backObjectFieldName, JList frontContainingObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        super(
                BoundFields.boundField(
                        backContainingObject,
                        BindableFields.forClassWithName(
                                Classes.classFor(backContainingObject),
                                backObjectFieldName
                        )
                ),
                BoundFields.boundField(
                        frontContainingObject,
                        new SyntheticBindableField<>(
                                Classes.classFor(frontContainingObject),
                                frontObjectFieldName,
                                additionalGetterParamsForFieldName(frontObjectFieldName),
                                additionalSetterParamsForFieldName(frontObjectFieldName)
                        )
                ),
                updateStrategy,
                converter);

        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    public JListBinding(E backObject, String backObjectFieldName, JList frontObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy) {
        this(backObject, backObjectFieldName, frontObject, frontObjectFieldName, updateStrategy, new BasicBindingConverter());
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    private static List<Object> additionalGetterParamsForFieldName(String fieldName) {
        switch (fieldName) {
            case SYNTHETIC_FIELD_SELECTED_VALUE:
            case SYNTHETIC_FIELD_SELECTED_VALUE_IGNORE_ADJUSTING:
            case SYNTHETIC_FIELD_SELECTED_VALUES_LIST:
            case SYNTHETIC_FIELD_SELECTED_VALUES_LIST_IGNORE_ADJUSTING:
            default:
                return Collections.EMPTY_LIST;
        }
    }
    
    private static List<Object> additionalSetterParamsForFieldName(String fieldName) {
        switch (fieldName) {
            case SYNTHETIC_FIELD_SELECTED_VALUE:
            case SYNTHETIC_FIELD_SELECTED_VALUE_IGNORE_ADJUSTING:
                return Arrays.asList(true);
            case SYNTHETIC_FIELD_SELECTED_VALUES_LIST:
            case SYNTHETIC_FIELD_SELECTED_VALUES_LIST_IGNORE_ADJUSTING:
            default:
                return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    protected void bind() {
        super.bind();
        
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
        
        frontBoundField.getContainingObject().addListSelectionListener(listSelectionListener);
        this.propertyChangeSupport.addPropertyChangeListener(frontObjectListener);
    }
    
    @Override
    protected void unbind() {
        super.unbind();
        
        frontBoundField.getContainingObject().removeListSelectionListener(listSelectionListener);
        this.propertyChangeSupport.removePropertyChangeListener(frontObjectListener);
    }
    
    protected void selectionChanged() {
        Object oldSelectedValue = cachedSelectedValue;
        List<Object> oldSelectedValuesList = cachedSelectedValuesList;
        
        cachedSelectedValue = frontBoundField.getContainingObject().getSelectedValue();
        cachedSelectedValuesList = frontBoundField.getContainingObject().getSelectedValuesList();
        
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_VALUE, oldSelectedValue, cachedSelectedValue);
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_VALUES_LIST, oldSelectedValuesList, cachedSelectedValuesList);
    }
    
    protected void selectionChangedIgnoreAdjusting() {
        Object oldSelectedValueIgnoreAdjusting = cachedSelectedValueIgnoreAdjusting;
        List<Object> oldSelectedValuesListIgnoreAdjusting = cachedSelectedValuesListIgnoreAdjusting;
        
        cachedSelectedValueIgnoreAdjusting = frontBoundField.getContainingObject().getSelectedValue();
        cachedSelectedValuesListIgnoreAdjusting = frontBoundField.getContainingObject().getSelectedValuesList();
        
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_VALUE_IGNORE_ADJUSTING, oldSelectedValueIgnoreAdjusting, cachedSelectedValueIgnoreAdjusting);
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_SELECTED_VALUES_LIST_IGNORE_ADJUSTING, oldSelectedValuesListIgnoreAdjusting, cachedSelectedValuesListIgnoreAdjusting);
    }
}
