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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Patrick Angle
 */
public class Maps {

    /**
     * Get the first key containing the provided value, or null if no key exists
     * for that value.
     *
     * @param <K>
     * @param <V>
     * @param map
     * @param value
     * @return
     */
    public static <K, V> K firstKeyForValue(Map<K, V> map, V value) {
        for (Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Unpack a nested map to be a flat map, combining the key path together
     * with the provided separator and prepending the providing prefix.
     *
     * @param map
     * @param prefix
     * @return
     */
    public static Map unpackNestedMap(Map map, String prefix, String seperator) {
        if (mapIsStringKeyed(map)) {
            Map<String, Object> newMap = new HashMap<>(map.size());
            for (Entry<String, Object> entry : ((Map<String, Object>) map).entrySet()) {
                if (entry.getValue() instanceof Map && mapIsStringKeyed((Map) entry.getValue())) {
                    newMap.putAll(unpackNestedMap((Map) entry.getValue(), prefix + entry.getKey() + seperator, seperator));
                } else {
                    newMap.put(prefix + entry.getKey(), entry.getValue());
                }
            }
            return newMap;
        } else {
            return map;
        }
    }

    /**
     * Check if a map is string-keyed. This method fails fast if the map has a
     * key other than a string, but must check every key in the map to verify
     * that the map is indeed string-keyed.
     *
     * @param map
     * @return
     */
    public static boolean mapIsStringKeyed(Map map) {
        if (map.keySet().size() > 0) {
            boolean stringKeyed = true;
            for (Object key : map.keySet()) {
                if (!(key instanceof String)) {
                    stringKeyed = false;
                    break;
                }
            }
            return stringKeyed;
        } else {
            return false;
        }
    }

    /**
     * The compliment to unpackNestedMap, this method packs a nested map by
     * separating the key by the provided regex. Additionally, the key
     *
     * @param map
     * @param prefixFilter
     * @param separatorRegex
     * @return
     */
    public static Map packNestedMap(Map<String, Object> map, String prefixFilter, String separatorRegex) {
        String[] filterParts = prefixFilter.split(separatorRegex);

        Map<String, Object> packedMap = new HashMap<>(map.size());

        for (Entry<String, Object> entry : map.entrySet()) {
            String[] keyParts = entry.getKey().split(separatorRegex);

            if (entry.getKey().startsWith(prefixFilter)) {
                if (filterParts.length == keyParts.length) {
                    // If the filter and key parts are the same length, use the last part of the orignal key as the key. This allows the filter to wildcard match on partial ends of keys.
                    packNestedMapRecursive(packedMap, new String[]{keyParts[keyParts.length - 1]}, entry.getValue());
                } else if (filterParts[filterParts.length - 1].equals(keyParts[filterParts.length - 1])) {
                    packNestedMapRecursive(packedMap, Arrays.copyOfRange(keyParts, filterParts.length, keyParts.length), entry.getValue());
                } else if (filterParts.length != 0) {
                    packNestedMapRecursive(packedMap, Arrays.copyOfRange(keyParts, filterParts.length - 1, keyParts.length), entry.getValue());
                } else {
                    packNestedMapRecursive(packedMap, keyParts, entry.getValue());

                }
            }
        }

        return packedMap;
    }

    protected static void packNestedMapRecursive(Map<String, Object> map, String[] keyParts, Object value) {
        if (keyParts.length == 0) {
            Logging.warning(Maps.class, "Value could not be mapped, as there were no entries in keyParts.");
        } else if (keyParts.length == 1) {
            map.put(keyParts[0], value);
        } else {
            packNestedMapRecursive((Map<String, Object>) map.computeIfAbsent(keyParts[0], (key) -> {
                return new HashMap<String, Object>();
            }), Arrays.copyOfRange(keyParts, 1, keyParts.length), value);
        }
    }
}
