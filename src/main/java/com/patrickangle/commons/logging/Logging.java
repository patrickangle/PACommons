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

import com.patrickangle.commons.util.Exceptions;
import com.patrickangle.commons.util.StringOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author patrickangle
 */
public class Logging {

    public static enum Level {
        Trace("Trace", List.of(ConsoleColor.MagentaForeground)),
        Debug("Debug", List.of(ConsoleColor.BlueForeground)),
        Info("Info", List.of(ConsoleColor.Reset)),
        Warning("Warning", List.of(ConsoleColor.YellowForeground)),
        Error("Error", List.of(ConsoleColor.RedForeground)),
        Exception("Thrown", List.of(ConsoleColor.RedForeground, ConsoleColor.WhiteBackground));

        private final String tag;
        private final List<ConsoleColor> consoleColors;

        Level(String tag, List<ConsoleColor> consoleColors) {
            this.tag = tag;
            this.consoleColors = consoleColors;
        }

        public String getTag() {
            return this.tag;
        }

        public List<ConsoleColor> getConsoleColors() {
            return this.consoleColors;
        }
    }

    public static enum FilterLevel {
        All(List.of(Level.values())),
        AllNormal(List.of(Level.Trace, Level.Debug, Level.Info, Level.Warning)),
        AllErrors(List.of(Level.Error, Level.Exception)),
        Trace(List.of(Level.Trace)),
        Debug(List.of(Level.Debug)),
        Info(List.of(Level.Info)),
        Warning(List.of(Level.Warning)),
        Error(List.of(Level.Error)),
        Exception(List.of(Level.Exception));

        private final List<Level> permittedLevels;

        FilterLevel(List<Level> permittedLevels) {
            this.permittedLevels = permittedLevels;
        }

        public boolean isPermitted(Level level) {
            return permittedLevels.contains(level);
        }
    }

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

    protected static final Map<FilterLevel, List<PrintStream>> additionalStreams = new EnumMap<>(FilterLevel.class);

    /**
     * Register an additional stream at FilterLevel.All.
     *
     * @param stream
     */
    public static void registerAdditionalStream(PrintStream stream) {
        registerAdditionalStream(FilterLevel.All, stream);
    }

    /**
     * Register an additional stream at the given filter level.
     *
     * @param filterLevel
     * @param stream
     */
    public static void registerAdditionalStream(FilterLevel filterLevel, PrintStream stream) {
        additionalStreams.computeIfAbsent(filterLevel, (t) -> {
            return new ArrayList<>();
        }).add(stream);
    }

    /**
     * Unregister all occurrences of the given stream from the additional
     * logging streams.
     *
     * @param stream
     */
    public static void unregisterAdditionalStream(PrintStream stream) {
        for (List<PrintStream> streams : additionalStreams.values()) {
            streams.remove(stream);
        }
    }

    /**
     * Unregister the given stream only from the given filter level.
     *
     * @param filterLevel
     * @param stream
     */
    public static void unregisterAdditionalStream(FilterLevel filterLevel, PrintStream stream) {
        additionalStreams.computeIfAbsent(filterLevel, (t) -> {
            return new ArrayList<>();
        }).remove(stream);
    }

//    public static final List<PrintStream> outputStreams = new CopyOnWriteArrayList<>();
//    public static final List<PrintStream> errorStreams = new CopyOnWriteArrayList<>();
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

    public static void trace(Object caller, Object message) {
        log(caller, message, Level.Trace);
//        log(caller, message, "  [TRACE]", false, ConsoleColors.MagentaForeground);
//        logOutAll(ConsoleColors.PURPLE + "  [TRACE] " + currentTimestamp() + " " + caller.getSimpleName() + ": " + decorateAllLines(ConsoleColors.PURPLE, message) + ConsoleColors.RESET);
    }

    public static void debug(Object caller, Object message) {
        log(caller, message, Level.Debug);
//        log(caller, message, "  [DEBUG]", false, ConsoleColors.BlueForeground);
//        logOutAll(ConsoleColors.BLUE + "  [] " + currentTimestamp() + " " + caller.getSimpleName() + ": " + decorateAllLines(ConsoleColors.BLUE, message) + ConsoleColors.RESET);
    }

    public static void info(Object caller, Object message) {
        log(caller, message, Level.Info);
//        log(caller, message, "   [INFO]", false, ConsoleColors.Reset);
//        logOutAll(ConsoleColors.RESET + "   [INFO] " + currentTimestamp() + " " + caller.getSimpleName() + ": " + decorateAllLines(ConsoleColors.RESET, message) + ConsoleColors.RESET);
    }

    public static void warning(Object caller, Object message) {
        log(caller, message, Level.Warning);
//        log(caller, message, "[WARNING]", false, ConsoleColors.YellowForeground);
//        logOutAll(ConsoleColors.YELLOW + "[WARNING] " + currentTimestamp() + " " + caller.getSimpleName() + ": " + decorateAllLines(ConsoleColors.YELLOW, message) + ConsoleColors.RESET);
    }

