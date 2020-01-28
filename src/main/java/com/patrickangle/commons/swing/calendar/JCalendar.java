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

import com.patrickangle.commons.swing.calendar.model.data.CalendarModel;
import com.patrickangle.commons.swing.calendar.model.renderer.CalendarCellRenderer;
import com.patrickangle.commons.swing.calendar.model.selection.CalendarSelectionListener;
import com.patrickangle.commons.swing.calendar.model.selection.CalendarSelectionMode;
import com.patrickangle.commons.swing.calendar.model.selection.CalendarSelectionModel;
import com.patrickangle.commons.swing.calendar.model.selection.DefaultCalendarSelectionModel;
import java.beans.BeanProperty;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;
import javax.swing.plaf.ListUI;

/**
 *
 * @author patrickangle
 */
public class JCalendar extends JComponent {
    private static final String uiClassID = "CalendarUI";

    private CalendarView view;
    
    private CalendarModel dataModel;
    private CalendarSelectionModel selectionModel;
    private Map<CalendarView, CalendarCellRenderer> cellRenderers;
    
    private ZonedDateTime viewingDate;
    
    public JCalendar(CalendarModel dataModel, ZonedDateTime viewingDate) {
        if (dataModel == null) {
            throw new IllegalArgumentException("dataModel must be non null");
        }
        if (viewingDate == null) {
            viewingDate = ZonedDateTime.now();
        }
        
        // Register with the ToolTipManager so that tooltips from the renderer show through.
        ToolTipManager.sharedInstance().registerComponent(this);
        
        this.dataModel = dataModel;
        this.selectionModel = createSelectionModel();
        this.cellRenderers = new EnumMap<CalendarView, CalendarCellRenderer>(CalendarView.class);
    }
    
    protected CalendarSelectionModel createSelectionModel() {
        return new DefaultCalendarSelectionModel();
    }
    
    

    public CalendarView getView() {
        return view;
    }

    public void setView(CalendarView view) {
        this.firePropertyChange("view", this.view, this.view = view);
    }

    public CalendarModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(CalendarModel dataModel) {
        this.firePropertyChange("dataModel", this.dataModel, this.dataModel = dataModel);
    }

    public CalendarSelectionModel getSelectionModel() {
        return selectionModel;
    }

    public ZonedDateTime getViewingDate() {
        return viewingDate;
    }

    public void setViewingDate(ZonedDateTime viewingDate) {
        this.firePropertyChange("viewingDate", this.viewingDate, this.viewingDate = viewingDate);
        
    }
    
    // <editor-fold defaultstate="collapsed" desc="CalendarSelectionModel Delegated Methods">

    public Collection<CalendarEvent> getSelectedEvents() {
        return selectionModel.getSelectedEvents();
    }

    public void setSelectedEvents(Collection<CalendarEvent> events) {
        selectionModel.setSelectedEvents(events);
    }

    public void setSelectedEvent(CalendarEvent event) {
        selectionModel.setSelectedEvent(event);
    }

    public void clearSelection() {
        selectionModel.clearSelection();
    }

    public void addSelectedEvents(Collection<CalendarEvent> events) {
        selectionModel.addSelectedEvents(events);
    }

    public void addSelectedEvent(CalendarEvent event) {
        selectionModel.addSelectedEvent(event);
    }

    public void removeSelectedEvents(Collection<CalendarEvent> events) {
        selectionModel.removeSelectedEvents(events);
    }

    public void removeSelectedEvent(CalendarEvent event) {
        selectionModel.removeSelectedEvent(event);
    }

    public boolean isSelectionEmpty() {
        return selectionModel.isSelectionEmpty();
    }

    public boolean getValueIsAdjusting() {
        return selectionModel.getValueIsAdjusting();
    }

    public void setValueIsAdjusting(boolean adjusting) {
        selectionModel.setValueIsAdjusting(adjusting);
    }

    public CalendarSelectionMode getSelectionMode() {
        return selectionModel.getSelectionMode();
    }

    public void setSelectionMode(CalendarSelectionMode mode) {
        selectionModel.setSelectionMode(mode);
    }

    public void addCalendarSelectionListener(CalendarSelectionListener l) {
        selectionModel.addCalendarSelectionListener(l);
    }

    public void removeCalendarSelectionListener(CalendarSelectionListener l) {
        selectionModel.removeCalendarSelectionListener(l);
    }
    
    // </editor-fold>

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    public ListUI getUI() {
        return (ListUI)ui;
    }

    @BeanProperty(hidden = true, visualUpdate = true, description
            = "The UI object that implements the Component's LookAndFeel.")
    public void setUI(ListUI ui) {
        super.setUI(ui);
    }
}
