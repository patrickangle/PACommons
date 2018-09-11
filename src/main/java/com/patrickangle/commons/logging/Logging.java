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
package com.patrickangle.commons.logging;

import com.patrickangle.commons.util.ConsoleColors;
import com.patrickangle.commons.util.Exceptions;
import com.patrickangle.commons.util.StringOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author patrickangle
 */
public class Logging {

    // The system's default System.err before it is interuppted to capture error logging.
    private static final PrintStream NATIVE_SYSTEM_OUT = System.out;

    // The system's default System.err before it is interuppted to capture error logging.
    private static final PrintStream NATIVE_SYSTEM_ERR = System.err;

    // When enabled, calls to System.out and System.err are mapped to Logging.info and Logging.error
    private static boolean interceptSystemStreams = false;

    // When enabled, calls to Logging.trace, Logging.debug, Logging.info, Logging.warning, Logging.error, and Logging.exception are mapped to the default System.out or System.err as needed.
    private static boolean injectIntoSystemStreams = true;
    
    private static final PrintStream REPLACEMENT_SYSTEM_OUT = new PrintStream(new StringOutputStream() {
        @Override
        public void wroteString(String s) {
            Logging.info(Logging.class, s);
        }
    });
    
    private static final PrintStream REPLACEMENT_SYSTEM_ERR = new PrintStream(new StringOutputStream() {
        @Override
        public void wroteString(String s) {
            Logging.error(Logging.class, s);
        }
    });
    
    public static final List<PrintStream> outputStreams = new CopyOnWriteArrayList<>();
    public static final List<PrintStream> errorStreams = new CopyOnWriteArrayList<>();

    public static boolean isInterceptSystemStreams() {
        return interceptSystemStreams;
    }

    public static void setInterceptSystemStreams(boolean interceptSystemStreams) {
        Logging.interceptSystemStreams = interceptSystemStreams;

        if (Logging.interceptSystemStreams) {
            System.setOut(REPLACEMENT_SYSTEM_OUT);
            System.setErr(REPLACEMENT_SYSTEM_ERR);
        } else {
            System.setOut(NATIVE_SYSTEM_OUT);
            System.setErr(NATIVE_SYSTEM_ERR);
        }
    }

    public static boolean isInjectIntoSystemStreams() {
        return injectIntoSystemStreams;
    }

    public static void setInjectIntoSystemStreams(boolean injectIntoSystemStreams) {
        Logging.injectIntoSystemStreams = injectIntoSystemStreams;
    }

    public static void trace(Class caller, String message) {
        logOutAll(ConsoleColors.PURPLE + "  [TRACE] " + currentTimestamp() + " " + caller.getSimpleName() + ": " + decorateAllLines(ConsoleColors.PURPLE, message) + ConsoleColors.RESET);
    }

    public static void debug(Class caller, String message) {
        logOutAll(ConsoleColors.BLUE + "  [DEBUG] " + currentTimestamp() + " " + caller.getSimpleName() + ": " + decorateAllLines(ConsoleColors.BLUE, message) + ConsoleColors.RESET);
    }

    public static void info(Class caller, String message) {
        logOutAll(ConsoleColors.RESET + "   [INFO] " + currentTimestamp() + " " + caller.getSimpleName() + ": " + decorateAllLines(ConsoleColors.RESET, message) + ConsoleColors.RESET);
    }

    public static void warning(Class caller, String message) {
        logOutAll(ConsoleColors.YELLOW + "[WARNING] " + currentTimestamp() + " " + caller.getSimpleName() + ": " + decorateAllLines(ConsoleColors.YELLOW, message) + ConsoleColors.RESET);
    }

    public static void error(Class caller, String message) {
        logErrAll(ConsoleColors.RED + "  [ERROR] " + currentTimestamp() + " " + caller.getSimpleName() + ": " + decorateAllLines(ConsoleColors.RED, message) + ConsoleColors.RESET);
    }

    public static void exception(Class caller, Throwable exception) {
        logErrAll(ConsoleColors.RED_BOLD_BRIGHT + ConsoleColors.WHITE_BACKGROUND_BRIGHT + ConsoleColors.RED_UNDERLINED + "[EXCEPTION] " + Instant.now().toString() + " " + caller.getSimpleName() + "\n" + ConsoleColors.RESET + decorateAllLines(ConsoleColors.RED, Exceptions.humanReadableThrowable(exception)) + ConsoleColors.RESET);
    }
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd',' yyyy hh:mm:ssa").withLocale(Locale.US).withZone(ZoneId.systemDefault());

    private static String currentTimestamp() {
        return formatter.format(Instant.now()).replace("AM", "a").replace("PM", "p");
    }
    
    private static void logOutAll(String message) {
        
        for (PrintStream stream : outputStreams) {
            if (message.endsWith("\n") || message.endsWith("\n" + ConsoleColors.RESET)) {
                stream.print(message);
            } else {
                stream.println(message);
            }
        }
        if (injectIntoSystemStreams) {
            if (message.endsWith("\n") || message.endsWith("\n" + ConsoleColors.RESET)) {
                NATIVE_SYSTEM_OUT.print(message);
            } else {
                NATIVE_SYSTEM_OUT.println(message);
            }
        }
    }
    
    private static void logErrAll(String message) {
        for (PrintStream stream : errorStreams) {
            if (message.endsWith("\n") || message.endsWith("\n" + ConsoleColors.RESET)) {
                stream.print(message);
            } else {
                stream.println(message);
            }
        }
        if (injectIntoSystemStreams) {
            if (message.endsWith("\n") || message.endsWith("\n" + ConsoleColors.RESET)) {
                NATIVE_SYSTEM_ERR.print(message);
            } else {
                NATIVE_SYSTEM_ERR.println(message);
            }
        }
    }
    
    private static String decorateAllLines(String ansiDecorations, String lines) {
        return ansiDecorations + lines.replace("\n", ConsoleColors.RESET + "\n" + ansiDecorations) + ConsoleColors.RESET;
    }
}