    public static void error(Object caller, Object message) {
        log(caller, message, Level.Error);
//        log(caller, message, "  [ERROR]", true, ConsoleColors.RedForeground);
//        logErrAll(ConsoleColors.RED + "  [ERROR] " + currentTimestamp() + " " + caller.getSimpleName() + ": " + decorateAllLines(ConsoleColors.RED, message) + ConsoleColors.RESET);
    }

    public static void exception(Object caller, Throwable exception) {
        log(caller, Exceptions.humanReadableThrowable(exception), Level.Exception);
//        log(caller, Exceptions.humanReadableThrowable(exception), " [THROWN] ", true, ConsoleColors.RedForeground, ConsoleColors.WhiteBackground);
//        logErrAll(ConsoleColors.RED_BOLD_BRIGHT + ConsoleColors.WHITE_BACKGROUND_BRIGHT + ConsoleColors.RED_UNDERLINED + " [THROWN] " + currentTimestamp() + " " + caller.getSimpleName() + "\n" + ConsoleColors.RESET + decorateAllLines(ConsoleColors.RED, Exceptions.humanReadableThrowable(exception)) + ConsoleColors.RESET);
    }

    private static void log(Object caller, Object message, Level level) {
        StringBuilder logMessage = new StringBuilder();

        // Append the log level
        logMessage.append(level.getTag());

        logMessage.append(" ");

        // Append the current time
        logMessage.append(currentTimestamp());

        // Append the class name
        if (caller instanceof Class) {
            logMessage.append(" ");
            logMessage.append(((Class) caller).getSimpleName());
        } else if (caller != null) {
            logMessage.append(" ");
            logMessage.append(caller.getClass().getSimpleName());
        }

        logMessage.append(": ");

        // Log the message
        if (message != null) {
            logMessage.append(message);
        } else {
            logMessage.append("null");
        }

        String decoratedLogMessage = decorateAllLines(ConsoleColor.getEscapeCode(level.getConsoleColors().toArray(new ConsoleColor[0])), logMessage.toString());

        if (level == Level.Error || level == Level.Exception) {
            logErrAll(decoratedLogMessage);
        } else {
            logOutAll(decoratedLogMessage);
        }

        for (Entry<FilterLevel, List<PrintStream>> entry : additionalStreams.entrySet()) {
            if (entry.getKey().isPermitted(level)) {
                for (PrintStream stream : entry.getValue()) {
                    if (decoratedLogMessage.endsWith("\n") || decoratedLogMessage.endsWith("\n" + ConsoleColor.Reset.getEscapeCode())) {
                        stream.print(decoratedLogMessage);
                    } else {
                        stream.println(decoratedLogMessage);
                    }
                }
            }
        }
    }

//    private static void log(Class caller, Object message, String tag, boolean isError, ConsoleColors... consoleColors) {
//        StringBuilder logMessage = new StringBuilder(tag);
//        logMessage.append(" ");
//        logMessage.append(currentTimestamp());
//
//        if (caller != null) {
//            logMessage.append(" ");
//            logMessage.append(caller.getSimpleName());
//        }
//
//        logMessage.append(": ");
//
//        if (message != null) {
//            logMessage.append(message);
//        } else {
//            logMessage.append("null");
//        }
//
//        String decoratedLogMessage = decorateAllLines(ConsoleColors.getEscapeCode(consoleColors), logMessage.toString());
//
//        if (isError) {
//            logErrAll(decoratedLogMessage);
//        } else {
//            logOutAll(decoratedLogMessage);
//        }
//    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd',' yyyy hh:mm:ssa").withLocale(Locale.US).withZone(ZoneId.systemDefault());

    private static String currentTimestamp() {
        return formatter.format(Instant.now()).replace("AM", "a").replace("PM", "p");
    }

    private static void logOutAll(String message) {

//        for (PrintStream stream : outputStreams) {
//            if (message.endsWith("\n") || message.endsWith("\n" + ConsoleColors.Reset.getEscapeCode())) {
//                stream.print(message);
//            } else {
//                stream.println(message);
//            }
//        }
        if (injectIntoSystemStreams) {
            if (message.endsWith("\n") || message.endsWith("\n" + ConsoleColor.Reset.getEscapeCode())) {
                NATIVE_SYSTEM_OUT.print(message);
            } else {
                NATIVE_SYSTEM_OUT.println(message);
            }
        }
    }

    private static void logErrAll(String message) {
//        for (PrintStream stream : errorStreams) {
//            if (message.endsWith("\n") || message.endsWith("\n" + ConsoleColors.Reset.getEscapeCode())) {
//                stream.print(message);
//            } else {
//                stream.println(message);
//            }
//        }
        if (injectIntoSystemStreams) {
            if (message.endsWith("\n") || message.endsWith("\n" + ConsoleColor.Reset.getEscapeCode())) {
                NATIVE_SYSTEM_ERR.print(message);
            } else {
                NATIVE_SYSTEM_ERR.println(message);
            }
        }
    }

    private static String decorateAllLines(String ansiDecorations, String lines) {
        return ansiDecorations + lines.replace("\n", ConsoleColor.Reset.getEscapeCode() + "\n" + ansiDecorations) + ConsoleColor.Reset.getEscapeCode();
    }
}
