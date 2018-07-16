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
public class DocumentServerHttpHandler implements HttpHandler {

    protected File documentDirectory;
    protected List<String> defaultFiles;
    protected boolean showDirectoryListings;

    public DocumentServerHttpHandler(File documentsDirectory) {
        this(documentsDirectory, new ArrayList<String>(Arrays.asList("index.html", "index.htm")), true);
    }

    public DocumentServerHttpHandler(File documentDirectory, List<String> defaultFiles, boolean showDirectoryListings) {
        this.documentDirectory = documentDirectory;
        this.defaultFiles = defaultFiles;
        this.showDirectoryListings = showDirectoryListings;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        String requestPath = he.getRequestURI().getPath();
        if (requestPath.startsWith(he.getHttpContext().getPath())) {
            requestPath = requestPath.substring(he.getHttpContext().getPath().length());
        }

        File fileOrDirectory = resolveFileOrDirectoryIfPossible(new File(documentDirectory, requestPath));
        
        System.out.println("requestPath=" + requestPath + "\tfileOrDirectory=" + fileOrDirectory.toString());
        
        if (fileOrDirectory != null) {
            if (fileOrDirectory.isFile()) {
                handleFileRequest(fileOrDirectory, he);
            } else if (fileOrDirectory.isDirectory() && showDirectoryListings) {
                handleDirectoryRequest(fileOrDirectory, he);
            } else {
                handleUnavailableRequest(fileOrDirectory, he);
            }
        } else {
            handleUnavailableRequest(fileOrDirectory, he);
        }
    }

    public void handleFileRequest(File file, HttpExchange he) throws IOException {
        byte[] response = Files.bytesFromFile(file);
        
        if (response == null) {
            handleUnavailableRequest(file, he);
        }
        
        he.getResponseHeaders().add("Content-Type", Files.mimeTypeFromFile(file));
        he.sendResponseHeaders(200, response.length);
        OutputStream os = he.getResponseBody();
        os.write(response);
        os.close();
    }

    public void handleDirectoryRequest(File directory, HttpExchange he) throws IOException {
        byte[] response = this.directoryListingPageForDirectory(directory, he).getBytes();
        
        he.getResponseHeaders().add("Content-Type", "text/html");
        he.sendResponseHeaders(200, response.length);
        OutputStream os = he.getResponseBody();
        os.write(response);
        os.close();
    }

    public void handleUnavailableRequest(File fileOrDirectory, HttpExchange he) throws IOException {
        byte[] response = "<html><body><h1>Resource Unavailable</h1></body></html>".getBytes();
        he.getResponseHeaders().add("Content-Type", "text/html");
        he.sendResponseHeaders(404, response.length);
        OutputStream os = he.getResponseBody();
        os.write(response);
        os.close();
    }
    
    protected String directoryListingPageForDirectory(File directory, HttpExchange he) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>Directory Listing</h3>");
        sb.append("<h2>" + he.getRequestURI().getPath() + "</h2>");
        sb.append("<table><tr><td></td><td><a href='..'>./..</a></td></tr>");
        for(File file : directory.listFiles()) {
            sb.append("<tr><td>" + (file.isDirectory() ? "DIR" : "FILE") + "</td><td><a href='./" + file.getName() + "'>" + file.getName() + "</a></tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    public File resolveFileOrDirectoryIfPossible(File fileOrDirectory) {
        if (fileOrDirectory.exists()) {
            if (fileOrDirectory.isFile()) {
                return fileOrDirectory;
            } else if (fileOrDirectory.isDirectory()) {
                for (String defaultFile : this.defaultFiles) {
                    File potentialFile = new File(fileOrDirectory, defaultFile);
                    if (potentialFile.exists() && potentialFile.isFile()) {
                        return potentialFile;
                    }
                }
                return fileOrDirectory;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
