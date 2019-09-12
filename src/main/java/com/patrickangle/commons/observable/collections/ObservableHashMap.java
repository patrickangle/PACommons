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

import com.patrickangle.commons.observable.interfaces.PropertyChangeObservable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 * @author patrickangle
 */
public class ObservableHashMap<K, V> extends HashMap<K, V> implements ObservableMap<K, V> {

    private ObservableMapSupport<K, V> observableMapSupport;

    private PropertyChangeListener entryPropertyChangeListener;
    private ObservableListListener entryListListener;

    public ObservableHashMap() {
        commonInit();
    }

    public ObservableHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        commonInit();
    }

    public ObservableHashMap(int initialCapacity) {
        super(initialCapacity);
        commonInit();
    }

    public ObservableHashMap(Map<? extends K, ? extends V> m) {
        super(m);
        commonInit();
    }

    private void commonInit() {
        observableMapSupport = new ObservableMapSupport<>(this);

        entryPropertyChangeListener = (propertyChangeEvent) -> {
            observableMapSupport.fireEntryPropertyChanged((V) propertyChangeEvent.getSource(), propertyChangeEvent);
        };

        entryListListener = new ObservableListListenerAdapter() {
            @Override
            public void elementPropertyChanged(ObservableList list, int index, Object element, PropertyChangeEvent propertyChangeEvent) {
                observableMapSupport.fireEntryPropertyChanged((V) list, propertyChangeEvent);
            }
        };

        // TODO: Add support for nested maps as well.
        this.values().forEach((t) -> {
            PropertyChangeObservable.addPropertyChangeListener(t, entryPropertyChangeListener);
            if (t instanceof List) {
                ObservableList.addObservableListListener((List) t, entryListListener);
            }
        });
    }

    @Override
    public V put(K key, V value) {
        V oldValue = super.put(key, value);
        if (oldValue != null) {
            observableMapSupport.fireEntryReplaced(key, oldValue, value);
        } else {
            observableMapSupport.fireEntriesAdded(key, value);
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
        super.putAll(m);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        boolean hasValue = containsKey(key);
        V value = super.computeIfAbsent(key, mappingFunction);
        if (!hasValue) {
            observableMapSupport.fireEntriesAdded(key, value);
        }
        return value;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        boolean hasValue = containsKey(key);
        V existingValue = get(key);
        V value = super.computeIfPresent(key, remappingFunction);
        if (hasValue) {
            observableMapSupport.fireEntryReplaced(key, existingValue, value);
        }
        return value;
    }

    @Override
    public V remove(Object key) {
        V oldValue = super.remove(key);
        if (oldValue != null) {
            observableMapSupport.fireEntriesRemoved((K) key, oldValue);
        }
        return oldValue;
    }

    @Override
    public boolean remove(Object key, Object value) {
        boolean wasRemoved = super.remove(key, value);
        if (wasRemoved) {
            observableMapSupport.fireEntriesRemoved((K) key, (V) value);
        }
        return wasRemoved;
    }

    @Override
    public V replace(K key, V value) {
        V oldValue = super.replace(key, value);
        if (oldValue != null) {
            observableMapSupport.fireEntryReplaced(key, oldValue, value);
        }
        return value;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        boolean wasReplaced = super.replace(key, oldValue, newValue);
        if (wasReplaced) {
            observableMapSupport.fireEntryReplaced(key, oldValue, newValue);
        }
        return wasReplaced;
    }

    @Override
    public void clear() {
        for (K k : keySet()) {
            remove(k);
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        for (Map.Entry<K, V> entry : entrySet()) {
            V oldValue = entry.setValue(function.apply(entry.getKey(), entry.getValue()));
            observableMapSupport.fireEntryReplaced(entry.getKey(), oldValue, entry.getValue());
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        V oldValue = super.putIfAbsent(key, value);
        if (oldValue == null) {
            observableMapSupport.fireEntriesAdded(key, value);
        }
        return value;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V oldValue = get(key);
        V newValue = super.compute(key, remappingFunction);
        
        if (oldValue == null && newValue != null) {
            observableMapSupport.fireEntriesAdded(key, newValue);
        } else if (oldValue != null && newValue == null) {
            observableMapSupport.fireEntriesRemoved(key, oldValue);
        } else if (oldValue != null && newValue != null) {
            observableMapSupport.fireEntryReplaced(key, oldValue, newValue);
        }
        
        return newValue;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        V oldValue = get(key);
        V newValue = super.merge(key, value, remappingFunction);
        
        if (oldValue == null && newValue != null) {
            observableMapSupport.fireEntriesAdded(key, newValue);
        } else if (oldValue != null && newValue == null) {
            observableMapSupport.fireEntriesRemoved(key, oldValue);
        } else if (oldValue != null && newValue != null) {
            observableMapSupport.fireEntryReplaced(key, oldValue, newValue);
        }
        
        return newValue;
    }

    @Override
    public void addObservableMapListener(ObservableMapListener<K, V> listener) {
        observableMapSupport.addObservableMapListener(listener);
    }

    @Override
    public void removeObservableMapListener(ObservableMapListener<K, V> listener) {
        observableMapSupport.removeObservableMapListener(listener);
    }
}
