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
package com.patrickangle.commons.observable.collections;

import com.patrickangle.commons.logging.Logging;
import com.patrickangle.commons.util.Lists;
import java.awt.Dimension;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 *
 * @author Patrick Angle
 */
public class ObservableLists {
    /**
     * Returns a non-concurrent two-dimensional observable list with the given initial item at 0,0.
     * @param <T>
     * @param initialItem
     * @return 
     */
    public static <T extends Object> List<List<T>> twoDimensionalList(T initialItem) {
        List<List<T>> outerList = new ObservableArrayList<>(1);
        outerList.add(new ObservableArrayList<>(List.of(initialItem)));
        return outerList;
        
    }
    
    /**
     * Returns a non-concurrent two-dimensional observable list with the given items.
     * @param <T>
     * @param initialItems
     * @return 
     */
    public static <T extends Object> List<List<T>> twoDimensionalList(T[][] initialItems) {
        List<List<T>> outerList = new ObservableArrayList<>(initialItems.length);
        
        for (T[] innerItems : initialItems) {
            List<T> innerList = new ObservableArrayList<>(innerItems.length);
            
            for (T item : innerItems) {
                innerList.add(item);
            }
            
            outerList.add(innerList);
        }
        
        return outerList;
    }
    
    /**
     * Returns a concurrent two-dimensional observable list with the given initial item at 0,0.
     * @param <T>
     * @param initialItem
     * @return 
     */
    public static <T extends Object> List<List<T>> twoDimensionaConcurrentlList(T initialItem) {
        List<List<T>> outerList = new ObservableCopyOnWriteArrayList<>();
        outerList.add(new ObservableArrayList<>(List.of(initialItem)));
        return outerList;
    }
    
    /**
     * Returns a concurrent two-dimensional observable list with the given items.
     * @param <T>
     * @param initialItems
     * @return 
     */
    public static <T extends Object> List<List<T>> twoDimensionalConcurrentList(T[][] initialItems) {
        List<List<T>> outerList = new ObservableCopyOnWriteArrayList<>();
        
        for (T[] innerItems : initialItems) {
            List<T> innerList = new ObservableCopyOnWriteArrayList<>();
            
            for (T item : innerItems) {
                innerList.add(item);
            }
            
            outerList.add(innerList);
        }
        
        return outerList;
    }
    
    /**
     * Update the height of the given two-dimensional list. This method requires
     * that the provided two-dimensional list be rectangular.
     * @param <T>
     * @param outerList
     * @param rows 
     */
    public static <T extends Object> void updateHeightInTwoDimensionalList(List<List<T>> outerList, int rows) {
        // Because adjusting the height of a two-dimensional list does not create a new list, we can defer to the Lists.updateHeightInTwoDimensionalList implementation.
        Lists.updateHeightInTwoDimensionalList(outerList, rows);
    }
    
    /**
     * Update the width of the given two-dimensional list. This method requires
     * that the provided two-dimensional list be rectangular.
     * @param <T>
     * @param outerList
     * @param columns 
     */
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
                    List<T> innerList = new ObservableArrayList<>(dimensions.height);
                    
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
     * Update the height of the given two-dimensional list. This method requires
     * that the provided two-dimensional list be rectangular. The method
     * specifically treats the provided list as needing to be concurrent.
     * @param <T>
     * @param outerList
     * @param rows 
     */
    public static <T extends Object> void updateHeightInTwoDimensionalConcurrentList(List<List<T>> outerList, int rows) {
        // Because adjusting the height of a two-dimensional list does not create a new list, we can defer to the Lists.updateHeightInTwoDimensionalList implementation.
        Lists.updateHeightInTwoDimensionalList(outerList, rows);
    }
    
    /**
     * Update the width of the given two-dimensional list. This method requires
     * that the provided two-dimensional list be rectangular. The method
     * specifically treats the provided list as needing to be concurrent.
     * @param <T>
     * @param outerList
     * @param columns 
     */
    public static <T extends Object> void updateWidthInTwoDimensionalConcurrentList(List<List<T>> outerList, int columns) {
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
                    List<T> innerList = new ObservableCopyOnWriteArrayList<>();
                    
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
}
