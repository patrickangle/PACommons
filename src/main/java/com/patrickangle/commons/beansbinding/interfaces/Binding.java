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
package com.patrickangle.commons.beansbinding.interfaces;

/**
 *
 * @author patrickangle
 * @param B Type of Back object
 * @param F Type of Front object
 */
public interface Binding {

    public boolean isBound();

    public void setBound(boolean bound);

    public enum UpdateStrategy {
        /**
         * Initially reads from the back property to write the front property, and does not bind the objects any further.
         */
        READ_ONCE(false, false, true),
        
        /**
         * Initially reads from the front property to write the back property, and does not bind the objects any further.
         */
        WRITE_ONCE(false, false, false),
        
        /**
         * Initially reads from the back property to write the front property, and then tracks the back property to update the front property when the back property changes.
         */
        READ_ONLY(true, false, true),
        
        /**
         * Initially reads from the front property to write the back property, and then tracks the front property to update the back property when the front property changes.
         */
        WRITE_ONLY(false, true, false),
        
        /**
         * Initially reads from the back property to write the front property, and then tracks both the front and back property to update each other when either changes. This is the most common binding for connecting interface component models to backing data.
         */
        READ_WRITE(true, true, true),
        
        /**
         * Initially reads from the front property to write the back property, and then tracks the back property to update the front property when the back property changes.
         */
        WRITE_FIRST_READ_AFTER(false, true, true),
        
        /**
         * Initially reads from the back property to write the front property, and then tracks the front property to update the back property when the front property changes.
         */
        READ_FIRST_WRITE_AFTER(true, false, false);

        private final boolean forward;
        private final boolean backward;

        private final boolean forwardOnce;

        UpdateStrategy(boolean forward, boolean backward, boolean forwardOnce) {
            this.forward = forward;
            this.backward = backward;
            this.forwardOnce = forwardOnce;
        }

        public boolean isForward() {
            return forward;
        }

        public boolean isBackward() {
            return backward;
        }

        public boolean isForwardOnce() {
            return forwardOnce;
        }
    }
    
    public interface Converter<BF, FF> {
        public FF convertForward(BF object);
        public BF convertBackward(FF object);
    }
    
    public abstract class ForwardConverter<BF, FF> implements Converter<BF, FF> {
        public BF convertBackward(FF object) {
            throw new UnsupportedOperationException("Illegal backward conversion.");
        }
    }
}
