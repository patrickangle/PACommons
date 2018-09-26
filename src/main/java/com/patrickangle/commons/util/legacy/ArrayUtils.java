/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util.legacy;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Patrick Angle
 */
@Deprecated(forRemoval = true)
public class ArrayUtils {
@Deprecated(forRemoval = true)
    public static String byteArrayToHex(byte[] bytes) {
        return byteArrayToHex(bytes, true);
    }
@Deprecated(forRemoval = true)
    public static String byteArrayToHex(byte[] bytes, boolean autoLineBreak) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (byte b : bytes) {
            if (autoLineBreak && i % 16 == 0) {
                sb.append("\n");
            }
            sb.append(" ");
            if (b < 16 && b >= 0) {
                sb.append("0");
            }
            sb.append(Integer.toHexString((int) (b & 0xff)).toUpperCase());
            i++;
        }
        return sb.toString();
    }
@Deprecated(forRemoval = true)
    public static int numberOfItemsIn2DArray(Object[][] array) {
        int itemsIn2DArray = 0;

        for (Object[] innerArray : array) {
            itemsIn2DArray += innerArray.length;
        }

        return itemsIn2DArray;
    }
@Deprecated(forRemoval = true)
    public static boolean isSquare2DArray(Object[][] array) {
        if (array.length > 0) {
            int expectedInnerLength = array[0].length;

            for (Object[] innerArray : array) {
                if (innerArray.length != expectedInnerLength) {
                    return false;
                }
            }
        }

        return true;
    }
@Deprecated(forRemoval = true)
    public static boolean isSquare2DArray(boolean[][] array) {
        if (array.length > 0) {
            int expectedInnerLength = array[0].length;

            for (boolean[] innerArray : array) {
                if (innerArray.length != expectedInnerLength) {
                    return false;
                }
            }
        }

        return true;
    }
@Deprecated(forRemoval = true)
    public static byte[] arrayListToArray(ArrayList<Byte> arrayList) {
        byte[] returnArray = new byte[arrayList.size()];

        for (int i = 0; i < arrayList.size(); i++) {
            returnArray[i] = arrayList.get(i);
        }

        return returnArray;
    }
@Deprecated(forRemoval = true)
    public static Dimension dimensionsOf2DArray(Object[][] array) {
        if (array.length > 0 && ArrayUtils.isSquare2DArray(array)) {
            return new Dimension(array.length, array[0].length);
        } else if (array.length == 0) {
            return new Dimension();
        } else {
            int tallestColumn = 0;
            for (Object[] innerArray : array) {
                tallestColumn = Math.max(tallestColumn, innerArray.length);
            }

            return new Dimension(array.length, tallestColumn);
        }
    }
@Deprecated(forRemoval = true)
    public static Dimension dimensionsOf2DArray(boolean[][] array) {
        if (array.length > 0 && ArrayUtils.isSquare2DArray(array)) {
            return new Dimension(array.length, array[0].length);
        } else if (array.length == 0) {
            return new Dimension();
        } else {
            int tallestColumn = 0;
            for (boolean[] innerArray : array) {
                tallestColumn = Math.max(tallestColumn, innerArray.length);
            }

            return new Dimension(array.length, tallestColumn);
        }
    }
@Deprecated(forRemoval = true)
    public static String booleanArrayToXMap(boolean[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                sb.append(array[x][y] ? "X" : " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
@Deprecated(forRemoval = true)
    public static Class underlyingClassOfArray(Object[] array) {
        Class c = array.getClass();
        while (c.isArray()) {
            c = c.getComponentType();
        }
        return c;
    }
@Deprecated(forRemoval = true)
    public static int depthOfArray(Object array) {
        int depth = 0;
        Class c = array.getClass();
        while (c.getComponentType() != null) {
            c = c.getComponentType();
            depth++;
        }

        System.out.println(array);
        System.out.println(depth);
        return depth;
    }
@Deprecated(forRemoval = true)
    public static Object[][] splitArray(Object[] arrayToSplit, int chunkSize) {
        if (chunkSize <= 0) {
            return null;  // just in case :)
        }
        // first we have to check if the array can be split in multiple 
        // arrays of equal 'chunk' size
        int rest = arrayToSplit.length % chunkSize;  // if rest>0 then our last array will have less elements than the others 
        // then we check in how many arrays we can split our input array
        int chunks = arrayToSplit.length / chunkSize + (rest > 0 ? 1 : 0); // we may have to add an additional array for the 'rest'
        // now we know how many arrays we need and create our result array
        Object[][] arrays = new Object[chunks][];
        // we create our resulting arrays by copying the corresponding 
        // part from the input array. If we have a rest (rest>0), then
        // the last array will have less elements than the others. This 
        // needs to be handled separately, so we iterate 1 times less.
        for (int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++) {
            // this copies 'chunk' times 'chunkSize' elements into a new array
            arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize);
        }
        if (rest > 0) { // only when we have a rest
            // we copy the remaining elements into the last chunk
            arrays[chunks - 1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest);
        }
        return arrays; // that's it
    }
@Deprecated(forRemoval = true)
    public static <T extends Object> T[] reverseArray(T[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            T temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
        return array;
    }

}
