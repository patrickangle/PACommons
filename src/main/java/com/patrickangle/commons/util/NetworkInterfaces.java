/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.patrickangle.commons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patrick Angle
 */
public class NetworkInterfaces {
    private static ArrayList<CrossPlatformNetworkInterface> availableNetworkInterfaces;
    public static void updateAvailableNetworkInterfaces() {
        availableNetworkInterfaces = new ArrayList<>();
        
        WindowsNetworkInterface[] windowsInterfaces = getWindowsNetworkInterfaces();
        
        try {
            for (NetworkInterface netint : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (netint.getInetAddresses().hasMoreElements()) {
                    // Interface has at least one address, and is therefore available.
                    
                    CrossPlatformNetworkInterface networkInterface = new CrossPlatformNetworkInterface(netint.getDisplayName(), netint.getName(), hardwareAddressByteArrayToString(netint.getHardwareAddress()), Collections.list(netint.getInetAddresses()).toArray(new InetAddress[0]));
                    
                    for (WindowsNetworkInterface windowsInterface : windowsInterfaces) {
                        if (networkInterface.getMacAddress().equals(windowsInterface.getMacAddress())) {
                            networkInterface.setDisplayName(windowsInterface.getConnectionName());
                        }
                    }
                    
                    availableNetworkInterfaces.add(networkInterface);
                    
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(NetworkInterfaces.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static CrossPlatformNetworkInterface[] getAvailableNetworkInterfaces() {
        if (availableNetworkInterfaces == null) {
            updateAvailableNetworkInterfaces();
        }
        
        return availableNetworkInterfaces.toArray(new CrossPlatformNetworkInterface[0]);
    }

    private static String hardwareAddressByteArrayToString(byte[] hardwareAddress) {
        if (hardwareAddress == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hardwareAddress.length; i++) {
            sb.append(String.format("%02X%s", hardwareAddress[i], (i < hardwareAddress.length - 1) ? "-" : ""));
        }
        return sb.toString();
    }
    
    private static WindowsNetworkInterface[] getWindowsNetworkInterfaces() {
        ArrayList<WindowsNetworkInterface> returnInterfaces = new ArrayList<>();
        
        if (OperatingSystems.current() == OperatingSystems.Windows) {
            try {
                Process getMacProcess = Runtime.getRuntime().exec("getmac /fo csv /v");
                
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(getMacProcess.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(getMacProcess.getErrorStream()));
                
                boolean isFirst = true;
                String s = null;
                while ((s = stdInput.readLine()) != null) {
                    if (!isFirst) {                        
                        String[] params = s.split(",");
                        for (int i = 0; i < params.length; i++) {
                            if (params[i].startsWith("\"")) {
                                params[i] = params[i].substring(1);
                            }
                            if (params[i].endsWith("\"")) {
                                params[i] = params[i].substring(0, params[i].length() - 1);
                            }
                        }
                        
                        returnInterfaces.add(new WindowsNetworkInterface(params[0], params[1], params[2], params[3]));
                    }
                    
                    isFirst = false;
                }
                
                while ((s = stdError.readLine()) != null) {
                    System.err.println(s);
                }
            } catch (IOException ex) {
                Exceptions.raiseThrowableToUser(ex);
            }
        }
        
        return returnInterfaces.toArray(new WindowsNetworkInterface[0]);
    }

    private static class WindowsNetworkInterface {
        private String connectionName;
        private String networkAdapter;
        private String macAddress;
        private String transportName;

        public WindowsNetworkInterface(String connectionName, String networkAdapter, String macAddress, String transportName) {
            this.connectionName = connectionName;
            this.networkAdapter = networkAdapter;
            this.macAddress = macAddress;
            this.transportName = transportName;
        }

        @Override
        public String toString() {
            return connectionName + "|" + networkAdapter + "|" + macAddress + "|" + transportName;
        }

        public String getConnectionName() {
            return connectionName;
        }

        public void setConnectionName(String connectionName) {
            this.connectionName = connectionName;
        }

        public String getNetworkAdapter() {
            return networkAdapter;
        }

        public void setNetworkAdapter(String networkAdapter) {
            this.networkAdapter = networkAdapter;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public String getTransportName() {
            return transportName;
        }

        public void setTransportName(String transportName) {
            this.transportName = transportName;
        }
        
        
    }

    public static class CrossPlatformNetworkInterface {
        private String displayName;
        private String internalName;
        private String macAddress;
        private InetAddress[] inetAddresses;

        public CrossPlatformNetworkInterface(String displayName, String internalName, String macAddress, InetAddress[] inetAddresses) {
            this.displayName = displayName;
            this.internalName = internalName;
            this.macAddress = macAddress;
            this.inetAddresses = inetAddresses;
        }
        
        @Override
        public String toString() {
            return displayName + "|" + internalName + "|" + macAddress + "|" + Arrays.toString(inetAddresses);
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getInternalName() {
            return internalName;
        }

        public void setInternalName(String internalName) {
            this.internalName = internalName;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public InetAddress[] getInetAddresses() {
            return inetAddresses;
        }

        public void setInetAddresses(InetAddress[] inetAddresses) {
            this.inetAddresses = inetAddresses;
        }
        
        
    }
}
