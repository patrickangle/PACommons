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
package com.patrickangle.commons.swing.calendar.model.renderer;

import com.patrickangle.commons.swing.calendar.CalendarEvent;
import com.patrickangle.commons.swing.calendar.JCalendar;
import java.awt.Component;
import javax.swing.plaf.ListUI;

/**
 *
 * @author patrickangle
 */
public interface CalendarCellRenderer {

    /**
     * Return a component that has been configured to display the specified
     * value. That component's paint() method is then called to render the cell.
     * For the Month view of a calendar, the cell will fill the width of the day
     * and use its preferred height. For the Week view of a calendar, the cell
     * will be dynamically sized to fit among other events that may have
     * overlapping dates.
     *
     * JCalendar allows you to configure a CalendarCellRenderer for each view
     * mode that it supports.
     *
     * @param calendar the JCalendar we're painting
     * @param event the CalendarEvent we're painting
     * @param isSelected true if the specified cell is selected
     * @return a component whose paint() method will render the specified event
     */
    public Component getCalendarCellRendererComponent(JCalendar calendar, CalendarEvent event, boolean isSelected);
}
