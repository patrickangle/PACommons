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

/**
 * This interface represents the current state of the selection for a set of
 * CalendarEvents. The selection is modeled as a collection of events from which
 * events are added upon selection and removed upon deselection.
 *
 * @author patrickangle
 */
public interface CalendarSelectionModel {

    /**
     * Returns the selected events.
     *
     * @return the selected events
     */
    public Collection<CalendarEvent> getSelectedEvents();

    /**
     * Changes the selection to be only the provided events.
     *
     * @param events
     */
    public void setSelectedEvents(Collection<CalendarEvent> events);

    /**
     * Changes the selection to be only the provided event.
     *
     * @param event the event
     */
    public void setSelectedEvent(CalendarEvent event);

    /**
     * Change the selection to the empty set. If this represents a change to the
     * current selection then notify each CalendarSelectionListener.
     */
    public void clearSelection();

    /**
     * Add the provided events to the selection model.
     *
     * @param events the events
     */
    public void addSelectedEvents(Collection<CalendarEvent> events);

    /**
     * Add the provided event to the selection model.
     *
     * @param event the event
     */
    public void addSelectedEvent(CalendarEvent event);

    /**
     * Remove the provided events from the selection model. This is typically
     * called to sync the selection model with a corresponding change in the
     * data model.
     *
     * @param events the events
     */
    public void removeSelectedEvents(Collection<CalendarEvent> events);

    /**
     * Remove the provided event from the selection model. This is typically
     * called to sync the selection model with a corresponding change in the
     * data model.
     *
     * @param event the event
     */
    public void removeSelectedEvent(CalendarEvent event);

    /**
     * Returns true if no CalendarEvents are selected.
     *
     * @return true if no CalendarEvents are selected
     */
    public boolean isSelectionEmpty();

    /**
     * Returns true if the selection is undergoing a series of changes.
     *
     * @return true if the selection is undergoing a series of changes
     */
    public boolean getValueIsAdjusting();

    /**
     * Sets the valueIsAdjusting property, which indicates whether or not
     * upcoming selection changes should be considered part of a single change.
     * The value of this property is used to initialize the valueIsAdjusting
     * property of the CalendarSelectionModelEvents that are generated. For
     * example, if the selection is being updated in response to a user drag,
     * this property can be set to true when the drag is initiated and set to
     * false when the drag is finished. During the drag, listeners receive
     * events with a valueIsAdjusting property set to true. At the end of the
     * drag, when the change is finalized, listeners receive an event with the
     * value set to false. Listeners can use this pattern if they wish to update
     * only when a change has been finalized.
     *
     * Setting this property to true begins a series of changes that is to be
     * considered part of a single change. When the property is changed back to
     * false, an event is sent out characterizing the entire selection change
     * (if there was one), with the event's valueIsAdjusting property set to
     * false.
     *
     * @param adjusting the new value of the property
     */
    public void setValueIsAdjusting(boolean adjusting);

    /**
     * Returns the current selection mode
     *
     * @return the current selection mode
     */
    public CalendarSelectionMode getSelectionMode();

    /**
     * Sets the selection mode.
     *
     * CalendarSelectionMode.Single - Only one CalendarEvent can be selected at
     * a time. In this mode, setSelectedEvents and addSelectedEvents only honor
     * the last element in the collection. Additionally, addSelectedEvent will
     * behave identically to setSelectedEvent, overriding the current selection.
     *
     * CalendarSelectionMode.Multiple - In this mode, there is no restriction on
     * what can be selected.
     *
     * @param mode the selection mode
     * @throws IllegalArgumentException if the selection mode isn't allowed.
     */
    public void setSelectionMode(CalendarSelectionMode mode);

    /**
     * Add a listener to the calendar that's notified each time a change to the
     * selection occurs.
     *
     * @param l the CalendarSelectionListener
     */
    public void addCalendarSelectionListener(CalendarSelectionListener l);

    /**
     * Remove a listener from the calendar that's notified each time a change to
     * the selection occurs.
     *
     * @param l the CalendarSelectionListener
     */
    public void removeCalendarSelectionListener(CalendarSelectionListener l);
}
