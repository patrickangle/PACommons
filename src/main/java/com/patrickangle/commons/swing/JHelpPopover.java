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

import com.patrickangle.commons.swing.tearawaydialog.JTearawayDialog;
import com.patrickangle.commons.swing.tearawaydialog.TearawayDialogUI;
import java.awt.Component;
import java.awt.Window;

/**
 * A special type of JTearawayDialog that provides active help, when help has
 * been enabled by a HelpPopoverManager. This subclass exists solely to provide a separate set TearawayDialogUI specifically for JHelpPopovers.
 *
 * @author patrickangle
 */
public class JHelpPopover extends JTearawayDialog {

    private static final String UI_CLASS_ID = "HelpPopoverUI";

    public JHelpPopover(Window parent, Component attachedTo, String identifier) {
        super(parent, attachedTo, identifier);
    }

    @Override
    public String getUIClassID() {
        return UI_CLASS_ID; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected TearawayDialogUI getDefaultUI() {
        return new BasicHelpPopoverDialogUI();
    }
}
