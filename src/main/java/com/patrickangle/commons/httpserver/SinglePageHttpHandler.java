/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.httpserver;

import com.patrickangle.commons.util.Files;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Patrick Angle
 */
public abstract class SinglePageHttpHandler implements HttpHandler {
    
    public abstract String getPageContents();
    public abstract String getMimeType();

    @Override
    public void handle(HttpExchange he) throws IOException {
        String requestPath = he.getRequestURI().getPath();
        if (requestPath.startsWith(he.getHttpContext().getPath())) {
            requestPath = requestPath.substring(he.getHttpContext().getPath().length());
        }        
        handleStringRequest(getPageContents(), he);
        
    }
    
    public void handleStringRequest(String file, HttpExchange he) throws IOException {
        byte[] response = file.getBytes();//Files.bytesFromFile(file);

        
        he.getResponseHeaders().add("Content-Type", getMimeType());
        he.sendResponseHeaders(200, response.length);
        OutputStream os = he.getResponseBody();
        os.write(response);
        os.close();
    }

}
