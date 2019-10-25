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

import com.patrickangle.commons.util.Colors;
import java.awt.Color;
import javax.swing.JComponent;

/**
 *
 * @author patrickangle
 */
public class ToolbarConstants {

    public static enum ButtonUIStyle {
        Default,
        SegmentedFirst,
        SegmentedMiddle,
        SegmentedLast,
        Dropdown;

        public static final String KEY = ButtonUIStyle.class.getName();

        public static ButtonUIStyle forComponent(JComponent c) {
            ButtonUIStyle value = (ButtonUIStyle) c.getClientProperty(KEY);
            return value != null ? value : Default;
        }
    }

    public static enum ButtonUIIconType {
        Default,
        Template;

        public static final String KEY = ButtonUIIconType.class.getName();

        public static ButtonUIIconType forComponent(JComponent c) {
            ButtonUIIconType value = (ButtonUIIconType) c.getClientProperty(KEY);
            return value != null ? value : Default;
        }
    }

    public static final int CORNER_DIAMETER = 8;

    public static final LightDarkPair<Color> BUTTON_BACKGROUND_NORMAL = new LightDarkPair<>(Colors.transparentColor(Color.WHITE, 0.12f), Colors.grey(0.43f));
    public static final LightDarkPair<Color> BUTTON_TEXT_NORMAL = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.62f), Colors.grey(1.0f));

    public static final LightDarkPair<Color> BUTTON_BACKGROUND_INACTIVE = new LightDarkPair<>(Colors.transparentColor(Color.WHITE, 0.0f), Colors.grey(0.25f));
    public static final LightDarkPair<Color> BUTTON_TEXT_INACTIVE = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.27f), Colors.grey(0.51f));

    public static final LightDarkPair<Color> BUTTON_BACKGROUND_PRESSED = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.02f), Colors.grey(0.54f));
    public static final LightDarkPair<Color> BUTTON_TEXT_PRESSED = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.75f), Colors.grey(1.0f));

    public static final LightDarkPair<Color> BUTTON_BACKGROUND_SHADOW_NORMAL = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.04f), Colors.grey(0.5f));
    public static final LightDarkPair<Color> BUTTON_BACKGROUND_SHADOW_INACTIVE = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.04f), Colors.grey(0.32f));

    // Special for the toggle-type buttons.
    public static final LightDarkPair<Color> TOGGLE_BUTTON_BACKGROUND_SELECTED = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.42f), Colors.grey(0.8f));
    public static final LightDarkPair<Color> TOGGLE_BUTTON_TEXT_SELECTED = new LightDarkPair<>(Colors.transparentColor(Color.WHITE, 1.0f), Colors.grey(0.25f));

    public static final LightDarkPair<Color> TOGGLE_BUTTON_BACKGROUND_SELECTED_PRESSED = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.50f), Colors.grey(0.94f));
    public static final LightDarkPair<Color> TOGGLE_BUTTON_TEXT_SELECTED_PRESSED = new LightDarkPair<>(Colors.transparentColor(Color.WHITE, 1.0f), Colors.grey(0.25f));

    public static final LightDarkPair<Color> TOGGLE_BUTTON_BACKGROUND_SELECTED_INACTIVE = new LightDarkPair<>(Colors.transparentColor(Color.BLACK, 0.07f), Colors.grey(0.36f));
    public static final LightDarkPair<Color> TOGGLE_BUTTON_TEXT_SELECTED_INACTIVE = new LightDarkPair<>(Colors.transparentColor(Color.WHITE, 0.25f), Colors.grey(0.2f));

    // Sets for different states.
    public static final BackgroundTextPair<LightDarkPair<Color>> BUTTON_NORMAL = new BackgroundTextPair<>(BUTTON_BACKGROUND_NORMAL, BUTTON_TEXT_NORMAL);
    public static final BackgroundTextPair<LightDarkPair<Color>> BUTTON_INACTIVE = new BackgroundTextPair<>(BUTTON_BACKGROUND_INACTIVE, BUTTON_TEXT_INACTIVE);
    public static final BackgroundTextPair<LightDarkPair<Color>> BUTTON_PRESSED = new BackgroundTextPair<>(BUTTON_BACKGROUND_PRESSED, BUTTON_TEXT_PRESSED);
    public static final BackgroundTextPair<LightDarkPair<Color>> TOGGLE_BUTTON_SELECTED = new BackgroundTextPair<>(TOGGLE_BUTTON_BACKGROUND_SELECTED, TOGGLE_BUTTON_TEXT_SELECTED);
    public static final BackgroundTextPair<LightDarkPair<Color>> TOGGLE_BUTTON_SELECTED_PRESSED = new BackgroundTextPair<>(TOGGLE_BUTTON_BACKGROUND_SELECTED_PRESSED, TOGGLE_BUTTON_TEXT_SELECTED_PRESSED);
    public static final BackgroundTextPair<LightDarkPair<Color>> TOGGLE_BUTTON_SELECTED_INACTIVE = new BackgroundTextPair<>(TOGGLE_BUTTON_BACKGROUND_SELECTED_INACTIVE, TOGGLE_BUTTON_TEXT_SELECTED_INACTIVE);
}
