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

import com.chockly.pm.IOUtils;
import com.chockly.pm.Main;
import com.chockly.pm.Profile;
import com.chockly.pm.ProfileFactory;
import java.io.File;

/**
 * Represents a game that operates by moving directories to activate profiles.
 * @author Curtis Oakley
 */
public abstract class AbstractDirGame implements Game {
    
    @Override
    public boolean activateProfile(Profile profile) {
        
        // Save the directories
        String dataDir = getDir();
        String saveDir = dataDir + getGameSaveDir();
        String profilesDir = dataDir + getSave() + File.separator;
        
        // Find the currently active profile
        ProfileFactory pf = ProfileFactory.getInstance();
        Profile[] profiles = pf.getProfiles(getId());
        Profile currentProfile = null;
        for(int x=0; x<profiles.length; x++){
            if(profiles[x].isActive()){
                currentProfile = profiles[x];
                break;
            }
        }
        
        // Create the Files that need to be renamed
        File saveFolder = new File(saveDir);
        File profileSaves = new File(profilesDir + profile.getSaveDir());
        
        // See if there is an active profile
        if(currentProfile == null){
            // No profile is active delete the saves folder
            if( saveFolder.exists() && !saveFolder.delete()){
                Main.handleException("Unable to activate the profile because the save folder contains saves from an unkown or de-activated profile.",
                        null, Main.WARN_LEVEL);
                return false;
            }
        } else {
            // Rename the saved games folder to the previous profile's saveDir
            if( !saveFolder.renameTo(
                    new File(profilesDir + currentProfile.getSaveDir())) )
            {
                Main.handleException("Unable to move the saved game folder.",
                        null, Main.WARN_LEVEL);
                return false;
            }
        }
        
        // Rename the profile's save game directory to the morrowind save directory
        if( profileSaves.renameTo(new File(saveDir)) ) {
            pf.setActive(profile);
            return true;
        } else {
            Main.handleException("Unable to move the profile's saved games into the save game folder.",
                    null, Main.WARN_LEVEL);
            return false;
        }
    }
    
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
        Profile[] existingProfiles = pf.getProfiles(getId());
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
                        pf.add(fileName, fileName, getId());
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
            File profileDir = new File(savesFolder, fileName);
            
            // Test if the file is directory
            if( profileDir.isDirectory() )
            {
                // See if this directory has already been processed
                if( profileData.get(fileName) == null ){
                    // Create a new profile from the directories name \\
                    
                    // Move the folder
                    if(profileDir.renameTo(new File(profilesFolder, fileName))){
                        // Update the hash map with the new dirName
                        profileData.put(fileName, fileName);

                        // Add the new profile
                        pf.add(fileName, fileName, getId());
                    } else {
                        Main.handleException("Unable to move the folder "
                                + profileDir.getPath() + ".",
                                null, Main.WARN_LEVEL);
                    }
                }
            }
        }
        
        // Re-activate the last active profile
        if(activeProfile != null){
            profilesFolder = new File(profilesFolder, activeProfile.getSaveDir());
            File[] saves = profilesFolder.listFiles();
            for(int i=0; i<saves.length; i++){
                IOUtils.moveFile(saves[i],
                        new File(savesFolder, saves[i].getName()));
            }
            
            // Delete the profile's folder in the profiles directory
            if( !profilesFolder.delete() )
                Main.handleException("Unable to reactivate the currently active profile.",
                        null, Main.WARN_LEVEL);
        }
    }
    
    @Override
    public void deactivateProfiles(){
        // Save the directories
        String dataDir = getDir();
        File profilesDir = new File(dataDir, getSave());
        File saveDir = new File(dataDir, getGameSaveDir());
        
        // Find the currently active profile
        Profile[] profiles = ProfileFactory.getInstance().getProfiles(getId());
        
        for(int i=0; i<profiles.length; i++){
            if( profiles[i].isActive() ){
                if( saveDir.renameTo(
                        new File(profilesDir, profiles[i].getSaveDir())) )
                {
                    profiles[i].setActive(false);
                }
                else
                {
                    Main.handleException("Unable to deactivate the active profile.",
                            null, Main.WARN_LEVEL);
                }
                break;
            }
        }
    }
    
    @Override
    public String getIni() {
        throw new UnsupportedOperationException("Not supported by this game.");
    }
    
    @Override
    public void setupProfile(Profile profile) {
        IOUtils.createFolder(
                new File(getDir() + getSave(), profile.getSaveDir()));
    }
    
    @Override
    public boolean usesIni(){
        return false;
    }
    
}
