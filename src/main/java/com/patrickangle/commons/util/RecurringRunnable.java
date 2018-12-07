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
import com.patrickangle.commons.logging.Logging;
import com.patrickangle.commons.observable.interfaces.PropertyChangeObservableBase;
import java.util.Arrays;

/**
 *
 * @author Patrick Angle
 */
public class RecurringRunnable extends PropertyChangeObservableBase implements Runnable {
    private static final int NANOSECONDS_IN_SECOND = 1_000_000_000;
    private static final int NANOSECONDS_IN_MILLISECOND = 1_000_000;
    private static final int DELAY_SKIPS_BEFORE_YIELD = 16;
    private static final int FRAME_RATE_SAMPLES_TO_STORE = 60;
    
    protected long currentFrame;
    protected Callback<Long> frameCallback;
    protected long targetPeriodLength;
    
    protected boolean running;
    
    protected final int[] frameRateSamples = new int[FRAME_RATE_SAMPLES_TO_STORE];
    protected int currentFPS;
    
    public RecurringRunnable(int targetFPS, Callback<Long> frameCallback) {
        this.frameCallback = frameCallback;
        this.targetPeriodLength = Math.round((double)NANOSECONDS_IN_SECOND / (double)targetFPS);
        this.running = false;
        this.currentFPS = 0;
        
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        
        // Pre-populate all frame samples with the target rate in order to more quickly be able to establish the currentFPS
        Arrays.fill(frameRateSamples, targetFPS);
    }
    
    public void setTargetFPS(int targetFPS) {
        this.targetPeriodLength = NANOSECONDS_IN_SECOND / targetFPS;
        System.out.println(targetPeriodLength);
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
    
    private void updateCurrentFPS(int latestFPS) {
        this.frameRateSamples[(int)(currentFrame % frameRateSamples.length)] = latestFPS;
        int sum = 0;
        for (int i : this.frameRateSamples) {
            sum += i;
        }
        int average = sum / frameRateSamples.length;
        
        this.setCurrentFPS(average);
    }
    
    @Override
    public void run() {
        newRun();
//        this.running = true;
//        this.currentFrame = 0;
//        this.setCurrentFPS((int) (NANOSECONDS_IN_SECOND / targetPeriodLength));
//        
//        long beforeTime, afterTime, timeDiff, sleepTime, frameEndTime;
//        long overSleepTime = 0L;
//        int delaySkips = 0;
//        
//        while(running) {
//            beforeTime = System.nanoTime();
//            this.currentFrame++;
//            
//            this.frameCallback.perform(this.currentFrame);
//            
//            afterTime = System.nanoTime();
//            timeDiff = afterTime - beforeTime;
//            sleepTime = (targetPeriodLength - timeDiff) - overSleepTime;
//            
//            if (sleepTime > 0) {
//                // Frame finished in time to rest.
//                try {
//                    Thread.sleep(sleepTime / NANOSECONDS_IN_MILLISECOND);
//                } catch (InterruptedException interruptedException) {
//                    overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
//                }
//            } else {
//                // Frame finished later than needed.
//                overSleepTime = 0L;
//                if (++delaySkips >= DELAY_SKIPS_BEFORE_YIELD) {
//                    Thread.yield();
//                    delaySkips = 0;
//                }
//            }
//
//            frameEndTime = System.nanoTime();
//            this.updateCurrentFPS(Math.round((float)NANOSECONDS_IN_SECOND / (float)(frameEndTime - beforeTime)));
//        }
    }
    
    public void newRun() {
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
//                System.out.println("FPS: " + framesThisSecond);
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
            
            runningTargetPeriodLength = targetPeriodLength - ((now - frameStartTime) - targetPeriodLength);
        }
    }
    
    public void stop() {
        this.running = false;
        this.setCurrentFPS(0);
    }
    
}