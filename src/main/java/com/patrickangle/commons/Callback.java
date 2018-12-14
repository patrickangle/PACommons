/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons;

import java.util.function.Consumer;

/**
 * Originally its own interface, Callback<T> is now defined to be compatible with Consumer<T> in order to better take advantage of core Java APIs.
 * @author Patrick Angle
 */
public interface Callback<T> extends Consumer<T> {

    @Override
    public default void accept(T t) {
        perform(t);
    }
    
    public void perform(T object);
}
