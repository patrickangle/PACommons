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
package com.patrickangle.commons.beansbinding.swing;

import com.patrickangle.commons.beansbinding.interfaces.AbstractBoundField;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.util.Classes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Patrick Angle
 */
public class JTextComponentBoundField extends AbstractBoundField<JTextComponent> {
    public static final String SYNTHETIC_FIELD_TEXT = "text$";
    public static final String SYNTHETIC_FIELD_TEXT_ON_FOCUS_LOST = "text$onFocusLost";
    public static final String SYNTHETIC_FIELD_TEXT_ON_ACTION_OR_FOCUS_LOST = "text$onActionOrFocusLost";
    
    protected JTextComponent containingObject;
    protected BindableField<JTextComponent> bindableField;
    
    protected PropertyChangeListener propertyChangeListener;
    
    protected DocumentListener documentListener;
    protected FocusListener focusListener;
    protected ActionListener actionListener;
    
    protected String cachedText;
    protected String cachedTextOnFocusLost;
    protected String cachedTextOnActionOrFocusLost;
    
    public JTextComponentBoundField(JTextComponent containingObject, BindableField<JTextComponent> bindableField) {
        this.containingObject = containingObject;
        this.bindableField = bindableField;
        
        this.commonInit();
    }
    
    public JTextComponentBoundField(JTextComponent containingObject, String fieldName) {
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
        if (documentListener == null) {
            documentListener = new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    textChanged();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    textChanged();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    textChanged();
                }
            };
        }
        containingObject.getDocument().addDocumentListener(documentListener);
        
        if (focusListener == null) {
            focusListener = new FocusListener() {
                @Override
                public void focusLost(FocusEvent e) {
                    textChangedOnFocusLost();
                    textChangedOnActionOrFocusLost();
                }

                @Override
                public void focusGained(FocusEvent e) {
                    // No action
                }
            };
        }
        containingObject.addFocusListener(focusListener);
        
        if (containingObject instanceof JTextField) {
            if (actionListener == null) {
                actionListener = (ActionEvent actionEvent) -> {
                    textChangedOnActionOrFocusLost();
                };
            }
            
            ((JTextField) containingObject).addActionListener(actionListener);
        }
    }
    
    public JTextComponent getContainingObject() {
        return this.containingObject;
    }
    
    public BindableField<JTextComponent> getBindableField() {
        return this.bindableField;
    }

    @Override
    public Class getFieldClass() {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_TEXT:
            case SYNTHETIC_FIELD_TEXT_ON_FOCUS_LOST:
            case SYNTHETIC_FIELD_TEXT_ON_ACTION_OR_FOCUS_LOST:
            default:
                return super.getFieldClass();
        }
    }

    @Override
    public Object getValue() {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_TEXT:
            case SYNTHETIC_FIELD_TEXT_ON_FOCUS_LOST:
            case SYNTHETIC_FIELD_TEXT_ON_ACTION_OR_FOCUS_LOST:
            default:
                return super.getValue();
        }
    }

    @Override
    public void setValue(Object value) {
        switch(bindableField.getFieldName()) {
            case SYNTHETIC_FIELD_TEXT:
            case SYNTHETIC_FIELD_TEXT_ON_FOCUS_LOST:
            case SYNTHETIC_FIELD_TEXT_ON_ACTION_OR_FOCUS_LOST:
            default:
                super.setValue(value);
        }
    }
    
    protected void textChanged() {
        String oldText = cachedText;
        cachedText = containingObject.getText();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_TEXT, oldText, cachedText);
    }
    
    protected void textChangedOnFocusLost() {
        String oldText = cachedTextOnFocusLost;
        cachedTextOnFocusLost = containingObject.getText();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_TEXT_ON_FOCUS_LOST, oldText, cachedTextOnFocusLost);
    }
    
    protected void textChangedOnActionOrFocusLost() {
        String oldText = cachedTextOnActionOrFocusLost;
        cachedTextOnActionOrFocusLost = containingObject.getText();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_TEXT_ON_ACTION_OR_FOCUS_LOST, oldText, cachedTextOnActionOrFocusLost);
    }
}
