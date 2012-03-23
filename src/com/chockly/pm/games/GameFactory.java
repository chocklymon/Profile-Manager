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

import com.chockly.pm.ArrayHelper;
import com.chockly.pm.Config;
import com.chockly.pm.Main;
import com.chockly.pm.XMLHelper;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 * Keeps track of all the Games in the program.
 * @author Curtis Oakley
 */
public class GameFactory {

    /** A byte used to create profiles that are not tied to any game. */
    public static final byte NULL_GAME_ID = 0;
    /** This represents the ID for Morrowind type games. */
    public static final byte MORROWIND_ID = 2;
    /** This represents the ID for Oblivion type games. */
    public static final byte OBLIVION_ID = 4;
    /** This represents the ID for Skyrim type games. */
    public static final byte SKYRIM_ID = 6;
    /** This represents the ID for Fallout 3 type games. */
    public static final byte FALLOUT_3_ID = 8;
    /** This represents the ID for Fallout: New Vegas type games. */
    public static final byte FALLOUT_NV_ID = 10;
    
    private static final String CUSTOM_GAME_FILE = "lib/games.xml";
    
    private static CustomGame[] customGames = null;
    
    public static void addCustomGame(String name,
            String gameDir,
            String exe,
            String icon,
            String profileDir,
            String saveDir)
    {
        if(customGames == null)
            loadCustomGames();
        
        byte id = getNextAvailableId();
        
        // Increase the size of the array and add the new custom game.
        customGames = java.util.Arrays.copyOf(customGames, customGames.length + 1);
        customGames[customGames.length - 1] = new CustomGame(id,
                name, gameDir, exe, icon, profileDir, saveDir);
        
        // Save the changes
        saveCustomGames();
        
        // Put the custom game into the active games configuration.
        String activeTabs = Config.get(Config.Key.active_tabs);
        
        if(activeTabs != null)
            Config.set(Config.Key.active_tabs, activeTabs + ',' + id);
    }
    
    /** Checks if any of the game IDs are the same (conflicting). */
    private static void checkForConflicts(){
        // Check for conflicts with the built in ids
        boolean changed = checkForConflicts(getAllBuiltInGameIds());
        
        // Check for conflicts with the custom games
        byte[] ids = new byte[customGames.length];
        for(int i=0; i<customGames.length; i++){
            ids[i] = customGames[i].getId();
        }
        changed = checkForConflicts(ids) ? true : changed;
        
        // Save the profile id changes if needed
        if(changed)
            saveCustomGames();
    }
    
