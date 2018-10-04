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

import com.patrickangle.commons.logging.Logging;
import com.patrickangle.commons.util.legacy.ArrayUtils;
import java.awt.Font;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author patrickangle
 */
public class Exceptions {
    private static final int MAXIMUM_STACK_TRACE_DEPTH = 100;
    private static final int MAXIMUM_LINES_PER_CAUSE = 100;
    
    public static final Thread.UncaughtExceptionHandler GUI_UNCAUGHT_EXCEPTION_HANDLER = (Thread thread, Throwable throwable) -> {
        StringWriter writer = new StringWriter();
        writer.write(Platform.shortSystemDescriptor() + "\n");
        writer.write("Exception on thread: " + thread.getName() + "\n");
        String humanReadableThrowable = humanReadableThrowable(throwable);
        writer.write(humanReadableThrowable);
        
        String exceptionInformation = writer.toString();
        
        Logging.exception(Exceptions.class, throwable);
        
        JTextArea area = new JTextArea(20, 80);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
        
        area.setTabSize(4);
        area.setText(exceptionInformation);
        area.setCaretPosition(0);
        
        String copyOption = "Copy to Clipboard";
        
        JOptionPane pane = new JOptionPane(new JScrollPane(area), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, null, new String[]{copyOption, "Close"});
        pane.createDialog(null, "Uncaught Exception").setVisible(true);
        
        if (pane.getValue().equals(copyOption)) {
            area.setSelectionStart(0);
            area.setSelectionEnd(area.getText().length());
            area.copy();
        }
    };
    
    public static final Thread.UncaughtExceptionHandler HEADLESS_UNCAUGHT_EXCEPTION_HANDLER = (Thread thread, Throwable throwable) -> {
        Logging.exception(Exceptions.class, throwable);
    };
    
    public static void installGuiUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(Exceptions.GUI_UNCAUGHT_EXCEPTION_HANDLER);
    }
    
    public static void installHeadlessUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(Exceptions.HEADLESS_UNCAUGHT_EXCEPTION_HANDLER);
    }
    
    @Deprecated
    public static void raiseThrowableToUser(Throwable throwable) {
        Exceptions.GUI_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(Thread.currentThread(), throwable);
    }
    
    public static void handleThrowableWithDefaultGuiHandler(Throwable throwable) {
        Exceptions.GUI_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(Thread.currentThread(), throwable);
    }
    
    public static void handleThrowableWithDefaultHeadlessHandler(Throwable throwable) {
        Exceptions.HEADLESS_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(Thread.currentThread(), throwable);
    }
    
    public static String humanReadableThrowable(Throwable throwable) {
        StringBuilder builder = new StringBuilder();
        humanReadableThrowableBuilder(builder, throwable, 0);
        return builder.toString();
    }
    
    public static void humanReadableThrowableBuilder(StringBuilder builder, Throwable throwable, int currentDepth) {
        if (currentDepth >= MAXIMUM_STACK_TRACE_DEPTH) {
            return;
        }
        Throwable cause = throwable.getCause();
        if (cause != null) {
            humanReadableThrowableBuilder(builder, throwable, currentDepth + 1);
        } else {
            builder.append(throwable.getClass().getSimpleName());
            builder.append(": ");
            builder.append(throwable.getMessage());
            builder.append("\n");
            StackTraceElement[] stack = throwable.getStackTrace();
            int line = 0;
            for (StackTraceElement element : stack) {
                builder.append("\t");
                builder.append(element.getClassName());
                builder.append(".");
                builder.append(element.getMethodName());
                builder.append("() line ");
                builder.append(element.getLineNumber());
                builder.append("\n");
                line++;
                if (line >= MAXIMUM_LINES_PER_CAUSE) {
                    break;
                }
            }
        }
    }
}
