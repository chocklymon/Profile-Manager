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

import com.chockly.pm.Config;
import com.chockly.pm.Main;
import com.chockly.pm.Utils;
import java.util.Arrays;

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

    public static void addCustomGame(String name, String dir, String exe, String text, String profileDir, String saveDir) {
        CustomGameFactory.getInstance().addCustomGame(name, dir, exe, text, profileDir, saveDir);
    }

    public static void addCustomGames(CustomGame[] games) {
        CustomGameFactory.getInstance().addCustomGames(games);
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
                    if( !activeTabs[x].isEmpty() ){
                        byte tab = Byte.parseByte(activeTabs[x]);

                        if(Utils.getIndex(allIds, tab) != -1){
                            activeIds[idsCount] = tab;
                            idsCount++;
                        }
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
        byte[] customGames = CustomGameFactory.getInstance().getIds();
        
        // Build the ids array
        byte[] builtInIds = getAllBuiltInGameIds();
        int builtInIdSize = builtInIds.length;
        
        byte[] allIds = new byte[builtInIdSize + customGames.length];
        
        // Populate the ids array
        System.arraycopy(builtInIds, 0, allIds, 0, builtInIdSize);
        System.arraycopy(customGames, 0, allIds, builtInIdSize, customGames.length);

        return allIds;
    }
    
    /**
     * Returns a new name object based on the provided id.
     * @param gameID The id number of the name to get.
     * @return A name object that is the specific implementation of name for the name id.
     */
    public static Game getGameFromID(byte gameID){
        // Check if the game id is a built in game
        switch(gameID){
            case MORROWIND_ID:
                return new Morrowind();
            case OBLIVION_ID:
                return new Oblivion();
            case SKYRIM_ID:
                return new Skyrim();
            case FALLOUT_3_ID:
                return new Fallout3();
            case FALLOUT_NV_ID:
                return new FalloutNV();
        }
        
        return CustomGameFactory.getInstance().getGameFromId(gameID);
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
     * Returns if the provided game id represents an custom game.
     * @param gameId The game id to check.
     * @return <tt>true</tt> if the game is a custom game, <tt>false</tt> false
     * otherwise.
     */
    public static boolean isCustomGame(byte gameId){
        return Utils.getIndex(getAllBuiltInGameIds(), gameId) == -1;
    }

    public static void removeCustomGame(byte gameID) {
        CustomGameFactory.getInstance().removeCustomGame(gameID);
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
            String saveDir) {
        CustomGameFactory.getInstance().updateCustomGame(game, name, gameDir, exe, icon, profileDir, saveDir);
    }
}
