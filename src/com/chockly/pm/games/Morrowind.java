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
package com.chockly.pm.games;

import com.chockly.pm.*;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The Elder Scrolls: III Morrowind's implementation of Game.
 * @author Curtis Oakley
 */
public class Morrowind extends AbstractDirGame {
    
    /**
     * Key used to retrieve/store the value indicating the folder where the 
     * Morrowind saved games are stored.<br/>
     * Like all other directory values, this one will have a trailing path
     * separator.
     */
    private static final String MORROWIND_DATA_DIR = "morrowind_data_dir";
    /**
     * Key used to retrieve/store the value indicating the name of the folder in
     * the MORROWIND_DATA_DIR where saves are stored.<br/>
     * The default value for the key is "Saves".
     */
    private static final String MORROWIND_SAVE_DIR = "morrowind_save_dir";
    /**
     * Key used to retrieve/store the value indicating the directory name where
     * morrowind profiles will be stored. This directory will be a child
     * directory of the MORROWIND_DATA_DIR.
     */
    private static final String MORROWIND_PROFILE_SAVE_DIR = "morrowind_profiles_dir";
    /**
     * Key used to retrieve the value indicating the executable to run when
     * launching Morrowind.
     */
    private static final String MORROWIND_EXE = "morrowind_exe";

    @Override
    public void autoSetupProfiles() {
        
        // Get the save folder
        File savesFolder = new File(getDir() + getGameSaveDir());
        File profilesFolder = new File(getDir() + getSave());
        
        // Create a hashset to save the profiles directory as a key and character's name as it's value
        java.util.HashMap<String, String> profileData = 
                new java.util.HashMap<String, String>();
        
        // Input any existing profiles
        ProfileFactory pf = ProfileFactory.getInstance();
        Profile[] existingProfiles = pf.getProfiles(GameFactory.MORROWIND_ID);
        Profile activeProfile = null;
        for(int i=0; i<existingProfiles.length; i++){
            profileData.put(
                    existingProfiles[i].getSaveDir(),
                    existingProfiles[i].getName());
            
            if(existingProfiles[i].isActive())
                activeProfile = existingProfiles[i];
        }
        
        
        // Get any folders in the profiles directory and setup the profiles.
        String[] files = profilesFolder.list();
        
        if(files != null){
            for(String fileName : files)
            {
                if( new File(profilesFolder, fileName).isDirectory() )
                {
                    // See if this directory has already been processed
                    if( profileData.get(fileName) == null ){
                        // Create a new profile from the directories name \\

                        // Update the hash map with the new dirName
                        profileData.put(fileName, fileName);

                        // Add the new profile
                        pf.add(fileName, fileName,
                                GameFactory.MORROWIND_ID);
                    }
                }
            }
        }
        
        // Get the files in the save folder
        files = savesFolder.list();
        
        // Exit if there are no files in the save folder
        if(files == null)
            return;
        
        // Loop through each file
        for(String fileName : files)
        {
            // Test if the file is directory
            if( new File(savesFolder, fileName).isDirectory() )
            {
                // See if this directory has already been processed
                if( profileData.get(fileName) == null ){
                    // Create a new profile from the directories name \\
                    
                    // Move the folder
                    File profileDir = new File(savesFolder, fileName);
                    if(profileDir.renameTo(new File(profilesFolder, fileName))){
                        // Update the hash map with the new dirName
                        profileData.put(fileName, fileName);

                        // Add the new profile
                        pf.add(fileName, fileName,
                                GameFactory.MORROWIND_ID);
                    } else {
                        Main.handleException("Unable to move the folder "
                                + profileDir.getPath() + ".",
                                null, Main.WARN_LEVEL);
                    }
                }
            }
            // Only process files that have an extension
            else if(fileName.contains("."))
            {
                // Validate the extension
                if( fileName.substring(fileName.lastIndexOf('.'))
                        .equalsIgnoreCase(".ess") )
                {

                    StringBuilder characterName = 
                            new StringBuilder(fileName.length());
                    
                    // TODO Check into dealing with multi-language character names.
                    // Read the file's contents to determine the characters name.
                    try {
                        java.io.RandomAccessFile ran = new 
                                java.io.RandomAccessFile(
                                new File(savesFolder, fileName), "r");

                        // Jump to approximately where the names starts
                        ran.seek(0x162L);
                        
                        // Read until 'GMDT' (47 4D 44 54) found
                        int in, previous1 = 0, previous2 = 0, previous3 = 0;
                        while( (in = ran.read()) != -1){
                            if(previous3 == 0x47 &&
                                    previous2 == 0x4d &&
                                    previous1 == 0x44 &&
                                    in == 0x54)
                            {
                                break;
                            }
                            else
                            {
                                previous3 = previous2;
                                previous2 = previous1;
                                previous1 = in;
                            }
                        }
                        
                        /* The characters name is exactly 96 bytes from the end
                        of the string GMDT. So jump to where the name starts. */
                        ran.seek(ran.getFilePointer() + 96);
                        
                        // Read the name (the name is null byte terminated).
                        while((in = ran.read()) != -1){
                            if(in == 0x00)
                                break;

                            characterName.append( (char) in );
                        }

                        ran.close();

                    } catch(IOException ioe){
                        // Handle the exception, and then skip the file.
                        Main.handleException(
                                "An IO Exception occured while trying to find the character name in the file " +
                                fileName + ".\nThis file will not be assigned to a profile.",
                                ioe, Main.WARN_LEVEL);
                        continue;
                    }

                    // Create the save directory's name
                    String dirName = Utils.sanitizeDir(characterName.toString());

                    // Ignore empty directory names
                    if( !dirName.isEmpty()){
                        
                        // See if we have already created this profile
                        if( profileData.get(dirName) == null ){
                            // Profile doesn't exists, add a new profile \\

                            // Make sure that the directory name doesn't exist
                            for(int x=1; new File(dirName).exists(); x++)
                            {
                                if(x == 1)
                                    dirName += x;
                                else
                                    dirName = dirName.substring(0, dirName.length()-1) + x;
                            }

                            // Update the hash map with the new dirName
                            profileData.put(dirName, characterName.toString());

                            // Add the new profile
                            pf.add(characterName.toString(), dirName, GameFactory.MORROWIND_ID);

                            /* Comment out this line to print out the newly added profile information
                            System.out.println("-New Profile-\nSave Folder: " + savesFolder +
                                "\nFile Name: " + fileName +
                                "\nDirectory Name: " + dirName +
                                "\nCharacter Name: " + characterName + "\n");
                            // */
                        }

                        // Move the saved game file
                        IOUtils.moveFile(
                                new File(savesFolder, fileName),
                                new File(profilesFolder, dirName + File.separator + fileName ));
                    }
                }
            }
        }
        
        // Re-activate the last active profile
        if(activeProfile != null){
            profilesFolder = new File(profilesFolder, activeProfile.getSaveDir());
            for(File save : profilesFolder.listFiles()){
                IOUtils.moveFile(save,
                        new File(savesFolder, save.getName()));
            }
            
            // Delete the profile's folder in the profiles directory
            if( !profilesFolder.delete() )
                Main.handleException("Unable to reactivate the currently active profile.",
                        null, Main.WARN_LEVEL);
        }
    }

