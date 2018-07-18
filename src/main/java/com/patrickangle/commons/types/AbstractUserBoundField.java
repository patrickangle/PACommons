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
package com.patrickangle.commons.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.beansbinding.swing.models.ObservableComboBoxModel;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.json.JsonableObject;
import com.patrickangle.commons.objectediting.interfaces.CustomObjectEditingComponent;
import com.patrickangle.commons.objectediting.util.ObjectFieldEditorFactory;
import com.patrickangle.commons.observable.collections.ObservableCollections;
import com.patrickangle.commons.observable.collections.ObservableList;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservableBase;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Patrick Angle
 */
public abstract class AbstractUserBoundField extends PropertyChangeObservableBase implements CustomObjectEditingComponent, JsonableObject {
    @JsonProperty protected String targetObjectName;
    @JsonProperty protected String targetFieldName;
    
    protected BoundField boundField;
    
    protected ObservableList<String> knownTargetObjectNames = ObservableCollections.concurrentObservableList(String.class);
    protected ObservableList<String> knownTargetFieldNames = ObservableCollections.concurrentObservableList(String.class);

    public String getTargetObjectName() {
        return targetObjectName;
    }

    public void setTargetObjectName(String targetObjectName) {
        String oldTargetObjectName = this.targetObjectName;
        this.targetObjectName = targetObjectName;
        
        updateKnownTargetFields();
        updateBoundField();
        
        this.propertyChangeSupport.firePropertyChange("targetObjectName", oldTargetObjectName, this.targetObjectName);
    }

    public String getTargetFieldName() {
        return targetFieldName;
    }

    public void setTargetFieldName(String targetFieldName) {
        String oldTargetFieldName = this.targetFieldName;
        this.targetFieldName = targetFieldName;
        
        updateKnownTargetFields();
        updateBoundField();
        
        this.propertyChangeSupport.firePropertyChange("targetFieldName", oldTargetFieldName, this.targetFieldName);
        
    }
    
    protected void updateKnownTargetFields() {
        knownTargetFieldNames.clear();
        
        List<BindableField> fields = BindableFields.forClass(this.classForTargetObjectNamed(this.targetObjectName));
        fields.forEach((bindableField) -> {
            knownTargetFieldNames.add(bindableField.getFieldName());
        });
    }
    
    protected abstract void updateBoundField();
    protected abstract Class classForTargetObjectNamed(String string);

    @Override
    public ObjectFieldEditorFactory.ComponentReturn customObjectEditingComponent(BindingGroup bindingGroup) {
        JPanel boundFieldEditor = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints;
        
        JLabel targetObjectLabel = new JLabel("$");
        gridBagConstraints = new GridBagConstraints();
        boundFieldEditor.add(targetObjectLabel, gridBagConstraints);
        
        JComboBox targetObjectComboBox = new JComboBox(new ObservableComboBoxModel(knownTargetObjectNames));
        targetObjectComboBox.setEditable(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        boundFieldEditor.add(targetObjectComboBox, gridBagConstraints);
        
        Binding targetObjectComboBoxBinding = new BasicBinding(this, "targetObjectName", targetObjectComboBox.getModel(), "selectedItem", Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(targetObjectComboBoxBinding);
        
        JLabel seperatorLabel = new JLabel(".");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        boundFieldEditor.add(seperatorLabel, gridBagConstraints);
        
        JComboBox targetFieldComboBox = new JComboBox(new ObservableComboBoxModel(knownTargetFieldNames));
        targetFieldComboBox.setEditable(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        boundFieldEditor.add(targetFieldComboBox, gridBagConstraints);
        
        Binding targetFieldComboBoxBinding = new BasicBinding(this, "targetFieldName", targetFieldComboBox.getModel(), "selectedItem", Binding.UpdateStrategy.READ_WRITE);
        bindingGroup.add(targetFieldComboBoxBinding);
        
        return new ObjectFieldEditorFactory.ComponentReturn(boundFieldEditor, false);
    }
}
