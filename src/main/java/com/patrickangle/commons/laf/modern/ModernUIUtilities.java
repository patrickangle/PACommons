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

import com.patrickangle.commons.laf.modern.ui.ModernButtonUI;
import com.patrickangle.commons.laf.modern.ui.ModernComboBoxUI;
import com.patrickangle.commons.laf.modern.ui.ModernScrollBarUI;
import com.patrickangle.commons.laf.modern.ui.ModernTextFieldUI;
import java.awt.Color;
import java.awt.Font;
import java.util.Map.Entry;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIDefaults.LazyInputMap;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.text.DefaultEditorKit;

/**
 *
 * @author patrickangle
 */
public class ModernUIUtilities {

    public static final String ACCENT_HIGHLIGHT_COLOR_KEY = "ModernLAF.AccentHighlightColor";
    public static final String ACCENT_LIGHT_COLOR_KEY = "ModernLAF.AccentLightColor";
    public static final String ACCENT_MEDIUM_COLOR_KEY = "ModernLAF.AccentMediumColor";
    public static final String ACCENT_DARK_COLOR_KEY = "ModernLAF.AccentDarkColor";

    public static final String PRIMARY_LIGHT_COLOR_KEY = "ModernLAF.PrimaryLightColor";
    public static final String PRIMARY_MEDIUM_LIGHT_COLOR_KEY = "ModernLAF.PrimaryMediumLightColor";
    public static final String PRIMARY_MEDIUM_COLOR_KEY = "ModernLAF.PrimaryMediumColor";
    public static final String PRIMARY_MEDIUM_DARK_COLOR_KEY = "ModernLAF.PrimaryMediumDarkColor";
    public static final String PRIMARY_DARK_COLOR_KEY = "ModernLAF.PrimaryDarkColor";
    public static final String PRIMARY_ULTRA_DARK_COLOR_KEY = "ModernLAF.rimaryUltraDarkColor";
    public static final String BACKGROUND_COLOR_KEY = "ModernLAF.BackgroundColor";

    public static final String WORKSPACE_BACKGROUND_COLOR_KEY = "ModernLAF.WorkspaceBackgroundColor";

    public static final String SHADOW_COLOR_KEY = "ModernLAF.ShadowColor";

