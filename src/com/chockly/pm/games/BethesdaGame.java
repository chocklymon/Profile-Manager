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
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Provides an abstract game that implements some of the methods common to the
 * Bethesda Game Studios games that use .ini files to control where the game
 * looks for the saved games at.
 * @author Curtis Oakley
 */
public abstract class BethesdaGame implements Game {
    
    protected static final String SAVES_FOLDER = "Saves";
    
    @Override
    public boolean activateProfile(Profile profile) {
        try
        {
            IOHelper.setINIValue(
                    getDir() + getIni(),
                    "SLocalSavePath",
                    getSave() + File.separator + profile.getSaveDir() + File.separator,
                    "[General]");
            
            ProfileFactory.getInstance().setActive(profile);
            
            return true;
        }
        catch(FileNotFoundException fnfe)
        {
            Main.handleException("Unable to find the file '" + getIni()
                    + "'\nMake sure that the settings for this game are correct.",
                    fnfe, Main.WARN_LEVEL);
            
            return false;
        }
    }
    
    /**
     * <p>
     * Auto Sets up profiles for a game.
     * </p><p>
     * This method obtains a list of all files in the games saves folder and
     * then process and creates profiles. A profile will be added for each
     * sub-directory of saved game folder and for each character name extracted
     * from the saved game files. If a profile with the given save directory 
     * already exists a new profile will not be created.
     * </p><p>
     * Saved game files are identified using their extensions as defined by the
     * validExtensions variable. Files who's names don't contain nameStart and
     * nameEnd will have their contents read to retrieve the character's name.
     * The file reader will begin reading the character's name from the
     * nameStartOffset and continue reading chars until a null byte has been read
     * then the backTrackAmount will be removed from the character's name (this
     * is to remove any extra chars that may be between the end of the character's
     * name of the null byte). File names that do contain the nameStart and
     * nameEnd will use the file name to get the character's name. The nameStart
     * variable is used to indicate what char sequence precedes a character's
     * name in the file name. The nameEnd variable indicates what char sequence
     * marks the end of the character's name. The repeatNameEnd variable
     * indicates if the nameEnd should be run twice (the character's name is
     * found using String.substring(0,String.lastIndexOf(nameEnd)), so setting
     * this to true will cause this to be be run twice).
     * </p>
     * 
     * @param validExtensions An array containing all the valid extensions for 
     * the game. The extensions in this array must contain the period.
     * 
     * @param nameStartOffset The number of bytes into the file that the character's
     * name starts.
     * 
     * @param backTrackAmount The number of bytes that should be removed from the
     * characters name once a null (\0) byte has been read in.
     * 
     * @param nameStart The string sequence that indicates that the start of
     * the character's name in the file's name.
     * 
     * @param nameEnd The string sequence that indicates the end of the
     * character's name in the file's name.
     * 
     * @param repeatNameEnd If the nameEnd variable should be subtracted from the
     * character's name twice.
     */
    protected void autoSetupProfiles(String[] validExtensions,
            long nameStartOffset,
            int backTrackAmount,
            String nameStart,
            String nameEnd,
            boolean repeatNameEnd)
    {
        // Detect the state of the deepscan
        String deepScan = Config.get(Config.Key.deep_scan);
        int deepScanFlag = 0;
        if( !deepScan.equalsIgnoreCase(Config.DEEP_SCAN_AUTO))
            deepScanFlag = Boolean.parseBoolean(deepScan) ? 1 : 2;
        
        // Get the save folder
        String savesFolder = getDir() + getSave() + File.separator;
        
        // Get the files in the save folder
        String[] files = (new File(savesFolder)).list();
        
        // Make sure that there are files to process
        if(files == null)
            return;
        
        // Create a hashset to save the profiles directory as a key and character's name as it's value
        java.util.HashMap<String, String> profileData = 
                new java.util.HashMap<String, String>();
        
        // Input any existing profiles
        ProfileFactory pf = ProfileFactory.getInstance();
        Profile[] existingProfiles = pf.getProfiles(getId());
        for(int i=0; i<existingProfiles.length; i++){
            profileData.put(
                    existingProfiles[i].getSaveDir(),
                    existingProfiles[i].getName());
        }
        
        // Loop through each file
        for(String fileName : files)
        {
            // Test if the file is directory
            if( new File(savesFolder + fileName).isDirectory() )
            {
                // See if this directory has already been processed
                if( profileData.get(fileName) == null ){
                    // Create a new profile from the directories name \\
                    
                    // Update the hash map with the new dirName
                    profileData.put(fileName, fileName);

                    // Add the new profile
                    pf.add(fileName, fileName, getId());
                }
            }
            // Only process files that have an extension
            else if(fileName.contains("."))
            {
                // Validate the extension
                if( hasValidExtension(validExtensions, fileName) )
                {

                    StringBuilder characterName = 
                            new StringBuilder(fileName.length());
                    try {
                        // See if the character name can be extracted from the file's name
                        if( fileName.contains(nameStart) && fileName.contains(nameEnd)
                                && deepScanFlag != 1)
                        {
                            // Get the characters name using the file's name.
                            characterName.append(fileName);
                            characterName.delete(0,
                                    characterName.indexOf(nameStart) + nameStart.length());

                            characterName.delete(characterName.lastIndexOf(nameEnd),
                                    characterName.length());

                            if(repeatNameEnd)
                                characterName.delete(characterName.lastIndexOf(nameEnd),
                                        characterName.length());

                        } else if(deepScanFlag != 2) {
                            // TODO Check into dealing with multi-language character names.
                            // Read the file's contents to determine the characters name.

                            java.io.RandomAccessFile ran = new 
                                    java.io.RandomAccessFile(savesFolder + fileName, "r");
                            
                            // Jump to where the name starts
                            ran.seek(nameStartOffset);
                            
                            // Read in the characters until null byte read
                            int in;
                            while( (in = ran.read()) != -1){
                                if(in == 0x00)
                                    break;

                                characterName.append( (char) in );
                            }

                            ran.close();

                            // Remove excess byte data
                            characterName.delete(
                                    characterName.length() - backTrackAmount,
                                    characterName.length());
                            
                        } else {
                            // Deep scan only file, but deep scan disabled, skip
                            continue;
                        }
                    } catch(IOException ioe){
                        // Handle the exception, and then skip the file.
                        Main.handleException(
                                "An IO Exception occured while trying to find the character name in the file " +
                                fileName + ".\nThis file will not be assigned to a profile.",
                                ioe, Main.WARN_LEVEL);
                        continue;
                    } catch(StringIndexOutOfBoundsException sioobe){
                        Main.handleException(
                                "Unable to process the file " +
                                fileName + ".\nThis file will not be assigned to a profile.",
                                sioobe, Main.WARN_LEVEL);
                        continue;
                    }

                    // Create the save directory's name
                    String dirName = characterName.toString()
                            .replaceAll("[ ]", "");
                    
                    // Ignore empty directory names
                    if( !dirName.isEmpty()){

                        // See if we have already created this profile
                        if( profileData.get(dirName) == null ){
                            // Profile doesn't exists, add a new profile

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
                            pf.add(
                                    characterName.toString(), dirName, getId());

                            /* Comment out this line to print out the newly added profile information
                            System.out.println("-New Profile-\nSave Folder: " + savesFolder +
                                "\nFile Name: " + fileName +
                                "\nDirectory Name: " + dirName +
                                "\nCharacter Name: " + characterName + "\n");
                            // */
                        }

                        // Move the saved game file
                        IOHelper.moveFile(
                                new File(savesFolder + fileName),
                                new File(savesFolder + dirName + File.separator + fileName ));
                    }
                }
            }
        }
    }
    
