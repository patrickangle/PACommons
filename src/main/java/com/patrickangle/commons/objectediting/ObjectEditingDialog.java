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
package com.patrickangle.commons.objectediting;

import java.awt.Dialog;
import java.awt.Frame;
import javax.swing.JDialog;

/**
 *
 * @author patrickangle
 */
public class ObjectEditingDialog extends JDialog {
    protected ObjectEditingPanel objectEditingPanel;
    
    public ObjectEditingDialog(Dialog parent, boolean modal, Object editingObject) {
        super(parent, modal);
        commonInit(editingObject);
    }
    
    public ObjectEditingDialog(Frame parent, boolean modal, Object editingObject) {
        super(parent, modal);
        commonInit(editingObject);
    }
    
    private void commonInit(Object editingObject) {
        this.objectEditingPanel = new ObjectEditingPanel(editingObject);
        this.add(objectEditingPanel);
    }
}
