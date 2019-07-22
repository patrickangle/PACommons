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

import java.util.Locale;

/**
 * An enumeration representing different operating systems on which Java could
 * be running. You can get the current operating system by calling
 * OperatingSystems.current(). The first call will check system properties to
 * establish the current operating system, and subsequent calls will return a
 * cached value. It is highly unlikely that the operating system will change in
 * middle of running a program.
 *
 * @author patrickangle
 */
public enum OperatingSystems {
    Windows,
    Unix,
    PosixUnix,
    Solaris,
    Macintosh,
    Other,
    Unavailable;

    private static OperatingSystems cachedCurrent;

    private String name;
    private String version;
    private String architecture;
    private String bitness;

    /**
     * Returns the name of the operating system that was detected. This will
     * match what the System object returns in its `os.name` property, but in
     * lower case. Returns null if the operating system was obtained by a method
     * other than OperatingSystems.current().
     *
     * @return
     */
    public String getName() {
        return name;
    }

    protected void setName(String osName) {
        this.name = osName;
    }

    public String getVersion() {
        return version;
    }

    protected void setVersion(String version) {
        this.version = version;
    }

    public String getArchitecture() {
        return architecture;
    }

    protected void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getBitness() {
        return bitness;
    }

    protected void setBitness(String bitness) {
        this.bitness = bitness;
    }

    public static OperatingSystems current() {
        if (OperatingSystems.cachedCurrent != null) {
            return OperatingSystems.cachedCurrent;
        }

        OperatingSystems returnSystem = OperatingSystems.Other;

        String osName = System.getProperty("os.name");

        if (osName == null) {
            return OperatingSystems.Unavailable;
        }

        osName = osName.toLowerCase(Locale.ENGLISH);

        if (osName.contains("windows")) {
            returnSystem = OperatingSystems.Windows;
        } else if (osName.contains("linux")
                || osName.contains("mpe/ix")
                || osName.contains("freebsd")
                || osName.contains("irix")
                || osName.contains("digital unix")
                || osName.contains("unix")) {
            returnSystem = OperatingSystems.Unix;
        } else if (osName.contains("mac os")
                || osName.contains("mac")) {
            returnSystem = OperatingSystems.Macintosh;
        } else if (osName.contains("sun os")
                || osName.contains("sunos")
                || osName.contains("solaris")) {
            returnSystem = OperatingSystems.Solaris;
        } else if (osName.contains("hp-ux")
                || osName.contains("aix")) {
            returnSystem = OperatingSystems.PosixUnix;
        } else {
            returnSystem = OperatingSystems.Other;
        }
        returnSystem.setName(osName);
        returnSystem.setVersion(System.getProperty("os.version"));

        String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

        returnSystem.setArchitecture(arch != null ? arch : wow64Arch);

        returnSystem.setBitness(arch != null && arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64") ? "64" : "32");

        OperatingSystems.cachedCurrent = returnSystem;

        return returnSystem;
    }
}
