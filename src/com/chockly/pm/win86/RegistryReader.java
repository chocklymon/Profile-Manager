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
package com.chockly.pm.win86;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Provides methods to read the Windows Registry using the reg.exe.
 * 
 * @author Curtis Oakley
 * @version 0.5
 */
public class RegistryReader {
    
    /**
     * Constructs a new RegistryReader.
     * @throws IOException 
     */
    public RegistryReader() throws IOException {
        String requestWow6432Node = runQuery(HKLM_SOFTWARE + WOW_6432_NODE);
        
        if(requestWow6432Node == null)
            usesWow6432Node = false;
    }
    
    /**
     * Gets the key value for an entry in in the Windows Registry.
     * 
     * @param path The path to the key, this method automatically appends the
     * correct path to the software key directory.
     * 
     * @param key The key to look for.
     * @return
     * @throws IOException 
     */
    public String getKeyValue(String path, String key) throws IOException {
        // Build the query
        StringBuilder query = new StringBuilder(64);
        query.append('"' + HKLM_SOFTWARE);
        
        if(usesWow6432Node)
            query.append(WOW_6432_NODE);
        
        query.append(path).append("\" /v \"").append(key).append('"');
        
        // Run it
        String output = runQuery(query.toString());
        
        // Extract the key's value from the output
        if(output == null)
            return null;
            
        output = output.substring(output.indexOf(key)+key.length());
        
        output = output.substring(output.indexOf("REG"));
        
        output = output.substring(output.indexOf(' '));
        
        return output.trim();
    }
    
    /**
     * Runs a query against the windows registry.
     * @param query The query to run.
     * @return The query string result, or null if an error occurred or the 
     * query returned no result.
     * @throws IOException 
     */
    private String runQuery(String query) throws IOException {
        String line;
        StringBuilder output = new StringBuilder(128);
        
        Process p = Runtime.getRuntime().exec(REG_QUERY + query);
    
        BufferedReader in = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
        
        while((line = in.readLine()) != null){
            output.append(line);
        }
        in.close();
        
        String returnMe = output.toString().trim();
        
        if(returnMe.isEmpty())
            return null;
        
        if(returnMe.toLowerCase().contains("error"))
            return null;
        
        return output.toString();
    }
    
    private boolean usesWow6432Node = true;
    
    private static final String REG_QUERY = "reg query ";
    private static final String HKLM_SOFTWARE = "HKLM\\Software\\";
    private static final String WOW_6432_NODE = "Wow6432Node\\";
    
}
