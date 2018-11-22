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
package com.patrickangle.commons.httpserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patrickangle.commons.Callback;
import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author patrickangle
 */
public class ServerApiHttpHandler extends CallbackJsonHttpHandler<Object>{
    protected Map<String, ApiHandler> apiHandlers;
    
    public ServerApiHttpHandler() {
        this.apiHandlers = new HashMap<>();
    }
    
    public ServerApiHttpHandler(Map<String, ApiHandler> apiHandlers) {
        this.apiHandlers = new HashMap<>(apiHandlers);
    }
    
    public Map<String, ApiHandler> getApiHandlers() {
        return this.apiHandlers;
    }

    @Override
    public void simpleHandle(HttpExchange exchange, Map<String, String> queryParameters, Consumer<Object> callback) {
        String requestPath = exchange.getRequestURI().getPath();
        if (requestPath.startsWith(exchange.getHttpContext().getPath())) {
            requestPath = requestPath.substring(exchange.getHttpContext().getPath().length());
        }
        
        apiHandlers.getOrDefault(requestPath, (defaultExchange, defaultQueryParameters, defaultCallback) -> {
            defaultCallback.accept(new Object() {
                @JsonProperty public String error = "Invalid endpoint: " + defaultExchange.getRequestURI().getPath();
            });
        }).handle(exchange, queryParameters, callback);
    }
    
    public interface ApiHandler {
        public void handle(HttpExchange exchange, Map<String, String> queryParameters, Consumer<Object> callback);
    }
}