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
package com.patrickangle.commons.types;

import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.BindingGroup;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.swing.models.ObservableComboBoxModel;
import com.patrickangle.commons.objectediting.util.ObjectFieldEditorFactory;
import com.patrickangle.commons.observable.collections.ObservableArrayList;
import com.patrickangle.commons.observable.collections.ObservableCollections;
import com.patrickangle.commons.util.NetworkInterfaces;
import java.awt.Dimension;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author Patrick Angle
 */
public class LocalInterface extends RemoteAddress {
    
    public LocalInterface() {
        this.address = "0.0.0.0";
        this.port = 0;
    }
    
    @Override
    public ObjectFieldEditorFactory.ComponentReturn customObjectEditingComponent(BindingGroup bindingGroup) {
        NetworkInterfaces.CrossPlatformNetworkInterface[] networkInterfaces = NetworkInterfaces.availableNetworkInterfaces();
        ArrayList<LocalInterfaceComboBoxItem> networkInterfaceItems = new ArrayList<>(networkInterfaces.length);
        
        int currentAddressItemIndex = -1;
        for (NetworkInterfaces.CrossPlatformNetworkInterface networkInterface : networkInterfaces) {
            for (InetAddress address : networkInterface.getInetAddresses()) {
                if (address.getHostAddress().equals(this.address)) {
                    currentAddressItemIndex = networkInterfaceItems.size();
                }
                
                networkInterfaceItems.add(new LocalInterfaceComboBoxItem(networkInterface.getDisplayName() + " (" + address.getHostAddress() + ")", address.getHostAddress()));
            }
        }
        networkInterfaceItems.add(0, new LocalInterfaceComboBoxItem("0.0.0.0"));
        
        JComboBox<LocalInterfaceComboBoxItem> interfaceEditor = new JComboBox<>(new ObservableComboBoxModel<>(new ObservableArrayList<>(networkInterfaceItems)));
        
        interfaceEditor.setEditable(true);
        interfaceEditor.setSelectedIndex(0);
        
        Binding binding = new BasicBinding(this, "address", interfaceEditor.getModel(), "selectedItem", Binding.UpdateStrategy.READ_WRITE, new LocalInterfaceComboBoxItemConverter());
        bindingGroup.add(binding);
        
        return new ObjectFieldEditorFactory.ComponentReturn(interfaceEditor, false);
    }
    
    public static class LocalInterfaceComboBoxItemConverter implements Binding.Converter<String, Object> {
        
        @Override
        public Object convertForward(String object) {
            return new LocalInterfaceComboBoxItem(object);
        }
        
        @Override
        public String convertBackward(Object object) {
            if (object instanceof String) {
                return (String) object;
            } else if (object instanceof LocalInterfaceComboBoxItem) {
                return ((LocalInterfaceComboBoxItem) object).getIpAddress();
            } else {
                return "0.0.0.0";
            }            
        }
        
    }

//    @Override
//    public JComponent getPropertyEditor() {
//        NetworkInterfaces.CrossPlatformNetworkInterface[] networkInterfaces = NetworkInterfaces.availableNetworkInterfaces();
//        ArrayList<LocalInterfaceComboBoxItem> networkInterfaceItems = new ArrayList<>(networkInterfaces.length);
//        
//        
//        int currentAddressItemIndex = -1;
//        for (NetworkInterfaces.CrossPlatformNetworkInterface networkInterface : networkInterfaces) {
//            for (InetAddress address : networkInterface.getInetAddresses()) {
//                if (address.getHostAddress().equals(this.address)) {
//                    currentAddressItemIndex = networkInterfaceItems.size();
//                }
//                
//                networkInterfaceItems.add(new LocalInterfaceComboBoxItem(networkInterface.getDisplayName() + " (" + address.getHostAddress() + ")", address.getHostAddress()));
//            }
//        }
//        networkInterfaceItems.add(0, new LocalInterfaceComboBoxItem("0.0.0.0"));
//        BindingGroup bindingGroup = new BindingGroup();
//        
//        JComboBox<LocalInterfaceComboBoxItem> returnEditor = new JComboBox<>(networkInterfaceItems.toArray(new LocalInterfaceComboBoxItem[0]));
//        returnEditor.setEditable(true);
//        
//        Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, ELProperty.create("${address}"), returnEditor, BeanProperty.create("selectedItem"));
//        binding.setConverter(new LocalInterfaceComboBoxItemConverter());
//        
//        bindingGroup.addBinding(binding);
//        
//        bindingGroup.bind();
//        return returnEditor;
//    }
//    
//    private static class LocalInterfaceComboBoxItemConverter extends Converter<String, Object> {
//
//        @Override
//        public Object convertForward(String value) {
//            return new LocalInterfaceComboBoxItem(value);
//        }
//
//        @Override
//        public String convertReverse(Object value) {
//            if (value instanceof String) {
//                return (String)value;
//            } else if (value instanceof LocalInterfaceComboBoxItem) {
//                return ((LocalInterfaceComboBoxItem)value).getIpAddress();
//            } else {
//                return "0.0.0.0";
//            }            
//        }
//        
//    }
    private static class LocalInterfaceComboBoxItem {

        private String visibleName;
        private String ipAddress;
        
        public LocalInterfaceComboBoxItem(String visibleName, String ipAddress) {
            this.visibleName = visibleName;
            this.ipAddress = ipAddress;
        }
        
        public LocalInterfaceComboBoxItem(String ipAddress) {
            this.ipAddress = ipAddress;
            
            if (ipAddress.equals("0.0.0.0")) {
                this.visibleName = "Automatic";
            }
            
            if (visibleName == null) {
                NetworkInterfaces.CrossPlatformNetworkInterface[] networkInterfaces = NetworkInterfaces.availableNetworkInterfaces();
                for (NetworkInterfaces.CrossPlatformNetworkInterface networkInterface : networkInterfaces) {
                    for (InetAddress address : networkInterface.getInetAddresses()) {
                        if (address.getHostAddress().equals(ipAddress)) {
                            this.visibleName = networkInterface.getDisplayName() + " (" + ipAddress + ")";
                        }
                    }
                }
            }
            
            if (visibleName == null) {
                this.visibleName = ipAddress;
            }
        }
        
        public String getVisibleName() {
            return visibleName;
        }
        
        public void setVisibleName(String visibleName) {
            this.visibleName = visibleName;
        }
        
        public String getIpAddress() {
            return ipAddress;
        }
        
        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }
        
        @Override
        public String toString() {
            return visibleName;
        }
        
        @Override
        public int hashCode() {
            int hash = 3;
            hash = 67 * hash + Objects.hashCode(this.visibleName);
            hash = 67 * hash + Objects.hashCode(this.ipAddress);
            return hash;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LocalInterfaceComboBoxItem other = (LocalInterfaceComboBoxItem) obj;
            if (!Objects.equals(this.visibleName, other.visibleName)) {
                return false;
            }
            if (!Objects.equals(this.ipAddress, other.ipAddress)) {
                return false;
            }
            return true;
        }
        
    }
}
