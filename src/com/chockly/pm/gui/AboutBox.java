/* Profile Manager
 * Copyright (C) 2012 Curtis Oakley
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.chockly.pm.gui;

/**
 * Shows the Profile Manager about dialog.
 * @author Curtis Oakley
 */
public class AboutBox extends javax.swing.JDialog {

    /** Creates new About dialog. */
    public AboutBox(java.awt.Frame parent) {
        super(parent, false);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleTxt = new javax.swing.JTextField();
        versionLabel = new javax.swing.JLabel();
        verstionTxt = new javax.swing.JTextField();
        authorLabel = new javax.swing.JLabel();
        authorTxt = new javax.swing.JTextField();
        iconCreditsLabel = new javax.swing.JLabel();
        iconsTxt = new javax.swing.JEditorPane();
        copyrightTxt = new javax.swing.JEditorPane();
        exitBtn = new javax.swing.JButton();
        pmIcon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/com/chockly/pm/resources/information-white.png")).getImage());
        setLocationByPlatform(true);
        setResizable(false);

        titleTxt.setBackground(new java.awt.Color(240, 240, 240));
        titleTxt.setEditable(false);
        titleTxt.setFont(new java.awt.Font("Tahoma", 1, 18));
        titleTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        titleTxt.setText("Profile Manager");
        titleTxt.setBorder(null);

        versionLabel.setText("Version:");

        verstionTxt.setBackground(new java.awt.Color(240, 240, 240));
        verstionTxt.setEditable(false);
        verstionTxt.setFont(new java.awt.Font("Tahoma", 1, 11));
        verstionTxt.setText(com.chockly.pm.Main.VERSION_NUM);
        verstionTxt.setBorder(null);

        authorLabel.setText("Author:");

        authorTxt.setBackground(new java.awt.Color(240, 240, 240));
        authorTxt.setEditable(false);
        authorTxt.setFont(new java.awt.Font("Tahoma", 1, 11));
        authorTxt.setText("Curtis Oakley");
        authorTxt.setBorder(null);

        iconCreditsLabel.setText("Icons:");

        iconsTxt.setBackground(new java.awt.Color(240, 240, 240));
        iconsTxt.setBorder(null);
        iconsTxt.setContentType("text/html");
        iconsTxt.setEditable(false);
        iconsTxt.setText("<html><head></head><body style=\"font-size:11;font-family:Tahoma\"><b>Fugue Icons</b> by Yusuke Kamiyamane<br> http://p.yusukekamiyamane.com/<br><br>© 2012 Yusuke Kamiyamane. All rights reserved.<br>These icons are licensed under a Creative Commons<br>Attribution 3.0 License.<br><br><b>Game Icons</b> by Bethesda Studios<br><br><b>Fallout: New Vegas Die Icon</b> by Shoedude<br>http://shoedude.deviantart.com/art/Fallout-New-<br>Vegas-Die-Icon-3-186884666</body></html>");

        copyrightTxt.setBackground(new java.awt.Color(240, 240, 240));
        copyrightTxt.setContentType("text/html");
        copyrightTxt.setEditable(false);
        copyrightTxt.setText("<html><head></head><body style=\"font-size:11;font-family:Tahoma\">Profile Manager by Curtis Oakley is licensed under the GNU GPLv3 (http://www.gnu.org/licenses/)<br/>© 2012 Curtis Oakley.<br/>http://pmanager.chocklydigital.com</body></html>");

        exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/chockly/pm/resources/cross-script.png"))); // NOI18N
        exitBtn.setText("Close");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });

        pmIcon.setIcon(new javax.swing.ImageIcon("lib/help_docs/assets/profile-manager-icon.png"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(authorLabel)
                                    .addComponent(versionLabel)
                                    .addComponent(iconCreditsLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(iconsTxt)
                                    .addComponent(authorTxt)
                                    .addComponent(verstionTxt))))
                        .addContainerGap(447, Short.MAX_VALUE))
                    .addComponent(pmIcon, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(copyrightTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 195, Short.MAX_VALUE)
                        .addComponent(exitBtn)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(titleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(versionLabel)
                            .addComponent(verstionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(authorLabel)
                            .addComponent(authorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(iconCreditsLabel)
                            .addComponent(iconsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(pmIcon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(copyrightTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exitBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        dispose();
    }//GEN-LAST:event_exitBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel authorLabel;
    private javax.swing.JTextField authorTxt;
    private javax.swing.JEditorPane copyrightTxt;
    private javax.swing.JButton exitBtn;
    private javax.swing.JLabel iconCreditsLabel;
    private javax.swing.JEditorPane iconsTxt;
    private javax.swing.JLabel pmIcon;
    private javax.swing.JTextField titleTxt;
    private javax.swing.JLabel versionLabel;
    private javax.swing.JTextField verstionTxt;
    // End of variables declaration//GEN-END:variables
}
