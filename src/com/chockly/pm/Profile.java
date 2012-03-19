/* Profile Manger
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
 * Profiles contain data representing a separation of saved games (a profile)
 * for a given game.
 * @version 1.4.2
 * @author Curtis Oakley
 */
public class Profile implements java.io.Serializable, Comparable<Profile> {

    private String profileName = null;
    private String profileSaveDir = null;
    private String image = null;
    
    private final byte game;
    private final int ID;
    
    private boolean active;
    
    private static final long serialVersionUID = 5L;

    /**
     * Creates a new profile.
     * @param name The profile's name.
     * @param saveDir The folder name where the saved games for the profile are
     * stored.
     * @param gameID The game ID for the game that this profile is for.
     * @param id The ID for the profile.<br/>
     * This ID should be unique for all al profiles in the system. Generally the
     * ID creation and assignment is handled by the ProfileFactory.
     */
    public Profile(String name, String saveDir, byte gameID, int id){
        profileName = name;
        profileSaveDir = saveDir;
        game = gameID;
        ID = id;
    }
    
    /**
     * Creates a duplicate of the profile, with a new GameId.
     * @param newGameId The new game ID.
     * @return A duplicate of the Profile with with a different game id.
     */
    public Profile clone(byte newGameId){
        return new Profile(profileName,profileSaveDir,newGameId,ID);
    }

    /**
     * Compares profiles by their profile names.
     * @param o The profile to compare to.
     * @return 
     */
    @Override
    public int compareTo(Profile o) {
        return profileName.compareToIgnoreCase(o.getName());
    }
    
    /**
     * Indicates if the profile is active or not.
     * @return True if the profile is marked as active.
     */
    public boolean isActive(){
        return active;
    }
    
    /**
     * Set whether or not this profile is active.
     * @param isActive Set to true if the profile is active false otherwise.
     */
    public void setActive(boolean isActive){
        active = isActive;
    }

    /**
     * Returns the Profiles ID. The profile factory automatically assigns this.
     * @return The profiles unique ID.
     * @see ProfileFactory#getProfile(int)
     */
    public int getID(){
        return ID;
    }

    /**
     * Returns the Game ID for the profile.
     * @return The Game ID for the profile.
     */
    public byte getGameID(){
        return game;
    }

    /**
     * Returns the profile's name.
     * @return The name of the profile.
     */
    public String getName() {
        return profileName;
    }

    /**
     * Changes the profile's name.
     * @param profileName The new name for the profile.
     */
    public void setName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * Returns the name of the folder where the profile's saves are stored.
     * @return The directory name for the profiles data storage.
     */
    public String getSaveDir() {
        return profileSaveDir;
    }

    /**
     * Changes the profile's save game folder.
     * @param profileSaveDir The folder name where the profiles save's are stored.
     */
    public void setSaveDir(String profileSaveDir) {
        this.profileSaveDir = profileSaveDir;
    }

    /**
     * Returns the name of the image file that represents the profile's image.
     * @return The name of the profiles image.
     */
    public String getImage() {
        return image;
    }

    /**
     * Changes the image file associated with this profile.
     * @param image The name of the profile image to associate with this profile.
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Returns the profile name.
     * @return The name of the profile. See also {@link Profile#getProfileName()}
     */
    @Override
    public String toString(){
        return profileName;
    }

}
