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
package com.patrickangle.commons.objectediting;

import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.objectediting.annotations.ObjectEditingProperty;
import com.patrickangle.commons.objectediting.util.ObjectEditingBindings;
import com.patrickangle.commons.objectediting.util.ObjectFieldEditorFactory;
import com.patrickangle.commons.util.Annotations;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 *
 * @author patrickangle
 */
public class ObjectEditingPanel extends JPanel {
    protected Object editingObject;
    
    protected BindingGroup bindingGroup;
    
    public ObjectEditingPanel() {
        super(true);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        this.bindingGroup = new BindingGroup();
    }
    
    public ObjectEditingPanel(Object editingObject) {
        super(true);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        this.editingObject = editingObject;
        this.bindingGroup = new BindingGroup();
        
        if (this.editingObject != null) {
            build();
        }
    }

    public Object getEditingObject() {
        return editingObject;
    }

    public void setEditingObject(Object editingObject) {
        tearDown();
        Object oldEditingObject = this.editingObject;
        this.editingObject = editingObject;
        this.firePropertyChange("editingObject", oldEditingObject, this.editingObject);
        
        if (this.editingObject != null) {
            build();
        }
    }
    
    protected void tearDown() {
        bindingGroup.unbind();
        bindingGroup.clear();
        
        this.removeAll();
        
        this.revalidate();
        this.repaint();
    }
    
    protected void build() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        LinkedHashMap<String, JPanel> sectionPanels = new LinkedHashMap<>();
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 6;
        c.ipady = 6;
        c.insets = new Insets(1, 1, 1, 1);
        
        int runningGridY = 0;
        for (BindableField field : ObjectEditingBindings.bindableFieldsForObject(editingObject)) {
            ObjectEditingProperty config = Annotations.valueFromAnnotationOnField(BindableFields.reflectionFieldForBindableField(field), ObjectEditingProperty.class);
            String declaringClass = ObjectEditingBindings.getGroupNameForBindableField(field);
            sectionPanels.putIfAbsent(declaringClass, new JPanel(new GridBagLayout()));
            
            ObjectFieldEditorFactory.ComponentReturn componentReturn = ObjectFieldEditorFactory.createEditorForObject(editingObject, field, bindingGroup);
            
            c.gridy = runningGridY;
            c.gridwidth = componentReturn.isSelfLabeled() ? 2 : 1;
            
            if (!componentReturn.isSelfLabeled()) {
                c.gridx = 0;
                c.weightx = 0.0;
                
                JLabel label = new JLabel(ObjectEditingBindings.nameForBindableField(field), JLabel.TRAILING);
                if (componentReturn.multiLineEditor) {
                    label.setVerticalAlignment(SwingConstants.TOP);
                }
                label.setLabelFor(componentReturn.getComponent());
                if (!config.help().equals("")) {
                    label.setToolTipText(config.help());
                }
                sectionPanels.get(declaringClass).add(label, c);
            }
            
            c.gridx = componentReturn.isSelfLabeled() ? 0 : 1;
            c.weightx = 1.0;
            
            sectionPanels.get(declaringClass).add(componentReturn.getComponent(), c);
            
            runningGridY++;
        }
        
        List<String> sectionPanelNames = new ArrayList<>(sectionPanels.keySet());
        Collections.reverse(sectionPanelNames);
        sectionPanelNames.forEach((sectionPanelName) -> {
            JPanel sectionPanel = sectionPanels.get(sectionPanelName);
            
            TitledBorder border = new TitledBorder(sectionPanelName);
            sectionPanel.setBorder(border);
            
            this.add(sectionPanel);
        });
        
        this.add(new Box.Filler(new Dimension(), new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE), new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)));
//        SpringLayouts.makeCompactGrid(this, sectionPanels.size(), 1, 6, 6, 6, 6);
        bindingGroup.bind();
        this.revalidate();
        this.repaint();
    }
}
