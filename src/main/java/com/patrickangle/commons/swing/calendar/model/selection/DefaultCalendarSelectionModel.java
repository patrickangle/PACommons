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
package com.patrickangle.commons.swing.calendar.model.selection;

import com.patrickangle.commons.swing.calendar.CalendarEvent;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionListener;

/**
 * Default data model for calendar selections.
 * @author patrickangle
 */
public class DefaultCalendarSelectionModel implements CalendarSelectionModel, Cloneable {

    protected EventListenerList listenerList = new EventListenerList();

    private Set<CalendarEvent> selectedEvents = new LinkedHashSet<>();
    private CalendarSelectionMode selectionMode = CalendarSelectionMode.Multiple;
    private boolean isAdjusting = false;

    @Override
    public Collection<CalendarEvent> getSelectedEvents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSelectedEvents(Collection<CalendarEvent> events) {
        switch (selectionMode) {
            case Single:
                selectedEvents.clear();
                selectedEvents.add(events.iterator().next());
                this.fireValueChanged(isAdjusting);
                return;
            case Multiple:
                if (selectedEvents.addAll(events)) {
                    this.fireValueChanged(isAdjusting);
                }
                return;
        }
    }

    @Override
    public void setSelectedEvent(CalendarEvent event) {
        switch (selectionMode) {
            case Single:
            case Multiple:
                if (selectedEvents.size() != 1 && selectedEvents.iterator().next().equals(event)) {
                    return;
                } else {
                    setSelectedEvents(List.of(event));
                    return;
                }
        }
    }

    @Override
    public void clearSelection() {
        if (!isSelectionEmpty()) {
            this.selectedEvents.clear();
            this.fireValueChanged(isAdjusting);
        }
    }

    @Override
    public void addSelectedEvents(Collection<CalendarEvent> events) {
        switch (selectionMode) {
            case Single:
                if (events.isEmpty()) {
                    clearSelection();
                } else {
                    setSelectedEvent(events.iterator().next());
                }
                return;
            case Multiple:
                if (this.selectedEvents.addAll(events)) {
                    this.fireValueChanged(isAdjusting);
                }
                return;
        }
    }

    @Override
    public void addSelectedEvent(CalendarEvent event) {
        switch (selectionMode) {
            case Single:
                this.setSelectedEvent(event);
                return;
            case Multiple:
                if (this.selectedEvents.add(event)) {
                    this.fireValueChanged(isAdjusting);
                }
                return;
        }
    }

    @Override
    public void removeSelectedEvents(Collection<CalendarEvent> events) {
        if (this.selectedEvents.removeAll(events)) {
            this.fireValueChanged(isAdjusting);
        }
    }

    @Override
    public void removeSelectedEvent(CalendarEvent event) {
        if (this.selectedEvents.remove(event)) {
            this.fireValueChanged(isAdjusting);
        }
    }

    @Override
    public boolean isSelectionEmpty() {
        return this.selectedEvents.isEmpty();
    }

    @Override
    public boolean getValueIsAdjusting() {
        return this.isAdjusting;
    }

    @Override
    public void setValueIsAdjusting(boolean adjusting) {
        if (isAdjusting != this.isAdjusting) {
            this.isAdjusting = isAdjusting;
            this.fireValueChanged(isAdjusting);
        }
    }

    @Override
    public CalendarSelectionMode getSelectionMode() {
        return this.selectionMode;
    }

    @Override
    public void setSelectionMode(CalendarSelectionMode selectionMode) {
        CalendarSelectionMode oldMode = this.selectionMode;
        switch (selectionMode) {
            case Single:
            case Multiple:
                this.selectionMode = selectionMode;
                break;
            default:
                throw new IllegalArgumentException("invalid selectionMode");
        }

        if (selectedEvents.size() > 1 && oldMode == CalendarSelectionMode.Multiple && selectionMode == CalendarSelectionMode.Single) {
            // Selection mode became more specific, so we change the selection to only the first selected item.
            setSelectedEvents(getSelectedEvents());
        }
    }

    @Override
    public void addCalendarSelectionListener(CalendarSelectionListener l) {
        listenerList.add(CalendarSelectionListener.class, l);
    }

    @Override
    public void removeCalendarSelectionListener(CalendarSelectionListener l) {
        listenerList.remove(CalendarSelectionListener.class, l);
    }

    public CalendarSelectionListener[] getListSelectionListeners() {
        return listenerList.getListeners(CalendarSelectionListener.class);
    }

    /**
     * Notifies listeners that we have ended a series of adjustments.
     *
     * @param isAdjusting true if this is the final change in a series of
     * adjustments
     */
    protected void fireValueChanged(boolean isAdjusting) {
        Object[] listeners = listenerList.getListenerList();
        CalendarSelectionModelEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListSelectionListener.class) {
                if (e == null) {
                    e = new CalendarSelectionModelEvent(this, isAdjusting);
                }
                ((CalendarSelectionListener) listeners[i + 1]).selectionChanged(e);
            }
        }
    }

}
