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
    String name() default "";
    String help() default "";
    double numberMinimumValue() default Integer.MIN_VALUE;
    double numberMaximumValue() default Integer.MAX_VALUE;
    double numberStepValue() default 1;
    NumberEditor numberEditor() default NumberEditor.SPINNER_CONTROL;
    Class listNewItemClass() default Object.class;
    
    public enum NumberEditor {
        TEXT_CONTROL,
        SPINNER_CONTROL,
        SLIDER_CONTROL;
    }
}
