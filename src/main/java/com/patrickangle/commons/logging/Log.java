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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Patrick Angle
 */
public class Log {

    private static Log sharedInstance = new Log();

    private List<LogItem> logItems;
    
    public Log() {
        this.logItems = new CopyOnWriteArrayList<>();
    }

    public List<LogItem> getLogItems() {
        return logItems;
    }

    public void logTrace(Class caller, String message) {
        this.logItems.add(new LogItem(caller, LogLevel.Trace, message));
    }
    
    public void logDebug(Class caller, String message) {
        this.logItems.add(new LogItem(caller, LogLevel.Debug, message));
    }
    
    public void logInfo(Class caller, String message) {
        this.logItems.add(new LogItem(caller, LogLevel.Info, message));
    }
    
    public void logWarning(Class caller, String message) {
        this.logItems.add(new LogItem(caller, LogLevel.Warning, message));
    }
    
    public void logError(Class caller, String message) {
        this.logItems.add(new LogItem(caller, LogLevel.Error, message));
    }
    
    public void logException(Class caller, String message) {
        this.logItems.add(new LogItem(caller, LogLevel.Trace, message));
    }
    
    public void clear() {
        this.logItems.clear();
    }

    public static Log sharedInstance() {
        return Log.sharedInstance;
    }
    
    public static void trace(Class caller, String message) {
        Log.sharedInstance().logTrace(caller, message);
    }
    
    public static void debug(Class caller, String message) {
        Log.sharedInstance().logDebug(caller, message);
    }
    
    public static void info(Class caller, String message) {
        Log.sharedInstance().logInfo(caller, message);
    }
    
    public static void warning(Class caller, String message) {
        Log.sharedInstance().logWarning(caller, message);
    }
    
    public static void error(Class caller, String message) {
        Log.sharedInstance().logError(caller, message);
    }
    
    public static void exception(Class caller, String message) {
        Log.sharedInstance().logException(caller, message);
    }
}
