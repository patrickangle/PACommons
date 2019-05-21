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
    protected static final String style = "<style>body {font-family: sans-serif;} h1 {font-size: 1.5em;} h2 {font-size: 1.25em;} h3 {font-size: 1.1em;}</style>";
    protected static final String fileImage = "<image src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAEGWlDQ1BrQ0dDb2xvclNwYWNlR2VuZXJpY1JHQgAAOI2NVV1oHFUUPrtzZyMkzlNsNIV0qD8NJQ2TVjShtLp/3d02bpZJNtoi6GT27s6Yyc44M7v9oU9FUHwx6psUxL+3gCAo9Q/bPrQvlQol2tQgKD60+INQ6Ium65k7M5lpurHeZe58853vnnvuuWfvBei5qliWkRQBFpquLRcy4nOHj4g9K5CEh6AXBqFXUR0rXalMAjZPC3e1W99Dwntf2dXd/p+tt0YdFSBxH2Kz5qgLiI8B8KdVy3YBevqRHz/qWh72Yui3MUDEL3q44WPXw3M+fo1pZuQs4tOIBVVTaoiXEI/MxfhGDPsxsNZfoE1q66ro5aJim3XdoLFw72H+n23BaIXzbcOnz5mfPoTvYVz7KzUl5+FRxEuqkp9G/Ajia219thzg25abkRE/BpDc3pqvphHvRFys2weqvp+krbWKIX7nhDbzLOItiM8358pTwdirqpPFnMF2xLc1WvLyOwTAibpbmvHHcvttU57y5+XqNZrLe3lE/Pq8eUj2fXKfOe3pfOjzhJYtB/yll5SDFcSDiH+hRkH25+L+sdxKEAMZahrlSX8ukqMOWy/jXW2m6M9LDBc31B9LFuv6gVKg/0Szi3KAr1kGq1GMjU/aLbnq6/lRxc4XfJ98hTargX++DbMJBSiYMIe9Ck1YAxFkKEAG3xbYaKmDDgYyFK0UGYpfoWYXG+fAPPI6tJnNwb7ClP7IyF+D+bjOtCpkhz6CFrIa/I6sFtNl8auFXGMTP34sNwI/JhkgEtmDz14ySfaRcTIBInmKPE32kxyyE2Tv+thKbEVePDfW/byMM1Kmm0XdObS7oGD/MypMXFPXrCwOtoYjyyn7BV29/MZfsVzpLDdRtuIZnbpXzvlf+ev8MvYr/Gqk4H/kV/G3csdazLuyTMPsbFhzd1UabQbjFvDRmcWJxR3zcfHkVw9GfpbJmeev9F08WW8uDkaslwX6avlWGU6NRKz0g/SHtCy9J30o/ca9zX3Kfc19zn3BXQKRO8ud477hLnAfc1/G9mrzGlrfexZ5GLdn6ZZrrEohI2wVHhZywjbhUWEy8icMCGNCUdiBlq3r+xafL549HQ5jH+an+1y+LlYBifuxAvRN/lVVVOlwlCkdVm9NOL5BE4wkQ2SMlDZU97hX86EilU/lUmkQUztTE6mx1EEPh7OmdqBtAvv8HdWpbrJS6tJj3n0CWdM6busNzRV3S9KTYhqvNiqWmuroiKgYhshMjmhTh9ptWhsF7970j/SbMrsPE1suR5z7DMC+P/Hs+y7ijrQAlhyAgccjbhjPygfeBTjzhNqy28EdkUh8C+DU9+z2v/oyeH791OncxHOs5y2AtTc7nb/f73TWPkD/qwBnjX8BoJ98VQNcC+8AAAC+SURBVDgRY2BAgP9AJjZ8CygeiFCGmwXSrIaE1YHsPCD+DMRfgLgGiDEAE4YIQgBk4G4g5gLiACAuAeKpQMwMxHCAzwCQor9ADFJzEoi9gTgMiFcCMSsQgwEhA2DqQPQlIHYHYnMgLgNiMGCEMYA0yMkgf6ODm0gCf4Dsd1CsCRJnAREEALKhYkC1oLDwhOkhxgUwtTBaCsjYD8RgvaSEAcyAFzAGiCbHgH+UGoCsnywXDDcD0NMBiv8IcMB6AYM4Hoc0T8wOAAAAAElFTkSuQmCC\"/>";
    protected static final String dirImage = "<image src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAEGWlDQ1BrQ0dDb2xvclNwYWNlR2VuZXJpY1JHQgAAOI2NVV1oHFUUPrtzZyMkzlNsNIV0qD8NJQ2TVjShtLp/3d02bpZJNtoi6GT27s6Yyc44M7v9oU9FUHwx6psUxL+3gCAo9Q/bPrQvlQol2tQgKD60+INQ6Ium65k7M5lpurHeZe58853vnnvuuWfvBei5qliWkRQBFpquLRcy4nOHj4g9K5CEh6AXBqFXUR0rXalMAjZPC3e1W99Dwntf2dXd/p+tt0YdFSBxH2Kz5qgLiI8B8KdVy3YBevqRHz/qWh72Yui3MUDEL3q44WPXw3M+fo1pZuQs4tOIBVVTaoiXEI/MxfhGDPsxsNZfoE1q66ro5aJim3XdoLFw72H+n23BaIXzbcOnz5mfPoTvYVz7KzUl5+FRxEuqkp9G/Ajia219thzg25abkRE/BpDc3pqvphHvRFys2weqvp+krbWKIX7nhDbzLOItiM8358pTwdirqpPFnMF2xLc1WvLyOwTAibpbmvHHcvttU57y5+XqNZrLe3lE/Pq8eUj2fXKfOe3pfOjzhJYtB/yll5SDFcSDiH+hRkH25+L+sdxKEAMZahrlSX8ukqMOWy/jXW2m6M9LDBc31B9LFuv6gVKg/0Szi3KAr1kGq1GMjU/aLbnq6/lRxc4XfJ98hTargX++DbMJBSiYMIe9Ck1YAxFkKEAG3xbYaKmDDgYyFK0UGYpfoWYXG+fAPPI6tJnNwb7ClP7IyF+D+bjOtCpkhz6CFrIa/I6sFtNl8auFXGMTP34sNwI/JhkgEtmDz14ySfaRcTIBInmKPE32kxyyE2Tv+thKbEVePDfW/byMM1Kmm0XdObS7oGD/MypMXFPXrCwOtoYjyyn7BV29/MZfsVzpLDdRtuIZnbpXzvlf+ev8MvYr/Gqk4H/kV/G3csdazLuyTMPsbFhzd1UabQbjFvDRmcWJxR3zcfHkVw9GfpbJmeev9F08WW8uDkaslwX6avlWGU6NRKz0g/SHtCy9J30o/ca9zX3Kfc19zn3BXQKRO8ud477hLnAfc1/G9mrzGlrfexZ5GLdn6ZZrrEohI2wVHhZywjbhUWEy8icMCGNCUdiBlq3r+xafL549HQ5jH+an+1y+LlYBifuxAvRN/lVVVOlwlCkdVm9NOL5BE4wkQ2SMlDZU97hX86EilU/lUmkQUztTE6mx1EEPh7OmdqBtAvv8HdWpbrJS6tJj3n0CWdM6busNzRV3S9KTYhqvNiqWmuroiKgYhshMjmhTh9ptWhsF7970j/SbMrsPE1suR5z7DMC+P/Hs+y7ijrQAlhyAgccjbhjPygfeBTjzhNqy28EdkUh8C+DU9+z2v/oyeH791OncxHOs5y2AtTc7nb/f73TWPkD/qwBnjX8BoJ98VQNcC+8AAAB6SURBVDgRY2AYDGAt0BH/kfAHIDuIFIeBNEsh4XAgeysQIxuKi72aEapQGkiTA56CNIFM/wKlcdmETfwzSA/MBYlAzlsgJgUIARUvYILqIFUzSNs7EAEzAMQmCwweA0CBSRZggeqSIEs3VNNFII0tnokRu0KJxdTRCwBcoTdaEN5cFwAAAABJRU5ErkJggg==\"/>";

    
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
        String redirectNeeded = redirectNeeded(he.getRequestURI().getPath(), requestPath);
        
        if (redirectNeeded != null) {
            handleRedirectRequest(he, redirectNeeded);
        }

        File fileOrDirectory = resolveFileOrDirectoryIfPossible(new File(documentDirectory, requestPath));
                
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
    
    public void handleRedirectRequest(HttpExchange he, String redirectLocation) throws IOException {
        he.getResponseHeaders().add("Location", redirectLocation);
        he.sendResponseHeaders(301, 0);
        OutputStream os = he.getResponseBody();
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
        byte[] response = ("<html><head>" + style + "</head><body><h1>404 - Resource Unavailable</h1></body></html>").getBytes();
        he.getResponseHeaders().add("Content-Type", "text/html");
        he.sendResponseHeaders(404, response.length);
        OutputStream os = he.getResponseBody();
        os.write(response);
        os.close();
    }
    
    protected String directoryListingPageForDirectory(File directory, HttpExchange he) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head>");
        sb.append(style);
        sb.append("</head><body>");
        sb.append("<h3>Directory Listing</h3>");
        sb.append("<h2>" + he.getRequestURI().getPath() + "</h2>");
        sb.append("<table><tr><td></td><td><a href='..'>./..</a></td></tr>");
        List<File> files = Arrays.asList(directory.listFiles());
        
        files.sort((o1, o2) -> {
            return o1.compareTo(o2);
        });
        
        for(File file : files) {
            sb.append("<tr><td>" + (file.isDirectory() ? dirImage : fileImage) + "</td><td><a href='./" + file.getName() + (file.isDirectory() ? "/" : "") + "'>" + file.getName() + "</a></tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
    
    public String redirectNeeded(String requestPath, String fileReference) {
        File file = new File(documentDirectory, fileReference);
        
        if (file.isDirectory()) {
            if (!requestPath.endsWith("/")) {
                return requestPath.concat("/");
            }
        }
        
        return null;
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
