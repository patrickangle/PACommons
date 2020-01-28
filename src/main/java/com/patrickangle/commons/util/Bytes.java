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

import com.patrickangle.commons.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Patrick Angle
 */
public class Bytes {

    public static short unsignedToShort(byte firstByte, byte secondByte) {
        return (short) ((firstByte << 8) | (secondByte & 0xFF));
    }

    /**
     * Get the index at which the provided search starts, or -1 if the search
     * term never occurs. Only the first index is returned.
     *
     * @param bytes
     * @param search
     * @return
     */
    public static int indexOfFirstOccurence(byte[] bytes, byte[] search) {
        if (bytes == null || search == null || search.length == 0) {
            return -1;
        }

        int searchIndex = 0;
        int searchStarted = -1;

        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == search[searchIndex]) {
                if (searchIndex == 0) {
                    searchStarted = i;
                }

                if (searchIndex == search.length - 1) {
                    return searchStarted;
                }

                searchIndex++;
            } else {
                searchStarted = -1;
                searchIndex = 0;
            }
        }

        return -1;
    }
    
    public static int indexOfFirstOccurence(List<Byte> bytes, List<Byte> search) {
        if (bytes == null || search == null || search.size() == 0) {
            return -1;
        }

        int searchIndex = 0;
        int searchStarted = -1;

        for (int i = 0; i < bytes.size(); i++) {
            if (bytes.get(i) == search.get(searchIndex)) {
                if (searchIndex == 0) {
                    searchStarted = i;
                }

                if (searchIndex == search.size() - 1) {
                    return searchStarted;
                }

                searchIndex++;
            } else {
                searchStarted = -1;
                searchIndex = 0;
            }
        }

        return -1;
    }

    /**
     * Returns a pair, containing the bytes before the separator as the first
     * value and the bytes after the separator as the second value. Returns null
     * if the separator does not exist in the bytes.
     *
     * @param bytes
     * @param separator
     * @return
     */
    public static Pair<byte[], byte[]> separateByBytes(byte[] bytes, byte[] separator) {
        int mark = indexOfFirstOccurence(bytes, separator);

        if (mark == -1) {
            return null;
        }

        return Pair.makePair(Arrays.copyOf(bytes, mark), Arrays.copyOfRange(bytes, mark + separator.length, bytes.length));
    }
    
    public static Pair<List<Byte>, List<Byte>> separateByBytes(List<Byte> bytes, List<Byte> separator) {
        int mark = indexOfFirstOccurence(bytes, separator);

        if (mark == -1) {
            return null;
        }

        return Pair.makePair(new ArrayList<>(bytes.subList(0, mark)), new ArrayList<>(bytes.subList(mark + separator.size(), bytes.size())));
    }
}
