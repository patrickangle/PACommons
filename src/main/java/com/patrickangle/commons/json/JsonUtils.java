/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.json;

import com.patrickangle.commons.json.serialization.ColorDeserializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
//import com.worldstage.viz280.platform.twitter.TwitterUtils;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patrick Angle
 */
public class JsonUtils {
    public static <T extends Object> T getObjectFromJson(String json, Class<T> hint) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(MapperFeature.AUTO_DETECT_CREATORS, MapperFeature.AUTO_DETECT_FIELDS, MapperFeature.AUTO_DETECT_GETTERS, MapperFeature.AUTO_DETECT_IS_GETTERS);
        mapper.configure(MapperFeature.INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES,false);
        
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Color.class, new ColorDeserializer());
        mapper.registerModule(module);

        return mapper.readValue(json, hint);
    }
    
    public static <T extends Object> T getObjectFromJson(File file, Class<T> hint) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(MapperFeature.AUTO_DETECT_CREATORS, MapperFeature.AUTO_DETECT_FIELDS, MapperFeature.AUTO_DETECT_GETTERS, MapperFeature.AUTO_DETECT_IS_GETTERS);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Color.class, new ColorDeserializer());
        mapper.registerModule(module);

        return mapper.readValue(file, hint);
    }
    
    public static Map<String, Object> getMapFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        InputStream from = new ByteArrayInputStream(json.getBytes());
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };

        try {
            return mapper.readValue(from, typeRef);
        } catch (IOException ex) {
//            Logger.getLogger(TwitterUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap<>();
        }
    }
    
    public static <T extends Object> List<T> getListFromJson(String json, Class<T> hint) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(MapperFeature.AUTO_DETECT_CREATORS, MapperFeature.AUTO_DETECT_FIELDS, MapperFeature.AUTO_DETECT_GETTERS, MapperFeature.AUTO_DETECT_IS_GETTERS);
        
        
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, hint);
        try {
            return mapper.readValue(json, listType);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<T>();
        }
    }
    
    public static <T extends Object> List<T> getListFromJson(File json, Class<T> hint) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(MapperFeature.AUTO_DETECT_CREATORS, MapperFeature.AUTO_DETECT_FIELDS, MapperFeature.AUTO_DETECT_GETTERS, MapperFeature.AUTO_DETECT_IS_GETTERS);
        
        
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, hint);
        try {
            return mapper.readValue(json, listType);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<T>();
        }
    }
    
    public static String prettyPrintJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object obj = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException ex) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
            return json;
        }
    }

}
