/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.httpserver;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.patrickangle.commons.json.serialization.ColorSerializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.patrickangle.commons.Callback;
import com.patrickangle.commons.util.URIs;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author Patrick Angle
 */
public abstract class CallbackJsonHttpHandler<T> implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        Map<String, String> params = URIs.queryStringToMap(he.getRequestURI().getRawQuery());

        simpleHandle(he, params, (object) -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.disable(MapperFeature.AUTO_DETECT_CREATORS, MapperFeature.AUTO_DETECT_FIELDS, MapperFeature.AUTO_DETECT_GETTERS, MapperFeature.AUTO_DETECT_IS_GETTERS);

            SimpleModule module = new SimpleModule();
            module.addSerializer(Color.class, new ColorSerializer());
            mapper.registerModule(module);

            try {
                byte[] response = mapper.writeValueAsString(object).getBytes();

                he.getResponseHeaders().add("Content-Type", "application/json");
                if (he.getResponseHeaders().get("Access-Control-Allow-Origin") == null) {
                    he.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                }
                if (he.getResponseHeaders().get("Access-Control-Allow-Headers") == null) {
                    he.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                }

                he.sendResponseHeaders(200, response.length);
                try (OutputStream os = he.getResponseBody()) {
                    os.write(response);
                }
            } catch (IOException ex) {
            }
        });
    }
    
    public abstract void simpleHandle(HttpExchange exchange, Map<String, String> queryParameters, Consumer<T> callback);

}
