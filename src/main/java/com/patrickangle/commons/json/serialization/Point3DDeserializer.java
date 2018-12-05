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
package com.patrickangle.commons.json.serialization;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.patrickangle.commons.types.Point3D;
import java.io.IOException;

/**
 *
 * @author Patrick Angle
 */
public class Point3DDeserializer extends JsonDeserializer<Point3D>{
    
    @Override
    public Point3D deserialize(JsonParser p, DeserializationContext dc) throws IOException, JsonProcessingException {
        try {
            return new Point3D(p.getText());
        } catch (NumberFormatException ex) {
            throw new JsonParseException(p, "Paramter could not be parsed as a double.", ex);
        }
    }
}