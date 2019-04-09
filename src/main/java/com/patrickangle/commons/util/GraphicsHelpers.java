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
package com.patrickangle.commons.util;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

/**
 *
 * @author Patrick Angle
 */
public class GraphicsHelpers {

    public static void drawCenteredString(Graphics2D g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }

    public static void protectGraphics(Graphics g, Consumer<Graphics> operation) {
        Graphics g2 = g.create();
        operation.accept(g2);
        g2.dispose();
    }

    public static void protectGraphics2D(Graphics2D g, Consumer<Graphics2D> operation) {
        Graphics2D g2 = (Graphics2D) g.create();
        operation.accept(g2);
        g2.dispose();
    }

    public static BufferedImage createClipImage(Graphics2D g, Shape s) {
        // Create a translucent intermediate image in which we can perform
        // the soft clipping
        GraphicsConfiguration gc = g.getDeviceConfiguration();
        BufferedImage img = gc.createCompatibleImage(s.getBounds().width, s.getBounds().height, Transparency.TRANSLUCENT);
        Graphics2D g2 = img.createGraphics();

        // Clear the image so all pixels have zero alpha
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, img.getWidth(), img.getHeight());

        // Render our clip shape into the image.  Note that we enable
        // antialiasing to achieve the soft clipping effect.  Try
        // commenting out the line that enables antialiasing, and
        // you will see that you end up with the usual hard clipping.
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(s);

        g2.dispose();

        return img;
    }

    // draw the shadow before the object to avoid unintended side effects.
    public static void drawBorderShadow(Graphics2D graphics, Shape s, int shadowWidth) {        
        
        
        Graphics2D g = (Graphics2D) graphics.create();
        enableAntialiasing(g);
        g.translate(0.0,0.0);
        
        
//        g.setComposite(AlphaComposite.DstOver);

//    g.setBackground(Colors.transparentColor());
        Color baseColor = g.getColor();
        
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, baseColor.getAlpha() / 255.0f));
                
        int sw = shadowWidth * 2;
        for (int i = sw; i >= 2; i -= 2) {
            float pct = (float) (sw - i) / (sw - 1);
            
            g.setColor(Colors.transparentColor(baseColor, pct));
            
            g.setStroke(new BasicStroke(i));
            g.draw(s);
        }
        
        
//        g.setComposite(AlphaComposite.Clear);
//        g.setColor(Color.WHITE);
//        g.fill(s);
        
        g.dispose();
    }

    public static void enableAntialiasing(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    public static void enableStrokeNormalization(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
    
    public static Rectangle2D normalizeRectangle(Rectangle2D rect) {
        return new Rectangle2D.Double(rect.getWidth() < 0 ? rect.getX() + rect.getWidth() : rect.getX(), rect.getHeight() < 0 ? rect.getY() + rect.getHeight(): rect.getY(), Math.abs(rect.getWidth()), Math.abs(rect.getHeight()));
    }
    
    public static Rectangle normalizeRectangle(Rectangle rect) {
        return new Rectangle(rect.width < 0 ? rect.x + rect.width : rect.x, rect.height < 0 ? rect.y + rect.height: rect.y, Math.abs(rect.width), Math.abs(rect.height));
    }
}
