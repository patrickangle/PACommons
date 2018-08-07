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
package com.patrickangle.commons.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.patrickangle.commons.json.serialization.ColorDeserializer;
import com.patrickangle.commons.json.serialization.ColorSerializer;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
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
public class JsonObjects {
    public static ObjectMapper defaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, "@class");
        mapper.disable(MapperFeature.AUTO_DETECT_CREATORS, MapperFeature.AUTO_DETECT_FIELDS, MapperFeature.AUTO_DETECT_GETTERS, MapperFeature.AUTO_DETECT_IS_GETTERS);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Color.class, new ColorDeserializer());
        module.addSerializer(Color.class, new ColorSerializer());
        mapper.registerModule(module);

        return mapper;
    }
    
    public static <T extends Object> T deserialize(Class<T> objectClass, String json) throws IOException {
        return JsonObjects.defaultMapper().readValue(json, objectClass);
    }
    
    public static <T extends Object> T deserialize(Class<T> objectClass, File jsonFile) throws IOException {
        return JsonObjects.defaultMapper().readValue(jsonFile, objectClass);
    }
    
    public static Map<String, Object> deserializeMap(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {};
        
        return mapper.readValue(json, typeReference);
    }
    
    public static Map<String, Object> deserializeMap(File jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {};
        
        return mapper.readValue(jsonFile, typeReference);
    }
    
    public static <T extends Object> List<T> deserializeList(Class<T> listContentClass, String json) throws IOException {
        ObjectMapper mapper = JsonObjects.defaultMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, listContentClass);
        
        return mapper.readValue(json, listType);
    }
    
    public static <T extends Object> List<T> deserializeList(Class<T> listContentClass, File jsonFile) throws IOException {
        ObjectMapper mapper = JsonObjects.defaultMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, listContentClass);
        
        return mapper.readValue(jsonFile, listType);
    }
    
    public static String serialize(Object object) throws IOException {
        return JsonObjects.defaultMapper().writeValueAsString(object);
    }
    
    public static void serializeToFile(Object object, File file) throws IOException {
        JsonObjects.defaultMapper().writeValue(file, object);
    }
    
    public static String prettyPrint(String json) {
        try {
            ObjectMapper mapper = JsonObjects.defaultMapper();
            Object obj = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException ex) {
            return json;
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
