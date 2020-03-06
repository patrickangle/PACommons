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
package com.patrickangle.commons.laf.modern.colors;

import com.patrickangle.commons.laf.modern.util.ModernColors;
import com.patrickangle.commons.util.Colors;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import javax.swing.JComponent;

/**
 *
 * @author patrickangle
 */
public abstract class AbstractModernColors implements ModernColors {
    @Override
    public Color componentShadowColor(JComponent c) {
        return Colors.transparentColor(Color.BLACK, 0.3f);
    }

    @Override
    public Paint componentFocusRingPaint(JComponent c) {
        return Colors.transparentColor(accentColor(), 0.5f);
    }

    @Override
    public Paint componentNormalPaint(JComponent c) {
        return Colors.grey(0.4f);
    }

    @Override
    public Paint componentSelectedPaint(JComponent c) {
        return Colors.withBrightness(accentColor(), 0.8f);
    }

    @Override
    public Paint componentRolloverPaint(JComponent c) {
        return Colors.grey(0.5f);
    }

    @Override
    public Paint componentRolloverSelectedPaint(JComponent c) {
        return Colors.withBrightness(accentColor(), 0.9f);
    }

    @Override
    public Paint componentPressedPaint(JComponent c) {
        return Colors.grey(0.3f);
    }

    @Override
    public Paint componentPressedSelectedPaint(JComponent c) {
        return Colors.withBrightness(accentColor(), 0.7f);
    }

    @Override
    public Paint componentDisabledPaint(JComponent c) {
        return Colors.grey(0.3f);
    }

    @Override
    public Paint componentNormalTextPaint(JComponent c) {
        return Colors.transparentColor(Color.WHITE, 0.96f);
    }

    @Override
    public Paint componentSelectedTextPaint(JComponent c) {
        return Colors.transparentColor(Color.WHITE, 0.96f);
    }

    @Override
    public Paint componentDisabledTextPaint(JComponent c) {
        return Colors.transparentColor(Color.WHITE, 0.3f);
    }
    
    @Override
    public Color textAreaNormalTextColor() {
        return Colors.transparentColor(Color.WHITE, 0.96f);
    }
    
    @Override
    public Color textAreaDisabledTextColor() {
        return Colors.transparentColor(Color.WHITE, 0.3f);
    }
    
    @Override
    public Paint textAreaNormalBackgroundPaint(JComponent c) {
        return Colors.transparentColor(Colors.grey(0.3f), 0.9f);
    }
    
    @Override
    public Paint textAreaDisabledBackgroundPaint(JComponent c) {
        return Colors.transparentColor(Colors.grey(0.25f), 0.9f);
    }
    
    @Override
    public Paint textAreaNormalBaselinePaint(JComponent c) {
        return Colors.transparentColor(Colors.grey(0.33f), 0.9f);
    }
    
    @Override
    public Paint textAreaDisabledBaselinePaint(JComponent c) {
        return Colors.transparentColor(Colors.grey(0.28f), 0.9f);
    }
    
    @Override
    public Paint textAreaNormalBorderPaint(JComponent c) {
        return Colors.transparentColor(Colors.grey(0.33f), 0.9f);
    }
    
    @Override
    public Paint textAreaDisabledBorderPaint(JComponent c) {
        return Colors.transparentColor(Colors.grey(0.29f), 0.9f);
    }
    
    @Override
    public Color textAreaSelectionHighlightColor() {
        return Colors.transparentColor(accentColor(), 0.5f);
    }

    @Override
    public Paint windowBackgroundPaint(JComponent c) {
        return Colors.grey(0.2f);
    }

    @Override
    public Paint panelBackgroundPaint(JComponent c) {
        return Colors.grey(0.2f);
    }
    
    public Paint workspaceBackgroundPaint(JComponent c) {
        return Colors.grey(0.1f);
    }
    
    
    
    /**
     * Get the current accent color, which may change between invocations.
     * @return 
     */
    public abstract Color accentColor();    
}