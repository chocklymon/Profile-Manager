/*
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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

/**
 * Creates a JFileChooser to select an Image.<br/>
 * <br/>
 * The JFileChooser will display with an thumbnail viewing component.<br/>
 * <br/>
 * Adapted from code found in the Java Tutorials
 * <a href="http://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html">
 * link</a>.
 * @author Curtis Oakley
 */
public class ImageFileChooser extends JComponent implements PropertyChangeListener {
     
    private JFileChooser fc;
    private ImageIcon thumbnail = null;
    private File file = null;

    /**
     * Creates a new ImageFileChooser.
     * @param profileImageDir The current image file for the profile. This will
     * be used to set the JFileChooser's current directory.
     */
    public ImageFileChooser(String profileImageDir){
        fc = new JFileChooser();
        
        fc.setApproveButtonText("Select");
        fc.setFileFilter(new ImageFileFilter());
        fc.setAcceptAllFileFilterUsed(false);
        fc.setDialogTitle("Select profile image");
        
        if(profileImageDir == null || profileImageDir.isEmpty()) // Try to set the directory to the user's my pictures
            fc.setCurrentDirectory(new File(System.getenv("USERPROFILE") + "\\Pictures"));
        else // Try to set the dirctory to the current profile images file
            fc.setSelectedFile(new File(profileImageDir));
        
        setPreferredSize(new java.awt.Dimension(150, 100));
    }
    
    // The following JavaDoc is copied from the the JFileChooser.getSelectedFile
    /**
     * Returns the selected file. This can be set either by the programmer via 
     * setFile or by a user action, such as either typing the filename into the 
     * UI or selecting the file from a list in the UI.
     * @return the selected file
     * @see JFileChooser.setSelectedFile
     */
    public File getSelectedFile(){
        return fc.getSelectedFile();
    }
    
    private void loadImage() {
        if (file == null) {
            thumbnail = null;
            return;
        }

        // Get the ImageIcon
        ImageIcon icon = new ImageIcon(file.getPath());
        if (icon != null) {
            if (icon.getIconWidth() > 150) {
                thumbnail = new ImageIcon(
                        icon.getImage().getScaledInstance(150, -1, Image.SCALE_DEFAULT));
            } else {
                thumbnail = icon;
            }
        }
    }
    
    // Parts of the following JavaDoc is copied from the the JFileChooser.showOpenDialog
    /**
     * Pops up the Choose Image File chooser dialog.
     * 
     * @param parent the parent component of the dialog, can be null; see showDialog for details 
     * 
     * @return the return state of the file chooser on popdown:<br/>
     * JFileChooser.CANCEL_OPTION<br/>
     * JFileChooser.APPROVE_OPTION<br/>
     * JFileChooser.ERROR_OPTION if an error occurs or the dialog is dismissed
     * 
     * @throws HeadlessException - if GraphicsEnvironment.isHeadless() returns true. 
     * @see GraphicsEnvironment.isHeadless
     */
    public int showOpenDialog(Component parent){
        fc.addPropertyChangeListener(this);
        fc.setAccessory(this);
        return fc.showOpenDialog(parent);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (thumbnail == null) {
            loadImage();
        }
        if (thumbnail != null) {
            int x = getWidth()/2 - thumbnail.getIconWidth()/2;
            int y = getHeight()/2 - thumbnail.getIconHeight()/2;

            if (y < 0) {
                y = 0;
            }

            if (x < 5) {
                x = 5;
            }
            thumbnail.paintIcon(this, g, x, y);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        boolean update = false;
        String prop = evt.getPropertyName();

        //If the directory changed, don't show an image.
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
            file = null;
            update = true;

        //If a file became selected, find out which one.
        } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            file = (File) evt.getNewValue();
            update = true;
        }

        //Update the preview accordingly.
        if (update) {
            thumbnail = null;
            if (isShowing()) {
                loadImage();
                repaint();
            }
        }
    }
    
}
