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
package com.patrickangle.commons.laf.modern.ui.util;

import com.patrickangle.commons.util.CompatibleImageUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author patrickangle
 */
public class NoisePaint extends TexturePaint {

    private static final long DEFAULT_SEED = 72194;

    public NoisePaint(Color color, float variance, float mean) {
        super(generateTexture(DEFAULT_SEED, color, variance, mean, new Dimension(64, 64)), new Rectangle(0, 0, 64, 64));
    }

    private static BufferedImage generateTexture(long seed, Color color, float variance, float mean, Dimension dimension) {
        Random r = new Random(seed);
        
        int red = ((color.getRGB() >> 16) & 0xff) << 16;
        int green = ((color.getRGB() >> 8) & 0xff) << 8;
        int blue = ((color.getRGB() >> 0) & 0xff) << 0;
        
        BufferedImage image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = CompatibleImageUtil.getPixelsFrom(image);
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int alpha = (int) Math.round((r.nextDouble() * Math.sqrt(variance) + mean) * 255);
                pixels[(y * width) + x] = (alpha << 24) + red + green + blue;
            }
        }
            
        CompatibleImageUtil.setPixelsOn(image, pixels);
        
        return image;
    }

    @Override
    public int getTransparency() {
        return TRANSLUCENT;
    }
}
