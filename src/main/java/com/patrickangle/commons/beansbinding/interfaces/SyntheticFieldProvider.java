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
package com.patrickangle.commons.beansbinding.interfaces;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Patrick Angle
 */
public interface SyntheticFieldProvider {
    public Object getSyntheticFieldValue(String fieldName);
    
    public interface Writeable extends SyntheticFieldProvider {
        public void setSyntheticFieldValue(String fieldName, Object value);
    }
    
    // Required to delcare your sythetic fields, but must be declared on your class.
    public static List<String> syntheticFieldNames() {
        return Collections.EMPTY_LIST;
    }
    
    public static Class syntheticFieldType(String fieldName) {
        return Object.class;
    }
}
