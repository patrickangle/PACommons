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

/**
 *
 * @author Patrick Angle
 */
public class Strings {

    public static Pair<String, Integer> trailingIntegerComponents(String string) {
        int offset = string.length();
        for (int i = string.length() - 1; i >= 0; i--) {
            char c = string.charAt(i);
            if (Character.isDigit(c)) {
                offset--;
            } else {
                if (offset == string.length()) {
                    // No int at the end
                    return Pair.makePair(string, Integer.MIN_VALUE);
                }
                return Pair.makePair(string.substring(0, offset), Integer.parseInt(string.substring(offset)));

            }
        }
        return Pair.makePair(string.substring(0, offset), Integer.parseInt(string.substring(offset)));
    }
    
    public static String incrementTrailingInteger(String string, int defaultStartingNumber) {
        Pair<String, Integer> components = Strings.trailingIntegerComponents(string);
        
        if (components.b == Integer.MIN_VALUE) {
            return components.a + " " + defaultStartingNumber;
        } else {
            return components.a + (components.b + 1);
        }
    }
}
