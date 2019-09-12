/*
 * Copyright (C) 2006-2007 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package com.patrickangle.commons.observable.collections;

import java.beans.PropertyChangeEvent;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Notification types from an {@code ObservableMap}.
 *
 * @author sky
 */
public interface ObservableMapListener<K, V> {
    
    public void entryAdded(ObservableMap<K, V> map, K key, V value);
    public void entryRemoved(ObservableMap<K, V> map, K key, V value);
    public void entryReplaced(ObservableMap<K, V> map, K key, V oldValue, V newValue);
    public void entryPropertyChanged(ObservableMap<K, V> map, K key, V value, PropertyChangeEvent propertyChangeEvent);
//    
//    
//    /**
//     * Notification that the value of an existing key has changed.
//     *
//     * @param map the {@code ObservableMap} that changed
//     * @param key the key
//     * @param lastValue the previous value
//     */
//    public void mapKeyValueChanged(ObservableMap map, Object key,
//                                   Object lastValue);
//
//    /**
//     * Notification that a key has been added.
//     *
//     * @param map the {@code ObservableMap} that changed
//     * @param key the key
//     */
//    public void mapKeyAdded(ObservableMap map, Object key);
//
//    /**
//     * Notification that a key has been removed
//     *
//     * @param map the {@code ObservableMap} that changed
//     * @param key the key
//     * @param value value for key before key was removed
//     */
//    public void mapKeyRemoved(ObservableMap map, Object key, Object value);
//
//    // PENDING: should we special case clear?
//    
//    public static final int NON_CONSECUTIVE_INDEXES = Integer.MIN_VALUE;
//    
//    public void elementsAdded(ObservableList<E> list, int startIndex, int length, List<E> newElements);
//    public void elementsRemoved(ObservableList<E> list, int startIndex, int length, List<E> oldElements);
//    public void elementReplaced(ObservableList<E> list, int index, E oldElement, E newElement);
//    public void elementPropertyChanged(ObservableList<E> list, int index, E element, PropertyChangeEvent proeprtyChangeEvent);
}
