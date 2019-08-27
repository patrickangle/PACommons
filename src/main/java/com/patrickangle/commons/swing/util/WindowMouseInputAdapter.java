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

import com.patrickangle.commons.swing.tearawaydialog.JTearawayDialog;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

/**
 * 
 * @author patrickangle
 */
public class WindowMouseInputAdapter implements MouseInputListener {

    /**
     * Maps from positions to cursor type. Refer to calculateCorner and
     * calculatePosition for details of this.
     */
    private static final int[] cursorMapping = new int[]{
        Cursor.NW_RESIZE_CURSOR,
        Cursor.NW_RESIZE_CURSOR,
        Cursor.N_RESIZE_CURSOR,
        Cursor.NE_RESIZE_CURSOR,
        Cursor.NE_RESIZE_CURSOR,
        Cursor.NW_RESIZE_CURSOR,
        0,
        0,
        0,
        Cursor.NE_RESIZE_CURSOR,
        Cursor.W_RESIZE_CURSOR,
        0,
        0,
        0,
        Cursor.E_RESIZE_CURSOR,
        Cursor.SW_RESIZE_CURSOR,
        0,
        0,
        0,
        Cursor.SE_RESIZE_CURSOR,
        Cursor.SW_RESIZE_CURSOR,
        Cursor.SW_RESIZE_CURSOR,
        Cursor.S_RESIZE_CURSOR,
        Cursor.SE_RESIZE_CURSOR,
        Cursor.SE_RESIZE_CURSOR
    };
    
    /** The amount of space (in pixels) that the cursor is changed on. */
    private static final int CORNER_DRAG_WIDTH = 16;

    /** Region from edges that dragging is active from. */
    private static final int BORDER_DRAG_THICKNESS = 5;

    private final Component header;
    
    private final Runnable onDragHandler;

    private Cursor lastCursor = new Cursor(Cursor.DEFAULT_CURSOR);

    /**
     * Set to true if the drag operation is moving the window.
     */
    private boolean isMovingWindow;

    /**
     * Used to determine the corner the resize is occuring from.
     */
    private int dragCursor;

    /**
     * X location the mouse went down on for a drag operation.
     */
    private int dragOffsetX;

    /**
     * Y location the mouse went down on for a drag operation.
     */
    private int dragOffsetY;

    /**
     * Width of the window when the drag started.
     */
    private int dragWidth;

    /**
     * Height of the window when the drag started.
     */
    private int dragHeight;
    
    public WindowMouseInputAdapter() {
        this(null, null, () -> {});
    }
    
    public WindowMouseInputAdapter(Window w) {
        this(w, (Component) null);
    }
    
    public WindowMouseInputAdapter(Component header) {
        this(null, header);
    }
    
    public WindowMouseInputAdapter(Runnable onDragHandler) {
        this(null, null, onDragHandler);
    }
    
    public WindowMouseInputAdapter(Window w, Component header) {
        this(w, header, () -> {});
    }
    
    public WindowMouseInputAdapter(Window w, Runnable onDragHandler) {
        this(w, null, onDragHandler);
    }
    
    public WindowMouseInputAdapter(Component header, Runnable onDragHandler) {
        this(null, header, onDragHandler);
    }
    
    public WindowMouseInputAdapter(Window w, Component header, Runnable onDragHandler) {
        this.header = header;
        
        if (w != null) {
            w.addMouseListener(this);
            w.addMouseMotionListener(this);
        }
        
        this.onDragHandler = onDragHandler;
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent ev) {
        Point dragWindowOffset = ev.getPoint();
        Window w = (Window) ev.getSource();

        if (w != null) {
            w.toFront();
        }

        Frame f = null;
        Dialog d = null;
        JTearawayDialog jtd = null;

        if (w instanceof Frame) {
            f = (Frame) w;
        } else if (w instanceof Dialog) {
            d = (Dialog) w;
        } else if (w instanceof JTearawayDialog) {
            jtd = (JTearawayDialog) w;
        }
        
        

        int frameState = (f != null) ? f.getExtendedState() : 0;

        if (getTitlePane(ev) != null) {
            Point convertedDragWindowOffset = SwingUtilities.convertPoint(w, dragWindowOffset, getTitlePane(ev));

            if (getTitlePane(ev).contains(convertedDragWindowOffset)) {
                if ((f != null && ((frameState & Frame.MAXIMIZED_BOTH) == 0) || (d != null) || (jtd != null))
                        && dragWindowOffset.y >= BORDER_DRAG_THICKNESS && dragWindowOffset.x >= BORDER_DRAG_THICKNESS
                        && dragWindowOffset.x < w.getWidth() - BORDER_DRAG_THICKNESS) {
                    isMovingWindow = true;
                    dragOffsetX = dragWindowOffset.x;
                    dragOffsetY = dragWindowOffset.y;
                    return;
                }
            }
        }

        if (f != null && f.isResizable() && ((frameState & Frame.MAXIMIZED_BOTH) == 0) || (d != null && d.isResizable()) || (jtd != null && jtd.isResizeable())) {
            dragOffsetX = dragWindowOffset.x;
            dragOffsetY = dragWindowOffset.y;
            dragWidth = w.getWidth();
            dragHeight = w.getHeight();
            dragCursor = getCursor(calculateCorner(w, dragWindowOffset.x, dragWindowOffset.y));
        }
    }

