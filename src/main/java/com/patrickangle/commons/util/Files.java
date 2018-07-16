/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.activation.MimetypesFileTypeMap;

/**
 *
 * @author Patrick Angle
 */
public class Files {
    public static String mimeTypeFromFile(File file) throws IOException {
        return MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file);
    }

    public static byte[] bytesFromFile(File file) throws IOException {
        byte[] bytearray = new byte[(int) file.length()];

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        bis.read(bytearray, 0, bytearray.length);
        bis.close();

        return bytearray;
    }
}
