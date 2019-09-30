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

import com.patrickangle.commons.logging.Logging;
import com.patrickangle.commons.swing.tearawaydialog.JTearawayDialog;
import com.patrickangle.commons.swing.tearawaydialog.TearawayDialogUI;
import com.patrickangle.commons.util.AquaUtils;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Window.Type;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.PanelUI;
import javax.swing.plaf.RootPaneUI;

/**
 *
 * @author patrickangle
 */
public class MacTearawayDialogUI implements TearawayDialogUI {

    private PropertyChangeListener attachedListener;
    private PropertyChangeListener visibleListener;

    @Override
    public void installUI(JTearawayDialog dialog) {
        AquaUtils.installAquaRootPaneUiIfAvailable(dialog.getRootPane());

        dialog.setType(Type.UTILITY);

        dialog.getRootPane().putClientProperty(AquaUtils.WINDOW_STYLE, AquaUtils.WINDOW_STYLE_VALUE_HUD);
        dialog.getRootPane().putClientProperty(AquaUtils.WINDOW_BRUSH_METAL_LOOK, Boolean.TRUE);
        dialog.getRootPane().putClientProperty(AquaUtils.WINDOW_DRAGGABLE_BACKGROUND, Boolean.FALSE);

//        dialog.setBackground(new ColorUIResource(SystemColor.window));
        dialog.getRootPane().setBackground(new ColorUIResource(SystemColor.window));

        if (dialog.isAttached()) {
            dialog.getRootPane().putClientProperty(AquaUtils.WINDOW_TRANSPARENT_TITLE_BAR, Boolean.TRUE);
            dialog.getRootPane().putClientProperty(AquaUtils.WINDOW_CLOSEABLE, Boolean.FALSE);
        }

        attachedListener = (event) -> {
            if (dialog.isVisible() && (boolean) event.getNewValue() == false && (boolean) event.getOldValue() == true) {
                dialog.getRootPane().putClientProperty(AquaUtils.WINDOW_TRANSPARENT_TITLE_BAR, Boolean.FALSE);
                dialog.getRootPane().putClientProperty(AquaUtils.WINDOW_CLOSEABLE, Boolean.TRUE);
            }
        };
        
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                dialog.setAttached(false);
            }
            
        });

        visibleListener = (event) -> {
            // TODO: remove this listener when setting not visible.
            dialog.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) {
                    if (dialog.isVisible()) {
                        dialog.setAttached(false);
                    }
                }
            });
        };

        dialog.addPropertyChangeListener("attached", attachedListener);

    }

    @Override
    public void installContentPaneUI(JPanel c) {
        // TODO: The content pane should not change, so this function should not be needed.
        AquaUtils.installAquaPaneUiIfAvailable(c);
    }

    @Override
    public void uninstallUI(JTearawayDialog dialog) {
    }

    @Override
    public JComponent createHeaderComponent(JTearawayDialog dialog) {
        return null;
    }

    @Override
    public Border getDialogBorder(JTearawayDialog dialog, boolean attached, int attachmentDirection) {
        return new EmptyBorder(0, 0, 0, 0);
    }

    @Override
    public Insets getBodyInsets(JTearawayDialog dialog) {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public void paintBackground(Graphics g, JTearawayDialog dialog, Rectangle bounds, Point attachedTo) {
    }

    @Override
    public void paintBorder(Graphics g, JTearawayDialog dialog, Rectangle bounds, Point attachedTo) {
    }

}
