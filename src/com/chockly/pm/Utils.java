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

import java.lang.reflect.Array;

/**
 * Contains methods helpful to Array manipulations.
 * @author Curtis Oakley
 */
public class Utils {
    
    /**
     * Removes the specified index from the array. Returning an array with a
     * length one shorter than the original.
     * 
     * @param original The array to splice.
     * @param index The index of the object in the array to remove.
     * @return The array with with the specified index removed.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] splice(T[] original, int index) {
        return (T[]) splice(original, index, original.getClass());
    }
    
    private static <T,U> T[] splice(U[] original, int index, Class<? extends T[]> newType) {
        int newLength = original.length - 1;
        // Safe because casting to the same type as the one passed in.
        @SuppressWarnings("unchecked") T[] copy = ((Object)newType == (Object)Object[].class)
            ? (T[]) new Object[newLength]
            : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        
        if(newLength > 0){
            System.arraycopy(original, 0, copy, 0, index);
            System.arraycopy(original, index+1, copy, index, copy.length-index);
        }
        
        return copy;
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
