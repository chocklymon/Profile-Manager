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

package com.chockly.pm;

import com.chockly.pm.games.GameFactory;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 * Holds all the profiles for the program. Use this to manage the profiles.
 * @author Curtis Oakley
 */
public class ProfileFactory {

    /**
     * Adds a new profile and sets it up.
     * @param name The name of the profile.
     * @param saveDir The save directory to use for the profile.<br/>
     * This field should be checked by {@link ProfileFactory#profileDirExists(java.lang.String, byte) }
     * before being sent in to ensure that there is no conflict with exiting profiles.
     * This method doesn't perform this check, but leaves it up to the calling object.
     * @param gameID The Game ID for the game that the profile belongs to.<br/>
     * For a list of valid game IDs see the {@link GameFactory}.
     */
    public static void addProfile(String name, String saveDir, byte gameID){

        if(profilesNotLoaded)
            loadProfiles();

        Profile newProfile = new Profile(name, saveDir, gameID, profileIdCounter);

        // Add the profile to profiles array
        if(profiles.length == size)
            profiles = Arrays.copyOf(profiles, size+3);
        
        profiles[size] = newProfile;
        size++;

        GameFactory.getGameFromID(gameID).setupProfile(newProfile);

        profileIdCounter++;
    }
    
    /**
     * Finds and returns the profile with the specific ID.
     * @param id The id number of the profile to retrieve.
     * @return The Profile with the given id.
     */
    public static Profile getProfile(int id){
        
        if(id < 0)
            throw new IllegalArgumentException("Pofile IDs must be positive.");

        if(profilesNotLoaded)
            loadProfiles();

        for(int x=0; x < size; x++){
            if(profiles[x].getID() == id){
                return profiles[x];
            }
        }

        return null;
    }

    /**
     * Retrieves all profiles for a game.
     * @param gameID The game to retrieve the profiles for.
     * @return Array of all profiles for the given game, if no profiles exits returns an array with zero length.
     */
    public static Profile[] getProfiles(byte gameID){

        if(profilesNotLoaded)
            loadProfiles();

        Profile[] gameProfiles = new Profile[10];
        int length = 0;

        for(int x=0; x<size; x++){

            if(profiles[x].getGameID() == gameID){

                if(length == gameProfiles.length)
                    gameProfiles = Arrays.copyOf(gameProfiles, length+5);

                gameProfiles[length] = profiles[x];
                length++;
            }
        }

        return Arrays.copyOf(gameProfiles, length);
    }

    /**
     * Tests if the given directory is already used by an existing profile for the game.
     * @param dir The directory name to test against.
     * @param gameID The ID number of the game that the pofiles should belong to.
     * @return True if there is a game profile that already uses the provided directory, false otherwise.
     */
    public static boolean profileDirExists(String dir, byte gameID){
        if(profilesNotLoaded)
            loadProfiles();
        
        /* Deprecated
        // Check the block directory profile
        if(profiles.get(0).getProfileSaveDir().equalsIgnoreCase(dir))
            return true;
        */
        
        boolean exists = false;

        // Check all the profiles for the game.
        for(int x=0; x<size; x++){
            if( profiles[x].getGameID() == gameID 
                    && profiles[x].getSaveDir().equalsIgnoreCase(dir) )
            {
                exists = true;
                break;
            }
        }

        return exists;
    }

    /**
     * Removes the given profile from the profile factory.
     * @param p The profile to remove.
     */
    public static void removeProfile(Profile p){
        // Loop to find the profile
        for (int index = 0; index < size; index++){
            if (p.equals(profiles[index])) {
                // Profile found, remove it
                int numMoved = size - index - 1;
                if (numMoved > 0)
                    System.arraycopy(profiles, index+1, profiles, index, numMoved);
                
                profiles[--size] = null;
            }
        }
    }
    
    /**
     * Marks the provided profile as being active, while marking other profiles
     * as in-active.
     * @param profile The Profile to mark as active. 
     */
    public static void setActive(Profile profile){
        byte gameID = profile.getGameID();

        for(int x=0; x<size; x++){
            if(profiles[x].getGameID() == gameID){
                if(profile.equals(profiles[x]))
                    profile.setActive(true);
                else
                    profiles[x].setActive(false);
            }
        }
    }

    /** Loads profiles from the hard disk. */
    public static void loadProfiles(){
        if(profilesNotLoaded){
            // Load the profiles
            try {
                ObjectInputStream in = new ObjectInputStream(
                        new java.io.FileInputStream(Config.PROFILE_DATA_DIR + PROFILES_FILE) );

                profiles = (Profile[]) in.readObject();
                profileIdCounter = in.readInt();

                in.close();

                size = profiles.length;
                
                profilesNotLoaded = false;

            } catch(java.io.FileNotFoundException fnfe){
                // Create a new pofile array
                System.out.println("Profile data not found, executing first run code.");
                
                (new File(Config.PROFILE_DATA_DIR)).mkdir();
                
                profiles = new Profile[5];
                size = 0;
                
                profileIdCounter = 1;
                profilesNotLoaded = false;
            } catch(IOException ioe){
                Main.handleException(
                        "A fatal IO Exception occured while attempting to load the profiles.",
                        ioe, Main.FATAL_LEVEL);
            } catch(ClassNotFoundException cnfe){
                Main.handleException("A fatal error has occured.",
                        cnfe, Main.FATAL_LEVEL);
            } catch(ClassCastException cce){
                Main.handleException("A fatal error has occured.",
                        cce, Main.FATAL_LEVEL);
            }
        }
    }

    /** Saves the profiles to disk. */
    public static void saveProfiles(){
        // Trim profiles to size
        profiles = Arrays.copyOf(profiles, size);

        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new java.io.FileOutputStream(Config.PROFILE_DATA_DIR + PROFILES_FILE));

            out.writeObject(profiles);
            out.writeInt(profileIdCounter);

            out.close();

        } catch(IOException ioe){
            Main.handleException("An error occured while saving the profiles.",
                    ioe, Main.WARN_LEVEL);
        }
    }


    private static Profile[] profiles;
    private static int size;

    private static int profileIdCounter;

    private static boolean profilesNotLoaded = true;


    private static final String PROFILES_FILE = "profiles.obj";

}
