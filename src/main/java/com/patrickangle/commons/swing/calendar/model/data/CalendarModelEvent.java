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

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.EventObject;
import javax.swing.event.TableModelEvent;

/**
 *
 * @author patrickangle
 */
public class CalendarModelEvent extends EventObject {
    protected final CalendarModelEventType type;
    protected final ZonedDateTime intervalStart;
    protected final ZonedDateTime intervalEnd;
    /**
     * 
     * @param source
     * @param type
     * @param date 
     */
    public CalendarModelEvent(CalendarModel source, CalendarModelEventType type, ZonedDateTime date) {
        super(source);
        
        ZonedDateTime intervalStart = date.minusHours(date.getHour()).minusMinutes(date.getMinute()).minusSeconds(date.getSecond()).minusNanos(date.getNano());
        ZonedDateTime intervalEnd = intervalStart.plusDays(1);
        
        this.type = type;
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
    }
    
    public CalendarModelEvent(CalendarModel source, CalendarModelEventType type, ZonedDateTime intervalStart, ZonedDateTime intervalEnd) {
        super(source);
        this.type = type;
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
    }

    public CalendarModelEventType getType() {
        return type;
    }

    public ZonedDateTime getIntervalStart() {
        return intervalStart;
    }

    public ZonedDateTime getIntervalEnd() {
        return intervalEnd;
    }
}
