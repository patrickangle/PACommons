/*
 * WorldStage Waltz
 * 
 * This software is the sole property of WorldStage. WorldStage retains all
 * rights to any code or APIs contained herein.
 * 
 * (C) 2018 WorldStage. All rights reserved.
 */
package com.patrickangle.commons.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.swing.models.ObservableComboBoxModel;
import com.patrickangle.commons.json.JsonableObject;
import com.patrickangle.commons.objectediting.interfaces.CustomObjectEditingComponent;
import com.patrickangle.commons.objectediting.util.ObjectFieldEditorFactory;
import com.patrickangle.commons.observable.collections.ObservableArrayList;
import com.patrickangle.commons.observable.collections.ObservableCollections;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservableBase;
import com.patrickangle.commons.util.Displays;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.util.ArrayList;
import javax.swing.JComboBox;

/**
 *
 * @author Patrick Angle
 */
public class Display extends PropertyChangeObservableBase implements CustomObjectEditingComponent, JsonableObject {
    @JsonProperty protected int number;

    public Display() {
        this.number = 0;
    }
    
    public Display(int number) {
        this.number = number;
    }
    
    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        int oldNumber = this.number;
        this.number = number;        
        this.propertyChangeSupport.firePropertyChange("number", oldNumber, this.number);
    }
    
    public Dimension getResolution() {
        DisplayMode displayMode = Displays.displayMode(this.number);
        return new Dimension(displayMode.getWidth(), displayMode.getHeight());
    }
    
    public int getRefreshRate() {
        return Displays.displayMode(this.number).getRefreshRate();
    }
    
    public int getColorDepth() {
        return Displays.displayMode(this.number).getBitDepth();
    }

    @Override
    public ObjectFieldEditorFactory.ComponentReturn customObjectEditingComponent(BindingGroup bindingGroup) {
        Display[] displays = Displays.availableDisplays();
        ArrayList<Display.DisplayComboBoxItem> displayItems = new ArrayList<>(displays.length);
        
        for (Display display : displays) {
            displayItems.add(new DisplayComboBoxItem(display.number));
        }
        
        JComboBox<Display.DisplayComboBoxItem> displayEditor = new JComboBox<>(new ObservableComboBoxModel<>(new ObservableArrayList<>(displayItems)));
        displayEditor.setEditable(true);
        
        Binding binding = new BasicBinding(this, "number", displayEditor.getModel(), "selectedItem", Binding.UpdateStrategy.READ_WRITE, new DisplayComboBoxConverter());
        bindingGroup.add(binding);
        
        return new ObjectFieldEditorFactory.ComponentReturn(displayEditor, false);
    }
    
    public static class DisplayComboBoxConverter implements Binding.Converter<Integer, Object> {

        @Override
        public Object convertForward(Integer object) {
            return new DisplayComboBoxItem(object);
        }

        @Override
        public Integer convertBackward(Object object) {
            if (object instanceof DisplayComboBoxItem) {
                return ((DisplayComboBoxItem)object).getNumber();
            } else if (object instanceof String) {
                try {
                    return Integer.parseInt((String)object);
                } catch (NumberFormatException ex) {
                    return 0;
                }
            } else {
                return 0;
            }
        }
        
    }
    
    public static class DisplayComboBoxItem {
        private int number;
        private String displayName;
        
        public DisplayComboBoxItem(int number) {
            this.number = number;
            try {
                DisplayMode mode = Displays.displayMode(number);
                this.displayName = "Display " + (number+1) + " (" + mode.getWidth() + "x" + mode.getHeight() + " " + mode.getRefreshRate() + "hz " + mode.getBitDepth() + "-bit color" + ")";
            } catch (ArrayIndexOutOfBoundsException ex) {
                this.displayName = "Display " + (number+1);
            }
        }

        public int getNumber() {
            return number;
        }

        @Override
        public String toString() {
            return displayName;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + this.number;
            return hash;
        }

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
            final DisplayComboBoxItem other = (DisplayComboBoxItem) obj;
            return this.number == other.number;
        }
        
        
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.number;
        return hash;
    }

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
        final Display other = (Display) obj;
        if (this.number != other.number) {
            return false;
        }
        return true;
    }
    
    
    
}
