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
package com.patrickangle.commons.objectediting.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author patrickangle
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectEditingProperty {
    // The human-readable name of the property
    String name() default "";
    
    // The tooltip for the property. Note that this tooltip is not applied to editing components that are user-provided.
    String help() default "";
    
    // Hints for several editors
    SetOnRule setOn() default SetOnRule.ANY_CHANGE;
    
    // Hints for number-spinners
    double numberMinimumValue() default Integer.MIN_VALUE;
    double numberMaximumValue() default Integer.MAX_VALUE;
    double numberStepValue() default 1;
    NumberEditor numberEditor() default NumberEditor.SPINNER_CONTROL;
    
    // Properties for reflective creation of list and map contents
    Class listNewItemClass() default Object.class;
    Class mapKeyNewItemClass() default Object.class;
    Class mapValueNewItemClass() default Object.class;
    
    // Determine whether or not the entry will be editable.
    boolean mutable() default true;
    
    
    public enum NumberEditor {
        TEXT_CONTROL,
        SPINNER_CONTROL,
        SLIDER_CONTROL;
    }
    
    public enum SetOnRule {
        ANY_CHANGE,
        FOCUS_LOST,
        ACTION_OR_FOCUS_LOST;
    }
}
