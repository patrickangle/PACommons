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
package com.patrickangle.commons.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patrickangle.commons.json.JsonableObject;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservableBase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Patrick Angle
 */
public class Time extends PropertyChangeObservableBase implements JsonableObject {
    @JsonProperty protected long milliseconds;
    
    private static final SimpleDateFormat timeFormatA = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final SimpleDateFormat timeFormatB = new SimpleDateFormat("mm:ss.SSS");
    private static final SimpleDateFormat timeFormatC = new SimpleDateFormat("ss.SSS");
    private static final SimpleDateFormat timeFormatD = new SimpleDateFormat("ss");
    
    public Time() {
        milliseconds = 0;
    }
    
    public Time(long milliseconds) {
        this.milliseconds = milliseconds;
    }
    
    public Time(String time) {
        try {
            this.milliseconds = timeFormatA.parse(time).getTime();
            this.milliseconds += TimeZone.getDefault().getRawOffset();
        } catch (ParseException exA) {
            try {
                this.milliseconds = timeFormatB.parse(time).getTime();
            } catch (ParseException exB) {
                try {
                    this.milliseconds = timeFormatC.parse(time).getTime();
                } catch (ParseException exC) {
                    try {
                        this.milliseconds = timeFormatD.parse(time).getTime();
                    } catch (ParseException exD) {
                        milliseconds = 0;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return timeFormatA.format(new Date(milliseconds - TimeZone.getDefault().getRawOffset()));
    }
    
    public void setMilliseconds(long milliseconds) {
        long oldMilliseconds = this.milliseconds;
        this.milliseconds = milliseconds;
        this.propertyChangeSupport.firePropertyChange("milliseconds", oldMilliseconds, this.milliseconds);
    }
    
    public long getMilliseconds() {
        return this.milliseconds;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Time) {
            return ((Time)o).getMilliseconds() == this.getMilliseconds();
        } else {
            return false;
        }
    }
}
