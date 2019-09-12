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
package com.patrickangle.commons.observable.collections;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author Patrick Angle
 */
public class ObservableMapSupport<K, V> {
    private ObservableMap<K, V> map;
    
    private CopyOnWriteArrayList<ObservableMapListener<K, V>> observableMapListeners;
    
    public ObservableMapSupport(ObservableMap<K, V> map) {
        this.map = map;
        this.observableMapListeners = new CopyOnWriteArrayList<>();
    }
    
    public void fireEntriesAdded(K key, V value) {
        observableMapListeners.stream().forEach((t) -> {
            t.entryAdded(map, key, value);
        });
    }
    
    public void fireEntriesRemoved(K key, V value) {
        observableMapListeners.stream().forEach((t) -> {
            t.entryRemoved(map, key, value);
        });
    }
    
    public void fireEntryReplaced(K key, V oldValue, V newValue) {
        observableMapListeners.stream().forEach((t) -> {
            t.entryReplaced(map, key, oldValue, newValue);
        });
    }
    
    public void fireEntryPropertyChanged(K key, V value, PropertyChangeEvent propertyChangeEvent) {
        observableMapListeners.stream().forEach((t) -> {
            t.entryPropertyChanged(map, key, value, propertyChangeEvent);
        });
    }
    
    public void fireEntryPropertyChanged(V value, PropertyChangeEvent propertyChangeEvent) {
        observableMapListeners.stream().forEach((t) -> {
            for (K key : keysForValue(value)) {
                fireEntryPropertyChanged(key, value, propertyChangeEvent);
            }
        });
    }

    public void addObservableMapListener(ObservableMapListener<K, V> listener) {
        observableMapListeners.add(listener);        
    }
    
    public void removeObservableMapListener(ObservableMapListener<K, V> listener) {
        observableMapListeners.remove(listener);
    }
    
    public Set<K> keysForValue(V value) {
        return map.entrySet()
              .stream()
              .filter(entry -> entry.getValue() == value)
              .map(Map.Entry::getKey)
              .collect(Collectors.toSet());
    }
    
    public Set<Entry<K, V>> entriesForValue(V value) {
        return map.entrySet()
              .stream()
              .filter(entry -> entry.getValue() == value)
              .collect(Collectors.toSet());
    }
}
