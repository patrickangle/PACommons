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
package com.patrickangle.commons.laf.modern;

import java.awt.Color;

/**
 *
 * @author patrickangle
 */
public class ModernUIColors {

    public static Color accentHighlightColor;
    public static Color accentLightColor;
    public static Color accentMediumColor;
    public static Color accentDarkColor;

    static {
        updateColors();
    }
    
    public static final void updateColors() {
        switch (ModernUIAquaAppearanceAdapter.systemColorScheme()) {
            case Blue:
                accentHighlightColor = new Color(0x1190eb);
                accentLightColor = new Color(0x1b6bbc);
                accentMediumColor = new Color(0x204e8b);
                accentDarkColor = new Color(0x1e3c6e);
                break;
            case Purple:
                accentHighlightColor = new Color(0x967adc);
                accentLightColor = new Color(0x876ec6);
                accentMediumColor = new Color(0x7861af);
                accentDarkColor = new Color(0x69559a);
                break;
            case Pink:
                accentHighlightColor = new Color(0xd770ad);
                accentLightColor = new Color(0xc2649c);
                accentMediumColor = new Color(0xac5989);
                accentDarkColor = new Color(0x974e79);
                break;
            case Red:
                accentHighlightColor = new Color(0xda4453);
                accentLightColor = new Color(0xc43d4a);
                accentMediumColor = new Color(0xae3743);
                accentDarkColor = new Color(0x99303a);
                break;
            case Orange:
                accentHighlightColor = new Color(0xe9573f);
                accentLightColor = new Color(0xd24e38);
                accentMediumColor = new Color(0xba4532);
                accentDarkColor = new Color(0xa43c2b);
                break;
            case Yellow:
                accentHighlightColor = new Color(0xf6bb41);
                accentLightColor = new Color(0xdda83b);
                accentMediumColor = new Color(0xc59635);
                accentDarkColor = new Color(0xac832d);
                break;
            case Green:
                accentHighlightColor = new Color(0x8cc152);
                accentLightColor = new Color(0x7eae4a);
                accentMediumColor = new Color(0x6f9a41);
                accentDarkColor = new Color(0x628739);
                break;
            case Graphite:
                accentHighlightColor = new Color(0xaab2bd);
                accentLightColor = new Color(0x99a0aa);
                accentMediumColor = new Color(0x888e96);
                accentDarkColor = new Color(0x777d84);
                break;
        }
    }

    public static final Color primaryLightColor = new Color(0xEBEBEB);
    public static final Color primaryMediumLightColor = new Color(0x939393);
    public static final Color primaryMediumColor = new Color(0x757575);
    public static final Color primaryMediumDarkColor = new Color(0x585858);
    public static final Color primaryDarkColor = new Color(0x303030);
    public static final Color primaryUltraDarkColor = new Color(0x1D1D1D);

    public static final Color backgroundColor = new Color(0x262626);
    public static final Color workspaceBackgroundColor = new Color(0x141414);

    public static final Color tooltipColor = new Color(0xd9cc75);

    public static final Color infoColor = new Color(0x0376b7);
    public static final Color questionColor = new Color(0x24b028);
    public static final Color warningColor = new Color(0xf8ba32);
    public static final Color errorColor = new Color(0xed271c);
}
