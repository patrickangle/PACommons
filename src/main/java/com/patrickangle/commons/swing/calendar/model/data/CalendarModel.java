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
package com.patrickangle.commons.swing.calendar.model.data;

import com.patrickangle.commons.swing.calendar.CalendarEvent;
import java.time.ZonedDateTime;
import java.util.List;

/**
 *
 * @author patrickangle
 */
public interface CalendarModel {
    /**
     * Get all events that occur on the given date. The date is taken to the precision of DAYS, where the HOURS, MINUTES, SECONDS, MILLISECONDS, and NANOSECONDS fields are ignored.
     * @param date
     * @return 
     */
    public List<CalendarEvent> getEventsOn(ZonedDateTime date);
    
    /**
     * Get all events that occur between the given dates. The dates are taken to the precision of NANOSECONDS, ignoring no fields.
     * @param start
     * @param end
     * @return 
     */
    public List<CalendarEvent> getEventsBetween(ZonedDateTime start, ZonedDateTime end);
    
    public void addCalendarModelListener(CalendarModelListener listener);
    
    public void removeCalendarModelListener(CalendarModelListener listener);
}
