/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.json;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 *
 * @author Patrick Angle
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@objectType")
public interface JsonableObject {
//    @JsonProperty("@objectType")
//    public default String jacksonTypeInfoObjectType() {
//        return this.getClass().getSimpleName();
//    }
}
