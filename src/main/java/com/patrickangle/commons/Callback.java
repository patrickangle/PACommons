/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons;

/**
 *
 * @author Patrick Angle
 */
public interface Callback<T> {
    public void perform(T object);
}
