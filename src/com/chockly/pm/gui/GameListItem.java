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
 * Represents a Game Object as an item to be used in a JList with JCheckBoxes.
 * @author Curtis Oakley
 */
public class GameListItem {
    
    private final String label;
    private final byte id;
    private boolean isSelected = false;

    /**
     * Creates a new GameListItem.
     * @param name The name of the game this list item represents.
     * @param id The ID number of the game this list item represents.
     */
    public GameListItem(String name, byte id){
        this.label = name;
        this.id = id;
    }

    public boolean isSelected(){
        return isSelected;
    }
    
    public byte getID(){
        return id;
    }

    public void setSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

    @Override
    public String toString(){
        return label;
    }
}
