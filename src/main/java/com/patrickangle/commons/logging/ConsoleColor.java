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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author patrickangle
 */
public enum ConsoleColor {
//    public enum Colors {
        Reset(0, new Color(0, 0, 0)),
        
        BlackForeground(30, new Color(0, 0, 0)),
        RedForeground(31, new Color(194, 54, 33)),
        GreenForeground(32, new Color(37, 188, 36)),
        YellowForeground(33, new Color(173, 173, 39)),
        BlueForeground(34, new Color(73, 46, 225)),
        MagentaForeground(35, new Color(211, 56, 211)),
        CyanForeground(36, new Color(51, 187, 200)),
        WhiteForeground(37, new Color(203, 204, 205)),
        
        BrightBlackForeground(90, new Color(129, 131, 131)),
        BrightRedForeground(91, new Color(252, 57, 31)),
        BrightGreenForeground(92, new Color(49, 231, 34)),
        BrightYellowForeground(93, new Color(234, 236, 35)),
        BrightBlueForeground(94, new Color(88, 51, 255)),
        BrightMagentaForeground(95, new Color(249, 53, 248)),
        BrightCyanForeground(96, new Color(20, 240, 240)),
        BrightWhiteForeground(97, new Color(235, 235, 235)),
        
        BlackBackground(40, new Color(0, 0, 0)),
        RedBackground(41, new Color(194, 54, 33)),
        GreenBackground(42, new Color(37, 188, 36)),
        YellowBackground(43, new Color(173, 173, 39)),
        BlueBackground(44, new Color(73, 46, 225)),
        MagentaBackground(45, new Color(211, 56, 211)),
        CyanBackground(46, new Color(51, 187, 200)),
        WhiteBackground(47, new Color(203, 204, 205)),
        
        BrightBlackBackground(100, new Color(129, 131, 131)),
        BrightRedBackground(101, new Color(252, 57, 31)),
        BrightGreenBackground(102, new Color(49, 231, 34)),
        BrightYellowBackground(103, new Color(234, 236, 35)),
        BrightBlueBackground(104, new Color(88, 51, 255)),
        BrightMagentaBackground(105, new Color(249, 53, 248)),
        BrightCyanBackground(106, new Color(20, 240, 240)),
        BrightWhiteBackground(107, new Color(235, 235, 235)),
        ;
        
        private int code;
        private Color color;
        
        private ConsoleColor(int code, Color color) {
            this.code = code;
            this.color = color;
        }
        
        public int getCode() {
            return this.code;
        }
        
        public String getEscapeCode() {
            return "\033[" + this.code + "m";
        }
        
        public Color getColor() {
            return this.color;
        }
        
        public static String getEscapeCode(ConsoleColor... codes) {
            StringBuilder returnValue = new StringBuilder("\033[");
            
            for (int i = 0; i < codes.length; i++) {
                if (i != 0) {
                    returnValue.append(";");
                }
                returnValue.append(codes[i].getCode());
            }
            
            returnValue.append("m");
            
            return returnValue.toString();
        }
        
        public static Color foregroundForEscapeCode(String escapeCode) {
            Color returnColor = ConsoleColor.WhiteForeground.getColor();
            
            List<ConsoleColor> consoleColors = colorsFromEscapeCode(escapeCode);
            
            for (ConsoleColor consoleColor : consoleColors) {
                if (consoleColor.toString().toLowerCase().contains("foreground")) {
                    returnColor = consoleColor.getColor();
                }
            }
            
            return returnColor;
        }
        
        private static boolean isEscapeCode(String escapeCode) {
            return escapeCode.startsWith("\033[") && escapeCode.endsWith("m");
        }
        
        private static List<ConsoleColor> colorsFromEscapeCode(String escapeCode) {
            if (!isEscapeCode(escapeCode)) {
                return List.of();
            }
            
            String codes = escapeCode.substring("\033[".length(), escapeCode.length() - "m".length());
            
            String[] codeParts = codes.split("\\;");
            
            List<ConsoleColor> returnList = new ArrayList<>();
            
            for (String codeString : codeParts) {
                try {
                    int code = Integer.parseInt(codeString);
                    for (ConsoleColor cc : ConsoleColor.values()) {
                        if (cc.getCode() == code) {
                            returnList.add(cc);
                        }
                    }
                }  catch (Exception e) {
                }
            }
            
            return returnList;
        }
        
//    }
//    // Reset
//    public static final String RESET = "\033[0m";  // Text Reset
//
//    // Regular Colors
//    public static final String BLACK = "\033[0;30m";   // BLACK
//    public static final String RED = "\033[0;31m";     // RED
//    public static final String GREEN = "\033[0;32m";   // GREEN
//    public static final String YELLOW = "\033[0;33m";  // YELLOW
//    public static final String BLUE = "\033[0;34m";    // BLUE
//    public static final String PURPLE = "\033[0;35m";  // PURPLE
//    public static final String CYAN = "\033[0;36m";    // CYAN
//    public static final String WHITE = "\033[0;37m";   // WHITE
//
//    // Bold
//    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
//    public static final String RED_BOLD = "\033[1;31m";    // RED
//    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
//    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
//    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
//    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
//    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
//    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE
//
//    // Underline
//    public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
//    public static final String RED_UNDERLINED = "\033[4;31m";    // RED
//    public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
//    public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
//    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
//    public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
//    public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
//    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE
//
//    // Background
//    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
//    public static final String RED_BACKGROUND = "\033[41m";    // RED
//    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
//    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
//    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
//    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
//    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
//    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE
//
//    // High Intensity
//    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
//    public static final String RED_BRIGHT = "\033[0;91m";    // RED
//    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
//    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
//    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
//    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
//    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
//    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE
//
//    // Bold High Intensity
//    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
//    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
//    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
//    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
//    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
//    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
//    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
//    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE
//
//    // High Intensity backgrounds
//    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
//    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
//    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
//    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
//    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
//    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
//    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
//    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE
}