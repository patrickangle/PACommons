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

import com.patrickangle.commons.logging.Logging;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Patrick Angle
 */
public class LocalNetworkInterfaces {
    public static class LocalNetworkInterface {
        public static enum IpAddressVersion {
            IPv4,
            IPv6;
        }
        
        String humanReadableName;
        String address;
        short subnetMask;
        String networkAddress;
        IpAddressVersion addressVersion;
        
        private LocalNetworkInterface(InterfaceAddress interfaceAddress) {
            this(interfaceAddress.getAddress() + "\\" + interfaceAddress.getNetworkPrefixLength(), interfaceAddress);
        }
        
        private LocalNetworkInterface(String humanReadableName, InterfaceAddress interfaceAddress) {
            this.humanReadableName = humanReadableName;
            this.address = interfaceAddress.getAddress().getHostAddress();
            this.subnetMask = interfaceAddress.getNetworkPrefixLength();
            
            
            StringBuilder expandedSubnetMaskString = new StringBuilder();
                for (int i = 0; i < subnetMask; i++) {
                    expandedSubnetMaskString.insert(0, "1");
                }
                while(expandedSubnetMaskString.length() < 128) {
                    expandedSubnetMaskString.append("0");
                }

            if (interfaceAddress.getAddress() instanceof Inet4Address) {
                Inet4Address inetAddress = (Inet4Address) interfaceAddress.getAddress();
                
                byte[] expandedSubnetMask = new byte[4];
                String clippedExpandedSubnetMaskString = expandedSubnetMaskString.substring(0, 32);
                for (int i = 0; i < expandedSubnetMask.length; i++) {
                    expandedSubnetMask[i] = (byte)Integer.parseUnsignedInt(clippedExpandedSubnetMaskString.substring((i * 8), ((i * 8) + 8)), 2);
                }
                
                byte[] addressBytes = Arrays.copyOf(inetAddress.getAddress(), 4);

                for (int i = 0; i < 4; i++) {
                    addressBytes[i] &= expandedSubnetMask[i]; 
                }
                
                try {
                    this.networkAddress = InetAddress.getByAddress(addressBytes).getHostAddress();
                    this.addressVersion = IpAddressVersion.IPv4;
                } catch (UnknownHostException ex) {
                    Logging.exception(LocalNetworkInterface.class, ex);
                }
                
                
            } else if (interfaceAddress.getAddress() instanceof Inet6Address) {
                Inet6Address inetAddress = (Inet6Address) interfaceAddress.getAddress();
                
                byte[] expandedSubnetMask = new byte[16];
                String clippedExpandedSubnetMaskString = expandedSubnetMaskString.substring(0, 128);
                for (int i = 0; i < expandedSubnetMask.length; i++) {
                    expandedSubnetMask[i] = (byte)Integer.parseUnsignedInt(clippedExpandedSubnetMaskString.substring((i * 8), ((i * 8) + 8)), 2);
                }
                
                byte[] addressBytes = Arrays.copyOf(inetAddress.getAddress(), 16);

                for (int i = 0; i < 16; i++) {
                    addressBytes[i] &= expandedSubnetMask[i];
                }

                try {
                    this.networkAddress = InetAddress.getByAddress(addressBytes).getHostAddress();
                    this.addressVersion = IpAddressVersion.IPv6;
                } catch (UnknownHostException ex) {
                    Logging.exception(LocalNetworkInterface.class, ex);
                }
            } else {
                this.networkAddress = this.address;
            }
        }

        public String getHumanReadableName() {
            return humanReadableName;
        }

        public String getAddress() {
            return address;
        }

        public short getSubnetMask() {
            return subnetMask;
        }

        public String getNetworkAddress() {
            return networkAddress;
        }

        public IpAddressVersion getAddressVersion() {
            return addressVersion;
        }

        @Override
        public String toString() {
            return "LocalNetworkInterface{" + "humanReadableName=" + humanReadableName + ", address=" + address + ", subnetMask=" + subnetMask + ", networkAddress=" + networkAddress + ", addressVersion=" + addressVersion + '}';
        }
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

        public String getConnectionName() {
            return connectionName;
        }

