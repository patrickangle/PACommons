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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.patrickangle.commons.json.serialization.TimeDeserializer;
import com.patrickangle.commons.json.serialization.TimeSerializer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A basic structure representing a specific amount of time, with millisecond
 * precision.
 * 
 * A JSON serializer and deserializer are also provided in the
 * `com.patrickangle.commons.json.serialization` package as `.TimeSerializer`
 * and `.TimeSerializer`
 *
 * @author patrickangle
 */
@JsonSerialize(using = TimeSerializer.class)
@JsonDeserialize(using = TimeDeserializer.class)
public class Time {
    protected long milliseconds;
    
    private static final SimpleDateFormat TIME_FORMAT_A = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final SimpleDateFormat TIME_FORMAT_B = new SimpleDateFormat("mm:ss.SSS");
    private static final SimpleDateFormat TIME_FORMAT_C = new SimpleDateFormat("ss.SSS");
    private static final SimpleDateFormat TIME_FORMAT_D = new SimpleDateFormat("ss");
    
    public Time() {
        milliseconds = 0;
    }
    
    public Time(long milliseconds) {
        this.milliseconds = milliseconds;
    }
    
    public Time(String time) {
        try {
            this.milliseconds = TIME_FORMAT_A.parse(time).getTime();
            this.milliseconds += TimeZone.getDefault().getRawOffset();
        } catch (ParseException exA) {
            try {
                this.milliseconds = TIME_FORMAT_B.parse(time).getTime();
            } catch (ParseException exB) {
                try {
                    this.milliseconds = TIME_FORMAT_C.parse(time).getTime();
                } catch (ParseException exC) {
                    try {
                        this.milliseconds = TIME_FORMAT_D.parse(time).getTime();
                    } catch (ParseException exD) {
                        milliseconds = 0;
                    }
                }
            }
        }
    }
    
    public long getMilliseconds() {
        return this.milliseconds;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (int) (this.milliseconds ^ (this.milliseconds >>> 32));
        return hash;
    }

    /**
     * Check the equality of this Time with the given object by comparing the exact millisecond of each time.
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Time other = (Time) obj;
        if (this.milliseconds != other.milliseconds) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return TIME_FORMAT_A.format(new Date(milliseconds - TimeZone.getDefault().getRawOffset()));
    }
    
    public static Time parseTime(String string) {
        return new Time(string);
    }
}
