/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util.legacy;

/**
 *
 * @author Patrick Angle
 */
@Deprecated(forRemoval = true)
public class NumberUtils {
    @Deprecated(forRemoval = true)
    public static short bytesToShort(byte b1, byte b2) {
        return (short) ((b1 << 8) | (b2 & 0xFF));
    }
}
