/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sosafe.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import sosafe.control.ViewControlMessage;
import sosafe.control.ViewController;
import sosafe.report.BillReportGenerator;
import sosafe.report.HTMLFormatter;
import sosafe.report.SimpleHTMLFormatter;

/**
 *
 * @author Z
 */
public class BillPanel extends javax.swing.JPanel {

    /**
     * Creates new form BillPanel
     */
    public BillPanel() {
        initComponents();
        refreshBillReport();
        ViewController.getInstance().addObserver(new BillPanel.RefreshObserver());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser = new javax.swing.JFileChooser();
        billLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();

        jFileChooser.setCurrentDirectory(null);
        jFileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setPreferredSize(new java.awt.Dimension(1260, 411));

        billLabel.setText("Bill");

        saveButton.setText("Save Report");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(billLabel)
                    .addComponent(saveButton))
                .addContainerGap(1157, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(billLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 352, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        int retVal = jFileChooser.showOpenDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            long timestamp = new Date().getTime();
            File filename = new File(jFileChooser.getSelectedFile(), "SoSafeBillReport_" + timestamp + ".html");
            try {
                Writer out = new BufferedWriter(new FileWriter(filename));
                HTMLFormatter formatter = new SimpleHTMLFormatter();
                BillReportGenerator reporter = new BillReportGenerator(formatter);
                reporter.buildBillReport();
                out.write(formatter.getResult(true));
                out.flush();
                out.close();
                JOptionPane.showMessageDialog(null, "Your bill report was successfully saved in:\n" + filename.getParent()
                        + "\nIts name is: " + filename.getName());
            } catch (IOException ex) {
                Logger.getLogger(BillPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Failed to save your bill report in:\n" + filename.getParent()
                        + "\nPlease check the permission of that path.");
            }
        }

    }//GEN-LAST:event_saveButtonActionPerformed
    
    private class RefreshObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            if (arg instanceof ViewControlMessage) {
                ViewControlMessage msg = (ViewControlMessage) arg;
                if (msg.type.equals(ViewControlMessage.SENSOR_INFO_UPDATED)) {
                    refreshBillReport();
                }
            }
        }
    }
    
    private void refreshBillReport() {
        HTMLFormatter formatter = new SimpleHTMLFormatter();
        BillReportGenerator reporter = new BillReportGenerator(formatter);
        reporter.buildBillReport();
        billLabel.setText(formatter.getResult(false));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel billLabel;
    private javax.swing.JFileChooser jFileChooser;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}