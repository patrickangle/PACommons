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
package com.patrickangle.commons.observable.support;

import com.patrickangle.commons.observable.collections.ObservableList;
import com.patrickangle.commons.observable.collections.ObservableListListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author patrickangle
 */
public class ListDataSupport {
    private Object sourceBean;
    private List<ListDataListener> listDataListeners;
    private ObservableListListener observableListListener;
    
    public ListDataSupport(Object sourceBean) {
        this.sourceBean = sourceBean;
        this.listDataListeners = new CopyOnWriteArrayList<>();
        
        this.observableListListener = new ObservableListListener() {
            @Override
            public void listElementsAdded(ObservableList list, int index, int length) {
                fireIntervalAdded(index, index + length);
            }

            @Override
            public void listElementsRemoved(ObservableList list, int index, List oldElements) {
                fireIntervalRemoved(index, index + oldElements.size());
            }

            @Override
            public void listElementReplaced(ObservableList list, int index, Object oldElement) {
                fireContentsChanged(index, index + 1);
            }

            @Override
            public void listElementPropertyChanged(ObservableList list, int index) {
                fireContentsChanged(index, index + 1);
            }
        };
    }
    
    public void fireContentsChanged(int index0, int index1) {
        ListDataEvent event = new ListDataEvent(sourceBean, ListDataEvent.CONTENTS_CHANGED, index0, index1);
        
        listDataListeners.forEach((listDataListener) -> {
            listDataListener.contentsChanged(event);
        });
    }
    
    public void fireIntervalAdded(int index0, int index1) {
        ListDataEvent event = new ListDataEvent(sourceBean, ListDataEvent.INTERVAL_ADDED, index0, index1);
        
        listDataListeners.forEach((listDataListener) -> {
            listDataListener.contentsChanged(event);
        });
    }
    
    public void fireIntervalRemoved(int index0, int index1) {
        ListDataEvent event = new ListDataEvent(sourceBean, ListDataEvent.INTERVAL_REMOVED, index0, index1);
        
        listDataListeners.forEach((listDataListener) -> {
            listDataListener.contentsChanged(event);
        });
    }
    
    public void addListDataListener(ListDataListener listDataListener) {
        listDataListeners.add(listDataListener);
    }
    
    public void removeListDataListener(ListDataListener listDataListener) {
        listDataListeners.remove(listDataListener);
    }
    
    public ObservableListListener getObservableListListener() {
        return this.observableListListener;
    }
}
