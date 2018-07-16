/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Patrick Angle
 */
public class URIs {
    public static String queryStringFromMap(Map<String, String> map) {
        StringBuilder builder = new StringBuilder("?");
        boolean isFirst = true;
        
        for (Map.Entry<String, String> pair: map.entrySet()) {
            if (!isFirst) {
                builder.append("&");
            }
            builder.append(encodeQueryStringValue(pair.getKey()));
            builder.append("=");
            builder.append(encodeQueryStringValue(pair.getValue()));
                    
            isFirst = false;
        }
        
        return builder.toString();
    }
    
    public static String encodeQueryStringValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            return value;
        }
    }
    
    public static Map<String, String> queryStringToMap(String query) {
        Map<String, String> result = new HashMap<>();
        
        if (query == null) {
            return result;
        }
        
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
