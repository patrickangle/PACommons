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
package com.patrickangle.commons.playground;

import com.patrickangle.commons.objectediting.annotations.ObjectEditingProperty;

/**
 *
 * @author patrickangle
 */
public class BoringPojo {
    public static final String USER_EDITABLE_PROPERTY_GROUP = "Boring Plain Old Java Object";
    
    @ObjectEditingProperty(name="My verbose boolean statement name goes here.") protected boolean option11 = true;
    @ObjectEditingProperty(name="Integer with Help", help="This sets the thing for the other thing!") protected int option12 = 30;

    public boolean isOption11() {
        return option11;
    }

    public void setOption11(boolean option11) {
        this.option11 = option11;
    }

    public int getOption12() {
        return option12;
    }

    public void setOption12(int option12) {
        this.option12 = option12;
    }
    
    
}
