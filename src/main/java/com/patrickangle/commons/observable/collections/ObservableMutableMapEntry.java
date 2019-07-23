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

import com.patrickangle.commons.observable.interfaces.PropertyChangeObservableBase;
import java.util.Map;

/**
 *
 * @author patrickangle
 */
public class ObservableMutableMapEntry<K, V> extends PropertyChangeObservableBase implements Map.Entry<K, V> {
        private K key;
        private V value;
        
        public ObservableMutableMapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }
        
        public void setKey(K key) {
            this.propertyChangeSupport.firePropertyChange("key", this.key, this.key = key);
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            this.propertyChangeSupport.firePropertyChange("value", oldValue, this.value);
            return oldValue;
        }
        
}
