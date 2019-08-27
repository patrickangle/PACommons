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
package com.patrickangle.commons.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.function.Consumer;
import javax.swing.JComponent;

/**
 *
 * @author patrickangle
 */
public class HelpPopoverManager {
    private final Window window;

    public HelpPopoverManager(final Window window) {
        this.window = window;
    }

    public void activate() {
        doWithEntries((t) -> {
            t.activate();
        });
    }
    
    public void deactivate() {
        doWithEntries((t) -> {
            t.deactivate();
        });
    }
    
    protected void doWithEntries(Consumer<HelpPopoverEntry> consumer) {
        for (Window w : Window.getWindows()) {
            if (isChildWindow(w)) {
                doWithEntries(w, consumer);
            }
        }
    }
    
    protected boolean isChildWindow(Window w) {
        if (w == window || w.getOwner() == window) {
            return true;
        } else if (w.getOwner() == null) {
            return false;
        } else {
            return isChildWindow(w.getOwner());
        }
    }
    
    protected void doWithEntries(Container container, Consumer<HelpPopoverEntry> consumer) {
        for (Component c : container.getComponents()) {
            if (c instanceof JComponent) {
                Object possibleEntry = ((JComponent) c).getClientProperty(HelpPopoverEntry.HELP_TIP_CLIENT_PROPERTY_KEY);
                if (possibleEntry != null && possibleEntry instanceof HelpPopoverEntry) {
                    consumer.accept((HelpPopoverEntry) possibleEntry);
                }
            }
            
            if (c instanceof Container) {
                doWithEntries((Container) c, consumer);
            }
        }
    }
}
