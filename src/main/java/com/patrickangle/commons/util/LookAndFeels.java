/*
 * Patrick Angle Commons Library
 * Copyright 2018 Patrick Angle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrickangle.commons.util;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author patrickangle
 */
public class LookAndFeels {
    public static void useSystemLookAndFeel() {
        try {
            if (OperatingSystems.current() == OperatingSystems.Macintosh) {
                useMacOSLookAndFeel();
            } else {
                useGenericSystemLookAndFeel();
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Exceptions.raiseThrowableToUser(ex);
        }
    }

    private static void useGenericSystemLookAndFeel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    private static void useMacOSLookAndFeel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        // Use macOS native menu bar
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        UIManager.getInstalledLookAndFeels(); // Needed to prevent "java.lang.UnsatisfiedLinkError: com.apple.laf.ScreenMenu.addMenuListeners"

        // Use Quartz2D for drawing arbitrary graphics.
        System.setProperty("apple.awt.graphics.UseQuartz", "true");

        LookAndFeels.useGenericSystemLookAndFeel();

        // Disable split pane border by default, and make sure the pane updates continuously when dragged.
        UIManager.getDefaults().put("SplitPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("SplitPane.continuousLayout", true);

        // Titled borders should use their system style with slightly darker background.
        UIManager.put("TitledBorder.border", UIManager.getBorder("TitledBorder.aquaVariant"));
        UIManager.put("TitledBorder.font", UIManager.getDefaults().get("SmallSystemFont"));
    }

    public static void useNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            Exceptions.raiseThrowableToUser(ex);
        }
    }

    public static void useDarkNimbusLookAndFeel() {
        UIManager.put("control", new Color(64, 32, 32));
        UIManager.put("info", new Color(48, 24, 24)); // Tool tips
        UIManager.put("nimbusBase", new Color(48, 16, 16));
        UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
        UIManager.put("nimbusFocus", new Color(128, 64, 64));//new Color(115,164,209) );
        UIManager.put("nimbusGreen", new Color(176, 179, 50));
        UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
        UIManager.put("nimbusLightBackground", new Color(24, 8, 8));
        UIManager.put("nimbusOrange", new Color(191, 98, 4));
        UIManager.put("nimbusRed", new Color(169, 46, 34));
        UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
        UIManager.put("nimbusSelectionBackground", new Color(196, 128, 128));//new Color( 104, 93, 156) );
        UIManager.put("text", new Color(230, 230, 230));
//        UIManager.put("control", new Color(128, 128, 128));
//        UIManager.put("info", new Color(128, 128, 128));
//        UIManager.put("nimbusBase", new Color(18, 30, 49));
//        UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
//        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
//        UIManager.put("nimbusFocus", new Color(115, 164, 209));
//        UIManager.put("nimbusGreen", new Color(176, 179, 50));
//        UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
//        UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
//        UIManager.put("nimbusOrange", new Color(191, 98, 4));
//        UIManager.put("nimbusRed", new Color(169, 46, 34));
//        UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
//        UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
//        UIManager.put("text", new Color(230, 230, 230));
        LookAndFeels.useNimbusLookAndFeel();
    }
}
