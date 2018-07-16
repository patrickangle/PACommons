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
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;

/**
 *
 * @author patrickangle
 * @deprecated Use proper explicit BoundField to construct a binding exposing additional synthetic values.
 */
public class JTextComponentBinding<E> extends BasicBinding<E, JTextComponent>{
    public static final String SYNTHETIC_FIELD_TEXT = "text";
    public static final String SYNTHETIC_FIELD_TEXT_ON_FOCUS_LOST = "text$onFocusLost";
    public static final String SYNTHETIC_FIELD_TEXT_ON_ACTION_OR_FOCUS_LOST = "text$onActionOrFocusLost";
    
    private PropertyChangeSupport propertyChangeSupport;
    
    private DocumentListener documentListener;
    private FocusListener focusListener;
    private ActionListener actionListener;
    
    private String cachedText;
    private String cachedTextOnFocusLost;
    private String cachedTextOnActionOrFocusLost;
    
    public JTextComponentBinding(BoundField<E> backBoundField, BoundField<JTextComponent> frontBoundField, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        super(backBoundField, frontBoundField, updateStrategy, converter);
        this.propertyChangeSupport = new PropertyChangeSupport(this);

    }
    
    public JTextComponentBinding(BoundField<E> backBoundField, BoundField<JTextComponent> frontBoundField, Binding.UpdateStrategy updateStrategy) {
        super(backBoundField, frontBoundField, updateStrategy);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public JTextComponentBinding(E backContainingObject, String backObjectFieldName, JTextComponent frontContainingObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy, Binding.Converter converter) {
        super(backContainingObject, backObjectFieldName, frontContainingObject, frontObjectFieldName, updateStrategy, converter);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    public JTextComponentBinding(E backObject, String backObjectFieldName, JTextComponent frontObject, String frontObjectFieldName, Binding.UpdateStrategy updateStrategy) {
        super(backObject, backObjectFieldName, frontObject, frontObjectFieldName, updateStrategy);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    @Override
    protected void bind() {
        super.bind();
        
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
        frontBoundField.getContainingObject().getDocument().addDocumentListener(documentListener);
        
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
        frontBoundField.getContainingObject().addFocusListener(focusListener);
        
        if (frontBoundField.getContainingObject() instanceof JTextField) {
            if (actionListener == null) {
                actionListener = (ActionEvent actionEvent) -> {
                    textChangedOnActionOrFocusLost();
                };
            }
            
            ((JTextField) frontBoundField.getContainingObject()).addActionListener(actionListener);
        }
        
        this.propertyChangeSupport.addPropertyChangeListener(frontObjectListener);
    }
    
    @Override
    protected void unbind() {
        super.unbind();
        
        frontBoundField.getContainingObject().getDocument().removeDocumentListener(documentListener);
        frontBoundField.getContainingObject().removeFocusListener(focusListener);
        if (frontBoundField.getContainingObject() instanceof JTextField) {
            ((JTextField) frontBoundField.getContainingObject()).removeActionListener(actionListener);
        }
        
        this.propertyChangeSupport.removePropertyChangeListener(frontObjectListener);
    }
    
    protected void textChanged() {
        String oldText = cachedText;
        cachedText = frontBoundField.getContainingObject().getText();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_TEXT, oldText, cachedText);
    }
    
    protected void textChangedOnFocusLost() {
        String oldText = cachedTextOnFocusLost;
        cachedTextOnFocusLost = frontBoundField.getContainingObject().getText();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_TEXT_ON_FOCUS_LOST, oldText, cachedTextOnFocusLost);
    }
    
    protected void textChangedOnActionOrFocusLost() {
        String oldText = cachedTextOnActionOrFocusLost;
        cachedTextOnActionOrFocusLost = frontBoundField.getContainingObject().getText();
        propertyChangeSupport.firePropertyChange(SYNTHETIC_FIELD_TEXT_ON_ACTION_OR_FOCUS_LOST, oldText, cachedTextOnActionOrFocusLost);
    }
}
