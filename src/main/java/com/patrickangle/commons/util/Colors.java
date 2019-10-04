/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util;

import java.awt.Color;

/**
 *
 * @author Patrick Angle
 */
public class Colors {

    public static Color red() {
        return new Color(0xff5b55);
    }

    public static Color richRed() {
        return new Color(0xFF332b);
    }

    public static Color orange() {
        return new Color(0xFFA134);
    }

    public static Color richOrange() {
        return new Color(0xFF8A02);
    }

    public static Color yellow() {
        return new Color(0xFFD034);
    }

    public static Color richYellow() {
        return new Color(0xFFC502);
    }

    public static Color green() {
        return new Color(0x7ADD5A);
    }

    public static Color richGreen() {
        return new Color(0x59D531);
    }

    public static Color blue() {
        return new Color(0x46B6F8);
    }

    public static Color richBlue() {
        return new Color(0x18A4F7);
    }

    public static Color purple() {
        return new Color(0xD086E3);
    }

    public static Color richPurple() {
        return new Color(0xC568DD);
    }

    public static Color transparentColor() {
        return new Color(0, 0, 0, 0);
    }

    public static Color transparentColor(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color transparentColor(Color color, float alpha) {
        return Colors.transparentColor(color, (int)((float)color.getAlpha() * alpha));
    }
    
    public static Color grey(int grey) {
        return new Color(grey, grey, grey);
    }
    
    public static Color grey(float grey) {
        return new Color(grey, grey, grey);
    }

    public static Color randomColor() {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static String toHex(Color color) {
        String hexColour = Integer.toHexString(color.getRGB() & 0xffffff);
        if (hexColour.length() < 6) {
            hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
        }
        return "#" + hexColour;
    }
}
