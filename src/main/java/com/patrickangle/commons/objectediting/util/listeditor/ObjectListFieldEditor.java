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

import com.patrickangle.commons.observable.collections.ObservableArrayList;
import com.patrickangle.commons.observable.collections.ObservableCollections;
import com.patrickangle.commons.observable.collections.ObservableList;
import com.patrickangle.commons.observable.collections.ObservableListListener;
import java.beans.PropertyChangeEvent;
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
        backingList = new ObservableArrayList<>();
        commonInit();
    }
    
    public ObjectListFieldEditor(List<E> backingList) {
        this.backingList = backingList;
        commonInit();
    }
    
    private void commonInit() {
        observableListListener = new ObservableListListener() {
            // @todo why did I not implement any of these handlers?

            @Override
            public void elementsAdded(ObservableList list, int startIndex, int length, List newElements) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void elementsRemoved(ObservableList list, int startIndex, int length, List oldElements) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void elementReplaced(ObservableList list, int index, Object oldElement, Object newElement) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void elementPropertyChanged(ObservableList list, int index, Object element, PropertyChangeEvent proeprtyChangeEvent) {
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
