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
import com.patrickangle.commons.logging.Logging;
import com.patrickangle.commons.util.URIs;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.function.Consumer;
import java.util.zip.GZIPOutputStream;

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
                
//                if (he.getRequestHeaders().get("Accept-Encoding").contains("gzip")) {
                    he.getResponseHeaders().add("Content-Encoding", "gzip");

                    byte[] gzipResponse = gzipCompress(response);
                    he.sendResponseHeaders(200, gzipResponse.length);
                    try (OutputStream os = he.getResponseBody()) {
                        os.write(gzipResponse);
                    }
//                } else {
//                    Logging.warning(CallbackJsonHttpHandler.class, "A client did not report the ability to accept gzip from HttpServer. More bandwidth will be used by the client.");
//
//                    he.sendResponseHeaders(200, response.length);
//                    try (OutputStream os = he.getResponseBody()) {
//                        os.write(response);
//                    }
//                }
                
            } catch (IOException ex) {
                Logging.exception(CallbackJsonHttpHandler.class, ex);
            }
        });
    }
    
    public abstract void simpleHandle(HttpExchange exchange, Map<String, String> queryParameters, Consumer<T> callback);

    
    private static byte[] gzipCompress(byte[] uncompressedData) {
        byte[] result = new byte[]{};
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressedData.length);
             GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(uncompressedData);
            gzipOS.close();
            result = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
