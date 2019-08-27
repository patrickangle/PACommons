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
package com.patrickangle.commons.swing.util;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 * An adapter that allows any component to become a resize handle for its parent window.
 * @author patrickangle
 */
public class WindowResizeComponentMouseAdapter extends MouseAdapter {
    private int fXOffsetToWindowEdge;
    private int fYOffsetToWidnowEdge;

    @Override
    public void mousePressed(MouseEvent e) {
        Point windowPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), SwingUtilities.windowForComponent(e.getComponent()));
        fXOffsetToWindowEdge = SwingUtilities.windowForComponent(e.getComponent()).getWidth() - windowPoint.x;
        fYOffsetToWidnowEdge = SwingUtilities.windowForComponent(e.getComponent()).getHeight() - windowPoint.y;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point windowPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), SwingUtilities.windowForComponent(e.getComponent()));
        SwingUtilities.windowForComponent(e.getComponent()).setSize(windowPoint.x + fXOffsetToWindowEdge, windowPoint.y + fYOffsetToWidnowEdge);

        // the following two lines are a work-around to Sun bug 6318144:
        // http://bugs.sun.com/view_bug.do;?bug_id=6318144
//        SwingUtilities.windowForComponent(e.getComponent()).invalidate();
//        SwingUtilities.windowForComponent(e.getComponent()).validate();
        // Commented out to reduce redraw flicker on macOS.
    }
}
