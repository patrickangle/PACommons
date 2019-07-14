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
package com.patrickangle.commons.laf.modern.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;

/**
 *
 * @author patrickangle
 */
public class ModernBasicBorder implements Border, UIResource {

    private static final int BLUR_SAFE_REGION = 4;
    private Insets additionalInsets;

    public ModernBasicBorder(Insets additionalInsets) {
        this.additionalInsets = additionalInsets;
    }

    @Override
    public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {

    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new InsetsUIResource(BLUR_SAFE_REGION + additionalInsets.top, BLUR_SAFE_REGION + additionalInsets.left, BLUR_SAFE_REGION + additionalInsets.bottom, BLUR_SAFE_REGION + additionalInsets.right);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
