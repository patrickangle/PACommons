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
package com.patrickangle.commons.objectediting.interfaces;

import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.objectediting.util.ObjectFieldEditorFactory;
import javax.swing.undo.UndoManager;

/**
 *
 * @author patrickangle
 */
public interface CustomObjectEditingComponent {
    public ObjectFieldEditorFactory.ComponentReturn customObjectEditingComponent(BindableField bindableField, BindingGroup bindingGroup, UndoManager undoManager);
    
    public default ObjectFieldEditorFactory.ComponentReturn customObjectEditingComponentRenderer(BindableField field, BindingGroup bindingGroup, UndoManager undoManager) {
        return this.customObjectEditingComponent(field, bindingGroup, undoManager);
    }
    
}
