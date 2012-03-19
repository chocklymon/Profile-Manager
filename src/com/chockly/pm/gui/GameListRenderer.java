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

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * This ListCellRenderer implementation renders a JList so that it's components
 * are JCheckBoxes.<br/>
 * <br/>
 * This code was modified from code found at http://helpdesk.objects.com.au/java/how-do-add-a-checkbox-to-items-in-a-jlist
 * <a href="http://helpdesk.objects.com.au/java/how-do-add-a-checkbox-to-items-in-a-jlist">link</a>
 * and code by Winston Prakash found at http://www.winstonprakash.com/sources/java2html/list/JCheckBoxList.java.html.
 * <a href="http://www.winstonprakash.com/sources/java2html/list/JCheckBoxList.java.html">link</a>
 * @author Curtis Oakley
 */
public class GameListRenderer extends JCheckBox implements ListCellRenderer {
    
    @Override
    public Component getListCellRendererComponent( JList list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus)
    {
        setEnabled(list.isEnabled());
        setSelected(((GameListItem)value).isSelected());
        setFont(list.getFont());
        setText(value.toString());
        
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        return this;
    }
}
