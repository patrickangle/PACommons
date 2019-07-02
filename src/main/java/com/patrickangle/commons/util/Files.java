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
import java.util.Arrays;
import javax.activation.MimetypesFileTypeMap;

/**
 *
 * @author Patrick Angle
 */
public class Files {

    public static String mimeTypeFromFile(File file) throws IOException {
        System.out.println(file.getName());
        if (file.getName().endsWith(".m3u") || file.getName().endsWith(".m3u8")) {
            return "application/x-mpegurl";
        } else if (file.getName().endsWith(".3gp")) {
            return "video/3gpp";
        } else if (file.getName().endsWith(".mp4") || file.getName().endsWith(".m4a") || file.getName().endsWith(".m4p") || file.getName().endsWith(".m4b") || file.getName().endsWith(".m4r") || file.getName().endsWith(".m4v")) {
            return "video/mp4";
        } else if (file.getName().endsWith(".m1v")) {
            return "video/mpeg";
        } else if (file.getName().endsWith(".ogg")) {
            return "video/ogg";
        } else if (file.getName().endsWith(".mov") || file.getName().endsWith(".qt")) {
            return "video/quicktime";
        } else if (file.getName().endsWith(".webm")) {
            return "video/webm";
        } else if (file.getName().endsWith(".m4v")) {
            return "video/x-m4v";
        } else if (file.getName().endsWith(".asf") || file.getName().endsWith(".wma")) {
            return "video/ms-asf";
        } else if (file.getName().endsWith(".wmv")) {
            return "video/x-ms-wmv";
        } else if (file.getName().endsWith(".avi")) {
            return "video/x-msvideo";
        } else {
            return MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file);
        }
    }

    public static String mimeTypeFromFileContents(String string) throws IOException {
        return MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(string);
    }

    public static byte[] bytesFromFile(File file) throws IOException {
        byte[] bytearray = new byte[(int) file.length()];

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        bis.read(bytearray, 0, bytearray.length);
        bis.close();

        System.out.println(file.getName() + ": " + Arrays.toString(bytearray));
        
        return bytearray;
    }
}
