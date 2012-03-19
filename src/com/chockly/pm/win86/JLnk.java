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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * Provides means to create a windows shell shortcut (.lnk file).<br/>
 * <br/>
 * The shortcut is created by building a VBScript file and then running it.
 * 
 * @author Curtis Oakley
 * @version 0.4.1
 */
public class JLnk {
    
    private static final String SCRIPT_FILE_NAME = "makelink.vbs";
    
    private String arguments = "";
    private String description = "";
    private String folder;
    private int iconIndex = 0;
    private String iconLocation = "";
    private String name = "Shortcut";
    private String path;
    private String workingDirectory = "";
    
    /** Creates a new JLnk. */
    public JLnk(){}
    
    /**
     * Creates a new JLnk.
     * @param folder The folder for the shortcut.
     * @param path The path that the shortcut links to.
     */
    public JLnk(String folder, String path){
        this.folder = folder;
        this.path = path;
    }

    /**
     * Writes the shortcut to disk.
     * 
     * @throws IOException Thrown if an IOException occurred and prevented the
     * shortcut from being created.
     * @throws InterruptedException If the makelink script is interrupted. 
     * @throws IllegalStateException If the folder or path have not been set
     * before calling.
     */
    public void save() throws IOException, InterruptedException {
        
        // Make sure that the folder and path are defined
        if(folder == null || folder.isEmpty())
            throw new IllegalStateException("Folder must be defined before calling save");
        
        if(path == null || path.isEmpty())
            throw new IllegalStateException("Path must be defined before calling save");
        
        // IF the working directory is not defined, define it.
        if(workingDirectory == null || workingDirectory.isEmpty())
            if(path.contains("\\"))
                workingDirectory = path.substring(0, path.lastIndexOf('\\'));

	// Construct the visual basic script that builds the shortcut
        StringBuilder vbscript = new StringBuilder(128);
        String commonEndLine = "\"\r\n";

        vbscript.append("Set WshShell = WScript.CreateObject(\"WScript.Shell\")\r\n");
        vbscript.append("Set lnk = WshShell.CreateShortcut(\"").append(folder).append("\\").append(name).append(".LNK\")\r\n");
        vbscript.append("lnk.TargetPath = \"").append(path).append(commonEndLine);
        
        if(arguments != null && !arguments.isEmpty())
            vbscript.append("lnk.Arguments = \"").append(arguments).append(commonEndLine);
        
        if(description != null && !description.isEmpty())
            vbscript.append("lnk.Description = \"").append(description).append(commonEndLine);
	
        if(iconLocation != null && !iconLocation.isEmpty())
            vbscript.append("lnk.IconLocation = \"").append(iconLocation).append(",").append(iconIndex).append(commonEndLine);
        
        if(iconLocation != null && !iconLocation.isEmpty())
            vbscript.append("lnk.WorkingDirectory = \"").append(workingDirectory).append(commonEndLine);
        
        vbscript.append("lnk.Save\r\n");
        vbscript.append("Set lnk = Nothing");

        // Write the script to disk
        BufferedWriter out = null;
        File scriptFile = new File(SCRIPT_FILE_NAME);
        
        try {
            out = new java.io.BufferedWriter(new java.io.FileWriter(scriptFile));
            
            out.write( vbscript.toString() );
            
        } finally {
            if(out != null)
                out.close();
        }
        
        // Run the script
        Process exec = Runtime.getRuntime().exec("cscript " + SCRIPT_FILE_NAME);
        
        if(exec.waitFor() != 0)
            throw new IOException("Create .LNK script exited abnormally.");
        
        // Delete the script
        scriptFile.delete();
    }
    
    /**
     * Sets the arguments for this shortcut.
     * @param arguments The shortcut's arguments.
     */
    public void setArguments(String arguments){
        this.arguments = arguments;
    }
    
    /**
     * Sets the description for this shortcut.
     * @param description The shortcut's description.
     */
    public void setDescription(String description){
        this.description = description;
    }
    
    /**
     * Sets the folder for this shortcut. This is where the shortcut will be
     * saved to disk.
     * @param folder The shortcut's folder.
     */
    public void setFolder(String folder){
        this.folder = folder;
    }
    
    /**
     * Sets the icon index for this shortcut.
     * @param iconIndex The shortcut's icon index.
     */
    public void setIconIndex(int iconIndex){
        this.iconIndex = iconIndex;
    }
    
    /**
     * Sets the icon location for this shortcut.
     * @param iconLocation The shortcut's icon location.
     */
    public void setIconLocation(String iconLocation){
        this.iconLocation = iconLocation;
    }
    
    /**
     * Sets the file name of the shortcut.<br/>
     * <br/>
     * If this is not defined, it defaults to "Shortcut".
     * @param name The shortcut's base name.
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Sets the path for this shortcut. This is where the shortcut will link to.
     * @param path The shortcut's path.
     */
    public void setPath(String path){
        this.path = path;
    }
    
    /**
     * Sets the working directory for the shortcut.<br/>
     * <br/>
     * If not set this defaults the path's containing directory if the path
     * links to a file.
     * @param workingDirectory 
     */
    public void setWorkingDirectory(String workingDirectory){
        this.workingDirectory = workingDirectory;
    }

}
