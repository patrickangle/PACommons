/*
 * WorldStage Waltz
 * 
 * Copyright © 2018 WorldStage.
 * All rights reserved.
 * 
 * Unauthorized tampering, copying, or distribution of this file,
 *   via any medium, is strictly prohibited.
 * 
 * The contents of this file are proprietary and confidential.
 */
package com.patrickangle.commons.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author patrickangle
 */
public abstract class StringOutputStream extends OutputStream {

        private final StringBuilder sb = new StringBuilder();

        public StringOutputStream() {
            
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }

        @Override
        public void write(int b) throws IOException {

            if (b == '\r') {
                return;
            }

            if (b == '\n') {
                final String text = sb.toString() + "\n";

                wroteString(text);
                sb.setLength(0);
            } else {
                sb.append((char) b);
            }
        }
        
        public abstract void wroteString(String s);
    }
