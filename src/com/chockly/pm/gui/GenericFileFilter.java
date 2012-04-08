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

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Creates a new Executable File Filter.<br/>
 * <br/>
 * This will only accept files that have an .exe, .bat, .cmd, .com, or .jar
 * extension.
 * @author Curtis Oakley
 */
public class GenericFileFilter extends FileFilter {
    
    private final String[] extensions;
    private final String description;
    
    public GenericFileFilter(String[] extensions, String description){
        this.extensions = new String[extensions.length];
        for(int i=0; i<extensions.length; i++){
            this.extensions[i] = extensions[i].toLowerCase();
        }
        this.description = description;
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory())
            return true;

        String s = f.getName();
        int index = s.lastIndexOf('.');

        if (index > 0 &&  index < s.length() - 1) {
            String  ext = s.substring(index+1).toLowerCase();
            for(int i=0; i<extensions.length; i++){
                if(extensions[i].equals(ext))
                    return true;
            }
        }
        
        return false;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