    public static void installLafDefaults(UIDefaults defaults) {
        installInputMapDefaults(defaults);

        defaults.put(ACCENT_HIGHLIGHT_COLOR_KEY, new Color(0x0090e8));
        defaults.put(ACCENT_LIGHT_COLOR_KEY, new Color(0x146bb9));
        defaults.put(ACCENT_MEDIUM_COLOR_KEY, new Color(0x1e4e89));
        defaults.put(ACCENT_DARK_COLOR_KEY, new Color(0x1d3c6c));

        defaults.put(PRIMARY_LIGHT_COLOR_KEY, new Color(0xd3e1eb));
        defaults.put(PRIMARY_MEDIUM_LIGHT_COLOR_KEY, new Color(0x9faebb));
        defaults.put(PRIMARY_MEDIUM_COLOR_KEY, new Color(0x67778a));
        defaults.put(PRIMARY_MEDIUM_DARK_COLOR_KEY, new Color(0x47566d));
        defaults.put(PRIMARY_DARK_COLOR_KEY, new Color(0x29323f));
        defaults.put(PRIMARY_ULTRA_DARK_COLOR_KEY, new Color(0x232a35));
        defaults.put(BACKGROUND_COLOR_KEY, new Color(0x212833));

        defaults.put(WORKSPACE_BACKGROUND_COLOR_KEY, new Color(0x151a21));

        defaults.put(SHADOW_COLOR_KEY, Color.BLACK);
        
        defaults.put("text", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("textText", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("infoText", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("Label.foreground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("OptionPane.messageForeground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("link.forground", defaults.getColor(ACCENT_LIGHT_COLOR_KEY));
        
//        defaults.put("EditorPane.background", defaults.getColor(defaults))
        
//        defaults.put("text", defaults)
        
//text=bbbbbb
//textText=bbbbbb
//infoText=bbbbbb
//OptionPane.messageForeground=bbbbbb
//        EditorPane.background
//EditorPane.border
//EditorPane.caretBlinkRate
//EditorPane.caretForeground
//EditorPane.font
//EditorPane.foreground
//EditorPane.inactiveForeground
//EditorPane.margin
//EditorPane.selectionBackground
//EditorPane.selectionForeground
//        link.foreground=589df6
        defaults.put("Panel.background", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("Viewport.background", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("ScrollPane.background", defaults.getColor(WORKSPACE_BACKGROUND_COLOR_KEY));
        
        defaults.put("window", defaults.getColor(BACKGROUND_COLOR_KEY));
        defaults.put("control", defaults.getColor(BACKGROUND_COLOR_KEY));
        
        
        Font systemFont = new Font(Font.DIALOG, Font.PLAIN, 14);

        defaults.put("defaultFont", systemFont);

        ModernButtonUI.installIntoDefaults(defaults);
        ModernTextFieldUI.installIntoDefaults(defaults);
        ModernComboBoxUI.installIntoDefaults(defaults);
        ModernScrollBarUI.installIntoDefaults(defaults);

//        defaults.put("OptionPane.informationIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneInfo.png"))));
//        defaults.put("OptionPane.questionIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneQuestion.png"))));
//        defaults.put("OptionPane.warningIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneWarning.png"))));
//        defaults.put("OptionPane.errorIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneError.png"))));
//        defaults.put("ButtonUI", ModernButtonUI.class.getName());
//        defaults.put("Button.border", new ModernButtonPainter(new Insets(2, 8, 2, 8)));
    }
    
    public static void installDefaults(UIDefaults defaults) {
        installInputMapDefaults(defaults);

        defaults.put(ACCENT_HIGHLIGHT_COLOR_KEY, new Color(0x0090e8));
        defaults.put(ACCENT_LIGHT_COLOR_KEY, new Color(0x146bb9));
        defaults.put(ACCENT_MEDIUM_COLOR_KEY, new Color(0x1e4e89));
        defaults.put(ACCENT_DARK_COLOR_KEY, new Color(0x1d3c6c));

        defaults.put(PRIMARY_LIGHT_COLOR_KEY, new Color(0xd3e1eb));
        defaults.put(PRIMARY_MEDIUM_LIGHT_COLOR_KEY, new Color(0x9faebb));
        defaults.put(PRIMARY_MEDIUM_COLOR_KEY, new Color(0x67778a));
        defaults.put(PRIMARY_MEDIUM_DARK_COLOR_KEY, new Color(0x47566d));
        defaults.put(PRIMARY_DARK_COLOR_KEY, new Color(0x29323f));
        defaults.put(PRIMARY_ULTRA_DARK_COLOR_KEY, new Color(0x232a35));
        defaults.put(BACKGROUND_COLOR_KEY, new Color(0x212833));

        defaults.put(WORKSPACE_BACKGROUND_COLOR_KEY, new Color(0x151a21));

        defaults.put(SHADOW_COLOR_KEY, Color.BLACK);
        
        defaults.put("text", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("textText", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("infoText", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("Label.foreground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("OptionPane.messageForeground", defaults.getColor(PRIMARY_LIGHT_COLOR_KEY));
        defaults.put("link.forground", defaults.getColor(ACCENT_LIGHT_COLOR_KEY));
        
//        defaults.put("EditorPane.background", defaults.getColor(defaults))
        
//        defaults.put("text", defaults)
        
//text=bbbbbb
//textText=bbbbbb
//infoText=bbbbbb
//OptionPane.messageForeground=bbbbbb
//        EditorPane.background
//EditorPane.border
//EditorPane.caretBlinkRate
//EditorPane.caretForeground
//EditorPane.font
//EditorPane.foreground
//EditorPane.inactiveForeground
//EditorPane.margin
//EditorPane.selectionBackground
//EditorPane.selectionForeground
//        link.foreground=589df6
//        defaults.put("Panel.background", defaults.getColor(BACKGROUND_COLOR_KEY));
//        defaults.put("Viewport.background", defaults.getColor(BACKGROUND_COLOR_KEY));
//        defaults.put("ScrollPane.background", defaults.getColor(WORKSPACE_BACKGROUND_COLOR_KEY));
//        
//        defaults.put("window", defaults.getColor(BACKGROUND_COLOR_KEY));
//        defaults.put("control", defaults.getColor(BACKGROUND_COLOR_KEY));
        
        
        Font systemFont = new Font(Font.DIALOG, Font.PLAIN, 14);

        defaults.put("defaultFont", systemFont);

        ModernButtonUI.installIntoDefaults(defaults);
//        ModernTextFieldUI.installIntoDefaults(defaults);
//        ModernComboBoxUI.installIntoDefaults(defaults);
        ModernScrollBarUI.installIntoDefaults(defaults);

//        defaults.put("OptionPane.informationIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneInfo.png"))));
//        defaults.put("OptionPane.questionIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneQuestion.png"))));
//        defaults.put("OptionPane.warningIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneWarning.png"))));
//        defaults.put("OptionPane.errorIcon", new IconUIResource(new ImageIcon(Images.fromClasspath("com/patrickangle/commons/laf/modern/icons/OptionPaneError.png"))));
//        defaults.put("ButtonUI", ModernButtonUI.class.getName());
//        defaults.put("Button.border", new ModernButtonPainter(new Insets(2, 8, 2, 8)));
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("os.name")
            + " " + System.getProperty("os.version")
            + " " + System.getProperty("java.version"));
        UIManager.LookAndFeelInfo[] lfa =
            UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo lf : lfa) {
            UIManager.setLookAndFeel(lf.getClassName());
            UIDefaults uid = UIManager.getLookAndFeelDefaults();
            System.out.println("***"
                + " " + lf.getName()
                + " " + lf.getClassName()
                + " " + uid.size() + " entries");
        }
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
            UIDefaults uid = UIManager.getLookAndFeelDefaults();
            System.out.println("***"
                + " " + "BasicLookAndFeel"
                + " " + ""
                + " " + uid.size() + " entries");
            for (Entry e : uid.entrySet()) {
                System.out.println(e.getKey() + "\t" + e.getValue());
            }
    }

    private static void installInputMapDefaults(UIDefaults defaults) {
        Object fieldInputMap = new LazyInputMap(
                new Object[]{"ctrl C", DefaultEditorKit.copyAction, "ctrl V",
                        DefaultEditorKit.pasteAction, "ctrl X",
                        DefaultEditorKit.cutAction, "COPY",
                        DefaultEditorKit.copyAction, "PASTE",
                        DefaultEditorKit.pasteAction, "CUT",
                        DefaultEditorKit.cutAction, "shift LEFT",
                        DefaultEditorKit.selectionBackwardAction,
                        "shift KP_LEFT",
                        DefaultEditorKit.selectionBackwardAction, "shift RIGHT",
                        DefaultEditorKit.selectionForwardAction,
                        "shift KP_RIGHT",
                        DefaultEditorKit.selectionForwardAction, "ctrl LEFT",
                        DefaultEditorKit.previousWordAction, "ctrl KP_LEFT",
                        DefaultEditorKit.previousWordAction, "ctrl RIGHT",
                        DefaultEditorKit.nextWordAction, "ctrl KP_RIGHT",
                        DefaultEditorKit.nextWordAction, "ctrl shift LEFT",
                        DefaultEditorKit.selectionPreviousWordAction,
                        "ctrl shift KP_LEFT",
                        DefaultEditorKit.selectionPreviousWordAction,
                        "ctrl shift RIGHT",
                        DefaultEditorKit.selectionNextWordAction,
                        "ctrl shift KP_RIGHT",
                        DefaultEditorKit.selectionNextWordAction, "ctrl A",
                        DefaultEditorKit.selectAllAction, "HOME",
                        DefaultEditorKit.beginLineAction, "END",
                        DefaultEditorKit.endLineAction, "shift HOME",
                        DefaultEditorKit.selectionBeginLineAction, "shift END",
                        DefaultEditorKit.selectionEndLineAction, "typed \010",
                        DefaultEditorKit.deletePrevCharAction, "DELETE",
                        DefaultEditorKit.deleteNextCharAction, "RIGHT",
                        DefaultEditorKit.forwardAction, "LEFT",
                        DefaultEditorKit.backwardAction, "KP_RIGHT",
                        DefaultEditorKit.forwardAction, "KP_LEFT",
                        DefaultEditorKit.backwardAction, "ENTER",
                        JTextField.notifyAction, "ctrl BACK_SLASH", "unselect"
                        /*DefaultEditorKit.unselectAction*/, "control shift O",
                        "toggle-componentOrientation"
                        /*DefaultEditorKit.toggleComponentOrientation*/});

        Object multilineInputMap = new LazyInputMap(
                new Object[]{"ctrl C", DefaultEditorKit.copyAction, "ctrl V",
                        DefaultEditorKit.pasteAction, "ctrl X",
                        DefaultEditorKit.cutAction, "COPY",
                        DefaultEditorKit.copyAction, "PASTE",
                        DefaultEditorKit.pasteAction, "CUT",
                        DefaultEditorKit.cutAction, "shift LEFT",
                        DefaultEditorKit.selectionBackwardAction,
                        "shift KP_LEFT",
                        DefaultEditorKit.selectionBackwardAction, "shift RIGHT",
                        DefaultEditorKit.selectionForwardAction,
                        "shift KP_RIGHT",
                        DefaultEditorKit.selectionForwardAction, "ctrl LEFT",
                        DefaultEditorKit.previousWordAction, "ctrl KP_LEFT",
                        DefaultEditorKit.previousWordAction, "ctrl RIGHT",
                        DefaultEditorKit.nextWordAction, "ctrl KP_RIGHT",
                        DefaultEditorKit.nextWordAction, "ctrl shift LEFT",
                        DefaultEditorKit.selectionPreviousWordAction,
                        "ctrl shift KP_LEFT",
                        DefaultEditorKit.selectionPreviousWordAction,
                        "ctrl shift RIGHT",
                        DefaultEditorKit.selectionNextWordAction,
                        "ctrl shift KP_RIGHT",
                        DefaultEditorKit.selectionNextWordAction, "ctrl A",
                        DefaultEditorKit.selectAllAction, "HOME",
                        DefaultEditorKit.beginLineAction, "END",
                        DefaultEditorKit.endLineAction, "shift HOME",
                        DefaultEditorKit.selectionBeginLineAction, "shift END",
                        DefaultEditorKit.selectionEndLineAction,

                        "UP", DefaultEditorKit.upAction, "KP_UP",
                        DefaultEditorKit.upAction, "DOWN",
                        DefaultEditorKit.downAction, "KP_DOWN",
                        DefaultEditorKit.downAction, "PAGE_UP",
                        DefaultEditorKit.pageUpAction, "PAGE_DOWN",
                        DefaultEditorKit.pageDownAction, "shift PAGE_UP",
                        "selection-page-up", "shift PAGE_DOWN",
                        "selection-page-down", "ctrl shift PAGE_UP",
                        "selection-page-left", "ctrl shift PAGE_DOWN",
                        "selection-page-right", "shift UP",
                        DefaultEditorKit.selectionUpAction, "shift KP_UP",
                        DefaultEditorKit.selectionUpAction, "shift DOWN",
                        DefaultEditorKit.selectionDownAction, "shift KP_DOWN",
                        DefaultEditorKit.selectionDownAction, "ENTER",
                        DefaultEditorKit.insertBreakAction, "typed \010",
                        DefaultEditorKit.deletePrevCharAction, "DELETE",
                        DefaultEditorKit.deleteNextCharAction, "RIGHT",
                        DefaultEditorKit.forwardAction, "LEFT",
                        DefaultEditorKit.backwardAction, "KP_RIGHT",
                        DefaultEditorKit.forwardAction, "KP_LEFT",
                        DefaultEditorKit.backwardAction, "TAB",
                        DefaultEditorKit.insertTabAction, "ctrl BACK_SLASH",
                        "unselect"
                        /*DefaultEditorKit.unselectAction*/, "ctrl HOME",
                        DefaultEditorKit.beginAction, "ctrl END",
                        DefaultEditorKit.endAction, "ctrl shift HOME",
                        DefaultEditorKit.selectionBeginAction, "ctrl shift END",
                        DefaultEditorKit.selectionEndAction, "ctrl T",
                        "next-link-action", "ctrl shift T",
                        "previous-link-action", "ctrl SPACE",
                        "activate-link-action", "control shift O",
                        "toggle-componentOrientation"
                        /*DefaultEditorKit.toggleComponentOrientation*/});

        Object[] actionDefaults = {
                // these are just copied from Metal L&F -- no values in Basic L&F
                //!! Should get input maps from the native L&F for all map defaults
                "TextField.focusInputMap", fieldInputMap,
                "PasswordField.focusInputMap", fieldInputMap,
                "TextArea.focusInputMap", multilineInputMap,
                "TextPane.focusInputMap", multilineInputMap,
                "EditorPane.focusInputMap", multilineInputMap,};

        defaults.putDefaults(actionDefaults);
    }

//    public static void installInputMapDefaults(UIDefaults defaults) {
//        // Make ENTER work in JTrees
//        InputMap treeInputMap = (InputMap) defaults.get("Tree.focusInputMap");
//        if (treeInputMap != null) { // it's really possible. For example,  GTK+ doesn't have such map
//            treeInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "toggle");
//        }
//        // Cut/Copy/Paste in JTextAreas
//        InputMap textAreaInputMap = (InputMap) defaults.get("TextArea.focusInputMap");
//        if (textAreaInputMap != null) { // It really can be null, for example when LAF isn't properly initialized (Alloy license problem)
//            installCutCopyPasteShortcuts(textAreaInputMap, false);
//        }
//        // Cut/Copy/Paste in JTextFields
//        InputMap textFieldInputMap = (InputMap) defaults.get("TextField.focusInputMap");
//        if (textFieldInputMap != null) { // It really can be null, for example when LAF isn't properly initialized (Alloy license problem)
//            installCutCopyPasteShortcuts(textFieldInputMap, false);
//        }
//        // Cut/Copy/Paste in JPasswordField
//        InputMap passwordFieldInputMap = (InputMap) defaults.get("PasswordField.focusInputMap");
//        if (passwordFieldInputMap != null) { // It really can be null, for example when LAF isn't properly initialized (Alloy license problem)
//            installCutCopyPasteShortcuts(passwordFieldInputMap, false);
//        }
//        // Cut/Copy/Paste in JTables
//        InputMap tableInputMap = (InputMap) defaults.get("Table.ancestorInputMap");
//        if (tableInputMap != null) { // It really can be null, for example when LAF isn't properly initialized (Alloy license problem)
//            installCutCopyPasteShortcuts(tableInputMap, true);
//        }
//    }
//
//    private static void installCutCopyPasteShortcuts(InputMap inputMap, boolean useSimpleActionKeys) {
//        String copyActionKey = useSimpleActionKeys ? "copy" : DefaultEditorKit.copyAction;
//        String pasteActionKey = useSimpleActionKeys ? "paste" : DefaultEditorKit.pasteAction;
//        String cutActionKey = useSimpleActionKeys ? "cut" : DefaultEditorKit.cutAction;
//        // Ctrl+Ins, Shift+Ins, Shift+Del
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.CTRL_MASK | InputEvent.CTRL_DOWN_MASK), copyActionKey);
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.SHIFT_MASK | InputEvent.SHIFT_DOWN_MASK), pasteActionKey);
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_MASK | InputEvent.SHIFT_DOWN_MASK), cutActionKey);
//        // Ctrl+C, Ctrl+V, Ctrl+X
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.CTRL_DOWN_MASK), copyActionKey);
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK | InputEvent.CTRL_DOWN_MASK), pasteActionKey);
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK | InputEvent.CTRL_DOWN_MASK), DefaultEditorKit.cutAction);
//    }

}
