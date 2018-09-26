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

import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.Taskbar;
import java.awt.Window;

/**
 *
 * @author Patrick Angle
 */
public class SafeTaskbar {
    public static Image getIconImage() {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_IMAGE)) {
            return Taskbar.getTaskbar().getIconImage();
        } else {
            return null;
        }
    }
    
    public static PopupMenu getMenu() {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.MENU)) {
            return Taskbar.getTaskbar().getMenu();
        } else {
            return null;
        }
    }
    
    public static void requestUserAttention(boolean enabled, boolean critical) {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.USER_ATTENTION)) {
            Taskbar.getTaskbar().requestUserAttention(enabled, critical);
        }
    }
    
    public static void requestWindowUserAttention(Window w) {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.USER_ATTENTION_WINDOW)) {
            Taskbar.getTaskbar().requestWindowUserAttention(w);
        }
    }
    
    public static void setIconBadge(String badge) {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_BADGE_TEXT)) {
            Taskbar.getTaskbar().setIconBadge(badge);
        }
    }
    
    // Provided as a convenience outside of the normal API, and allows for a better check of if the feature is allowed.
    public static void setIconBadge(int badge) {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_BADGE_NUMBER)) {
            Taskbar.getTaskbar().setIconBadge(String.valueOf(badge));
        }
    }
    
    public static void setIconImage(Image image) {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_IMAGE)) {
            Taskbar.getTaskbar().setIconImage(image);
        }
    }
    
    public static void setMenu(PopupMenu menu) {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.MENU)) {
            Taskbar.getTaskbar().setMenu(menu);
        }
    }
    
    public static void setProgressValue(int value) {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.PROGRESS_VALUE)) {
            Taskbar.getTaskbar().setProgressValue(value);
        }
    }
    
    public static void setWindowIconBadge(Window w, Image badge) {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_BADGE_IMAGE_WINDOW)) {
            Taskbar.getTaskbar().setWindowIconBadge(w, badge);
        }
    }
    
    public static void setWindowProgressState(Window w, Taskbar.State state) {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.PROGRESS_STATE_WINDOW)) {
            Taskbar.getTaskbar().setWindowProgressState(w, state);
        }
    }
    
    public static void setWindowProgressValue(Window w, int value) {
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.PROGRESS_VALUE_WINDOW)) {
            Taskbar.getTaskbar().setWindowProgressValue(w, value);
        }
    }
    
    
}
