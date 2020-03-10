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
package com.patrickangle.commons.laf.modern.ui.icon;

import com.patrickangle.commons.laf.modern.ModernUIUtilities;
import com.patrickangle.commons.util.CompatibleImageUtil;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

/**
 *
 * @author patrickangle
 */
public class TemplateImageIcon implements Icon {
    private final Icon icon;
    private final Paint paint;
    
    private final BufferedImage templateImage;
    
    public TemplateImageIcon(Icon icon, Paint paint) {
        this.icon = icon;
        this.paint = paint;
        
        templateImage = CompatibleImageUtil.compatibleBufferedImage((int) (icon.getIconWidth() * ModernUIUtilities.getDisplayScale()), (int) (icon.getIconHeight() * ModernUIUtilities.getDisplayScale()), BufferedImage.TRANSLUCENT);
        Graphics2D g = templateImage.createGraphics();
        g.setPaint(paint);
        g.scale(ModernUIUtilities.getDisplayScale(), ModernUIUtilities.getDisplayScale());
        g.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        g.setComposite(AlphaComposite.DstIn);
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        g2.scale(ModernUIUtilities.getDisplayScaleInverse(), ModernUIUtilities.getDisplayScaleInverse());
        g2.drawImage(templateImage, 0, 0, c);
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return icon.getIconHeight();
    }
    
}