    protected Component getTitlePane(MouseEvent ev) {
        if (header != null) {
            return header;
        } else {
            return ev.getComponent();
        }
    }

    /**
     * @see
     * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent ev) {
        Window window = (Window) ev.getSource();
        if (dragCursor != 0 && window != null && !window.isValid()) {
            // Some Window systems validate as you resize, others won't,
            // thus the check for validity before repainting.
//            window.validate();
//            window.repaint();
//                getRootPane().repaint();
        }

        isMovingWindow = false;
        dragCursor = 0;
    }

    /**
     * @see
     * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent ev) {
//            JRootPane root = getRootPane();
//
//            if (root.getWindowDecorationStyle() == JRootPane.NONE) {
//                return;
//            }

        Window w = (Window) ev.getSource();

        Frame f = null;
        Dialog d = null;
        JTearawayDialog jtd = null;

        if (w instanceof Frame) {
            f = (Frame) w;
        } else if (w instanceof Dialog) {
            d = (Dialog) w;
        } else if (w instanceof JTearawayDialog) {
            jtd = (JTearawayDialog) w;
        }

        // Update the cursor
        int cursor = getCursor(calculateCorner(w, ev.getX(), ev.getY()));

        if (cursor != 0
                && ((f != null && (f.isResizable() && (f.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0))
                || (d != null && d.isResizable()) || (jtd != null && jtd.isResizeable()))) {
            w.setCursor(Cursor.getPredefinedCursor(cursor));
        } else {
            w.setCursor(Cursor.getPredefinedCursor(0));
        }
    }

    /**
     * Adjust the window bounds.
     *
     * @param bounds the original bounds.
     * @param min the minimum window size.
     * @param deltaX the x delta.
     * @param deltaY the y delta.
     * @param deltaWidth the width delta.
     * @param deltaHeight the height delta.
     */
    private void adjust(Rectangle bounds, Dimension min, int deltaX, int deltaY, int deltaWidth, int deltaHeight) {
        bounds.x += deltaX;
        bounds.y += deltaY;
        bounds.width += deltaWidth;
        bounds.height += deltaHeight;
        if (min != null) {
            if (bounds.width < min.width) {
                int correction = min.width - bounds.width;

                if (deltaX != 0) {
                    bounds.x -= correction;
                }

                bounds.width = min.width;
            }

            if (bounds.height < min.height) {
                int correction = min.height - bounds.height;

                if (deltaY != 0) {
                    bounds.y -= correction;
                }

                bounds.height = min.height;
            }
        }
    }

