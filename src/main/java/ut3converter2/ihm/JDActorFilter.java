/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JDActorFilter.java
 *
 * Created on 6 avr. 2009, 16:53:51
 */

package ut3converter2.ihm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ut3converter2.tools.T3DLevelAnalyser;
import ut3converter2.tools.T3DLevelClassFilter;

/**
 *
 * @author Hyperion
 */
public class JDActorFilter extends javax.swing.JDialog {

    File t3dinfile;
    File t3doutfile;

    /** Creates new form JDActorFilter */
    public JDActorFilter(java.awt.Frame parent,File T3DFile) {
        super(parent);
        initComponents();
        jlblInputT3DFile.setText(T3DFile.getAbsolutePath());
        t3dinfile = T3DFile;
        T3DLevelAnalyser ta = null;
        try {
            ta = new T3DLevelAnalyser(t3dinfile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JDActorFilter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JDActorFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadClassesFoundList(ta.getHmactors());
        t3doutfile = new File(T3DFile.getParent()+"\\"+T3DFile.getName().split("\\.")[0]+"_FLT.t3d");
        jlblOutputT3DFile.setText(t3doutfile.getAbsolutePath());
    }

    private void loadClassesFoundList(HashMap hmactors)
    {
        Iterator it = hmactors.keySet().iterator();
        ArrayList altmp = new ArrayList();
        while(it.hasNext())
        {
            altmp.add(it.next());
        }
        jListClassesFound.setListData(altmp.toArray());

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jlblInputT3DFile = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jlblOutputT3DFile = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpClassesFound = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListClassesFound = new javax.swing.JList();
        jbtnAddBasicCl = new javax.swing.JButton();
        jtfExcludeClasses = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtfIncludeClasses = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jbtnAddAll = new javax.swing.JButton();
        jBtnFilter = new javax.swing.JButton();
        jpNumActor = new javax.swing.JPanel();
        jcbCopyAfter = new javax.swing.JCheckBox();
        jtfactornum = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        exptofilter = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("T3D Level Filter");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("T3D Level Files"));

        jLabel3.setText("Input:");

        jlblInputT3DFile.setText("-");

        jLabel5.setText("Output:");

        jlblOutputT3DFile.setText("-");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlblInputT3DFile, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jlblOutputT3DFile, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlblInputT3DFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlblOutputT3DFile)
                .addContainerGap())
        );

        jpClassesFound.setBorder(javax.swing.BorderFactory.createTitledBorder("Classes Found"));

        jListClassesFound.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Brush", "Light" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListClassesFound.setDragEnabled(true);
        jScrollPane1.setViewportView(jListClassesFound);

        jbtnAddBasicCl.setText("Add Basic Classes");
        jbtnAddBasicCl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAddBasicClActionPerformed(evt);
            }
        });

        jLabel2.setText("Classes to exclude (e.g.: \"Light,AmbientSound\"):");

        jtfIncludeClasses.setText("All");
        jtfIncludeClasses.setDragEnabled(true);

        jLabel1.setText("Classes to include (by default will accept all):");

        jbtnAddAll.setText("Add all");
        jbtnAddAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAddAllActionPerformed(evt);
            }
        });

        jBtnFilter.setText("Filter");
        jBtnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpClassesFoundLayout = new javax.swing.GroupLayout(jpClassesFound);
        jpClassesFound.setLayout(jpClassesFoundLayout);
        jpClassesFoundLayout.setHorizontalGroup(
            jpClassesFoundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpClassesFoundLayout.createSequentialGroup()
                .addGroup(jpClassesFoundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpClassesFoundLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpClassesFoundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtfIncludeClasses, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                            .addComponent(jtfExcludeClasses, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                            .addGroup(jpClassesFoundLayout.createSequentialGroup()
                                .addComponent(jbtnAddBasicCl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jbtnAddAll))))
                    .addGroup(jpClassesFoundLayout.createSequentialGroup()
                        .addGap(231, 231, 231)
                        .addComponent(jBtnFilter)))
                .addContainerGap())
            .addGroup(jpClassesFoundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpClassesFoundLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jpClassesFoundLayout.setVerticalGroup(
            jpClassesFoundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpClassesFoundLayout.createSequentialGroup()
                .addContainerGap(143, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfIncludeClasses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(11, 11, 11)
                .addComponent(jtfExcludeClasses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpClassesFoundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnAddBasicCl)
                    .addComponent(jbtnAddAll))
                .addGap(36, 36, 36)
                .addComponent(jBtnFilter))
            .addGroup(jpClassesFoundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpClassesFoundLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(212, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Class Filter", jpClassesFound);

        jcbCopyAfter.setText("Copy after actor #");

        jtfactornum.setText("0");

        javax.swing.GroupLayout jpNumActorLayout = new javax.swing.GroupLayout(jpNumActor);
        jpNumActor.setLayout(jpNumActorLayout);
        jpNumActorLayout.setHorizontalGroup(
            jpNumActorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpNumActorLayout.createSequentialGroup()
                .addComponent(jcbCopyAfter, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfactornum, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(367, Short.MAX_VALUE))
        );
        jpNumActorLayout.setVerticalGroup(
            jpNumActorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpNumActorLayout.createSequentialGroup()
                .addContainerGap(177, Short.MAX_VALUE)
                .addGroup(jpNumActorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbCopyAfter)
                    .addComponent(jtfactornum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(154, 154, 154))
        );

        jTabbedPane1.addTab("Num Actor", jpNumActor);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jButton1.setText("Filter");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1, java.awt.BorderLayout.SOUTH);
        jPanel4.add(exptofilter, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jPanel4.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("String Filter", jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filter(int mode){
        T3DLevelClassFilter tcf = null;

        try {
            if(mode == T3DLevelClassFilter.MODE_DELLINE_WITHEXP){
                String line = exptofilter.getText();
                tcf = new T3DLevelClassFilter(t3dinfile, t3doutfile, line);
                tcf.filter();
                showFilterRatio(tcf);
            } else {
                this.dispose();
                this.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void closeThisPanel(){
        
    }
    private void showFilterRatio(T3DLevelClassFilter tcf){
        if(tcf!=null){
             JOptionPane.showMessageDialog(this, "<html>The T3D File has been correctly filtered!:<br>" +
                        "<li>Total Actors: "+tcf.getNumtotalactors()+"</li>" +
                        "<li>Num actors Copied: "+tcf.getNumclassescopied()+"</li>" +
                        "<li>Num actors Filtered: "+tcf.getNumclassesexcluded()+"</li>" +
                        "<li>Ratio: "+(tcf.getNumclassescopied()*100/tcf.getNumtotalactors())+" %</li></html>","Filter OK",JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private void jBtnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnFilterActionPerformed
        if(jtfIncludeClasses.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "You haven't set any classes to copy!", "Error: no included classes set!", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            T3DLevelClassFilter tcf = new T3DLevelClassFilter(t3dinfile, t3doutfile, jtfExcludeClasses.getText(), jtfIncludeClasses.getText());
            if(this.jcbCopyAfter.isSelected())
            {
                tcf.setNumActorToCopyAfter(Integer.valueOf(jtfactornum.getText()));
            }
            try {
                tcf.filter();
                JOptionPane.showMessageDialog(this, "<html>The T3D File has been correctly filtered!:<br>" +
                        "<li>Total Actors: "+tcf.getNumtotalactors()+"</li>" +
                        "<li>Num actors Copied: "+tcf.getNumclassescopied()+"</li>" +
                        "<li>Num actors Filtered: "+tcf.getNumclassesexcluded()+"</li>" +
                        "<li>Ratio: "+(tcf.getNumclassescopied()*100/tcf.getNumtotalactors())+" %</li></html>","Filter OK",JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                this.setVisible(false);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(JDActorFilter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(JDActorFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jBtnFilterActionPerformed

    private void jbtnAddBasicClActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAddBasicClActionPerformed

        jtfIncludeClasses.setText("Brush,Light,AmbientSound");
    }//GEN-LAST:event_jbtnAddBasicClActionPerformed

    private void jbtnAddAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAddAllActionPerformed
        jtfIncludeClasses.setText("All");
    }//GEN-LAST:event_jbtnAddAllActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        filter(T3DLevelClassFilter.MODE_DELLINE_WITHEXP);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField exptofilter;
    private javax.swing.JButton jBtnFilter;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jListClassesFound;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jbtnAddAll;
    private javax.swing.JButton jbtnAddBasicCl;
    private javax.swing.JCheckBox jcbCopyAfter;
    private javax.swing.JLabel jlblInputT3DFile;
    private javax.swing.JLabel jlblOutputT3DFile;
    private javax.swing.JPanel jpClassesFound;
    private javax.swing.JPanel jpNumActor;
    private javax.swing.JTextField jtfExcludeClasses;
    private javax.swing.JTextField jtfIncludeClasses;
    private javax.swing.JTextField jtfactornum;
    // End of variables declaration//GEN-END:variables

}
