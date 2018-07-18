/*
 * WorldStage Waltz
 * 
 * This software is the sole property of WorldStage. WorldStage retains all
 * rights to any code or APIs contained herein.
 * 
 * (C) 2018 WorldStage. All rights reserved.
 */
package com.patrickangle.commons.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author patrickangle
 */
public class TagIcons {
    public static final Icon RED = getTagIcon(new Color(0xFC6360), new Color(0xFA5662), 12);
    public static final Icon ORANGE = getTagIcon(new Color(0xFFFFFF), new Color(0xFFFFFF), 12);
    public static final Icon YELLOW = getTagIcon(new Color(0xFFFFFF), new Color(0xFFFFFF), 12);
    public static final Icon GREEN = getTagIcon(new Color(0x86DF6A), new Color(0x69CE48), 12);
    public static final Icon BLUE = getTagIcon(new Color(0xFFFFFF), new Color(0xFFFFFF), 12);
    public static final Icon PURPLE = getTagIcon(new Color(0xFFFFFF), new Color(0xFFFFFF), 12);
    public static final Icon GRAY = getTagIcon(new Color(0xFFFFFF), new Color(0xFFFFFF), 12);
    
    private static Icon getTagIcon(Color innerColor, Color outerColor, int size) {
        Image image = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(innerColor);
        g.fillOval(1, 1, size-2, size-2);
        
        return new ImageIcon(image);
    }
}
