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
package com.chockly.pm;

/**
 * Contains methods helpful to Array manipulations.
 * @author Curtis Oakley
 */
public class Utils {
    
    /**
     * Finds the the first index of a byte in a byte array, or -1 if not found.
     * @param search The byte array to search.
     * @param find The byte to search for, can also accept ints.
     * @return The first index of the byte, or -1.
     */
    public static int getIndex(byte[] search, int find){
        for(int x=0; x<search.length; x++){
            if(search[x] == find)
                return x;
        }
        return -1;
    }
    
    /**
     * Takes a directory name and removes any illegal characters and shortens
     * it to 64 characters if needed.
     * @param name The name of the directory to clean.
     * @return The sanitized directory name.
     */
    public static String sanitizeDir(String name){
        // Remove illegal characters
        name = name.replaceAll("[\\\\/:*?\\\"<>|"// Invalid folder chars
                + "\\x00-\\x1F]",// Control characters
                "");
        
        // Remove trailing white space
        name = name.trim();
        
        // Shorten to 64 characters in length
        if(name.length() > 64)
            name = name.substring(0, 64);
        
        return name;
    }
}
