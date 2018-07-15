/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.observable.collections;

import java.util.List;

/**
 * 
 * @author patrickangle
 * @author sky
 */
public interface ObservableList<E> extends List<E> {
    /**
     * Adds a listener that is notified when the list changes.
     *
     * @param listener the listener to add
     */
    public void addObservableListListener(ObservableListListener listener);

    /**
     * Removes a listener.
     *
     * @param listener the listener to remove
     */
    public void removeObservableListListener(ObservableListListener listener);

    /**
     * Returns {@code true} if this list sends out notification when
     * the properties of an element change. This method may be used
     * to determine if a listener needs to be installed on each of
     * the elements of the list.
     *
     * @return {@code true} if this list sends out notification when
     *         the properties of an element change
     */
    public boolean supportsElementPropertyChanged();
}
