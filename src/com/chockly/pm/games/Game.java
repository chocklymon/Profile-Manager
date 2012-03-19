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

import com.chockly.pm.Profile;
import javax.swing.Icon;

/**
 * This interface is used by the various individual games to provide a standard
 * way to deal with profile swapping, setup, and game files.
 * @author Curtis Oakley
 * @version 1.3
 */
public interface Game {

    /**
     * Sets the profile as being activated, and sets the game to run using the
     * provided profile.
     * @param profile The Pofile to run the game with.
     * @return True if the profile was correctly activated, false otherwise.
     */
    public boolean activateProfile(Profile profile);
    
    /**
     * Auto sets up profiles.<br/>
     * <br/>
     * This means that all the saved game files are separated into directories
     * and a new profile is created for each of the directories. The saved games
     * are separated based on the character's name.
     */
    public void autoSetupProfiles();
    
    /**
     * Deactivates all profiles. Effectively restoring the game to it's default
     * setting.
     */
    public void deactivateProfiles();
    
    /**
     * Gets the games directory.<br/>
     * <br/>
     * This is generally the parent directory of the {@link Game#getSave()}
     * directory and the game's configuration files.
     * 
     * @return The directory to the games save and ini directory.
     */
    public String getDir();
    
    /**
     * Get the data (saves and ini) directory's configuration key.
     * @return The data directory config key.
     * @see Game#getDir() 
     */
    public String getDirConfigKey();
    
    /**
     * Gets the path to the executable file to run when launching the game.
     * @return The string path to the game's executable file.
     */
    public String getExe();
    
    /**
     * Gets the executable path configuration key.
     * @return The exe path config key.
     */
    public String getExeConfigKey();
    
    /**
     * Returns the full name of the game.
     * @return The full name of the game.
     */
    public String getFullName();

    /**
     * Gets the name of the directory where the saved games are stored.<br/>
     * <br/>
     * This will generally return the same as {@link Game#getSave()} unless the
     * {@link Game#usesIni()} is <tt>false</tt>.
     * 
     * @return The name of the directory where profiles are stored.
     * @see Game#getSave()
     * @see Game#usesIni() 
     */
    public String getGameSaveDir();

    /**
     * Gets the Icon for the game.
     * @return The game's icon.
     */
    public Icon getIcon();
    
    /**
     * Gets the games ID
     * @return The id number of the game
     */
    public byte getId();
    
    /**
     * Gets the name of the games ini file.<br>
     * <br>
     * If {@link Game#usesIni()} returns <tt>false</tt> this will throw an
     * UnsupportedOperationException.<br/>
     * <br/>
     * {@link Game#getDir()} + <tt>this</tt> will return the full path the
     * game's .ini file.
     */
    public String getIni();

    /**
     * Returns the name of the game.
     * @return The short name of the game.
     */
    public String getName();

    /**
     * Gets the name of the directory where the profiles are saved.<br/>
     * <br/>
     * For games where {@link Game#usesIni()} is <tt>true</tt>
     * {@link Game#getDir()} + getSave() + {@link java.io.File#separator} +
     * {@link Profile#getSaveDir()} will be the full path to the profile's saved
     * game directory.
     * 
     * @return The save game directory name.
     * @see Game#usesIni() 
     */
    public String getSave();

    /**
     * Sets up a profile.<br/>
     * <br/>
     * This generally means that a directory is created for the profile's saved
     * games to be stored in.
     * @param profile The profile to set up.
     */
    public void setupProfile(Profile profile);

    /**
     * Indicates if the game modifies an ini file to change profiles.<br/>
     * <br/>
     * When this returns <tt>true</tt> then profiles are stored as child
     * directories to the games saved game directory, and {@link Game#getSave()}
     * will return the name of the game's save game directory. When this returns
     * <tt>false</tt> then game's profiles are saved in child directories of
     * the directory returned by {@link Game#getSave()} which will be on the
     * same level as the directory returned by {@link Game#getGameSaveDir()}.
     * Furthermore in this case the {@link Game#getIni()} will throw an
     * UnsupportedOperationException.
     * 
     * @return <tt>True</tt> if the game uses an ini file to set it active
     * profiles. <tt>False</tt> if the game moves folders to activate profiles.
     * 
     * @see Game#getGameSaveDir() 
     * @see Game#getSave()
     */
    public boolean usesIni();

}
