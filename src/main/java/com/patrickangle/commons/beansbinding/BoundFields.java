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
package com.patrickangle.commons.beansbinding;

import com.patrickangle.commons.beansbinding.interfaces.BindableField;
import com.patrickangle.commons.beansbinding.interfaces.BoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.JListBoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.JSliderBoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.JSpinnerBoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.JTableBoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.JTextComponentBoundField;
import com.patrickangle.commons.beansbinding.swing.boundfields.AbstractButtonBoundField;
import com.patrickangle.commons.beansbinding.util.BindableFields;
import com.patrickangle.commons.util.Classes;
import javax.swing.AbstractButton;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Patrick Angle
 */
public class BoundFields {
    public static <C extends Object> BoundField<C> boundField(C object, BindableField<C> bindableField) {
        if (object instanceof JSpinner) {
            return new JSpinnerBoundField((JSpinner) object, (BindableField<JSpinner>) bindableField);
        } else if (object instanceof JList) {
            return new JListBoundField((JList) object, (BindableField<JList>) bindableField);
        } else if (object instanceof JTextComponent) {
            return new JTextComponentBoundField((JTextComponent) object, (BindableField<JTextComponent>) bindableField);
        } else if (object instanceof JSlider) {
            return new JSliderBoundField((JSlider) object, (BindableField<JSlider>) bindableField);
        } else if (object instanceof JTable) {
            return new JTableBoundField((JTable) object, (BindableField<JTable>) bindableField);
        } else if (object instanceof AbstractButton) {
            return new AbstractButtonBoundField((AbstractButton) object, (BindableField<AbstractButton>) bindableField);
        } else {
            return new BasicBoundField<C>(object, bindableField);
        }
    }

    
    public static <C extends Object> BoundField<C> boundField(C object, String fieldName) {
        if (fieldName.contains(".")) {
            return new NestedBasicBoundField<>(object, fieldName);
        } else {
            return boundField(object, BindableFields.forClassWithName(Classes.classFor(object), fieldName));
        }
        
    }
}
