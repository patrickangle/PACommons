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
package com.patrickangle.commons.swing.calendar;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 *
 * @author patrickangle
 */
public abstract class CalendarUI extends ComponentUI {
    /**
     * 
     * @param view
     * @return 
     */
    public abstract Component createCalendarComponent(JCalendar calendar, CalendarView view);

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
    }
    
    public abstract void paintCalendarView(Graphics2D g, JCalendar calendar, CalendarView view);
    
    public abstract CalendarEvent eventAtLocation(JCalendar calendar, Point location);
    
    
}
