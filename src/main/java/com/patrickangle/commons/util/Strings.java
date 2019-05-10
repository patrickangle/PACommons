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
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Patrick Angle
 */
public class Strings {
    // [^\s\"']+|\"([^\"]*)\"|'([^']*)'
    private static Pattern SPLIT_BY_UNESCAPED_WHITESPACE_REGEX = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
    
    
    public static List<String> splitByUnescapedWhitespace(String string) {
        List<String> matchList = new ArrayList<String>();
        Matcher regexMatcher = SPLIT_BY_UNESCAPED_WHITESPACE_REGEX.matcher(string);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                // Add double-quoted string without the quotes
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                // Add single-quoted string without the quotes
                matchList.add(regexMatcher.group(2));
            } else {
                // Add unquoted word
                matchList.add(regexMatcher.group());
            }
        }
        
        return matchList;
    }

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
            return components.a + defaultStartingNumber;
        } else {
            return components.a + (components.b + 1);
        }
    }
    
    public static String bitwiseOr(String a, String b) {
        int length = Math.max(a.length(), b.length());
        
        char[] aChars = new char[length];
        char[] bChars = new char[length];
        
        a.getChars(0, a.length() - 1, aChars, 0);
        b.getChars(0, b.length() - 1, bChars, 0);
        
        char[] orChars = new char[length];
        
        for (int i = 0; i < length; i++) {
            orChars[i] = (char) (aChars[i] | bChars[i]);
        }
        
        return String.valueOf(orChars);
    }
    
    public static String bitwiseXor(String a, String b) {
        int length = Math.max(a.length(), b.length());
        
        char[] aChars = new char[length];
        char[] bChars = new char[length];
        
        a.getChars(0, a.length() - 1, aChars, 0);
        b.getChars(0, b.length() - 1, bChars, 0);
        
        char[] xorChars = new char[length];
        
        for (int i = 0; i < length; i++) {
            xorChars[i] = (char) (aChars[i] ^ bChars[i]);
        }
        
        return String.valueOf(xorChars);
    }
    
    public static String bitwiseAnd(String a, String b) {
        int length = Math.max(a.length(), b.length());
        
        char[] aChars = new char[length];
        char[] bChars = new char[length];
        
        a.getChars(0, a.length() - 1, aChars, 0);
        b.getChars(0, b.length() - 1, bChars, 0);
        
        char[] andChars = new char[length];
        
        for (int i = 0; i < length; i++) {
            andChars[i] = (char) (aChars[i] & bChars[i]);
        }
        
        return String.valueOf(andChars);
    }

    public static String fromFileOnClasspath(String path) throws IOException {
        return fromFileOnClasspath(path, Charset.defaultCharset());
    }
    
    public static String fromFileOnClasspath(String path, Charset charset) throws IOException {
        byte[] encoded = Strings.class.getClassLoader().getResourceAsStream(path).readAllBytes();
        return new String(encoded, charset);
    }
    
}
