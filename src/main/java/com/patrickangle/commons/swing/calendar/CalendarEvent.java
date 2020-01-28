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

import java.time.ZonedDateTime;
import java.util.Map;

/**
 *
 * @author patrickangle
 */
public interface CalendarEvent {
    /**
     * Get the time at which the event should occur.
     * @return 
     */
    public ZonedDateTime getTime();
    
    /**
     * Get the duration of the event, in nanoseconds.
     * @return 
     */
    public long getDuration();
    
    /**
     * Get the name of the event.
     * @return 
     */
    public String getName();
    
    /**
     * Get a description of the event.
     * @return 
     */
    public String getDescription();

    /**
     * Get a map of additional details pertaining to the event that may be used by the CellRenderers to display the events.
     * @return 
     */
    public Map<String, Object> getAdditionalDetails();
}
