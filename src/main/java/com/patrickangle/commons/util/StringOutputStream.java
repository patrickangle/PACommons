/*
 * Patrick Angle Commons Library
 * Copyright 2018 Patrick Angle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
