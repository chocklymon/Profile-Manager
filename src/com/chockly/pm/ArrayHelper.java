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

import com.chockly.pm.games.CustomGame;

/**
 * Contains methods helpful to Array manipulations.
 * @author Curtis Oakley
 */
public class ArrayHelper {
    
    /**
     * Removes the specified index from the custom game array. Returning an
     * array with a length one shorter.
     * @param array The CustomGame array to splice.
     * @param index The index of the CustomGame to remove.
     * @return The array with with the specified index removed.
     */
    public static CustomGame[] splice(CustomGame[] array, int index){
        CustomGame[] temp = new CustomGame[array.length-1];
        if(temp.length > 0){
            System.arraycopy(array, 0, temp, 0, index);
            System.arraycopy(array, index+1, temp, index, temp.length-index);
        }
        return temp;
    }
    
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
}
