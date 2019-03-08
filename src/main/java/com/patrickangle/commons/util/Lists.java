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

import com.patrickangle.commons.logging.Logging;
import com.patrickangle.commons.observable.collections.ObservableArrayList;
import com.patrickangle.commons.observable.collections.ObservableLists;
import java.awt.Dimension;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patrick Angle
 */
public class Lists {
    /**
     * Returns a two-dimensional list with the given initial item at 0,0.
     * @param <T>
     * @param initialItem
     * @return 
     */
    public static <T extends Object> List<List<T>> twoDimensionalList(T initialItem) {
        List<List<T>> outerList = new ArrayList<>(1);
        outerList.add(new ArrayList<>(List.of(initialItem)));
        return outerList;
        
    }
    
    /**
     * Returns a two-dimensional list with the given items.
     * @param <T>
     * @param initialItems
     * @return 
     */
    public static <T extends Object> List<List<T>> twoDimensionalList(T[][] initialItems) {
        List<List<T>> outerList = new ArrayList<>(initialItems.length);
        
        for (T[] innerItems : initialItems) {
            List<T> innerList = new ArrayList<>(innerItems.length);
            
            for (T item : innerItems) {
                innerList.add(item);
            }
            
            outerList.add(innerList);
        }
        
        return outerList;
    }
    
    public static int depthOfMultiDimensionalList(List list) {
        int depth = 0; // A list, even empty, will have a depth of 1 by default.
        Object currentDepthObject = list;
        
        // We now dive deeper into the list until we run out of lists, or a list is empty.
        while(currentDepthObject instanceof List && ((List) currentDepthObject).size() > 0) {
            currentDepthObject = ((List) currentDepthObject).get(0);
            depth++;
        }
        
        return depth;
    }
    
    public static <T extends Object> Dimension dimensionsOfTwoDimensionalList(List<List<T>> outerList) {
        if (outerList.size() > 0 && Lists.isRectangularTwoDimensionalList(outerList)) {
            return new Dimension(outerList.size(), outerList.get(0).size());
        } else if (outerList.size() == 0) {
            return new Dimension();
        } else {
            int tallestColumn = 0;
            
            for (List<T> innerList : outerList) {
                tallestColumn = Math.max(tallestColumn, innerList.size());
            }
            
            return new Dimension(outerList.size(), tallestColumn);
        }
    }
    
    public static <T extends Object> boolean isRectangularTwoDimensionalList(List<List<T>> outerList) {
        if (outerList.size() > 0) {
            int expectedInnerLength = outerList.get(0).size();
            
            for (List<T> innerList : outerList) {
                if (innerList.size() != expectedInnerLength) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public static Class genericTypeOfMultiDimensionalList(List list) {
        Class currentClass = list.getClass();
        Object currentObject = list;
        
        while(currentObject instanceof List && ((List) currentObject).size() > 0) {
            currentObject = ((List) currentObject).get(0);
            currentClass = currentObject.getClass();
        }
        
        return currentClass;
    }
    
    public static <T extends Object> void updateHeightInTwoDimensionalList(List<List<T>> outerList, int rows) {
        if (!Lists.isRectangularTwoDimensionalList(outerList)) {
            throw new IllegalArgumentException("Two-dimensional list must be rectangular to mutate the number of rows uniformly. Uneven two-dimensional lists are not supported.");
        }
        
        Dimension dimensions = Lists.dimensionsOfTwoDimensionalList(outerList);
        
        if (dimensions.height > rows) {
            // Remove rows to conform to the new height.
            for (List<T> innerList : outerList) {
                for (int i = innerList.size() - 1; i >= rows; i--) {
                    innerList.remove(i);
                }
            }
        } else if (dimensions.height < rows) {
            // Add rows to conform to the new height.
            int rowsToAdd = rows - dimensions.height;
            
            Class<T> memberClass = Lists.genericTypeOfMultiDimensionalList(outerList);
            
            try {
                Constructor<T> constructor = memberClass.getConstructor();
                
                for (List<T> innerList : outerList) {
                    for (int i = 0; i < rowsToAdd; i++) {
                        innerList.add(constructor.newInstance());
                    }
                }
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logging.exception(Lists.class, ex);
            }
        }
    }
    
    public static <T extends Object> void updateWidthInTwoDimensionalList(List<List<T>> outerList, int columns) {
        if (!Lists.isRectangularTwoDimensionalList(outerList)) {
            throw new IllegalArgumentException("Two-dimensional list must be rectangular to mutate the number of columns uniformly. Uneven two-dimensional lists are not supported.");
        }
        
        Dimension dimensions = Lists.dimensionsOfTwoDimensionalList(outerList);
        
        if (dimensions.width > columns) {
            // Remove columns to conform to the new width.
            for (int i = outerList.size() - 1; i >= columns; i--) {
                outerList.remove(i);
            }
        } else if (dimensions.width < columns) {
            // Add columns to conform to the new width.
            int columnsToAdd = columns - dimensions.width;
            Class<T> memberClass = Lists.genericTypeOfMultiDimensionalList(outerList);
            
            try {
                Constructor<T> constructor = memberClass.getConstructor();
                
                for (int i = 0; i < columnsToAdd; i++) {
                    List<T> innerList = new ArrayList<>(dimensions.height);
                    
                    for (int j = 0; j < dimensions.height; j++) {
                        innerList.add(constructor.newInstance());
                    }
                    
                    outerList.add(innerList);
                }
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logging.exception(ObservableLists.class, ex);
            }
        }
    }
    
    /**
     * Returns true if all items in the list are assignable to the provided class.
     * @param list
     * @param assignableToClass
     * @return 
     */
    public static boolean contentsAreAssignableTo(List list, Class assignableToClass) {
        for (Object o : list) {
            if (o == null || !assignableToClass.isAssignableFrom(o.getClass())) {
                return false;
            }
        }
        return true;
    }
}