        public String getNetworkAdapter() {
            return networkAdapter;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public String getTransportName() {
            return transportName;
        }
    }
    
    private static List<LocalNetworkInterface> availableInterfaces;
    
    public static List<LocalNetworkInterface> getAvailableInterfaces() {
        if (LocalNetworkInterfaces.availableInterfaces == null) {
            LocalNetworkInterfaces.updateAvailableInterfaces();
        }
        return LocalNetworkInterfaces.availableInterfaces;
    }
    
    public static void updateAvailableInterfaces() {
        List<LocalNetworkInterface> availableInterfaces = new ArrayList<LocalNetworkInterface>();
        
        Map<String, WindowsNetworkInterface> windowsInterfaces = windowsNetworkInterfaces();
        
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                String macAddress = LocalNetworkInterfaces.hardwareAddressByteArrayToString(networkInterface.getHardwareAddress());
                
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    String humanReadableName = networkInterface.getDisplayName();
                    
                    if (windowsInterfaces.containsKey(macAddress)) {
                        humanReadableName = windowsInterfaces.get(macAddress).getConnectionName();
                    }
                    LocalNetworkInterface newInterface = new LocalNetworkInterface(humanReadableName, interfaceAddress);
                    availableInterfaces.add(newInterface);
                }
            }
        } catch (SocketException ex) {
            Logging.exception(LocalNetworkInterfaces.class, ex);
        }
        
        LocalNetworkInterfaces.availableInterfaces = availableInterfaces;
    }
    
    private static Map<String, WindowsNetworkInterface> windowsNetworkInterfaces() {
        Map<String, WindowsNetworkInterface> returnInterfaces = new HashMap<String, WindowsNetworkInterface>();
        
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
                        
                        returnInterfaces.put(params[2], new WindowsNetworkInterface(params[0], params[1], params[2], params[3]));
                    }
                    
                    isFirst = false;
                }
                
                while ((s = stdError.readLine()) != null) {
                    Logging.error(LocalNetworkInterfaces.class, s);
                }
            } catch (IOException ex) {
                Logging.exception(LocalNetworkInterfaces.class, ex);
            }
        }
        
        return returnInterfaces;
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
    
    // TODO: Implement the reverse of this function
    public static String uncompressIPv6Address(String ipv6Address){
         StringBuilder address = new StringBuilder(ipv6Address);
        // Store the location where you need add zeroes that were removed during uncompression
        int tempCompressLocation=address.indexOf("::");
         
        //if address was compressed and zeroes were removed, remove that marker i.e "::"
        if(tempCompressLocation!=-1){
            address.replace(tempCompressLocation,tempCompressLocation+2,":");
        }
         
        //extract rest of the components by splitting them using ":"
        String[] addressComponents=address.toString().split(":");
         
        for(int i=0;i<addressComponents.length;i++){
            StringBuilder uncompressedComponent=new StringBuilder("");
            for(int j=0;j<4-addressComponents[i].length();j++){
                 
                //add a padding of the ignored zeroes during compression if required
                uncompressedComponent.append("0");
                 
            }
            uncompressedComponent.append(addressComponents[i]);
             
            //replace the compressed component with the uncompressed one
            addressComponents[i]=uncompressedComponent.toString();
        }
         
         
        //Iterate over the uncompressed address components to add the ignored "0000" components depending on position of "::"
        ArrayList<String> uncompressedAddressComponents=new ArrayList<String>();
         
        for(int i=0;i<addressComponents.length;i++){
            if(i==tempCompressLocation/4){
                for(int j=0;j<8-addressComponents.length;j++){
                    uncompressedAddressComponents.add("0000");
                }
            }
            uncompressedAddressComponents.add(addressComponents[i]);
             
        }
         
        //iterate over the uncomopressed components to append and produce a full address
        StringBuilder uncompressedAddress=new StringBuilder("");
        Iterator it=uncompressedAddressComponents.iterator();
        while (it.hasNext()) {
            uncompressedAddress.append(it.next().toString());
            uncompressedAddress.append(":");
        }
        uncompressedAddress.replace(uncompressedAddress.length()-1, uncompressedAddress.length(), "");
        return uncompressedAddress.toString();
    }
}
