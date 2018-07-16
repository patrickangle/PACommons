/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.httpserver;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patrick Angle
 */
public abstract class CORSBasicAuthenticator extends BasicAuthenticator {

    public CORSBasicAuthenticator(String realm) {
        super(realm);
    }

    @Override
    public Result authenticate(HttpExchange he) {
//        String origin = he.getRequestHeaders().getFirst("origin");
        he.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        if (he.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            he.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            he.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            try {
                he.sendResponseHeaders(204, -1);
            } catch (IOException ex) {
            }
        }

        return super.authenticate(he);
    }

}
