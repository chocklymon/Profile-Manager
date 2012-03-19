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

import com.chockly.pm.Profile;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * This ListCellRenderer renders the list items such that they have icons.<br/>
 * <br/>
 * This renderer expects the items in the list to be Profiles, since the icon
 * displayed with each item is determined by the Profile's active state.
 * @author Curtis Oakley
 */
public class ProfileListRenderer extends DefaultListCellRenderer {
    
    private ImageIcon activeIcon = new ImageIcon(
            getClass().getResource("/com/chockly/pm/resources/status.png"));
    
    private ImageIcon inActiveIcon = new javax.swing.ImageIcon(
            getClass().getResource("/com/chockly/pm/resources/status-grey.png"));
    
    @Override
    public Component getListCellRendererComponent( JList list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        
        if(((Profile) value).isActive())
            label.setIcon(activeIcon);
        else
            label.setIcon(inActiveIcon);
        
        return label;
    }
}