    @Override
    public void deactivateProfiles(){
        try
        {
            IOHelper.setINIValue(
                    getDir() + getIni(),
                    "SLocalSavePath",
                    getSave() + File.separator,
                    "[General]");
            
            ProfileFactory.getInstance().clearActiveProfile(getId());
        }
        catch(FileNotFoundException fnfe)
        {
            Main.handleException("Unable to find the file '" + getIni()
                    + "'\nMake sure that the settings for this game are correct.",
                    fnfe, Main.WARN_LEVEL);
        }
    }
    
    /**
     * Checks if a file name has a valid extension
     * @param extensions A String array list of valid extensions.<br/>
     * These extensions must contain the period.
     * @param fileName The name of the file to check.
     * @return True if the file name has a valid extensions, false otherwise.
     */
    private boolean hasValidExtension(String[] extensions, String fileName){
        // Get the extension
        fileName = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
        
        // Search for the extension
        for(int i=0; i<extensions.length; i++){
            if(extensions[i].equals(fileName))
                return true;
        }
        return false;
    }
    
    @Override
    public String getGameSaveDir(){
        return getSave();
    }
    
    @Override
    public void setupProfile(Profile profile) {
        IOHelper.createFolder(
                new File(getDir() + getSave(), profile.getSaveDir()));
    }
    
    @Override
    public boolean usesIni(){
        return true;
    }
}
