package com.patrickangle.commons.util.legacy;

import com.patrickangle.commons.util.Exceptions;
import java.awt.Dimension;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Patrick Angle
 */
public class ListUtils {

    public static <T extends Object> ArrayList<ArrayList<T>> createArray2dList(T initialItem) {
        ArrayList<ArrayList<T>> outerArrayList = new ArrayList<>(1);
        outerArrayList.add(new ArrayList<T>(1));
        outerArrayList.get(0).add(initialItem);
        return outerArrayList;
    }
    
    public static <T extends Object> List<List<T>> create2dList(T initialItem) {
        List<List<T>> outerArrayList = new ArrayList<>(1);
        outerArrayList.add(new ArrayList<T>(1));
        outerArrayList.get(0).add(initialItem);
        return outerArrayList;
    }
    
    public static <T extends Object> ArrayList<ArrayList<T>> createArray2dList(T[][] initialItems) {
        ArrayList<ArrayList<T>> outerArray = new ArrayList<>(initialItems.length);
        
        for (T[] innerItems : initialItems) {
            ArrayList<T> innerArray = new ArrayList<>(innerItems.length);
            for (T item : innerItems) {
                innerArray.add(item);
            }
            outerArray.add(innerArray);
        }
        
        return outerArray;
    }
    
    public static <T extends Object> List<List<T>> create2dList(T[][] initialItems) {
        List<List<T>> outerArray = new ArrayList<>(initialItems.length);
        
        for (T[] innerItems : initialItems) {
            ArrayList<T> innerArray = new ArrayList<>(innerItems.length);
            for (T item : innerItems) {
                innerArray.add(item);
            }
            outerArray.add(innerArray);
        }
        
        return outerArray;
    }

    public static int depthOfMultiDimensionList(Object list) {
        int depth = 0;
        Object currentDepthObject = list;
        while (currentDepthObject instanceof List && ((List) currentDepthObject).size() > 0) {
            currentDepthObject = ((List) currentDepthObject).get(0);
            depth++;
        }

        return depth;
    }

    public static <T> boolean isRectangularArray2dList(List<List<T>> array) {
        if (array.size() > 0) {
            int expectedInnerLength = array.get(0).size();

            for (List<T> innerArray : array) {
                if (innerArray.size() != expectedInnerLength) {
                    return false;
                }
            }
        }

        return true;
    }

    public static <T> boolean isRectangularArray2dList(ArrayList<ArrayList<T>> array) {
        if (array.size() > 0) {
            int expectedInnerLength = array.get(0).size();

            for (List<T> innerArray : array) {
                if (innerArray.size() != expectedInnerLength) {
                    return false;
                }
            }
        }

        return true;
    }

    public static <T> Dimension dimensionsOfArray2dList(List<List<T>> array) {
        if (array.size() > 0 && ListUtils.isRectangularArray2dList(array)) {
            return new Dimension(array.size(), array.get(0).size());
        } else if (array.size() == 0) {
            return new Dimension();
        } else {
            int tallestColumn = 0;
            for (List<T> innerArray : array) {
                tallestColumn = Math.max(tallestColumn, innerArray.size());
            }

            return new Dimension(array.size(), tallestColumn);
        }
    }

    public static <T> Dimension dimensionsOfArray2dList(ArrayList<ArrayList<T>> array) {
        if (array.size() > 0 && ListUtils.isRectangularArray2dList(array)) {
            return new Dimension(array.size(), array.get(0).size());
        } else if (array.size() == 0) {
            return new Dimension();
        } else {
            int tallestColumn = 0;
            for (List<T> innerArray : array) {
                tallestColumn = Math.max(tallestColumn, innerArray.size());
            }

            return new Dimension(array.size(), tallestColumn);
        }
    }

    public static Class classOfObjectInMultiDimensionList(Object list) {
        Class currentClass = list.getClass();
        Object currentObject = list;
        while (currentObject instanceof List && ((List) currentObject).size() > 0) {
            currentObject = ((List) currentObject).get(0);
            currentClass = currentObject.getClass();
        }

        return currentClass;
    }

    public static void changeRowsInArray2dList(List<List<Object>> arrayList, int numberOfRows) {
        Dimension dimensions = dimensionsOfArray2dList(arrayList);
        
        if (dimensions.height > numberOfRows) {
            for (List<Object> innerList : arrayList) {
                for (int i = innerList.size() - 1; i >= numberOfRows; i--) {
                    innerList.remove(i);
                }
            }
            
        } else if (dimensions.height < numberOfRows) {
            int rowsToAdd = numberOfRows - dimensions.height;
            
            Class memberClass = classOfObjectInMultiDimensionList(arrayList);
            try {
                Constructor constructor = memberClass.getConstructor();
                for (List<Object> innerList : arrayList) {
                    for (int i = 0; i < rowsToAdd; i++) {
                        
                            innerList.add(constructor.newInstance());
                        
                    }
                }
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Exceptions.raiseThrowableToUser(ex);
            }
        }
    }
    
    public static void changeColumnsInArray2dList(List<List<Object>> arrayList, int numberOfColumns) {
        Dimension dimensions = dimensionsOfArray2dList(arrayList);

        if (dimensions.width > numberOfColumns) {
            // Delete some number of columns
            for (int i = arrayList.size() - 1; i >= numberOfColumns; i--) {
                arrayList.remove(i);
            }

        } else if (dimensions.width < numberOfColumns) {
            // Create some number of columns
            int columnsToAdd = numberOfColumns - dimensions.width;
            Class memberClass = classOfObjectInMultiDimensionList(arrayList);
            try {
                Constructor constructor = memberClass.getConstructor();
                
                for (int i = 0; i < columnsToAdd; i++) {
                    ArrayList<Object> innerList = new ArrayList<>(dimensions.height);
                    for (int j = 0; j < dimensions.height; j++) {
                        innerList.add(constructor.newInstance());
                    }
                    arrayList.add(innerList);
                }
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Exceptions.raiseThrowableToUser(ex);
            }
        }
    }
    
    public static <T extends Object> List<List<T>> splitListIntoChunks(List<T> list, int chunkSize) {
        List<List<T>> chunks = new CopyOnWriteArrayList<List<T>>();
        for (int i = 0; i < list.size(); i += chunkSize) {
            chunks.add(new CopyOnWriteArrayList<>(list.subList(i, Math.min(i + chunkSize, list.size()))));
        }
        return chunks;
    }
    
    public static List<Object> flatten(List<?> list) {
        List<Object> ret = new ArrayList<>();
        flattenHelper(list, ret);
        return ret;
    }

    public static void flattenHelper(List<?> nestedList, List<Object> flatList) {
        for (Object item : nestedList) {
            if (item instanceof List<?>) {
                flattenHelper((List<?>) item, flatList);
            } else {
                flatList.add((Integer) item);
            }
        }
    }

    private static List<Object> lst(Object... objs) {
        return new ArrayList<>(Arrays.asList(objs));
    }
}