    @Override
    public String getDirConfigKey() {
        return MORROWIND_DATA_DIR;
    }

    @Override
    public String getDir() {
        return Config.get(MORROWIND_DATA_DIR, "C:\\Program Files\\Bethesda Softworks\\Morrowind\\");
    }

    @Override
    public String getExeConfigKey() {
        return MORROWIND_EXE;
    }

    @Override
    public String getExe() {
        return Config.get(MORROWIND_EXE, "C:\\Program Files\\Bethesda Softworks\\Morrowind\\Morrowind.exe");
    }

    @Override
    public String getFullName() {
        return "The Elder Scrolls III: Morrowind";
    }

    @Override
    public Icon getIcon() {
        return new ImageIcon(getClass().getResource("/com/chockly/pm/resources/morrowind-icon.png"));
    }
    
    @Override
    public byte getId(){
        return GameFactory.MORROWIND_ID;
    }

    @Override
    public String getName() {
        return "Morrowind";
    }
    
    @Override
    public String getGameSaveDir(){
        return Config.get(MORROWIND_SAVE_DIR, "Saves");
    }

    /**
     * Returns the directory name of the directory in the Morrowind data directory
     * (see {@link Morrowind#getDir()}) that contains the saved games of the 
     * profiles.
     * @return The directory name.
     */
    @Override
    public String getSave() {
        return Config.get(MORROWIND_PROFILE_SAVE_DIR, "Profiles");
    }
}
