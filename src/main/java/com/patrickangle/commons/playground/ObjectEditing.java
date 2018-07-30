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
package com.patrickangle.commons.playground;

import com.patrickangle.commons.beansbinding.BasicBinding;
import com.patrickangle.commons.beansbinding.interfaces.Binding;
import com.patrickangle.commons.beansbinding.swing.boundfields.JListBoundField;
import com.patrickangle.commons.beansbinding.swing.models.ObservableListModel;
import com.patrickangle.commons.objectediting.ObjectEditingDialog;
import com.patrickangle.commons.observable.collections.ObservableCollections;
import com.patrickangle.commons.util.Exceptions;
import com.patrickangle.commons.util.LookAndFeels;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author patrickangle
 */
public class ObjectEditing extends javax.swing.JFrame {
    List<FunPojo> funPojos = ObservableCollections.concurrentObservableList(Arrays.asList(new FunPojo(), new FunPojo(), new FunPojo(), new FunPojo(), new FunPojo(), new FunPojo(), new FunPojo(), new FunPojo()));
    
    
    /**
     * Creates new form ObjectEditing
     */
    public ObjectEditing() {
        initComponents();
        LookAndFeels.useSystemLookAndFeel();
        Exceptions.installUncaughtExceptionHandler();
        Binding binding1 = new BasicBinding(this, "funPojos", jList1.getModel(), "items", Binding.UpdateStrategy.READ_WRITE);
        binding1.setBound(true);
        
        Binding binding2 = new BasicBinding(objectEditingPanel1, "editingObject", jList1, JListBoundField.SYNTHETIC_FIELD_SELECTED_VALUE, Binding.UpdateStrategy.WRITE_ONLY);
        binding2.setBound(true);
        
//        throw new RuntimeException("Just a harmless test, worry not!");
    }

    public List<FunPojo> getFunPojos() {
        return funPojos;
    }

    public void setFunPojos(List<FunPojo> funPojos) {
        this.funPojos = funPojos;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        objectEditingPanel1 = new com.patrickangle.commons.objectediting.ObjectEditingPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setDividerLocation(500);

        jList1.setModel(new ObservableListModel<FunPojo>(FunPojo.class)
        );
        jScrollPane1.setViewportView(jList1);

        jSplitPane1.setLeftComponent(jScrollPane1);
        jSplitPane1.setRightComponent(objectEditingPanel1);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new ObjectEditingDialog(this, true, funPojos.get(0)).setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        LookAndFeels.useSystemLookAndFeel();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ObjectEditing().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JList<FunPojo> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private com.patrickangle.commons.objectediting.ObjectEditingPanel objectEditingPanel1;
    // End of variables declaration//GEN-END:variables
}
