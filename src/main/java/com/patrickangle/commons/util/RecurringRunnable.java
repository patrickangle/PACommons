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
package com.patrickangle.commons.util;

import com.patrickangle.commons.Callback;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservableBase;

/**
 *
 * @author Patrick Angle
 */
public class RecurringRunnable extends PropertyChangeObservableBase implements Runnable {

    private static final int NANOSECONDS_IN_SECOND = 1_000_000_000;
    private static final int NANOSECONDS_IN_MILLISECOND = 1_000_000;

    protected long currentFrame;
    protected Callback<Long> frameCallback;
    protected long targetPeriodLength;

    protected boolean running;

    protected int currentFPS;

    public RecurringRunnable(int targetFPS, Callback<Long> frameCallback) {
        this.frameCallback = frameCallback;
        this.targetPeriodLength = Math.round((double) NANOSECONDS_IN_SECOND / (double) targetFPS);
        this.running = false;
        this.currentFPS = 0;

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
    }

    public void setTargetFPS(int targetFPS) {
        this.targetPeriodLength = NANOSECONDS_IN_SECOND / targetFPS;
    }

    public int getCurrentFPS() {
        return currentFPS;
    }

    public void setCurrentFPS(int currentFPS) {
        int oldCurrentFPS = this.currentFPS;
        this.currentFPS = currentFPS;
        this.propertyChangeSupport.firePropertyChange("currentFPS", oldCurrentFPS, this.currentFPS);
    }

    public boolean isRunning() {
        return running;
    }

    public long getCurrentFrame() {
        return currentFrame;
    }

    @Override
    public void run() {
        this.running = true;
        this.currentFrame = 0;
        this.setCurrentFPS((int) Math.round((double) NANOSECONDS_IN_SECOND / (double) targetPeriodLength));

        long frameStartTime;
        long lastUpdateTime = System.nanoTime();
        long lastSecondTime = lastUpdateTime / NANOSECONDS_IN_SECOND;
        long thisSecondTime = lastSecondTime;
        int framesThisSecond = 0;
        long now;
        long runningTargetPeriodLength = targetPeriodLength;

        long defaultSleepTime = targetPeriodLength * 3 / 4;

        while (running) {
            frameStartTime = System.nanoTime();
            now = frameStartTime;
            currentFrame++;

            this.frameCallback.perform(currentFrame);

            lastUpdateTime = now;
            framesThisSecond++;

            thisSecondTime = lastUpdateTime / NANOSECONDS_IN_SECOND;
            if (thisSecondTime > lastSecondTime) {
                this.setCurrentFPS(framesThisSecond);
                framesThisSecond = 0;
                lastSecondTime = thisSecondTime;
            }

            while (now - lastUpdateTime < runningTargetPeriodLength) {
                Thread.yield();
                if (now - lastUpdateTime < defaultSleepTime) {
                    try {
                        Thread.sleep(defaultSleepTime / NANOSECONDS_IN_MILLISECOND, (int) defaultSleepTime % NANOSECONDS_IN_MILLISECOND);
                    } catch (InterruptedException e) {

                    }
                }

                now = System.nanoTime();
            }
            
            runningTargetPeriodLength = ((runningTargetPeriodLength) + (targetPeriodLength - ((now - frameStartTime) - targetPeriodLength))) / 2;
        }
    }
    
    public void stop() {
        this.running = false;
        this.setCurrentFPS(0);
    }

}
