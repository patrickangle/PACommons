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
package com.patrickangle.commons.objectediting.util.listeditor;

import com.patrickangle.commons.observable.collections.ObservableCollections;
import com.patrickangle.commons.observable.collections.ObservableList;
import com.patrickangle.commons.observable.collections.ObservableListListener;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Patrick Angle
 */
public class ObjectListFieldEditor<E> extends JPanel {
    protected List<E> backingList;
    protected ObservableListListener observableListListener;
    
    public ObjectListFieldEditor(Class<E> valueClass) {
        backingList = ObservableCollections.observableList(valueClass);
        commonInit();
    }
    
    public ObjectListFieldEditor(List<E> backingList) {
        this.backingList = backingList;
        commonInit();
    }
    
    private void commonInit() {
        observableListListener = new ObservableListListener() {
            @Override
            public void listElementsAdded(ObservableList list, int index, int length) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void listElementsRemoved(ObservableList list, int index, List oldElements) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void listElementReplaced(ObservableList list, int index, Object oldElement) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void listElementPropertyChanged(ObservableList list, int index) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        if (this.backingList instanceof ObservableList) {
            ((ObservableList) this.backingList).addObservableListListener(observableListListener);
        }
    }
    
    protected void addBelow(E item) {
        
    }
    
    

    public List<E> getBackingList() {
        return backingList;
    }

    public void setBackingList(List<E> backingList) {
        List<E> oldBackingList = this.backingList;
        this.backingList = backingList;
        this.firePropertyChange("backingList", oldBackingList, backingList);
        
        if (this.backingList instanceof ObservableList) {
            ((ObservableList) this.backingList).addObservableListListener(observableListListener);
        }
    }
    
}
