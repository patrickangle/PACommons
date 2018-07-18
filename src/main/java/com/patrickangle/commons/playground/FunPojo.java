/*
 * Patrick Angle Commons Library
 * Copyright 2018 Patrick Angle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrickangle.commons.playground;

import com.patrickangle.commons.objectediting.annotations.ObjectEditingProperty;
import com.patrickangle.commons.observable.collections.ObservableCollections;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import com.patrickangle.commons.types.Display;
import com.patrickangle.commons.types.LocalInterface;
import com.patrickangle.commons.types.Point;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author patrickangle
 */
public class FunPojo extends BoringPojo implements PropertyChangeObservable {
    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public static final String USER_EDITABLE_PROPERTY_GROUP = "Fun Plain Old Java Object";
    
    @ObjectEditingProperty(name="My verbose boolean statement name goes here.") protected boolean option1 = true;
    @ObjectEditingProperty(name="Integer with Help", help="This sets the thing for the other thing!") protected int option2 = 30;
    @ObjectEditingProperty(name="Float Number", numberMinimumValue = 0, numberMaximumValue = 300, numberStepValue = 0.5) protected float option3 = 256.7f;
    @ObjectEditingProperty(name="Double Slider", numberEditor = ObjectEditingProperty.NumberEditor.SLIDER_CONTROL) protected double option4 = 128.99999;
    @ObjectEditingProperty protected String option5 = "This is a test";
    @ObjectEditingProperty protected Color option6 = Color.BLUE;
    @ObjectEditingProperty(name="Int Point") protected Point.IntegerPoint intPoint = new Point.IntegerPoint(7, 14);
    @ObjectEditingProperty(name="Float Point") protected Point.FloatPoint floatPoint = new Point.FloatPoint(99.08f, 777.122324f);
    @ObjectEditingProperty(name="Double Point") protected Point.DoublePoint doublePoint = new Point.DoublePoint(7.0000045, 13.99862);
    @ObjectEditingProperty(name="Display") protected Display display = new Display(3);
    @ObjectEditingProperty(name="NIC") protected LocalInterface localInterface = new LocalInterface();
    
    @ObjectEditingProperty(name="Point List", listNewItemClass = Point.IntegerPoint.class) protected List<Point.IntegerPoint> pointList = ObservableCollections.concurrentObservableList(Arrays.asList(new Point.IntegerPoint(), new Point.IntegerPoint(), new Point.IntegerPoint(), new Point.IntegerPoint()));
    
    public FunPojo() {
        
    }

    public boolean isOption1() {
        return option1;
    }

    public void setOption1(boolean option1) {
        this.option1 = option1;
    }

    public int getOption2() {
        return option2;
    }

    public void setOption2(int option2) {
        this.option2 = option2;
    }

    public float getOption3() {
        return option3;
    }

    public void setOption3(float option3) {
        this.option3 = option3;
    }

    public double getOption4() {
        return option4;
    }

    public void setOption4(double option4) {
        this.option4 = option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption5(String option5) {
        String oldValue = this.option5;
        this.option5 = option5;
        this.pcs.firePropertyChange("option5", oldValue, this.option5);
    }

    public Color getOption6() {
        return option6;
    }

    public void setOption6(Color option6) {
        this.option6 = option6;
    }

    @Override
    public String toString() {
        return this.option5;
    }

    public Point.IntegerPoint getIntPoint() {
        return intPoint;
    }

    public void setIntPoint(Point.IntegerPoint intPoint) {
        this.intPoint = intPoint;
    }

    public Point.FloatPoint getFloatPoint() {
        return floatPoint;
    }

    public void setFloatPoint(Point.FloatPoint floatPoint) {
        this.floatPoint = floatPoint;
    }

    public Point.DoublePoint getDoublePoint() {
        return doublePoint;
    }

    public void setDoublePoint(Point.DoublePoint doublePoint) {
        this.doublePoint = doublePoint;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public LocalInterface getLocalInterface() {
        return localInterface;
    }

    public void setLocalInterface(LocalInterface localInterface) {
        this.localInterface = localInterface;
    }

    public List<Point.IntegerPoint> getPointList() {
        return pointList;
    }

    public void setPointList(List<Point.IntegerPoint> pointList) {
        this.pointList = pointList;
    }
    
    
    
    

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        pcs.addPropertyChangeListener(propertyChangeListener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        pcs.removePropertyChangeListener(propertyChangeListener);
    }
    
    
}
