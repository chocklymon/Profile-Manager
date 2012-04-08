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

package com.chockly.pm.games;

import com.chockly.pm.Config;
import com.chockly.pm.Main;

/**
 * Keeps track of all the Games in the program.
 * @author Curtis Oakley
 */
public class GameFactory {

    /** A byte used to create profiles that are not tied to any name. */
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
        byte[] activeIds = getAllGameIds();
        
        // Try to retrieve the active ids from the config
        try {
            String value = Config.get(Config.ACTIVE_TABS);
            if(value != null){
                String[] activeTabs = value.split(",");
                activeIds = new byte[activeTabs.length];
                for(int x=0; x<activeIds.length; x++){
                    activeIds[x] = Byte.parseByte(activeTabs[x]);
                }
            }
        } catch(NumberFormatException nfe){
            Main.handleException(null, nfe, Main.LOG_LEVEL);
        }
        
        return activeIds;
    }
    
    /**
     * Returns all game IDs.
     * @return All the game IDs in a byte array.
     */
    public static byte[] getAllGameIds(){
        return new byte[] {MORROWIND_ID, OBLIVION_ID, SKYRIM_ID, FALLOUT_3_ID, FALLOUT_NV_ID};
    }
    
    /**
     * Returns a new name object based on the provided id.
     * @param gameID The id number of the name to get.
     * @return A name object that is the specific implementation of name for the name id.
     */
    public static Game getGameFromID(byte gameID){
        Game game;
        switch(gameID){
            case MORROWIND_ID: game = new Morrowind(); break;
            case OBLIVION_ID: game = new Oblivion(); break;
            case SKYRIM_ID: game = new Skyrim(); break;
            case FALLOUT_3_ID: game = new Fallout3(); break;
            case FALLOUT_NV_ID: game = new FalloutNV(); break;
            default: System.err.println("Invalid Game ID"); game=null;
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

}