    /**
     * @see
     * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent ev) {
        Window w = (Window) ev.getSource();
        Point pt = ev.getPoint();

        if (isMovingWindow) {
            Point eventLocationOnScreen = ev.getLocationOnScreen();

            w.setLocation(eventLocationOnScreen.x - dragOffsetX, eventLocationOnScreen.y - dragOffsetY);
        } else if (dragCursor != 0) {
            Rectangle r = w.getBounds();
            Rectangle startBounds = new Rectangle(r);
            Dimension min = w.getMinimumSize();

            switch (dragCursor) {

                case Cursor.E_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX) - r.width, 0);
                    break;

                case Cursor.S_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, 0, pt.y + (dragHeight - dragOffsetY) - r.height);
                    break;

                case Cursor.N_RESIZE_CURSOR:
                    adjust(r, min, 0, pt.y - dragOffsetY, 0, -(pt.y - dragOffsetY));
                    break;

                case Cursor.W_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX, 0, -(pt.x - dragOffsetX), 0);
                    break;

                case Cursor.NE_RESIZE_CURSOR:
                    adjust(r, min, 0, pt.y - dragOffsetY, pt.x + (dragWidth - dragOffsetX) - r.width, -(pt.y - dragOffsetY));
                    break;

                case Cursor.SE_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX) - r.width, pt.y + (dragHeight - dragOffsetY) - r.height);
                    break;

                case Cursor.NW_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX, pt.y - dragOffsetY, -(pt.x - dragOffsetX), -(pt.y - dragOffsetY));
                    break;

                case Cursor.SW_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX, 0, -(pt.x - dragOffsetX), pt.y + (dragHeight - dragOffsetY) - r.height);
                    break;

                default:
                    break;
            }

            if (!r.equals(startBounds)) {
                w.setBounds(r);
                // Defer repaint/validate on mouseReleased unless dynamic
                // layout is active.
                if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
//                    w.validate();
//                    w.repaint();
                }
            }
        }
        
        onDragHandler.run();
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent ev) {
        Window w = (Window) ev.getSource();

        lastCursor = w.getCursor();
        mouseMoved(ev);
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent ev) {
        Window w = (Window) ev.getSource();

        w.setCursor(lastCursor);
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent ev) {
        Window w = (Window) ev.getSource();
        Frame f = null;

        if (w instanceof Frame) {
            f = (Frame) w;
        } else {
            return;
        }

        if (getTitlePane(ev) != null) {
            Point convertedPoint = SwingUtilities.convertPoint(w, ev.getPoint(), getTitlePane(ev));

            int state = f.getExtendedState();

            if (getTitlePane(ev).contains(convertedPoint)) {
                if ((ev.getClickCount() % 2) == 0 && ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0)) {
                    if (f.isResizable()) {
                        if ((state & Frame.MAXIMIZED_BOTH) != 0) {
                            f.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
                        } else {
                            f.setExtendedState(state | Frame.MAXIMIZED_BOTH);
                        }

                        return;
                    }
                }
            }
        }
    }

    /**
     * Returns the corner that contains the point <code>x</code>, <code>
     * y</code>, or -1 if the position doesn't match a corner.
     *
     * @param w the window.
     * @param x the x coordinate.
     * @param y the y coordinate.
     *
     * @return the corner containing the (x, y) coordinate, or -1 if the
     * position doesn't match a corner.
     */
    private int calculateCorner(Window w, int x, int y) {
        Insets insets = w.getInsets();
        int xPosition = calculatePosition(x - insets.left, w.getWidth() - insets.left - insets.right);
        int yPosition = calculatePosition(y - insets.top, w.getHeight() - insets.top - insets.bottom);

        if (xPosition == -1 || yPosition == -1) {
            return -1;
        }

        return yPosition * 5 + xPosition;
    }

    /**
     * Returns the Cursor to render for the specified corner. This returns 0 if
     * the corner doesn't map to a valid Cursor
     *
     * @param corner the corner index.
     *
     * @return the cursor mapping.
     */
    private int getCursor(int corner) {
        if (corner == -1) {
            return 0;
        }

        return cursorMapping[corner];
    }

    /**
     * Returns an integer indicating the position of <code>spot</code> in
     * <code>width</code>. The return value will be: 0 if <
     * BORDER_DRAG_THICKNESS 1 if < CORNER_DRAG_WIDTH 2 if >= CORNER_DRAG_WIDTH
     * &&
     * < width - BORDER_DRAG_THICKNESS 3 if >= width - CORNER_DRAG_WIDTH 4 if >=
     * width - BORDER_DRAG_THICKNESS 5 otherwise
     *
     * @param spot DOCUMENT ME!
     * @param width DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int calculatePosition(int spot, int width) {
        if (spot < BORDER_DRAG_THICKNESS) {
            return 0;
        }

        if (spot < CORNER_DRAG_WIDTH) {
            return 1;
        }

        if (spot >= (width - BORDER_DRAG_THICKNESS)) {
            return 4;
        }

        if (spot >= (width - CORNER_DRAG_WIDTH)) {
            return 3;
        }

        return 2;
    }
    
    
}
