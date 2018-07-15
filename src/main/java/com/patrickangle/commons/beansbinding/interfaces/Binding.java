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
        READ_ONCE(false, false, true),
        WRITE_ONCE(false, false, false),
        READ_ONLY(true, false, true),
        WRITE_ONLY(false, true, false),
        READ_WRITE(true, true, true);

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
}
