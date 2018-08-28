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
package com.patrickangle.commons.logging;

import java.time.Instant;

/**
 *
 * @author Patrick Angle
 */
public class LogItem {

        private LogLevel level;
        private Instant time;
        private Class caller;
        private String message;

        public LogItem(Class caller, LogLevel level, String message) {
            this.caller = caller;
            this.time = Instant.now();
            this.level = level;
            this.message = message;
        }

        public LogLevel getLevel() {
            return level;
        }

        public Instant getTime() {
            return time;
        }

        public Class getCaller() {
            return caller;
        }

        public String getMessage() {
            return message;
        }
    }
