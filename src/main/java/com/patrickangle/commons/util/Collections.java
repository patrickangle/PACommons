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
package com.patrickangle.commons.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Patrick Angle
 */
public class Collections {
    public static <T extends Object> boolean contentsAreEqualIgnoreOrder(Collection<T> a, Collection<T> b) {
        if (a == b) {
            return true;
        }
        
        if (a == null || b == null) {
            return false;
        }
        
        if (a.size() != b.size()) {
            return false;
        }
        
        List<T> collectionA = new ArrayList<>(a);
        List<T> collectionB = new ArrayList<>(b);
        
        java.util.Collections.sort((List) collectionA);
        java.util.Collections.sort((List) collectionB);
        
        return collectionA.equals(collectionB);
    }
    
    public static <T> Collection<T> filteredToClass(Collection<? extends Object> collection, Class<T> filterClass) {
        return (Collection<T>) collection.stream().filter(t -> filterClass.isInstance(t)).collect(Collectors.toList());
    }
    
    public static Collection<? extends Object> filteredToClasses(Collection<? extends Object> collection, List<Class<? extends Object>> filterClasses) {
        return (Collection<Object>) collection.stream().filter((t) -> {
            for(Class<? extends Object> c : filterClasses) {
                if (c.isInstance(t)) {
                    return true;
                };
            }
            return false;
        }).collect(Collectors.toList());
    }
}
