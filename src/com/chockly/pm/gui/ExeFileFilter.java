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

import com.chockly.pm.IOHelper;
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Creates a new Executable File Filter.<br/>
 * <br/>
 * This will only accept files that have an .exe, .bat, .cmd, .com, or .jar
 * extension.
 * @author Curtis Oakley
 */
public class ExeFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory())
            return true;

        String extension = IOHelper.getExtension(f);
        if (extension != null) {
            if (extension.equals("exe") ||
                    extension.equals("bat") ||
                    extension.equals("cmd") ||
                    extension.equals("com") ||
                    extension.equals("jar"))
                return true;
            else
                return false;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Executables (*.exe, *.bat)";
    }
}
