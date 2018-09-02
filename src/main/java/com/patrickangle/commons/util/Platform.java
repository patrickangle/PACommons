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
package com.patrickangle.commons.util;

/**
 *
 * @author Patrick Angle
 */
public class Platform {
    public static String jreVender() {
        return System.getProperty("java.vendor");
    }
    
    public static String jreVersion() {
        return System.getProperty("java.version");
    }
    
    public static String jvmName() {
        return System.getProperty("java.vm.name");
    }
    
    public static String jvmVender() {
        return System.getProperty("java.vm.vendor");
    }
    
    public static String jvmVersion() {
        return System.getProperty("java.vm.version");
    }
    
    public static int totalMemory() {
        return (int) ((double)Runtime.getRuntime().totalMemory()/1024.0/1024.0);
    }
    
    public static int freeMemory() {
        return (int) ((double)Runtime.getRuntime().freeMemory()/1024.0/1024.0);
    }
    
    public static int maxMemory() {
        return (int) ((double)Runtime.getRuntime().maxMemory()/1024.0/1024.0);
    }
    
    public static int availableProcessorCores() {
        return Runtime.getRuntime().availableProcessors();
    }
    
    public static String systemDescriptor() {
        return "Jave Runtime " + jreVender() + " " + jreVersion() + "\n"
                + "" + jvmName() + " " + jvmVender() + " " + jvmVersion() + "\n"
                + "" + OperatingSystems.current().getName() + " " + OperatingSystems.current().getVersion() + " (" + OperatingSystems.current().getArchitecture() + ")\n"
                + "Available Cores: " + availableProcessorCores() + "\n"
                + "Memory Usage: " + (totalMemory() - freeMemory()) + "mb / " + totalMemory() + "mb / " + maxMemory() + "mb";
    }
    
    public static String shortSystemDescriptor() {
        return jvmName() + " " + jvmVender() + " " + jvmVersion() + "\n"
                + "" + OperatingSystems.current().getName() + " " + OperatingSystems.current().getVersion() + " (" + OperatingSystems.current().getArchitecture() + ")\n";
    }
}


//Jave Runtime Oracle Corporation 10.0.1
//Java HotSpot(TM) 64-Bit Server VM "Oracle Corporation" 10.0.1+10
//mac os x 10.14 (null)
//Available Cores: 24
//Memory Usage: 22mb / 512mb / 8192mb
