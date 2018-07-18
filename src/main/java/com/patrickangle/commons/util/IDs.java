
package com.patrickangle.commons.util;

import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author Patrick Angle
 */
public class IDs {
    private static final HashMap<String, Integer> sequentialIDMap = new HashMap<>();
    public static int sequentialID(String context) {
        if (!sequentialIDMap.containsKey(context)) {
            sequentialIDMap.put(context, 0);
        }
        sequentialIDMap.put(context, sequentialIDMap.get(context) + 1);
        return sequentialIDMap.get(context);
    }
    
    public static UUID uuid() {
        return UUID.randomUUID();
    }
    
    public static String uuidString() {
        return uuid().toString();
    }
}