    /**
     * Checks if any of the custom games have the same ID number.
     * @param checkMe The byte array to check against.
     * @return True if a game had a conflict.
     */
    private static boolean checkForConflicts(byte[] checkMe){
        boolean changed = false;
        for(int i=0; i<customGames.length; i++){
            for(int j=0; j<checkMe.length; j++){
                if(customGames[i].getId() == checkMe[j]){
                    if(i == j && checkMe[j] > FALLOUT_NV_ID)
                        continue;// Same game skip
                    
                    changed = true;
                    
                    String[] options = new String[] {"Update Id","Ignore"};
                    
                    if( JOptionPane.showOptionDialog(null,
                            "The game '" + customGames[i].getName() + "' has the same ID number as another game.\n\nYou can change this game's ID number, or ignore this game and not load it.",
                            "Game ID Conflict",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]) == JOptionPane.YES_OPTION )
                    {
                        // Change the games id
                        customGames[i] = customGames[i].clone(getNextAvailableId());
                        checkMe[i] = customGames[i].getId();
                    }
                    else
                    {
                        // Remove the CustomGame
                        customGames = (CustomGame[]) ArrayHelper.splice(customGames, i);
                    }
                }
            }
        }
        return changed;
    }

    /**
     * Gets all the active game IDs.<br/>
     * <br/>
     * This checks with {@link com.chockly.pm.Config} to see which games are
     * marked as active, if there are no games marked as active all game IDs
     * are returned.
     * @return The active game IDs in a byte array.
     */
    public static byte[] getActiveGameIds(){
        // Create a default active game id array
        byte[] allIds = getAllGameIds();
        byte[] activeIds = Arrays.copyOf(allIds, allIds.length);
        
        int idsCount = 0;
        
        // Try to retrieve the active ids from the config
        String value = Config.get(Config.Key.active_tabs);

        if(value != null){
            String[] activeTabs = value.split(",");

            if(activeTabs.length > activeIds.length)
                activeIds = Arrays.copyOf(activeIds, activeTabs.length);

            try {
                for(int x=0; x<activeTabs.length; x++){
                    byte tab = Byte.parseByte(activeTabs[x]);

                    if(ArrayHelper.getIndex(allIds, tab) != -1){
                        activeIds[idsCount] = tab;
                        idsCount++;
                    }
                }
            } catch(NumberFormatException nfe){
                Main.handleException(null, nfe, Main.LOG_LEVEL);
            }
        } else {
            idsCount = allIds.length;
        }

        return Arrays.copyOf(activeIds, idsCount);
    }
    
    /**
     * Returns all game IDs for the custom games.
     * @return The id numbers of all the built in games.
     */
    public static byte[] getAllBuiltInGameIds(){
        return new byte[] {MORROWIND_ID,
            OBLIVION_ID,
            SKYRIM_ID,
            FALLOUT_3_ID,
            FALLOUT_NV_ID};
    }
    
    /**
     * Returns all game IDs.
     * @return All the game IDs in a byte array.
     */
    public static byte[] getAllGameIds(){
        // Load the custom games if needed
        if(customGames == null)
            loadCustomGames();
        
        // Build the ids array
        byte[] builtInIds = getAllBuiltInGameIds();
        int builtInIdSize = builtInIds.length;
        
        byte[] allIds = new byte[builtInIdSize + customGames.length];
        
        // Populate the ids array
        System.arraycopy(builtInIds, 0, allIds, 0, builtInIdSize);
        for(int i=builtInIdSize; i<allIds.length; i++){
            allIds[i] = customGames[i - builtInIdSize].getId();
        }

        return allIds;
    }
    
    /**
     * Returns a new name object based on the provided id.
     * @param gameID The id number of the name to get.
     * @return A name object that is the specific implementation of name for the name id.
     */
    public static Game getGameFromID(byte gameID){
        if(customGames == null)
            loadCustomGames();
        
        Game game;
        // Check if the game id is a built in game
        switch(gameID){
            case MORROWIND_ID: game = new Morrowind(); break;
            case OBLIVION_ID: game = new Oblivion(); break;
            case SKYRIM_ID: game = new Skyrim(); break;
            case FALLOUT_3_ID: game = new Fallout3(); break;
            case FALLOUT_NV_ID: game = new FalloutNV(); break;
            default: game=null;
        }
        
        if(game == null){
            // Search the customGames array
            for(int i=0; i<customGames.length; i++){
                if(customGames[i].getId() == gameID){
                    game = customGames[i];
                    break;
                }
            }
        }
        
        return game;
    }
    
    /**
     * Returns the name of the game for the provided gameID.
     * @param gameID The ID of the game to get the name of.
     * @return The short name of the game, or an empty string if the game is not found.
     */
    public static String getNameFromID(byte gameID){
        Game game = getGameFromID(gameID);
        return (game == null) ? "" : game.getName();
    }
    
    /**
     * Gets the next open ID number.
     * @return The next open ID number.
     */
    private static byte getNextAvailableId(){
        byte max = FALLOUT_NV_ID+1;
        
        for(int i=0; i<customGames.length; i++){
            if(customGames[i].getId() >= max){
                max = customGames[i].getId();
                max++;
            }
        }
        
        return max;
    }
    
    /**
     * Returns if the provided game id represents an custom game.
     * @param gameId The game id to check.
     * @return <tt>true</tt> if the game is a custom game, <tt>false</tt> false
     * otherwise.
     */
    public static boolean isCustomGame(byte gameId){
        return ArrayHelper.getIndex(getAllBuiltInGameIds(), gameId) == -1;
    }
    
    /** Loads the custom games from disk. */
    private static void loadCustomGames(){
        if(new java.io.File(CUSTOM_GAME_FILE).exists())
            customGames = XMLHelper.GamesFromXML(CUSTOM_GAME_FILE);
        
        if(customGames == null)
            // Create an empty custom game
            customGames = new CustomGame[0];
        else
            // check for id conflicts
            checkForConflicts();
    }
    
    /**
     * Removes the custom game from the list of custom games.
     * @param id The id number of the game to remove.
     */
    public static void removeCustomGame(byte id){
        for(int i=0; i<customGames.length; i++){
            if(id == customGames[i].getId()){
                // Remove the custom Game from the array and save
                customGames = (CustomGame[]) ArrayHelper.splice(customGames, i);
                saveCustomGames();
                
                // Remove from the active tabs
                String activeTabs = Config.get(Config.Key.active_tabs);
                if(activeTabs != null){
                    activeTabs.replace(Byte.toString(id), "");
                    activeTabs.replace(",,", "");
                    Config.set(Config.Key.active_tabs, activeTabs);
                }
                
                break;
            }
        }
    }
    
    /** Saves the custom games to disk. */
    public static void saveCustomGames(){
        if(customGames != null)
            XMLHelper.GamesToXML(customGames, CUSTOM_GAME_FILE);
    }

    /**
     * Updates the provided custom game with the new information provided.
     * @param game The custom game to update.
     * @param name The new name of the game.
     * @param gameDir The new game directory.
     * @param exe The new executable.
     * @param icon The new icon.
     * @param profileDir The new profile directory name.
     * @param saveDir The new save directory name.
     */
    public static void updateCustomGame(CustomGame game,
            String name,
            String gameDir,
            String exe,
            String icon,
            String profileDir,
            String saveDir)
    {
        for(int i=0; i<customGames.length; i++){
            if(customGames[i].equals(game)){
                customGames[i] = new CustomGame(game.getId(),
                        name, gameDir, exe, icon, profileDir, saveDir);
                saveCustomGames();
            }
        }
    }
}
